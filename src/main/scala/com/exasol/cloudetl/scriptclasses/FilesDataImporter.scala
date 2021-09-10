package com.exasol.cloudetl.scriptclasses

import java.util.HashMap
import java.util.function.Consumer

// import scala.collection.mutable.ListBuffer
import collection.JavaConverters._

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
// import com.exasol.cloudetl.source._
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.cloudetl.parquet.ChunkInterval
import com.exasol.cloudetl.parquet.Interval
import com.exasol.cloudetl.parquet.RowParquetChunkReader
import com.exasol.parquetio.data.Row

// import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.util.HadoopInputFile
import com.typesafe.scalalogging.LazyLogging

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
    // val fileFormat = storageProperties.getFileFormat()
    val bucket = Bucket(storageProperties)

    val files = groupFiles(iterator, 2)
    val nodeId = metadata.getNodeId
    val vmId = metadata.getVmId
    logger.info(s"The total number of files for node: $nodeId, vm: $vmId is '${files.size}'.")

    files.asScala.foreach { case (file, positions) =>
      logger.info(s"Importing from file '$file' with ${positions.size()} blocks.")
      mkStr(positions)
      // val source = Source(fileFormat, new Path(file), bucket.getConfiguration(), bucket.fileSystem)
      val source = new RowParquetChunkReader(HadoopInputFile.fromPath(new Path(file), bucket.getConfiguration()), positions)
      source.read(new Consumer[Row] {
        override def accept(row: Row): Unit = {
          iterator.emit(row.getValues().asScala: _*)
        }
      })
      // readAndEmit(transformValues(source), iterator)
      // source.close()
    }
  }

  private[this] def mkStr(positions: java.util.List[Interval]): Unit = {
    var i = 0
    while (i < positions.size()) {
      logger.info(s"[${positions.get(i).getStartPosition()}...${positions.get(i).getEndPosition()})")
      i += 1
    }
  }

  // private[this] def transformValues(source: Source): Iterator[Row] = {
  //   val converter = source.getValueConverter()
  //   converter.convert(source.stream())
  // }

  private[this] def groupFiles(iterator: ExaIterator, fileStartingIndex: Int): HashMap[String, java.util.List[Interval]] = {
    val files = new HashMap[String, java.util.List[Interval]]()
    // val files = ListBuffer[String]()
    do {
      val filename = iterator.getString(fileStartingIndex)
      val startIndex = iterator.getLong(fileStartingIndex + 1)
      val endIndex = iterator.getLong(fileStartingIndex + 2)
      if (!files.containsKey(filename)) {
        val _ = files.put(filename, new java.util.ArrayList[Interval]())
      }
      files.get(filename).add(new ChunkInterval(startIndex, endIndex))
    } while (iterator.next())
    files
  }

  // private[this] def readAndEmit(rowIterator: Iterator[Row], ctx: ExaIterator): Unit =
  //   rowIterator.foreach { row =>
  //     val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
  //     ctx.emit(columns: _*)
  //   }

}
