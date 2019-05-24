package com.exasol.cloudetl.sink

import java.util.UUID

import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.parquet.ParquetRowWriter
import com.exasol.cloudetl.parquet.ParquetWriteOptions
import com.exasol.cloudetl.util.SchemaUtil

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path

/**
 * A specific [[Sink]] implementation with records per file request.
 *
 * Given the number of records for each file and total number of
 * records, it is possible to balance the exported file sizes. Thus,
 * small files are not created for the last records.
 */
@SuppressWarnings(Array("org.wartremover.warts.Null", "org.wartremover.warts.Var"))
final class BatchSizedSink(
  nodeId: Long,
  vmId: String,
  numOfRecords: Long,
  columns: Seq[ExaColumnInfo],
  override val bucket: Bucket
) extends Sink[Row]
    with LazyLogging {

  // scalastyle:off null

  final val DEFAULT_BATCH_SIZE: Int = 100000

  private val requestedBatchSize: Int =
    bucket.properties.get("EXPORT_BATCH_SIZE").fold(DEFAULT_BATCH_SIZE)(_.toInt)

  private val numOfBuckets: Long = math.ceil(numOfRecords / requestedBatchSize.toDouble).toLong
  private val batchSize: Long = math.floor(numOfRecords / numOfBuckets.toDouble).toLong
  private var leftOvers: Long = numOfRecords % numOfBuckets

  private var writer: Writer[Row] = null
  private var recordsCount: Long = 0
  private var totalRecords: Long = 0

  /** Returns the total number of records written so far. */
  def getTotalRecords(): Long = totalRecords

  /** @inheritdoc */
  override def createWriter(path: String): Writer[Row] = new Writer[Row] {
    val newPath = new Path(bucket.bucketPath, path)
    val messageType = SchemaUtil.createParquetMessageType(columns, "exasol_export_schema")
    val options = ParquetWriteOptions(bucket.properties)
    val writer = ParquetRowWriter(newPath, bucket.getConfiguration(), messageType, options)

    override def write(value: Row): Unit =
      writer.write(value)

    override def close(): Unit =
      writer.close()
  }

  /**
   * @inheritdoc
   *
   * We check if the number of records written so far is more than the
   * next batch, if so closes current writer and creates a new one with
   * a different file path.
   */
  override def write(value: Row): Unit = {
    if (shouldRoll()) {
      openNewFile()
    }
    recordsCount += 1
    writer.write(value)
  }

  /** @inheritdoc */
  override def close(): Unit = {
    totalRecords += recordsCount
    recordsCount = 0
    if (writer != null) {
      writer.close()
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Return"))
  private def shouldRoll(): Boolean = {
    if (writer == null) {
      return true // scalastyle:ignore return
    }
    if (leftOvers > 0 && recordsCount >= batchSize + 1) {
      leftOvers -= 1
      true
    } else if (leftOvers == 0 && recordsCount >= batchSize) {
      true
    } else {
      false
    }
  }

  private def openNewFile(): Unit = {
    close()
    writer = createWriter(getNewPath())
  }

  private def getNewPath(): String = {
    val uuidStr = UUID.randomUUID.toString.replaceAll("-", "")
    s"exa_export_${nodeId}_${vmId}_$uuidStr.parquet"
  }

  // scalastyle:on
}
