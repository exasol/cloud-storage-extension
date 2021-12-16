package com.exasol.cloudetl.scriptclasses

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.helper.ExasolColumnValueProvider
import com.exasol.cloudetl.sink.BatchSizedSink
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.Row

import com.typesafe.scalalogging.LazyLogging

/**
 * A data exporter class that exports table data into a filesystem.
 */
object TableDataExporter extends LazyLogging {

  private[this] val STORAGE_PROPERTIES_INDEX = 1
  private[this] val SOURCE_COLUMNS_INDEX = 2
  private[this] val FIRST_COLUMN_INDEX = 3

  /**
   * Reads the table data and saves them to external filesystem.
   *
   * @param metadata an Exasol metadata object
   * @param iterator an Exasol iterator object
   */
  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val storageProperties = StorageProperties(iterator.getString(STORAGE_PROPERTIES_INDEX), metadata)
    val bucket = Bucket(storageProperties)
    val sourceColumnNames = iterator.getString(SOURCE_COLUMNS_INDEX).split("\\.")
    val columns = getColumns(metadata, sourceColumnNames.toSeq)
    val nodeId = metadata.getNodeId()
    val vmId = metadata.getVmId()
    val sink = new BatchSizedSink(nodeId, vmId, iterator.size(), columns, bucket)
    logger.info(s"Starting export from node: $nodeId, vm: $vmId.")
    do {
      sink.write(getRow(iterator, columns))
    } while (iterator.next())
    sink.close()
    iterator.emit(sink.getTotalRecords())
    logger.info(s"Exported '${sink.getTotalRecords()}' records from node '$nodeId' and vm '$vmId'.")
  }

  private[this] def getRow(iterator: ExaIterator, columns: Seq[ExaColumnInfo]): Row = {
    val columnValueProvider = ExasolColumnValueProvider(iterator)
    val values = ListBuffer[Any]()
    var index = 0
    while (index < columns.size) {
      val value = columnValueProvider.getColumnValue(FIRST_COLUMN_INDEX + index, columns(index))
      val _ = values.append(value)
      index += 1
    }
    Row(values.toSeq)
  }

  /**
   * Creates a sequence of [[ExaColumnInfo]] columns using an Exasol
   * [[ExaMetadata]] input column methods.
   *
   * Set the name of the column using `srcColumnNames` parameter.
   * Additionally, set the precision, scale and length using
   * corresponding functions on Exasol metadata for input columns.
   *
   * @param meta An Exasol [[ExaMetadata]] metadata
   * @param sourceColumnNames A sequence of column names per each input column in metadata
   * @return A sequence of [[ExaColumnInfo]] columns
   */
  private[this] def getColumns(meta: ExaMetadata, sourceColumnNames: Seq[String]): Seq[ExaColumnInfo] = {
    val totalColumnCount = meta.getInputColumnCount().toInt
    val columns = ListBuffer[ExaColumnInfo]()
    for { idx <- FIRST_COLUMN_INDEX until totalColumnCount } {
      columns.append(
        ExaColumnInfo(
          name = sourceColumnNames(idx - FIRST_COLUMN_INDEX),
          `type` = meta.getInputColumnType(idx),
          precision = meta.getInputColumnPrecision(idx).toInt,
          scale = meta.getInputColumnScale(idx).toInt,
          length = meta.getInputColumnLength(idx).toInt,
          isNullable = true
        )
      )
    }
    columns.toSeq
  }

}
