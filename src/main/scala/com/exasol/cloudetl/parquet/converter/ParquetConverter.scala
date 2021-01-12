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
 * The Parquet reader calls the [[ParquetRootConverter]] for the top
 * level Parquet schema. The root converter then generates subsequent
 * converters using [[ParquetConverterFactory]] for each type.
 *
 * The sealed trait ensures that all the implementations should be in
 * this file.
 */
sealed trait ParquetConverter

/**
 * A default converter for Parquet primitive types.
 *
 * The Parquet schema as below would be converted with this converter:
 *
 * {{{
 * message parquet_file_schema {
 *   required boolean column_boolean;
 *   required int32 column_int;
 *   required int64 column_long;
 *   required float column_float;
 *   required double column_double;
 *   required binary column_string;
 * }
 * }}}
 */
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

/**
 * A converter for Parquet binary type with {@code STRING} or {@code
 * UTF8} annotation.
 *
 * Since string types are stored using the dictiony encoding, the
 * converter uses the dictionary metadata when decoding.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required binary name (UTF8);
 *   required binary surname (STRING);
 * }
 * }}}
 */
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

/**
 * A converter for {@code DECIMAL} annotated Parquet types.
 *
 * The decimal annotation can be used for the following Parquet types:
 * {@code INT32}, {@code INT64}, {@code FIXED_LEN_BYTE_ARRAY} and {@code
 * BINARY}.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required int32 decimal_int (DECIMAL(9,2));
 *   required int64 decimal_long (DECIMAL(18,2));
 *   required fixed_len_byte_array(20) decimal_fixed (DECIMAL(20,2));
 *   required binary decimal_binary (DECIMAL(30,2));
 * }
 * }}}
 */
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

/**
 * A converter for Parquet {@code INT64} with {@code TIMESTAMP_MILLIS}
 * annotation.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required int64 timestamp (TIMESTAMP_MILLIS);
 * }
 * }}}
 */
final case class ParquetTimestampMillisConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addLong(value: Long): Unit =
    holder.put(index, DateTimeUtil.getTimestampFromMillis(value))
}

/**
 * A converter for Parquet {@code INT96} type.
 *
 * It is converted into a timestamp with nanosecond precision.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required int96 timestamp_nanos;
 * }
 * }}}
 */
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

/**
 * A converter for Parquet {@code INT32} with {@code DATE} annotation.
 *
 * The integer value represents the number of days since the epoch.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required int32 date (DATE);
 * }
 * }}}
 */
final case class ParquetDateConverter(index: Int, holder: ValueHolder)
    extends PrimitiveConverter
    with ParquetConverter {
  override def addInt(value: Int): Unit = {
    val date = DateTimeUtil.daysToDate(value.toLong)
    holder.put(index, date)
  }
}

/**
 * A Parquet converter for the
 * [[org.apache.parquet.schema.Type.Repetition.REPEATED]] group type.
 *
 * It is converted into an array of key value maps.
 *
 * The following schema is converted with this converter:
 * {{{
 * message parquet_file_schema {
 *   repeated group person {
 *     required binary name (UTF8);
 *     optional int32 age;
 *   }
 * }
 * }}}
 */
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
      converters(i) = ParquetConverterFactory(
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

/**
 * A Parquet converter for the
 * [[org.apache.parquet.schema.Type.Repetition.REPEATED]] group with a
 * single type.
 *
 * It is converted into an array of values.
 *
 * The following schema is converted with this converter:
 * {{{
 * message parquet_file_schema {
 *   repeated group person {
 *     required binary name (UTF8);
 *   }
 * }
 * }}}
 */
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
    if (fieldIndex != 0) {
      throw new IllegalArgumentException(
        s"Illegal index '$fieldIndex' to repeated primitive converter. It should be only '0'."
      )
    }
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
    ParquetConverterFactory(
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

/**
 * A Parquet converter for the {@code LIST} annotated types.
 */
sealed trait ArrayConverter {
  val index: Int
  val parentDataHolder: ValueHolder
  val dataHolder = new AppendedValueHolder()
  val elementConverter = createElementConverter()

  def getConverter(fieldIndex: Int): Converter = {
    if (fieldIndex != 0) {
      throw new IllegalArgumentException(
        s"Illegal index '$fieldIndex' to array converter. It should be only '0'."
      )
    }
    elementConverter
  }
  def start(): Unit = dataHolder.reset()
  def end(): Unit = parentDataHolder.put(index, dataHolder.getValues())

  def createElementConverter(): Converter
}

/**
 * A converter for the non standard Parquet list annotated group with a
 * single repeated type.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   optional group heights (LIST) {
 *     repeated int32 height;
 *   }
 * }
 * }}}
 */
final case class ArrayPrimitiveConverter(
  elementType: PrimitiveType,
  val index: Int,
  val parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter
    with ArrayConverter {

  override def createElementConverter(): Converter =
    ParquetConverterFactory(elementType, index, dataHolder)
}

/**
 * A converter for the standard 3-level Parquet list annotated group
 * type.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   optional group prices (LIST) {
 *     repeated group list {
 *       required double price;
 *     }
 *   }
 * }
 * }}}
 */
final case class ArrayGroupConverter(
  elementType: Type,
  val index: Int,
  val parentDataHolder: ValueHolder
) extends GroupConverter
    with ParquetConverter
    with ArrayConverter {

  override def createElementConverter(): Converter = new GroupConverter {
    val innerConverter = ParquetConverterFactory(elementType, index, dataHolder)
    override def getConverter(index: Int): Converter = innerConverter
    override def start(): Unit = {}
    override def end(): Unit = {}
  }
}

/**
 * A Parquet converter for the {@code MAP} annotated type.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   optional group map (MAP) {
 *     repeated group key_value {
 *       required binary key (UTF8);
 *       required int64 value;
 *     }
 *   }
 * }
 * }}}
 */
final case class MapConverter(groupType: GroupType, index: Int, parentDataHolder: ValueHolder)
    extends GroupConverter
    with ParquetConverter {
  private[this] val keysDataHolder = new AppendedValueHolder()
  private[this] val valuesDataHolder = new AppendedValueHolder()
  private[this] val converter = createMapConverter()

  override def getConverter(fieldIndex: Int): Converter = {
    if (fieldIndex < 0 || fieldIndex > 1) {
      throw new IllegalArgumentException(
        s"Illegal index '$fieldIndex' to map converter. It should be " +
          "either '0' for keys converter or '1' for values converter."
      )
    }
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
    val keysConverter = ParquetConverterFactory(mapKeyType, index, keysDataHolder)
    val valuesConverter = ParquetConverterFactory(mapValueType, index, valuesDataHolder)

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

/**
 * An abstract base class for Parquet struct converters.
 */
abstract class AbstractStructConverter(
  groupType: GroupType,
  index: Int,
  parentDataHolder: ValueHolder
) extends GroupConverter {
  private[this] val size = groupType.getFieldCount()
  protected[this] val dataHolder = IndexedValueHolder(size)
  private[this] val converters = createFieldConverters()

  override final def getConverter(fieldIndex: Int): Converter = converters(fieldIndex)
  override final def start(): Unit = dataHolder.reset()
  override final def end(): Unit = endOperation()

  def endOperation(): Unit

  private[this] def createFieldConverters(): Array[Converter] = {
    val converters = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      converters(i) = ParquetConverterFactory(groupType.getType(i), i, dataHolder)
    }
    converters
  }
}

/**
 * A converter for the Parquet nested group.
 *
 * The following schema fits this converter:
 * {{{
 * message parquet_file_schema {
 *   required binary name (UTF8);
 *   required group values {
 *     optional int32 height;
 *     optional int32 weight;
 *   }
 * }
 * }}}
 */
final case class StructConverter(groupType: GroupType, index: Int, parentDataHolder: ValueHolder)
    extends AbstractStructConverter(groupType, index, parentDataHolder)
    with ParquetConverter {

  override def endOperation(): Unit = {
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
