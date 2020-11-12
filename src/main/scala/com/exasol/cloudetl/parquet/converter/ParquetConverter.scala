package com.exasol.cloudetl.parquet.converter

import java.nio.ByteOrder

import com.exasol.cloudetl.util.DateTimeUtil

import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.PrimitiveConverter

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
  override def addBinary(value: Binary): Unit = holder.put(index, value.toStringUsingUTF8())
}

final case class ParquetDecimalConverter(
  index: Int,
  holder: ValueHolder,
  precision: Int,
  scale: Int
) extends PrimitiveConverter
    with ParquetConverter {
  // Converts decimals stored as INT32
  override def addInt(value: Int): Unit = holder.put(index, value)
  // Converts decimals stored as INT64
  override def addLong(value: Long): Unit = holder.put(index, value)
  override def addBinary(value: Binary): Unit = {
    val bi = new java.math.BigInteger(value.getBytes)
    val bd = new java.math.BigDecimal(bi, scale, new java.math.MathContext(precision))
    holder.put(index, bd)
  }
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
