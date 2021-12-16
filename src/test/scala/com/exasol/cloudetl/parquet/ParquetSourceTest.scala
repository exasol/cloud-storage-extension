package com.exasol.cloudetl.parquet

import java.util.function.Consumer

import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

import com.exasol.cloudetl.source.Source
import com.exasol.common.data.Row
import com.exasol.parquetio.data.{Row => ParquetRow}
import com.exasol.parquetio.reader.RowParquetChunkReader
import com.exasol.parquetio.reader.RowParquetReader

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.parquet.io.InputFile

final case class ParquetSourceTest(val path: Path, override val conf: Configuration) extends Source with LazyLogging {

  private[this] val schema = RowParquetReader.getSchema(getInputFile(path))
  private[this] var recordReader = createReader()
  private[this] val valueConverter = ParquetValueConverter(schema)
  override val fileSystem = null

  @SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures")) // fine in tests
  override def stream(): Iterator[Row] = {
    val values = ListBuffer.empty[Row]
    recordReader.read(new Consumer[ParquetRow] {
      override def accept(row: ParquetRow): Unit = {
        val rowValues = Row(valueConverter.convert(row).clone().toSeq) // reused
        values.append(rowValues)
      }
    })
    values.iterator
  }

  @SuppressWarnings(Array("org.wartremover.warts.Throw")) // fine in tests
  private[this] def createReader(): RowParquetChunkReader =
    try {
      new RowParquetChunkReader(getInputFile(path))
    } catch {
      case NonFatal(exception) =>
        logger.error(s"Could not create Parquet reader for path '$path'.", exception)
        throw new IllegalStateException(s"Could not create Parquet reader for path $path.", exception)
    }

  private[this] def getInputFile(path: Path): InputFile =
    HadoopInputFile.fromPath(path, conf)

  override def close(): Unit =
    recordReader = null

}
