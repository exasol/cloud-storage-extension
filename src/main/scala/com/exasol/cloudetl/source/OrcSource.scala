package com.exasol.cloudetl.source

import scala.util.control.NonFatal

import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.orc.OrcRowIterator

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.orc.OrcFile
import org.apache.orc.Reader

/**
 * An Orc source that is able to read orc formatted files.
 */
final case class OrcSource(
  override val paths: Seq[Path],
  override val fileSystem: FileSystem,
  override val conf: Configuration
) extends Source
    with LazyLogging {

  /** @inheritdoc */
  override def stream(): Seq[Iterator[Row]] =
    paths.map { path =>
      val orcReader = try {
        createReader(path)
      } catch {
        case NonFatal(exception) =>
          logger.error(s"Could not create orc reader for the path: $path", exception)
          throw exception
      }
      OrcRowIterator(orcReader).flatten
    }

  private[this] def createReader(path: Path): Reader = {
    val options = OrcFile.readerOptions(conf).filesystem(fileSystem)
    OrcFile.createReader(path, options)
  }

}
