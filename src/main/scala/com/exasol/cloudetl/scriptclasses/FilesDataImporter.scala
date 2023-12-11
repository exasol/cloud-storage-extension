package com.exasol.cloudetl.scriptclasses

import java.util.ArrayList
import java.util.List

import scala.collection.mutable.HashMap

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.emitter.FilesDataEmitter
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.parquetio.data.ChunkInterval
import com.exasol.parquetio.data.ChunkIntervalImpl

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
    var intervalCount = 0
    files.foreach { case (filename, intervals) =>
      logger.info(
        s"Importing ${intervals.size()} intervals '${getIntervalString(intervals)}' "
          + s"for file $filename on node '$nodeId' and vm '$vmId'."
      )
      intervalCount += intervals.size()
    }
    logger.info(s"Importing ${files.size} files with $intervalCount intervals")
    FilesDataEmitter(storageProperties, files).emit(iterator)
  }

  def collectFiles(iterator: ExaIterator): Map[String, List[ChunkInterval]] = {
    val files = new HashMap[String, List[ChunkInterval]]()
    do {
      val filename = iterator.getString(FILENAME_STARTING_INDEX)
      val startIndex = iterator.getLong(FILENAME_STARTING_INDEX + 1)
      val endIndex = iterator.getLong(FILENAME_STARTING_INDEX + 2)
      if (!files.contains(filename)) {
        val _ = files.put(filename, new ArrayList[ChunkInterval]())
      }
      files
        .get(filename)
        .map { list =>
          val _ = list.add(new ChunkIntervalImpl(startIndex, endIndex))
        }
    } while (iterator.next())
    files.toMap
  }

  private[this] def getIntervalString(intervals: List[ChunkInterval]): String = {
    val sb = new StringBuilder()
    for { i <- 0 until intervals.size() } {
      if (i > 0) {
        sb.append(", ")
      }
      sb.append("[")
        .append(intervals.get(i).getStartPosition())
        .append(",")
        .append(intervals.get(i).getEndPosition())
        .append(")")
    }
    sb.toString()
  }

}
