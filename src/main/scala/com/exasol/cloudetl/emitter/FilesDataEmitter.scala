package com.exasol.cloudetl.emitter

import java.io.IOException
import java.util.List
import java.util.function.Consumer

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.parquet.ParquetValueConverter
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.{Row => RegularRow}
import com.exasol.errorreporting.ExaError
import com.exasol.parquetio.data.ChunkInterval
import com.exasol.parquetio.data.Row
import com.exasol.parquetio.reader.RowParquetChunkReader
import com.exasol.parquetio.reader.RowParquetReader

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.parquet.io.InputFile

/**
 * A class that emits data read from storage into an Exasol database.
 *
 * @param properties a storage properties to create configurations
 * @param files a list of files with the chunks to read
 */
final case class FilesDataEmitter(properties: StorageProperties, files: Map[String, List[ChunkInterval]])
    extends Emitter
    with LazyLogging {

  private[this] val bucket = Bucket(properties)
  private[this] val fileFormat = properties.getFileFormat()

  override def emit(context: ExaIterator): Unit = fileFormat match {
    case FileFormat.PARQUET => emitParquetData(context)
    case FileFormat.DELTA   => emitParquetData(context)
    case _                  => emitRegularData(context)
  }

  private[this] def emitRegularData(context: ExaIterator): Unit =
    files.foreach { case (filename, _) =>
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
      val inputFile = getInputFile(filename)
      val converter = ParquetValueConverter(RowParquetReader.getSchema(inputFile))
      val source = new RowParquetChunkReader(inputFile, intervals)
      source.read(new Consumer[Row] {
        override def accept(row: Row): Unit =
          context.emit(converter.convert(row): _*)
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

}
