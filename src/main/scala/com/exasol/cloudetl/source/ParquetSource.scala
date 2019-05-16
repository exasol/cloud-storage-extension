package com.exasol.cloudetl.source

import scala.collection.JavaConverters._
import scala.language.reflectiveCalls
import scala.util.control.NonFatal

import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.parquet.RowReadSupport
import com.exasol.cloudetl.util.FileSystemUtil

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
 * A companion object to the [[ParquetSource]] class.
 *
 * Provides an apply method to create a parquet source.
 */
@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
object ParquetSource {

  def apply(pattern: String, fileSystem: FileSystem, conf: Configuration): ParquetSource =
    apply(FileSystemUtil.globWithPattern(pattern, fileSystem), fileSystem, conf)

}

/**
 * A Parquet source that can read parquet formatted files from Hadoop
 * compatible storage systems.
 */
final case class ParquetSource(
  override val paths: Seq[Path],
  override val fileSystem: FileSystem,
  override val conf: Configuration
) extends Source
    with LazyLogging {

  /** @inheritdoc */
  override def stream(): Seq[Iterator[Row]] =
    paths.map { path =>
      try {
        using(createReader(path)) { parquetReader =>
          Iterator.continually(parquetReader.read).takeWhile(_ != null)
        }
      } catch {
        case NonFatal(exception) =>
          logger.error(s"Could not create parquet reader for path: $path", exception);
          throw exception
      }
    }

  def createReader(path: Path): ParquetReader[Row] = {
    val newConf = new Configuration(conf)
    getSchema.foreach { schema =>
      newConf.set(ReadSupport.PARQUET_READ_SCHEMA, schema.toString)
    }

    ParquetReader.builder(new RowReadSupport, path).withConf(newConf).build()
  }

  def getSchema(): Option[MessageType] = {
    val footers = getFooters()
    if (footers.isEmpty) {
      logger.error(s"Could not read parquet metadata from paths: ${paths.take(5).mkString(",")}")
      throw new RuntimeException("Parquet footers are empty!")
    }
    footers.headOption.map(_.getParquetMetadata().getFileMetaData().getSchema())
  }

  def getFooters(): Seq[Footer] =
    paths.flatMap { path =>
      val status = fileSystem.getFileStatus(path)
      ParquetFileReader.readAllFootersInParallel(fileSystem.getConf, status).asScala
    }

  // scalastyle:off structural.type
  def using[T, U <: { def close(): Unit }](closeable: U)(f: U => T): T =
    try {
      f(closeable)
    } finally {
      closeable.close()
    }
  // scalastyle:on structural.type

}
