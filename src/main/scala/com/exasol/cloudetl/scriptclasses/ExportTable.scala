package com.exasol.cloudetl.scriptclasses

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.sink.BatchSizedSink
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.cloudetl.util.SchemaUtil

import com.typesafe.scalalogging.LazyLogging

@SuppressWarnings(Array("org.wartremover.warts.Var"))
object ExportTable extends LazyLogging {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val storageProperties = StorageProperties(iterator.getString(1), metadata)
    val bucket = Bucket(storageProperties)
    val srcColumnNames = iterator.getString(2).split("\\.")
    val firstColumnIdx = 3

    val nodeId = metadata.getNodeId
    val vmId = metadata.getVmId
    val columns = getColumns(metadata, srcColumnNames, firstColumnIdx)

    val sink = new BatchSizedSink(nodeId, vmId, iterator.size(), columns, bucket)

    logger.info(s"Starting export from node: $nodeId, vm: $vmId.")

    do {
      val row = getRow(iterator, firstColumnIdx, columns)
      sink.write(row)
    } while (iterator.next())

    sink.close()

    logger.info(s"Exported '${sink.getTotalRecords()}' records from node: $nodeId, vm: $vmId.")
  }

  private[this] def getRow(iter: ExaIterator, startIdx: Int, columns: Seq[ExaColumnInfo]): Row = {
    val vals = columns.zipWithIndex.map {
      case (col, idx) =>
        SchemaUtil.exaColumnToValue(iter, startIdx + idx, col)
    }
    Row(values = vals)
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
   * @param srcColumnNames A sequence of column names per each input
   *        column in metadata
   * @param startIdx A starting integer index to reference input column
   * @return A sequence of [[ExaColumnInfo]] columns
   */
  @SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
  private[this] def getColumns(
    meta: ExaMetadata,
    srcColumnNames: Seq[String],
    startIdx: Int
  ): Seq[ExaColumnInfo] = {
    val totalColumnCnt = meta.getInputColumnCount.toInt
    val columns = ListBuffer[ExaColumnInfo]()

    for { idx <- startIdx until totalColumnCnt } columns.append(
      ExaColumnInfo(
        name = srcColumnNames(idx - startIdx),
        `type` = meta.getInputColumnType(idx),
        precision = meta.getInputColumnPrecision(idx).toInt,
        scale = meta.getInputColumnScale(idx).toInt,
        length = meta.getInputColumnLength(idx).toInt,
        isNullable = true
      )
    )

    columns.toSeq
  }

}
