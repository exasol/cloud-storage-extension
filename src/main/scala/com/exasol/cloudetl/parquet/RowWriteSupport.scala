package com.exasol.cloudetl.parquet

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets.UTF_8

import scala.jdk.CollectionConverters._

import com.exasol.cloudetl.helper.DateTimeConverter._
import com.exasol.cloudetl.helper.ParquetSchemaConverter
import com.exasol.common.data.Row
import com.exasol.errorreporting.ExaError

import org.apache.hadoop.conf.Configuration
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.hadoop.api.WriteSupport.FinalizedWriteContext
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema.LogicalTypeAnnotation.DecimalLogicalTypeAnnotation
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName

/**
 * A Parquet [[org.apache.parquet.hadoop.api.WriteSupport]]
 * implementation that writes [[com.exasol.common.data.Row]] as a
 * Parquet data.
 *
 * This is mostly adapted from Spark codebase:
 *  - org.apache.spark.sql.execution.datasources.parquet.ParquetWriteSupport
 */
class RowWriteSupport(schema: MessageType) extends WriteSupport[Row] {

  // The number bytes required for timestamp buffer in Parquet
  private final val TIMESTAMP_MAX_BYTE_SIZE: Int = 12

  // This is a type that is responsible for writing a value in Row
  // values index to the RecordConsumer
  private type RowValueWriter = (Row, Int) => Unit

  // A list of `RowValueWriter`-s for each field type of Parquet
  // `schema`
  private var rootFieldWriters: Array[RowValueWriter] = _

  // A Parquet RecordConsumer that all values of a Row will be written
  private var recordConsumer: RecordConsumer = _

  // Reusable byte array used to write timestamps as Parquet INT96
  // values
  private val timestampBuffer = new Array[Byte](TIMESTAMP_MAX_BYTE_SIZE)

  // Reusable byte array used to write decimal values as Parquet
  // FIXED_LEN_BYTE_ARRAY values
  private val decimalBuffer =
    new Array[Byte](ParquetSchemaConverter.PRECISION_TO_BYTE_SIZE(ParquetSchemaConverter.DECIMAL_MAX_PRECISION - 1))

  override final def init(configuration: Configuration): WriteSupport.WriteContext = {
    this.rootFieldWriters = schema.getFields.asScala
      .map { case field =>
        makeWriter(field.asPrimitiveType())
      }
      .toArray[RowValueWriter]

    new WriteSupport.WriteContext(schema, new java.util.HashMap())
  }

  override final def prepareForWrite(record: RecordConsumer): Unit =
    this.recordConsumer = record

  override final def write(row: Row): Unit =
    consumeMessage {
      writeFields(row, schema, rootFieldWriters)
    }

  override final def finalizeWrite(): FinalizedWriteContext =
    new FinalizedWriteContext(new java.util.HashMap())

  private def writeFields(row: Row, schema: MessageType, writers: Array[RowValueWriter]): Unit = {
    var idx = 0
    while (idx < schema.getFieldCount) {
      val fieldType = schema.getType(idx)
      val fieldName = fieldType.getName()
      if (!row.isNullAt(idx)) {
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
    val typeName = primitiveType.getPrimitiveTypeName()
    val originalType = primitiveType.getOriginalType()

    typeName match {
      case PrimitiveTypeName.BOOLEAN =>
        (row: Row, index: Int) => recordConsumer.addBoolean(row.getAs[java.lang.Boolean](index))

      case PrimitiveTypeName.INT32 =>
        originalType match {
          case OriginalType.DATE =>
            makeDateWriter()
          case _ =>
            (row: Row, index: Int) => recordConsumer.addInteger(row.getAs[java.lang.Integer](index))
        }

      case PrimitiveTypeName.INT64 =>
        (row: Row, index: Int) => recordConsumer.addLong(row.getAs[java.lang.Long](index))

      case PrimitiveTypeName.FLOAT =>
        (row: Row, index: Int) => recordConsumer.addFloat(row.getAs[java.lang.Double](index).floatValue)

      case PrimitiveTypeName.DOUBLE =>
        (row: Row, index: Int) => recordConsumer.addDouble(row.getAs[java.lang.Double](index))

      case PrimitiveTypeName.BINARY =>
        (row: Row, index: Int) =>
          recordConsumer.addBinary(
            Binary.fromReusedByteArray(row.getAs[String](index).getBytes(UTF_8))
          )

      case PrimitiveTypeName.INT96 =>
        makeTimestampWriter()

      case PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY if originalType == OriginalType.DECIMAL =>
        val decimal = primitiveType.getLogicalTypeAnnotation().asInstanceOf[DecimalLogicalTypeAnnotation]
        makeDecimalWriter(decimal.getPrecision())

      case _ =>
        throw new UnsupportedOperationException(
          ExaError
            .messageBuilder("E-CSE-18")
            .message("Parquet type {{PARQUET_TYPE}} is not supported.")
            .parameter("PARQUET_TYPE", typeName.toString())
            .toString()
        )
    }
  }

  private def makeDateWriter(): RowValueWriter = (row: Row, index: Int) => {
    // Write the number of days since unix epoch as integer
    val date = row.getAs[java.sql.Date](index)
    val days = daysSinceEpoch(date)

    recordConsumer.addInteger(days.toInt)
  }

  private def makeTimestampWriter(): RowValueWriter = (row: Row, index: Int) => {
    val timestamp = row.getAs[java.sql.Timestamp](index)
    val micros = getMicrosFromTimestamp(timestamp)
    val (days, nanos) = getJulianDayAndNanos(micros)

    val buf = ByteBuffer.wrap(timestampBuffer)
    val _ = buf.order(ByteOrder.LITTLE_ENDIAN).putLong(nanos).putInt(days)

    recordConsumer.addBinary(Binary.fromReusedByteArray(timestampBuffer))
  }

  private def makeDecimalWriter(precision: Int): RowValueWriter = {
    require(
      precision >= 1,
      s"Decimal precision $precision should not be less than minimum precision 1"
    )
    require(
      precision <= ParquetSchemaConverter.DECIMAL_MAX_PRECISION,
      s"""|Decimal precision $precision should not exceed
          |max precision ${ParquetSchemaConverter.DECIMAL_MAX_PRECISION}
      """.stripMargin
    )

    // The number of bytes from given the precision
    val numBytes = ParquetSchemaConverter.PRECISION_TO_BYTE_SIZE(precision - 1)

    val bytesWriter = (row: Row, index: Int) => {
      val decimal = row.getAs[java.math.BigDecimal](index)
      val unscaled = decimal.unscaledValue()
      val bytes = unscaled.toByteArray
      val length = bytes.length
      val fixedLenBytesArray =
        if (length == numBytes) {
          // If the length of the underlying byte array of the unscaled
          // `BigDecimal` happens to be `numBytes`, just reuse it, so
          // that we don't bother copying it to `decimalBuffer`.
          bytes
        } else if (length < numBytes) {
          // Otherwise, the length must be less than `numBytes`.  In
          // this case we copy contents of the underlying bytes with
          // padding sign bytes to `decimalBuffer` to form the result
          // fixed-length byte array.

          // For negatives all high bits need to be 1 hence -1 used
          val signByte = if (unscaled.signum < 0) -1: Byte else 0: Byte
          java.util.Arrays.fill(decimalBuffer, 0, numBytes - length, signByte)
          System.arraycopy(bytes, 0, decimalBuffer, numBytes - length, length)
          decimalBuffer
        } else {
          throw new IllegalStateException(
            ExaError
              .messageBuilder("E-CSE-9")
              .message("The precision {{PRECISION}} is too small for decimal value.")
              .parameter("PRECISION", String.valueOf(precision))
              .mitigation("The precision should be at least as {{LEN}}.", String.valueOf(length))
              .toString()
          )
        }

      recordConsumer.addBinary(Binary.fromReusedByteArray(fixedLenBytesArray, 0, numBytes))
    }

    bytesWriter
  }

}
