package com.exasol.cloudetl.parquet

import com.exasol.cloudetl.source.ValueConverter
import com.exasol.common.data.Row
import com.exasol.common.json.JsonMapper

import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.Type.Repetition

/**
 * A Parquet value converter class that transforms nested values to JSON strings.
 */
final case class ParquetValueConverter(schema: MessageType) extends ValueConverter {

  override def convert(values: Iterator[Row]): Iterator[Row] =
    values.map(row => Row(mapComplexValuesToJSON(row.getValues())))

  private[this] def mapComplexValuesToJSON(values: Seq[Any]): Seq[Any] = {
    val size = values.size
    val convertedValues = Array.ofDim[Any](size)
    var i = 0
    while (i < size) {
      convertedValues(i) = convertValue(i, values(i))
      i += 1
    }
    convertedValues.toSeq
  }

  private[this] def convertValue(i: Int, value: Any): Any = {
    val fieldType = schema.getType(i)
    if (fieldType.isPrimitive() && !fieldType.isRepetition(Repetition.REPEATED)) {
      value
    } else {
      JsonMapper.toJson(value)
    }
  }
}
