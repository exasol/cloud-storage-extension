package com.exasol.cloudetl.parquet.converter

import org.apache.parquet.io.api.Converter
import org.apache.parquet.schema.GroupType
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._
import org.apache.parquet.schema.Type
import org.apache.parquet.schema.Type.Repetition

/**
 * Parquet field data type converter factory class.
 */
object ConverterFactory {

  /**
   * Creates Parquet converters that convert schema field value type
   * into a Java datatype.
   *
   * If Parquet field is a complex datatype, then it is converted to the
   * JSON string.
   *
   * @param fieldIndex a field index in the Parquet schema
   * @param parquetType a Parquet schema field type
   * @param parentDataHolder a parent data holder for this field
   * @return specific data converter for the field type
   */
  def apply(
    parquetType: Type,
    fieldIndex: Int,
    parentDataHolder: ValueHolder
  ): Converter =
    if (parquetType.isPrimitive()) {
      createPrimitiveConverter(parquetType.asPrimitiveType(), fieldIndex, parentDataHolder)
    } else {
      createComplexConverter(parquetType, fieldIndex, parentDataHolder)
    }

  private[this] def createPrimitiveConverter(
    parquetType: PrimitiveType,
    index: Int,
    parentHolder: ValueHolder
  ): Converter =
    parquetType.getPrimitiveTypeName() match {
      case BOOLEAN              => ParquetPrimitiveConverter(index, parentHolder)
      case DOUBLE               => ParquetPrimitiveConverter(index, parentHolder)
      case FLOAT                => ParquetPrimitiveConverter(index, parentHolder)
      case BINARY               => createBinaryConverter(parquetType, index, parentHolder)
      case FIXED_LEN_BYTE_ARRAY => createFixedByteArrayConverter(parquetType, index, parentHolder)
      case INT32                => createIntegerConverter(parquetType, index, parentHolder)
      case INT64                => createLongConverter(parquetType, index, parentHolder)
      case INT96                => ParquetTimestampInt96Converter(index, parentHolder)
    }

  private[this] def createComplexConverter(
    parquetType: Type,
    index: Int,
    parentHolder: ValueHolder
  ): Converter = {
    val groupType = parquetType.asGroupType()
    parquetType.getOriginalType() match {
      case OriginalType.LIST => createArrayConverter(groupType.getType(0), index, parentHolder)
      case OriginalType.MAP  => MapConverter(parquetType.asGroupType(), index, parentHolder)
      case _ =>
        if (groupType.isRepetition(Repetition.REPEATED)) {
          createRepeatedConverter(groupType, index, parentHolder)
        } else {
          StructConverter(groupType, index, parentHolder)
        }
    }
  }

  private[this] def createArrayConverter(
    repeatedType: Type,
    index: Int,
    holder: ValueHolder
  ): Converter =
    if (repeatedType.isPrimitive()) {
      ArrayPrimitiveConverter(repeatedType.asPrimitiveType(), index, holder)
    } else if (repeatedType.asGroupType().getFieldCount() > 1) {
      ArrayGroupConverter(repeatedType, index, holder)
    } else {
      val innerElementType = repeatedType.asGroupType().getType(0)
      ArrayGroupConverter(innerElementType, index, holder)
    }

  private[this] def createRepeatedConverter(
    groupType: GroupType,
    index: Int,
    holder: ValueHolder
  ): Converter =
    if (groupType.getFieldCount() > 1) {
      RepeatedGroupConverter(groupType, index, holder)
    } else {
      val innerPrimitiveType = groupType.getType(0).asPrimitiveType()
      RepeatedPrimitiveConverter(innerPrimitiveType, index, holder)
    }

  private[this] def createBinaryConverter(
    primitiveType: PrimitiveType,
    index: Int,
    holder: ValueHolder
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.UTF8    => ParquetStringConverter(index, holder)
    case OriginalType.DECIMAL => ParquetDecimalConverter(primitiveType, index, holder)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createFixedByteArrayConverter(
    primitiveType: PrimitiveType,
    index: Int,
    holder: ValueHolder
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.DECIMAL => ParquetDecimalConverter(primitiveType, index, holder)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createIntegerConverter(
    primitiveType: PrimitiveType,
    index: Int,
    holder: ValueHolder
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.DATE    => ParquetDateConverter(index, holder)
    case OriginalType.DECIMAL => ParquetDecimalConverter(primitiveType, index, holder)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createLongConverter(
    primitiveType: PrimitiveType,
    index: Int,
    holder: ValueHolder
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.TIMESTAMP_MILLIS => ParquetTimestampMillisConverter(index, holder)
    case OriginalType.DECIMAL          => ParquetDecimalConverter(primitiveType, index, holder)
    case _                             => ParquetPrimitiveConverter(index, holder)
  }

}
