package com.exasol.cloudetl.source

import scala.collection.JavaConverters._
import scala.util.control.NonFatal

import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.parquet.RowReadSupport

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.Footer
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.hadoop.ParquetReader
import org.apache.parquet.hadoop.api.ReadSupport
import org.apache.parquet.schema.MessageType

/**
 * A Parquet source that can read parquet formatted files from Hadoop
 * compatible storage systems.
 */
@SuppressWarnings(Array("org.wartremover.warts.Var", "org.wartremover.warts.Null"))
final case class ParquetSource(
  override val path: Path,
  override val conf: Configuration,
  override val fileSystem: FileSystem
) extends Source
    with LazyLogging {

  private var recordReader = createReader()

  /** @inheritdoc */
  override def stream(): Iterator[Row] =
    Iterator.continually(recordReader.read).takeWhile(_ != null)

  private[this] def createReader(): ParquetReader[Row] = {
    val newConf = new Configuration(conf)
    getSchema.foreach { schema =>
      newConf.set(ReadSupport.PARQUET_READ_SCHEMA, schema.toString)
    }

    try {
      ParquetReader.builder(new RowReadSupport, path).withConf(newConf).build()
    } catch {
      case NonFatal(exception) =>
        logger.error(s"Could not create parquet reader for path: $path", exception);
        throw exception
    }
  }

  def getSchema(): Option[MessageType] = {
    val footers = getFooters()
    if (footers.isEmpty) {
      logger.error(s"Could not read parquet metadata from paths: $path")
      throw new RuntimeException("Parquet footers are empty!")
    }
    footers.headOption.map(_.getParquetMetadata().getFileMetaData().getSchema())
  }

  private[this] def getFooters(): Seq[Footer] = {
    val status = fileSystem.getFileStatus(path)
    ParquetFileReader.readAllFootersInParallel(fileSystem.getConf, status).asScala
  }

  // scalastyle:off null
  override def close(): Unit =
    if (recordReader != null) {
      try {
        recordReader.close()
      } finally {
        recordReader = null
      }
    }
  // scalastyle:on null

}
