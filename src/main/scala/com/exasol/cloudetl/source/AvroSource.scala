package com.exasol.cloudetl.source

import scala.util.control.NonFatal

import com.exasol.cloudetl.avro.AvroRowIterator
import com.exasol.cloudetl.data.Row

import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.AvroFSInput
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

/**
 * An Avro source that can read avro formatted files stored in Hadoop
 * like distributed storage file systems.
 */
final case class AvroSource(
  override val paths: Seq[Path],
  override val fileSystem: FileSystem,
  override val conf: Configuration
) extends Source
    with LazyLogging {

  /** @inheritdoc */
  override def stream(): Seq[Iterator[Row]] =
    paths.map { path =>
      val avroReader = try {
        createReader(path)
      } catch {
        case NonFatal(exception) =>
          logger.error(s"Could not create avro reader for path: $path", exception);
          throw exception
      }
      AvroRowIterator(avroReader)
    }

  private[this] def createReader(path: Path): DataFileReader[GenericRecord] =
    new DataFileReader[GenericRecord](
      new AvroFSInput(fileSystem.open(path), fileSystem.getFileStatus(path).getLen),
      new GenericDatumReader[GenericRecord]()
    )

}
