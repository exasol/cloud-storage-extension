package com.exasol.cloudetl.scriptclasses

import java.util.ArrayList
import java.util.List

import scala.collection.mutable.HashMap

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.emitter.FilesDataEmitter
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.parquetio.data.ChunkInterval
import com.exasol.parquetio.data.Interval

import com.typesafe.scalalogging.LazyLogging

/**
 * A importer class that reads and imports data into Exasol database.
 */
object FilesDataImporter extends LazyLogging {

  private[this] val STORAGE_PROPERTIES_INDEX = 1
  private[this] val FILENAME_STARTING_INDEX = 2

  /**
   * Reads files and emits their data into Exasol iterator.
   *
   * @param metadata an Exasol metadata object
   * @param iterator an Exasol iterator object
   */
  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val storageProperties = StorageProperties(iterator.getString(STORAGE_PROPERTIES_INDEX), metadata)
    val files = collectFiles(iterator)
    val nodeId = metadata.getNodeId()
    val vmId = metadata.getVmId()
    logger.info(s"Collected total number of '${files.size}' files for node '$nodeId' and vm '$vmId'.")
    FilesDataEmitter(storageProperties, files).emit(iterator)
  }

  private[this] def collectFiles(iterator: ExaIterator): Map[String, List[Interval]] = {
    val files = new HashMap[String, List[Interval]]()
    do {
      val filename = iterator.getString(FILENAME_STARTING_INDEX)
      val startIndex = iterator.getLong(FILENAME_STARTING_INDEX + 1)
      val endIndex = iterator.getLong(FILENAME_STARTING_INDEX + 2)
      if (!files.contains(filename)) {
        val _ = files.put(filename, new ArrayList[Interval]())
      }
      files
        .get(filename)
        .map { list =>
          val _ = list.add(new ChunkInterval(startIndex, endIndex))
        }
    } while (iterator.next())
    files.toMap
  }

}
