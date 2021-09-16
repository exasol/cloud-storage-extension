package com.exasol.cloudetl.emitter

import java.lang.Long

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.parquetio.splitter.ParquetFileSplitter

import com.typesafe.scalalogging.LazyLogging
import org.apache.parquet.hadoop.util.HadoopInputFile

/**
 * A class that calculates and emits file based metadata from a storage path.
 *
 * @param properties a storage properties to create configurations
 * @param parallelism a parallelism number to create groups
 */
final case class FilesMetadataEmitter(properties: StorageProperties, parallelism: Int)
    extends Emitter
    with LazyLogging {

  private[this] val bucket = Bucket(properties)
  private[this] val paths = bucket.getPaths()
  private[this] val fileFormat = properties.getFileFormat()

  override def emit(context: ExaIterator): Unit = {
    logger.info(s"Found total of '${paths.size}' files in path '${bucket.bucketPath}'.")
    if (fileFormat != FileFormat.PARQUET) {
      emitRegularFilesMetadata(context)
    } else {
      emitParquetFilesMetadata(context)
    }
  }

  private[this] def emitRegularFilesMetadata(context: ExaIterator): Unit = {
    var index = 0L
    paths.foreach { case filename =>
      context.emit(filename.toString(), s"${index % parallelism}", Long.valueOf(0), Long.valueOf(0))
      index += 1L
    }
  }

  private[this] def emitParquetFilesMetadata(context: ExaIterator): Unit = {
    val chunkSize = properties.getChunkSize()
    val chunks = ListBuffer.empty[FilenameChunkInterval]
    paths.foreach { case filename =>
      val inputFile = HadoopInputFile.fromPath(filename, bucket.getConfiguration())
      val splits = new ParquetFileSplitter(inputFile, chunkSize).getSplits()
      for { i <- 0 until splits.size() } {
        chunks.append(
          FilenameChunkInterval(filename.toString(), splits.get(i).getStartPosition(), splits.get(i).getEndPosition())
        )
      }
    }
    val partitioner = Partitioner(chunks.size, parallelism)
    var index = 0L
    var count = 0L
    chunks.foreach { case interval =>
      val filename = interval.filename
      val start = interval.start
      val end = interval.end
      logger.info(s"Emitting filename metadata $filename -> [$start ... $end) on index $index.")
      context.emit(filename, s"$index", interval.start, interval.end)
      count += 1
      if (partitioner.isNewPartition(count)) {
        index += 1
        count = 0
      }
    }
  }

  private[this] case class FilenameChunkInterval(filename: String, start: Long, end: Long)

  private[this] case class Partitioner(total: Int, numberOfPartitions: Int) {
    val partitionSize: Long = math.floor(total / numberOfPartitions.toDouble).toLong
    var leftOvers: Long = total % numberOfPartitions

    def isNewPartition(currentCount: Long): Boolean =
      if (leftOvers > 0 && currentCount >= partitionSize + 1) {
        leftOvers -= 1
        true
      } else if (leftOvers == 0 && currentCount >= partitionSize) {
        true
      } else {
        false
      }
  }

}
