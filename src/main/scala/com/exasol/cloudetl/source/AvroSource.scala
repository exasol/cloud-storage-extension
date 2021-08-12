package com.exasol.cloudetl.source

import scala.util.control.NonFatal

import com.exasol.common.avro.AvroRowIterator
import com.exasol.common.data.Row
import com.exasol.errorreporting.ExaError

import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.AvroFSInput
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

/**
 * An Avro source that can read Avro formatted files stored in Hadoop
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

  /**
   * @inheritdoc
   *
   * For now Avro values do not require additional transformation.
   */
  override def getValueConverter(): ValueConverter = new ValueConverter {
    override def convert(rows: Iterator[Row]) = rows
  }

  private[this] def createReader(): DataFileReader[GenericRecord] =
    try {
      new DataFileReader[GenericRecord](
        new AvroFSInput(fileSystem.open(path), fileSystem.getFileStatus(path).getLen),
        new GenericDatumReader[GenericRecord]()
      )
    } catch {
      case NonFatal(exception) =>
        logger.error(s"Could not create avro reader for path: $path", exception);
        throw new SourceValidationException(
          ExaError
            .messageBuilder("E-CSE-26")
            .message("Could not create Avro reader for path {{PATH}}.")
            .parameter("PATH", path.toString())
            .toString(),
          exception
        )
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
