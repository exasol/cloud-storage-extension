package com.exasol.cloudetl.sink

import java.lang.{Long => JLong}
import java.util.UUID

import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.helper.ParquetSchemaConverter
import com.exasol.cloudetl.parquet.ParquetRowWriter
import com.exasol.cloudetl.parquet.ParquetWriteOptions
import com.exasol.common.data.Row

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.apache.parquet.schema.MessageType

/**
 * A specific [[Sink]] implementation with records per file request.
 *
 * Given the number of records for each file and total number of
 * records, it is possible to balance the exported file sizes. Thus,
 * small files are not created for the last records.
 */
final class BatchSizedSink(
  nodeId: Long,
  vmId: String,
  numOfRecords: Long,
  columns: Seq[ExaColumnInfo],
  override val bucket: Bucket
) extends Sink[Row]
    with LazyLogging {

  private[this] val DEFAULT_BATCH_SIZE: Int = 100000
  private[this] val requestedBatchSize: Int =
    bucket.properties.get("EXPORT_BATCH_SIZE").fold(DEFAULT_BATCH_SIZE)(_.toInt)
  private[this] val isParquetLowercaseSchema: Boolean = bucket.properties.isParquetLowercaseSchema()

  private[this] val numOfBuckets: Long = math.ceil(numOfRecords / requestedBatchSize.toDouble).toLong
  private[this] val batchSize: Long = math.floor(numOfRecords / numOfBuckets.toDouble).toLong
  private[this] var leftOvers: Long = numOfRecords % numOfBuckets

  private[this] var writer: Writer[Row] = null
  private[this] var recordsCount: Long = 0
  private[this] var totalRecords: Long = 0

  /** Returns the total number of records written so far. */
  def getTotalRecords(): JLong = totalRecords

  /** @inheritdoc */
  override def createWriter(path: String): Writer[Row] = new Writer[Row] {
    val newPath = new Path(bucket.bucketPath, path)
    val options = ParquetWriteOptions(bucket.properties)
    val writer = ParquetRowWriter(newPath, bucket.getConfiguration(), getParquetMessageType(), options)

    override def write(value: Row): Unit =
      writer.write(value)

    override def close(): Unit =
      writer.close()
  }

  private[this] def getParquetMessageType(): MessageType =
    ParquetSchemaConverter(isParquetLowercaseSchema).createParquetMessageType(columns, "exasol_export_schema")

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
    val parquetOptions = ParquetWriteOptions(bucket.properties)
    if (parquetOptions.compressionCodec == CompressionCodecName.UNCOMPRESSED) {
      s"exa_export_${nodeId}_${vmId}_$uuidStr.parquet"
    } else {
      val compressionExtension = parquetOptions.compressionCodec.getExtension()
      s"exa_export_${nodeId}_${vmId}_$uuidStr$compressionExtension.parquet"
    }
  }

}
