package com.exasol.cloudetl.parquet.converter

import com.exasol.common.json.JsonMapper

import org.apache.parquet.io.api.Converter
import org.apache.parquet.io.api.GroupConverter
import org.apache.parquet.schema.GroupType

/**
 * The main Parquet data types to [[com.exasol.common.data.Row]]
 * converter class.
 *
 * It calls separate converters for each field of the Parquet schema.
 *
 * @param schema the main schema for the Parquet file
 */
final case class ParquetRootConverter(schema: GroupType) extends GroupConverter {
  private[this] val size = schema.getFieldCount()
  private[this] val dataHolder = IndexedValueHolder(size)
  private[this] val converters = getFieldConverters()

  override def getConverter(fieldIndex: Int): Converter = converters(fieldIndex)
  override def start(): Unit = dataHolder.reset()
  override def end(): Unit = {}

  /**
   * Returns deserialized Parquet field values for this schema.
   *
   * It converts the non-primitive field types to JSON string.
   */
  def getResult(): Seq[Any] = dataHolder.getValues().zipWithIndex.map {
    case (value, i) =>
      if (schema.getType(i).isPrimitive()) {
        value
      } else {
        JsonMapper.toJson(value)
      }
  }

  private[this] def getFieldConverters(): Array[Converter] = {
    val converters = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      converters(i) = ConverterFactory(schema.getType(i), i, dataHolder)
    }
    converters
  }
}
