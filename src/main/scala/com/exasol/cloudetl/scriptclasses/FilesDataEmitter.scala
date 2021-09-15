package com.exasol.cloudetl.scriptclasses

import java.io.IOException
import java.util.List
import java.util.function.Consumer

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.{Row => RegularRow}
import com.exasol.errorreporting.ExaError
import com.exasol.parquetio.data.Interval
import com.exasol.parquetio.data.Row
import com.exasol.parquetio.reader.RowParquetChunkReader

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.parquet.io.InputFile

final case class FilesDataEmitter(properties: StorageProperties, files: Map[String, List[Interval]])
    extends LazyLogging {

  private[this] val bucket = Bucket(properties)
  private[this] val fileFormat = properties.getFileFormat()

  def emit(context: ExaIterator): Unit =
    if (fileFormat != FileFormat.PARQUET) {
      emitRegularData(context)
    } else {
      emitParquetData(context)
    }

  private[this] def emitRegularData(context: ExaIterator): Unit =
    files.foreach { case (filename, _) =>
      logFilename(filename)
      val source = Source(fileFormat, new Path(filename), bucket.getConfiguration(), bucket.fileSystem)
      source.stream().foreach { row =>
        context.emit(transformRegularRowValues(row): _*)
      }
      source.close()
    }

  private[this] def transformRegularRowValues(row: RegularRow): Array[Object] =
    row.getValues().map(_.asInstanceOf[Object]).toArray

  private[this] def emitParquetData(context: ExaIterator): Unit =
    files.foreach { case (filename, intervals) =>
      logFilename(filename)
      val source = new RowParquetChunkReader(getInputFile(filename), intervals)
      source.read(new Consumer[Row] {
        override def accept(row: Row): Unit =
          context.emit(row.getValues().toArray(): _*)
      })
    }

  private[this] def getInputFile(filename: String): InputFile =
    try {
      HadoopInputFile.fromPath(new Path(filename), bucket.getConfiguration())
    } catch {
      case exception: IOException =>
        throw new IllegalStateException(
          ExaError
            .messageBuilder("E-CSE-27")
            .message("Failed to create an input file from {{FILE}}.", filename)
            .mitigation("Please make sure that the file is not corrupted.")
            .toString(),
          exception
        )
    }

  private[this] def logFilename(filename: String): Unit =
    logger.info(s"Emitting data from file '$filename.")

}
