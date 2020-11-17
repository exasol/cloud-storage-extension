package com.exasol.cloudetl.parquet.converter

import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteOrder

import com.exasol.cloudetl.util.DateTimeUtil

import org.apache.parquet.column.Dictionary
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.PrimitiveConverter
import org.apache.parquet.schema.LogicalTypeAnnotation.DecimalLogicalTypeAnnotation
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._

/**
 * An interface for the Parquet data type converters.
 *
 * The sealed trait ensures that all the implementations should be in
 * this file.
 */
sealed trait ParquetConverter

final case class ParquetPrimitiveConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addBinary(value: Binary): Unit = holder.put(index, new String(value.getBytes()))
  override def addBoolean(value: Boolean): Unit = holder.put(index, value)
  override def addDouble(value: Double): Unit = holder.put(index, value)
  override def addFloat(value: Float): Unit = holder.put(index, value)
  override def addInt(value: Int): Unit = holder.put(index, value)
  override def addLong(value: Long): Unit = holder.put(index, value)
}

final case class ParquetStringConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  private[this] var decodedDictionary: Array[String] = null

  override def hasDictionarySupport(): Boolean = true

  override def setDictionary(dictionary: Dictionary): Unit = {
    decodedDictionary = new Array[String](dictionary.getMaxId() + 1)
    for { i <- 0 to dictionary.getMaxId() } {
      decodedDictionary(i) = dictionary.decodeToBinary(i).toStringUsingUTF8()
    }
  }

  override def addBinary(value: Binary): Unit =
    holder.put(index, value.toStringUsingUTF8())

  override def addValueFromDictionary(dictionaryId: Int): Unit =
    holder.put(index, decodedDictionary(dictionaryId))
}

final case class ParquetDecimalConverter(
  index: Int,
  holder: ValueHolder,
  primitiveType: PrimitiveType
) extends PrimitiveConverter
    with ParquetConverter {

  private[this] val decimalType =
    primitiveType.getLogicalTypeAnnotation().asInstanceOf[DecimalLogicalTypeAnnotation]
  private[this] val precision = decimalType.getPrecision()
  private[this] val scale = decimalType.getScale()

  private[this] var decodedDictionary: Array[BigDecimal] = null

  override def hasDictionarySupport(): Boolean = true

  override def setDictionary(dictionary: Dictionary): Unit = {
    decodedDictionary = new Array[BigDecimal](dictionary.getMaxId() + 1)
    for { i <- 0 to dictionary.getMaxId() } {
      decodedDictionary(i) = getDecimalFromType(dictionary, index)
    }
  }

  private[this] def getDecimalFromType(dictionary: Dictionary, index: Int): BigDecimal =
    primitiveType.getPrimitiveTypeName() match {
      case INT32  => getDecimalFromLong(dictionary.decodeToInt(index).toLong)
      case INT64  => getDecimalFromLong(dictionary.decodeToLong(index))
      case BINARY => getDecimalFromBinary(dictionary.decodeToBinary(index))
      case _ =>
        throw new UnsupportedOperationException(
          "Cannot convert parquet type to decimal type. Please check that Parquet decimal " +
            "type is stored as INT32, INT64, BINARY or FIXED_LEN_BYTE_ARRAY."
        )
    }

  private[this] def getDecimalFromLong(value: Long): BigDecimal =
    BigDecimal.valueOf(value, scale)

  private[this] def getDecimalFromBinary(value: Binary): BigDecimal = {
    val bigInteger = new BigInteger(value.getBytes())
    new BigDecimal(bigInteger, scale, new java.math.MathContext(precision))
  }

  override def addInt(value: Int): Unit = holder.put(index, getDecimalFromLong(value.toLong))

  override def addLong(value: Long): Unit = holder.put(index, getDecimalFromLong(value))

  override def addBinary(value: Binary): Unit = holder.put(index, getDecimalFromBinary(value))

  override def addValueFromDictionary(dictionaryId: Int): Unit =
    holder.put(index, decodedDictionary(dictionaryId))
}

final case class ParquetTimestampMillisConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addLong(value: Long): Unit =
    holder.put(index, DateTimeUtil.getTimestampFromMillis(value))
}

final case class ParquetTimestampInt96Converter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addBinary(value: Binary): Unit = {
    val buf = value.toByteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    val nanos = buf.getLong
    val days = buf.getInt
    val micros = DateTimeUtil.getMicrosFromJulianDay(days, nanos)
    val ts = DateTimeUtil.getTimestampFromMicros(micros)
    holder.put(index, ts)
  }
}

final case class ParquetDateConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addInt(value: Int): Unit = {
    val date = DateTimeUtil.daysToDate(value.toLong)
    holder.put(index, date)
  }
}
