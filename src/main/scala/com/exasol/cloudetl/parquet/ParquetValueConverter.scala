package com.exasol.cloudetl.parquet

import java.util.List

import com.exasol.common.json.JsonMapper
import com.exasol.parquetio.data.Row

import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.Type.Repetition

/**
 * A Parquet value converter class that transforms nested values to JSON strings.
 */
final case class ParquetValueConverter(schema: MessageType) {
  private[this] val size = schema.getFields.size()
  private[this] var convertedValues = Array.ofDim[Object](size)

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
      value
    } else {
      JsonMapper.toJson(value)
    }
  }

}
