package com.exasol.cloudetl.source

import scala.collection.JavaConverters._
import scala.language.reflectiveCalls

import com.exasol.cloudetl.row.Row
import com.exasol.cloudetl.row.RowReadSupport
import com.exasol.cloudetl.util.FsUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.Footer
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.hadoop.ParquetReader
import org.apache.parquet.hadoop.api.ReadSupport
import org.apache.parquet.schema.MessageType

@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
object ParquetSource {

  def apply(pattern: String, fs: FileSystem, conf: Configuration): ParquetSource =
    apply(FsUtil.globWithPattern(pattern, fs), fs, conf)

}

final case class ParquetSource(paths: Seq[Path], fs: FileSystem, conf: Configuration) {

  def stream(): Seq[Iterator[Row]] =
    paths.map { path =>
      try {
        using(createReader(path)) { parquetReader =>
          Iterator.continually(parquetReader.read).takeWhile(_ != null)
        }
      } catch {
        case ex: Throwable =>
          throw new RuntimeException(s"Could not create parquet reader for path $path", ex)
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
      throw new RuntimeException(
        s"Could not read parquet metadata at ${paths.take(5).mkString(",")}"
      )
    }
    footers.headOption.map(_.getParquetMetadata().getFileMetaData().getSchema())
  }

  def getFooters(): Seq[Footer] =
    paths.flatMap { path =>
      val status = fs.getFileStatus(path)
      ParquetFileReader.readAllFootersInParallel(fs.getConf, status).asScala
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
