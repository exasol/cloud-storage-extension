package com.exasol.cloudetl.scriptclasses

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.source._
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.Row

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path

/**
 * A importer class that reads and imports data into Exasol database.
 */
object FilesDataImporter extends LazyLogging {

  /**
   * Reads files and emits their data into Exasol iterator.
   *
   * @param metadata an Exasol metadata object
   * @param iterator an Exasol iterator object
   */
  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val storageProperties = StorageProperties(iterator.getString(1), metadata)
    val fileFormat = storageProperties.getFileFormat()
    val bucket = Bucket(storageProperties)

    val files = groupFiles(iterator, 2)
    val nodeId = metadata.getNodeId
    val vmId = metadata.getVmId
    logger.info(s"The total number of files for node: $nodeId, vm: $vmId is '${files.size}'.")

    files.foreach { file =>
      logger.debug(s"Importing from file: '$file'")
      val source =
        Source(fileFormat, new Path(file), bucket.getConfiguration(), bucket.fileSystem)
      readAndEmit(source.stream(), iterator)
      source.close()
    }
  }

  private[this] def groupFiles(
    iterator: ExaIterator,
    fileStartingIndex: Int
  ): Seq[String] = {
    val files = ListBuffer[String]()
    do {
      files.append(iterator.getString(fileStartingIndex))
    } while (iterator.next())
    files.toSeq
  }

  private[this] def readAndEmit(rowIterator: Iterator[Row], ctx: ExaIterator): Unit =
    rowIterator.foreach { row =>
      val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
      ctx.emit(columns: _*)
    }

}
