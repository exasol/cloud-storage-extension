package com.exasol.cloudetl.parquet

import java.nio.ByteBuffer
import java.nio.ByteOrder

import scala.collection.JavaConverters._

import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.util.DateTimeUtil
import com.exasol.cloudetl.util.SchemaUtil

import org.apache.hadoop.conf.Configuration
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.hadoop.api.WriteSupport.FinalizedWriteContext
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName

/**
 * A Parquet [[org.apache.parquet.hadoop.api.WriteSupport]] implementation that writes
 * [[com.exasol.cloudetl.data.Row]] as a Parquet data.
 *
 * This is mostly adapted from Spark codebase:
 *  - org.apache.spark.sql.execution.datasources.parquet.ParquetWriteSupport
 *
 */
@SuppressWarnings(
  Array(
    "org.wartremover.warts.AsInstanceOf",
    "org.wartremover.warts.Null",
    "org.wartremover.warts.Var"
  )
)
class RowWriteSupport(schema: MessageType) extends WriteSupport[Row] {

  // The number bytes required for timestamp buffer in Parquet
  private final val TIMESTAMP_MAX_BYTE_SIZE: Int = 12

  // This is a type that is responsible for writing a value in Row values index to the
  // RecordConsumer
  private type RowValueWriter = (Row, Int) => Unit

  // A list of `RowValueWriter`-s for each field type of Parquet `schema`
  private var rootFieldWriters: Array[RowValueWriter] = _

  // A Parquet RecordConsumer that all values of a Row will be written
  private var recordConsumer: RecordConsumer = _

  // Reusable byte array used to write timestamps as Parquet INT96 values
  private val timestampBuffer = new Array[Byte](TIMESTAMP_MAX_BYTE_SIZE)

  // Reusable byte array used to write decimal values as Parquet FIXED_LEN_BYTE_ARRAY values
  private val decimalBuffer =
    new Array[Byte](SchemaUtil.PRECISION_TO_BYTE_SIZE(SchemaUtil.DECIMAL_MAX_PRECISION - 1))

  final override def init(configuration: Configuration): WriteSupport.WriteContext = {
    this.rootFieldWriters = schema.getFields.asScala
      .map {
        case field =>
          makeWriter(field.asPrimitiveType())
      }
      .toArray[RowValueWriter]

    new WriteSupport.WriteContext(schema, new java.util.HashMap())
  }

  final override def prepareForWrite(record: RecordConsumer): Unit =
    this.recordConsumer = record

  final override def write(row: Row): Unit =
    consumeMessage {
      writeFields(row, schema, rootFieldWriters)
    }

  final override def finalizeWrite(): FinalizedWriteContext =
    new FinalizedWriteContext(new java.util.HashMap())

  private def writeFields(row: Row, schema: MessageType, writers: Array[RowValueWriter]): Unit = {
    var idx = 0
    while (idx < schema.getFieldCount) {
      val fieldType = schema.getType(idx)
      val fieldName = fieldType.getName()
      if (row.values(idx) != null) {
        consumeField(fieldName, idx) {
          writers(idx).apply(row, idx)
        }
      }
      idx += 1
    }
  }

  private def consumeMessage(fn: => Unit): Unit = {
    recordConsumer.startMessage()
    fn
    recordConsumer.endMessage()
  }

  private def consumeField(field: String, index: Int)(fn: => Unit): Unit = {
    recordConsumer.startField(field, index)
    fn
    recordConsumer.endField(field, index)
  }

  private def makeWriter(primitiveType: PrimitiveType): RowValueWriter = {
    val typeName = primitiveType.getPrimitiveTypeName
    val originalType = primitiveType.getOriginalType

    typeName match {
      case PrimitiveTypeName.BOOLEAN =>
        (row: Row, index: Int) =>
          recordConsumer.addBoolean(row.values(index).asInstanceOf[Boolean])

      case PrimitiveTypeName.INT32 =>
        originalType match {
          case OriginalType.DATE =>
            makeDateWriter()
          case OriginalType.DECIMAL =>
            val decimalMetadata = primitiveType.getDecimalMetadata
            makeDecimalWriter(decimalMetadata.getPrecision, decimalMetadata.getScale)
          case _ =>
            (row: Row, index: Int) =>
              recordConsumer.addInteger(row.values(index).asInstanceOf[Integer])
        }

      case PrimitiveTypeName.INT64 =>
        (row: Row, index: Int) =>
          recordConsumer.addLong(row.values(index).asInstanceOf[Long])

      case PrimitiveTypeName.FLOAT =>
        (row: Row, index: Int) =>
          recordConsumer.addFloat(row.values(index).asInstanceOf[Double].floatValue)

      case PrimitiveTypeName.DOUBLE =>
        (row: Row, index: Int) =>
          recordConsumer.addDouble(row.values(index).asInstanceOf[Double])

      case PrimitiveTypeName.BINARY =>
        (row: Row, index: Int) =>
          recordConsumer.addBinary(
            Binary.fromReusedByteArray(row.values(index).asInstanceOf[String].getBytes)
          )

      case PrimitiveTypeName.INT96 =>
        makeTimestampWriter()

      case PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY if originalType == OriginalType.DECIMAL =>
        val decimalMetadata = primitiveType.getDecimalMetadata
        makeDecimalWriter(decimalMetadata.getPrecision, decimalMetadata.getScale)

      case _ => throw new UnsupportedOperationException(s"Unsupported parquet type '$typeName'.")
    }
  }

  private def makeDateWriter(): RowValueWriter = (row: Row, index: Int) => {
    // Write the number of days since unix epoch as integer
    val date = row.values(index).asInstanceOf[java.sql.Date]
    val days = DateTimeUtil.daysSinceEpoch(date)

    recordConsumer.addInteger(days.toInt)
  }

  private def makeTimestampWriter(): RowValueWriter = (row: Row, index: Int) => {
    val timestamp = row.values(index).asInstanceOf[java.sql.Timestamp]
    val micros = DateTimeUtil.getMicrosFromTimestamp(timestamp)
    val (days, nanos) = DateTimeUtil.getJulianDayAndNanos(micros)

    val buf = ByteBuffer.wrap(timestampBuffer)
    val _ = buf.order(ByteOrder.LITTLE_ENDIAN).putLong(nanos).putInt(days)

    recordConsumer.addBinary(Binary.fromReusedByteArray(timestampBuffer))
  }

  private def makeDecimalWriter(precision: Int, scale: Int): RowValueWriter = {
    require(
      precision >= 1,
      s"Decimal precision $precision should not be less than minimum precision 1"
    )
    require(
      precision <= SchemaUtil.DECIMAL_MAX_PRECISION,
      s"""|Decimal precision $precision should not exceed
          |max precision ${SchemaUtil.DECIMAL_MAX_PRECISION}
      """.stripMargin
    )

    // The number of bytes from given the precision
    val numBytes = SchemaUtil.PRECISION_TO_BYTE_SIZE(precision - 1)

    val int32Writer = (row: Row, index: Int) => {
      val bigDecimalInt =
        row
          .values(index)
          .asInstanceOf[java.math.BigDecimal]
          .unscaledValue()
          .longValueExact()
          .toInt
      recordConsumer.addInteger(bigDecimalInt)
    }

    val int64Writer = (row: Row, index: Int) => {
      val bigDecimalLong =
        row.values(index).asInstanceOf[java.math.BigDecimal].unscaledValue().longValueExact()
      recordConsumer.addLong(bigDecimalLong)
    }

    val bytesWriter = (row: Row, index: Int) => {
      val decimal = row.values(index).asInstanceOf[java.math.BigDecimal]
      val unscaled = decimal.unscaledValue()
      val bytes = unscaled.toByteArray
      val fixedLenBytesArray =
        if (bytes.length == numBytes) {
          // If the length of the underlying byte array of the unscaled `BigDecimal` happens to be
          // `numBytes`, just reuse it, so that we don't bother copying it to `decimalBuffer`.
          bytes
        } else if (bytes.length < numBytes) {
          // Otherwise, the length must be less than `numBytes`.  In this case we copy contents of
          // the underlying bytes with padding sign bytes to `decimalBuffer` to form the result
          // fixed-length byte array.

          // For negatives all high bits need to be 1 hence -1 used
          val signByte = if (unscaled.signum < 0) -1: Byte else 0: Byte
          java.util.Arrays.fill(decimalBuffer, 0, numBytes - bytes.length, signByte)
          System.arraycopy(bytes, 0, decimalBuffer, numBytes - bytes.length, bytes.length)
          decimalBuffer
        } else {
          throw new IllegalStateException(
            s"The precision $precision is too small for decimal value."
          )
        }

      recordConsumer.addBinary(Binary.fromReusedByteArray(fixedLenBytesArray, 0, numBytes))
    }

    if (precision <= SchemaUtil.DECIMAL_MAX_INT_DIGITS) {
      // 1 <= precision <= 9, writes as INT32
      int32Writer
    } else if (precision <= SchemaUtil.DECIMAL_MAX_LONG_DIGITS) {
      // 10 <= precision <= 18, writes as INT64
      int64Writer
    } else {
      // 19 <= precision <= 38, writes as FIXED_LEN_BYTE_ARRAY
      bytesWriter
    }
  }

}
