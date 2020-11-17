package com.exasol.cloudetl.parquet.converter

import org.apache.parquet.io.api.Converter
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._
import org.apache.parquet.schema.Type

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
    fieldIndex: Int,
    parquetType: Type,
    parentDataHolder: ValueHolder
  ): Converter = {
    if (!parquetType.isPrimitive()) {
      throw new UnsupportedOperationException("Currently only primitive types are supported")
    }
    val primitiveType = parquetType.asPrimitiveType()
    primitiveType.getPrimitiveTypeName() match {
      case BOOLEAN => ParquetPrimitiveConverter(fieldIndex, parentDataHolder)
      case DOUBLE  => ParquetPrimitiveConverter(fieldIndex, parentDataHolder)
      case FLOAT   => ParquetPrimitiveConverter(fieldIndex, parentDataHolder)
      case BINARY  => createBinaryConverter(fieldIndex, parentDataHolder, primitiveType)
      case FIXED_LEN_BYTE_ARRAY =>
        createFixedByteArrayConverter(fieldIndex, parentDataHolder, primitiveType)
      case INT32 => createIntegerConverter(fieldIndex, parentDataHolder, primitiveType)
      case INT64 => createLongConverter(fieldIndex, parentDataHolder, primitiveType)
      case INT96 => ParquetTimestampInt96Converter(fieldIndex, parentDataHolder)
    }
  }

  private[this] def createBinaryConverter(
    index: Int,
    holder: ValueHolder,
    primitiveType: PrimitiveType
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.UTF8    => ParquetStringConverter(index, holder)
    case OriginalType.DECIMAL => ParquetDecimalConverter(index, holder, primitiveType)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createFixedByteArrayConverter(
    index: Int,
    holder: ValueHolder,
    primitiveType: PrimitiveType
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.DECIMAL => ParquetDecimalConverter(index, holder, primitiveType)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createIntegerConverter(
    index: Int,
    holder: ValueHolder,
    primitiveType: PrimitiveType
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.DATE    => ParquetDateConverter(index, holder)
    case OriginalType.DECIMAL => ParquetDecimalConverter(index, holder, primitiveType)
    case _                    => ParquetPrimitiveConverter(index, holder)
  }

  private[this] def createLongConverter(
    index: Int,
    holder: ValueHolder,
    primitiveType: PrimitiveType
  ): Converter = primitiveType.getOriginalType() match {
    case OriginalType.TIMESTAMP_MILLIS => ParquetTimestampMillisConverter(index, holder)
    case OriginalType.DECIMAL          => ParquetDecimalConverter(index, holder, primitiveType)
    case _                             => ParquetPrimitiveConverter(index, holder)
  }

}
