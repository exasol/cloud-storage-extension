package com.exasol.cloudetl.source

import scala.collection.JavaConverters._
import scala.util.control.NonFatal

import com.exasol.cloudetl.parquet.ParquetValueConverter
import com.exasol.common.data.Row
import com.exasol.errorreporting.ExaError
import com.exasol.parquetio.data.{Row => ParquetRow}
import com.exasol.parquetio.reader.RowParquetReader

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.hadoop.ParquetReader
import org.apache.parquet.hadoop.api.ReadSupport
import org.apache.parquet.hadoop.metadata.ParquetMetadata
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.parquet.schema.MessageType

/**
 * A Parquet source that can read parquet formatted files from Hadoop
 * compatible storage systems.
 */
final case class ParquetSource(
  override val path: Path,
  override val conf: Configuration,
  override val fileSystem: FileSystem
) extends Source
    with LazyLogging {

  private[this] val schema = getSchema()
  private[this] var recordReader = createReader()
  private[this] val valueConverter = getValueConverter()

  /**
   * @inheritdoc
   */
  override def stream(): Iterator[Row] =
    Iterator
      .continually(recordReader.read())
      .takeWhile(_ != null)
      .map(parquetRow => Row(parquetRow.getValues().asScala))

  /**
   * @inheritdoc
   */
  override def getValueConverter(): ValueConverter = ParquetValueConverter(schema)

  /**
   * Applies additional transformation to Parquet values.
   */
  def streamWithValueConverter(): Iterator[Row] = valueConverter.convert(stream())

  private[this] def createReader(): ParquetReader[ParquetRow] =
    try {
      RowParquetReader
        .builder(HadoopInputFile.fromPath(path, getConfWithSchema()))
        .build()
    } catch {
      case NonFatal(exception) =>
        logger.error(s"Could not create Parquet reader for path '$path'.", exception)
        throw new SourceValidationException(
          ExaError
            .messageBuilder("E-CSE-14")
            .message("Could not create Parquet reader for path {{PATH}}.")
            .parameter("PATH", path.toString())
            .toString(),
          exception
        )
    }

  private[this] def getConfWithSchema(): Configuration = {
    val newConf = new Configuration(conf)
    newConf.set(ReadSupport.PARQUET_READ_SCHEMA, schema.toString())
    newConf
  }

  /**
   * Returns Parquet schema from a file.
   */
  def getSchema(): MessageType = {
    val footers = getFooters()
    if (footers.isEmpty) {
      logger.error(s"Parquet file footers are empty for file '$path'.")
      throw new SourceValidationException(
        ExaError
          .messageBuilder("E-CSE-12")
          .message("Parquet footers are empty for file {{PATH}}.")
          .parameter("{{PATH}}", path.toString())
          .toString()
      )
    }
    footers.headOption
      .map(_.getFileMetaData().getSchema())
      .fold {
        throw new SourceValidationException(
          ExaError
            .messageBuilder("E-CSE-13")
            .message("Could not read schema from metadata of Parquet file {{PATH}}.")
            .parameter("{{PATH}}", path.toString())
            .toString()
        )
      }(identity)
  }

  private[this] def getFooters(): Seq[ParquetMetadata] =
    fileSystem.listStatus(path).toList.map { status =>
      val reader = ParquetFileReader.open(HadoopInputFile.fromStatus(status, conf))
      try {
        reader.getFooter()
      } finally {
        reader.close()
      }
    }

  /**
   * @inheritdoc
   */
  override def close(): Unit =
    if (recordReader != null) {
      try {
        recordReader.close()
      } finally {
        recordReader = null
      }
    }

}
