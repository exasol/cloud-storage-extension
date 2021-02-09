package com.exasol.cloudetl.parquet.converter

import com.exasol.common.json.JsonMapper

import org.apache.parquet.schema.GroupType
import org.apache.parquet.schema.Type.Repetition

/**
 * The main Parquet data types to [[com.exasol.common.data.Row]]
 * converter class.
 *
 * It calls separate converters for each field of the Parquet schema.
 *
 * @param schema the main schema for the Parquet file
 */
final case class ParquetRootConverter(schema: GroupType)
    extends AbstractStructConverter(schema, -1, EmptyValueHolder) {

  /**
   * Returns deserialized Parquet field values for this schema.
   *
   * It converts the non-primitive field types to JSON string.
   */
  def getResult(): Seq[Any] =
    dataHolder.getValues().zipWithIndex.map {
      case (value, i) =>
        val fieldType = schema.getType(i)
        if (fieldType.isPrimitive() && !fieldType.isRepetition(Repetition.REPEATED)) {
          value
        } else {
          JsonMapper.toJson(value)
        }
    }

  override def endOperation(): Unit = {}

}
