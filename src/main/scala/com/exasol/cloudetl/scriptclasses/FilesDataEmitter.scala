package com.exasol.cloudetl.scriptclasses

import java.util.List

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.source.ParquetSource
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.{Row => RegularRow}
import com.exasol.parquetio.data.Interval

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path

final case class FilesDataEmitter(properties: StorageProperties, files: Map[String, List[Interval]])
    extends LazyLogging {

  private[this] val bucket = Bucket(properties)
  private[this] val fileFormat = properties.getFileFormat()

  def emit(context: ExaIterator): Unit = {
    files.foreach { case (filename, intervals) =>
      logger.info(s"Emitting data from file '$filename.")
      val source = getSource(filename, intervals)
      source.stream().foreach { row =>
        context.emit(mapValuesToArray(row): _*)
      }
      source.close()
    }
  }

  private[this] def getSource(filename: String, chunks: List[Interval]): Source = {
    if (fileFormat != FileFormat.PARQUET) {
      Source(fileFormat, new Path(filename), bucket.getConfiguration(), bucket.fileSystem)
    } else {
      ParquetSource(new Path(filename), bucket.getConfiguration(), bucket.fileSystem, chunks)
    }
  }

  private[this] def mapValuesToArray(row: RegularRow): Array[Object] =
    row.getValues().map(_.asInstanceOf[Object]).toArray

}
