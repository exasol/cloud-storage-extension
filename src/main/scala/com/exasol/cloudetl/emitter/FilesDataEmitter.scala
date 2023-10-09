package com.exasol.cloudetl.emitter

import java.io.IOException
import java.util.List
import java.util.TimeZone
import java.util.function.Consumer

import scala.util.Using

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.parquet.ParquetValueConverter
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.cloudetl.transform.DefaultTransformation
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
  private[this] val defaultTransformation = new DefaultTransformation(properties)

  override def emit(context: ExaIterator): Unit = {
    checkIfTimezoneUTC()
    emitFileFormatData(context)
  }

  private[this] def checkIfTimezoneUTC(): Unit =
    if (properties.isEnabled("TIMEZONE_UTC")) {
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

  private[this] def emitFileFormatData(context: ExaIterator): Unit =
    fileFormat match {
      case FileFormat.PARQUET => emitParquetData(context)
      case FileFormat.DELTA   => emitParquetData(context)
      case _                  => emitRegularData(context)
    }

  private[this] def emitRegularData(context: ExaIterator): Unit = {
    var totalRowCount = 0
    files.foreach { case (filename, _) =>
      Using(Source(fileFormat, new Path(filename), bucket.getConfiguration(), bucket.fileSystem)) { source =>
        var rowCount = 0
        source.stream().foreach { row =>
          val values = defaultTransformation.transform(transformRegularRowValues(row))
          context.emit(values: _*)
          rowCount += 1
        }
        totalRowCount += rowCount
        logger.info(s"Imported file $filename with $rowCount rows ($totalRowCount until now)")
      }
    }
    logger.info(s"Imported ${files.size} files with $totalRowCount rows in total")
  }

  private[this] def transformRegularRowValues(row: RegularRow): Array[Object] =
    row.getValues().map(_.asInstanceOf[Object]).toArray

  private[this] def emitParquetData(context: ExaIterator): Unit = {
    var totalRowCount = 0
    files.foreach { case (filename, intervals) =>
      val inputFile = getInputFile(filename)
      val converter = ParquetValueConverter(RowParquetReader.getSchema(inputFile))
      val source = new RowParquetChunkReader(inputFile, intervals)
      var rowCount = 0
      source.read(new Consumer[Row] {
        override def accept(row: Row): Unit = {
          val values = defaultTransformation.transform(converter.convert(row))
          context.emit(values: _*)
          rowCount += 1
        }
      })
      totalRowCount += rowCount
      logger.info(
        s"Imported file $inputFile with $rowCount rows and ${intervals.size()} intervals ($totalRowCount rows until now)"
      )
    }
    logger.info(s"Imported ${files.size} files with $totalRowCount rows in total")
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
