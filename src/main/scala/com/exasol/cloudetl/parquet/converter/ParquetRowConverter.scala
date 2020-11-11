package com.exasol.cloudetl.parquet.converter

import java.nio.ByteOrder

import com.exasol.cloudetl.util.DateTimeUtil

import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.Converter
import org.apache.parquet.io.api.GroupConverter
import org.apache.parquet.io.api.PrimitiveConverter
import org.apache.parquet.schema.GroupType
import org.apache.parquet.schema.LogicalTypeAnnotation.DecimalLogicalTypeAnnotation
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type

final class RowRootConverter(schema: GroupType) extends GroupConverter {
  private val size = schema.getFieldCount
  private var values: Array[Any] = Array.ofDim[Any](size)
  private final val converters: Array[Converter] = {
    val arr = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      arr(i) = createNewConverter(schema.getType(i), i)
    }
    arr
  }

  private def createNewConverter(tpe: Type, idx: Int): Converter = {
    if (!tpe.isPrimitive()) {
      throw new UnsupportedOperationException("Currently only primitive types are supported")
    }
    makeReader(tpe.asPrimitiveType(), idx)
  }

  def currentResult(): Array[Any] =
    values

  override def getConverter(idx: Int): Converter =
    converters(idx)

  override def start(): Unit =
    values = Array.ofDim(converters.size)

  override def end(): Unit = {}

  private def makeReader(primitiveType: PrimitiveType, idx: Int): Converter = {
    val originalType = primitiveType.getOriginalType
    primitiveType.getPrimitiveTypeName() match {
      case PrimitiveTypeName.INT32 =>
        originalType match {
          case OriginalType.DATE    => new RowDateConverter(this, idx)
          case OriginalType.DECIMAL => createDecimalConverter(this, primitiveType, idx)
          case _                    => new RowPrimitiveConverter(this, idx)
        }
      case PrimitiveTypeName.BOOLEAN => new RowPrimitiveConverter(this, idx)
      case PrimitiveTypeName.DOUBLE  => new RowPrimitiveConverter(this, idx)
      case PrimitiveTypeName.FLOAT   => new RowPrimitiveConverter(this, idx)
      case PrimitiveTypeName.BINARY =>
        originalType match {
          case OriginalType.UTF8 => new RowStringConverter(this, idx)
          case _                 => new RowPrimitiveConverter(this, idx)
        }
      case PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY =>
        originalType match {
          case OriginalType.DECIMAL => createDecimalConverter(this, primitiveType, idx)
          case _                    => new RowPrimitiveConverter(this, idx)
        }
      case PrimitiveTypeName.INT64 =>
        originalType match {
          case OriginalType.TIMESTAMP_MILLIS => new RowTimestampMillisConverter(this, idx)
          case OriginalType.DECIMAL          => createDecimalConverter(this, primitiveType, idx)
          case _                             => new RowPrimitiveConverter(this, idx)
        }
      case PrimitiveTypeName.INT96 => new RowTimestampInt96Converter(this, idx)
    }
  }

  private[this] def createDecimalConverter(
    parent: RowRootConverter,
    primitiveType: PrimitiveType,
    index: Int
  ): RowDecimalConverter = {
    val decimalType =
      primitiveType.getLogicalTypeAnnotation().asInstanceOf[DecimalLogicalTypeAnnotation]
    new RowDecimalConverter(parent, index, decimalType.getPrecision(), decimalType.getScale())
  }

  private[this] final class RowPrimitiveConverter(val parent: RowRootConverter, val index: Int)
      extends PrimitiveConverter {

    override def addBinary(value: Binary): Unit =
      parent.currentResult.update(index, new String(value.getBytes()))

    override def addBoolean(value: Boolean): Unit =
      parent.currentResult.update(index, value)

    override def addDouble(value: Double): Unit =
      parent.currentResult.update(index, value)

    override def addFloat(value: Float): Unit =
      parent.currentResult.update(index, value)

    override def addInt(value: Int): Unit =
      parent.currentResult.update(index, value)

    override def addLong(value: Long): Unit =
      parent.currentResult.update(index, value)
  }

  final class RowStringConverter(val parent: RowRootConverter, val index: Int)
      extends PrimitiveConverter {
    override def addBinary(value: Binary): Unit =
      parent.currentResult.update(index, value.toStringUsingUTF8())
  }

  private final class RowDecimalConverter(
    val parent: RowRootConverter,
    val index: Int,
    precision: Int,
    scale: Int
  ) extends PrimitiveConverter {
    // Converts decimals stored as INT32
    override def addInt(value: Int): Unit =
      parent.currentResult.update(index, value)

    // Converts decimals stored as INT64
    override def addLong(value: Long): Unit =
      parent.currentResult.update(index, value)

    override def addBinary(value: Binary): Unit = {
      val bi = new java.math.BigInteger(value.getBytes)
      val bd = new java.math.BigDecimal(bi, scale, new java.math.MathContext(precision))
      parent.currentResult.update(index, bd)
    }
  }

  private final class RowTimestampMillisConverter(val parent: RowRootConverter, val index: Int)
      extends PrimitiveConverter {

    override def addLong(value: Long): Unit =
      parent.currentResult.update(index, DateTimeUtil.getTimestampFromMillis(value))
  }

  private final class RowTimestampInt96Converter(val parent: RowRootConverter, val index: Int)
      extends PrimitiveConverter {

    override def addBinary(value: Binary): Unit = {
      val buf = value.toByteBuffer.order(ByteOrder.LITTLE_ENDIAN)
      val nanos = buf.getLong
      val days = buf.getInt
      val micros = DateTimeUtil.getMicrosFromJulianDay(days, nanos)
      val ts = DateTimeUtil.getTimestampFromMicros(micros)

      parent.currentResult.update(index, ts)
    }
  }

  private final class RowDateConverter(val parent: RowRootConverter, val index: Int)
      extends PrimitiveConverter {

    override def addInt(value: Int): Unit = {
      val date = DateTimeUtil.daysToDate(value.toLong)
      parent.currentResult.update(index, date)
    }
  }

}
