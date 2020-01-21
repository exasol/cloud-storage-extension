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
  override val path: Path,
  override val conf: Configuration,
  override val fileSystem: FileSystem
) extends Source
    with LazyLogging {

  private var recordReader: DataFileReader[GenericRecord] = createReader()

  /** @inheritdoc */
  override def stream(): Iterator[Row] =
    AvroRowIterator(recordReader)

  private[this] def createReader(): DataFileReader[GenericRecord] =
    try {
      new DataFileReader[GenericRecord](
        new AvroFSInput(fileSystem.open(path), fileSystem.getFileStatus(path).getLen),
        new GenericDatumReader[GenericRecord]()
      )
    } catch {
      case NonFatal(exception) =>
        logger.error(s"Could not create avro reader for path: $path", exception);
        throw exception
    }

  override def close(): Unit =
    if (recordReader != null) {
      try {
        recordReader.close()
      } finally {
        recordReader = null
      }
    }

}
