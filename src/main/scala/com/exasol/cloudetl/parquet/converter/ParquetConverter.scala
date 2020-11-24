package com.exasol.cloudetl.parquet.converter

import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteOrder

import scala.collection.mutable.{Map => MMap}
import scala.collection.mutable.ArrayBuffer

import com.exasol.cloudetl.util.DateTimeUtil

import org.apache.parquet.column.Dictionary
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.Converter
import org.apache.parquet.io.api.GroupConverter
import org.apache.parquet.io.api.PrimitiveConverter
import org.apache.parquet.schema.GroupType
import org.apache.parquet.schema.LogicalTypeAnnotation.DecimalLogicalTypeAnnotation
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._
import org.apache.parquet.schema.Type

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
  primitiveType: PrimitiveType,
  index: Int,
  holder: ValueHolder
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

final case class RepeatedGroupConverter(
  groupType: GroupType,
  index: Int,
  parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter {
  private[this] val size = groupType.getFieldCount()
  private[this] val converters = createFieldConverters()
  private[this] val dataHolder = Array.ofDim[Any](size)
  private[this] var currentIndex: Int = 0
  private[this] var currentValues: MMap[String, Any] = null

  override def getConverter(fieldIndex: Int): Converter = converters(fieldIndex)

  override def start(): Unit = {
    if (currentValues == null) {
      parentDataHolder.put(index, dataHolder)
    }
    currentValues = MMap.empty[String, Any]
  }

  override def end(): Unit = {
    dataHolder(currentIndex) = currentValues
    currentIndex += 1
  }

  private[this] def createFieldConverters(): Array[Converter] = {
    val converters = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      converters(i) = ConverterFactory(
        groupType.getType(i),
        i,
        new ValueHolder {
          override def put(index: Int, value: Any): Unit = {
            val _ = currentValues.put(groupType.getFieldName(i), value)
          }
          override def reset(): Unit = {}
          override def getValues(): Seq[Any] = Seq.empty[Any]
        }
      )
    }
    converters
  }

}

final case class RepeatedPrimitiveConverter(
  elementType: Type,
  index: Int,
  parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter {
  private[this] val elementConverter = createPrimitiveElementConverter()
  private[this] val values = ArrayBuffer.empty[Any]
  private[this] var currentValue: Any = null

  override def getConverter(fieldIndex: Int): Converter = {
    require(fieldIndex == 0)
    elementConverter
  }
  override def start(): Unit = {
    if (currentValue == null) {
      parentDataHolder.put(index, values)
    }
    currentValue = null
  }
  override def end(): Unit = {
    val _ = values += currentValue
  }

  private[this] def createPrimitiveElementConverter(): Converter =
    ConverterFactory(
      elementType,
      index,
      new ValueHolder {
        override def put(index: Int, value: Any): Unit =
          currentValue = value
        override def reset(): Unit = {}
        override def getValues(): Seq[Any] = Seq.empty[Any]
      }
    )
}

sealed trait ArrayConverter {
  val index: Int
  val parentDataHolder: ValueHolder
  val dataHolder = new AppendedValueHolder()
  val elementConverter = createElementConverter()

  def getConverter(fieldIndex: Int): Converter = {
    require(fieldIndex == 0)
    elementConverter
  }
  def start(): Unit = dataHolder.reset()
  def end(): Unit = parentDataHolder.put(index, dataHolder.getValues())

  def createElementConverter(): Converter
}

final case class ArrayPrimitiveConverter(
  elementType: PrimitiveType,
  val index: Int,
  val parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter
    with ArrayConverter {

  override def createElementConverter(): Converter =
    ConverterFactory(elementType, index, dataHolder)
}

final case class ArrayGroupConverter(
  elementType: Type,
  val index: Int,
  val parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter
    with ArrayConverter {

  override def createElementConverter(): Converter = new GroupConverter {
    val innerConverter = ConverterFactory(elementType, index, dataHolder)
    override def getConverter(index: Int): Converter = innerConverter
    override def start(): Unit = {}
    override def end(): Unit = {}
  }
}

final case class MapConverter(groupType: GroupType, index: Int, parentDataHolder: ValueHolder)
    extends GroupConverter
    with ParquetConverter {
  private[this] val keysDataHolder = new AppendedValueHolder()
  private[this] val valuesDataHolder = new AppendedValueHolder()
  private[this] val converter = createMapConverter()

  override def getConverter(fieldIndex: Int): Converter = {
    require(fieldIndex < 2)
    converter
  }
  override def start(): Unit = {
    keysDataHolder.reset()
    valuesDataHolder.reset()
  }
  override def end(): Unit = {
    val keys = keysDataHolder.getValues()
    val values = valuesDataHolder.getValues()
    val map = keys.zip(values).toMap
    parentDataHolder.put(index, map)
  }

  private[this] def createMapConverter(): Converter = new GroupConverter {
    val mapType = groupType.getFields().get(0).asGroupType()
    val mapKeyType = mapType.getFields().get(0)
    val mapValueType = mapType.getFields().get(1)
    val keysConverter = ConverterFactory(mapKeyType, index, keysDataHolder)
    val valuesConverter = ConverterFactory(mapValueType, index, valuesDataHolder)

    override def getConverter(index: Int): Converter =
      if (index == 0) {
        keysConverter
      } else {
        valuesConverter
      }
    override def start(): Unit = {}
    override def end(): Unit = {}
  }
}

@SuppressWarnings(Array("org.wartremover.contrib.warts.UnsafeInheritance"))
class AbstractStructConverter(groupType: GroupType, index: Int, parentDataHolder: ValueHolder)
    extends GroupConverter {
  private[this] val size = groupType.getFieldCount()
  protected[this] val dataHolder = IndexedValueHolder(size)
  private[this] val converters = createFieldConverters()

  override def getConverter(fieldIndex: Int): Converter = converters(fieldIndex)
  override def start(): Unit = dataHolder.reset()
  override def end(): Unit = parentDataHolder.put(index, dataHolder.getValues())

  private[this] def createFieldConverters(): Array[Converter] = {
    val converters = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      converters(i) = ConverterFactory(groupType.getType(i), i, dataHolder)
    }
    converters
  }
}

final case class StructConverter(groupType: GroupType, index: Int, parentDataHolder: ValueHolder)
    extends AbstractStructConverter(groupType, index, parentDataHolder)
    with ParquetConverter {

  override def end(): Unit = {
    val map = dataHolder
      .getValues()
      .zipWithIndex
      .map {
        case (value, i) => (groupType.getFieldName(i), value)
      }
      .toMap
    parentDataHolder.put(index, map)
  }
}
