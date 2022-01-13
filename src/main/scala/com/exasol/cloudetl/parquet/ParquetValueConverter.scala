package com.exasol.cloudetl.parquet

import java.util.List

import com.exasol.common.json.JsonMapper
import com.exasol.parquetio.data.Row

import org.apache.parquet.schema.LogicalTypeAnnotation.uuidType
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY
import org.apache.parquet.schema.Type.Repetition
import java.util.UUID

/**
 * A Parquet value converter class that transforms nested values to JSON strings.
 */
final case class ParquetValueConverter(schema: MessageType) {
  private[this] val size = schema.getFields.size()
  private[this] var convertedValues = Array.ofDim[Object](size)

  /**
   * Converts {@link Row} into array of values.
   *
   * It maps the complex types (e.g, {@code MAP}, {@code LIST}) into JSON strings. The result array is emitted as an
   * Exasol table row.
   *
   * @param row a Parquet row
   * @return converted array of values
   */
  def convert(row: Row): Array[Object] = convertParquetComplexValuesToJSON(row.getValues())

  private[this] def convertParquetComplexValuesToJSON(values: List[Object]): Array[Object] = {
    var i = 0
    while (i < size) {
      convertedValues(i) = convertValue(i, values.get(i))
      i += 1
    }
    convertedValues
  }

  private[this] def convertValue(i: Int, value: Object): Object = {
    val fieldType = schema.getType(i)
    if (fieldType.isPrimitive() && !fieldType.isRepetition(Repetition.REPEATED)) {
      convertPrimitiveValue(fieldType.asPrimitiveType(), value)
    } else {
      JsonMapper.toJson(value)
    }
  }

  private[this] def convertPrimitiveValue(primitiveType: PrimitiveType, value: Object): Object = {
    val logicalType = primitiveType.getLogicalTypeAnnotation()
    primitiveType.getPrimitiveTypeName() match {
      case FIXED_LEN_BYTE_ARRAY if logicalType == uuidType() => value.asInstanceOf[UUID].toString()
      case _                                                 => value
    }
  }

}
