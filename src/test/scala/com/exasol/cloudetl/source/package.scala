package com.exasol.cloudetl

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

package object source {
  val Source: SourceFactory.type = SourceFactory
  val AvroSource: AvroSourceFactory.type = AvroSourceFactory
  val OrcSource: OrcSourceFactory.type = OrcSourceFactory
}

object SourceFactory {
  def apply(
    fileFormat: _root_.com.exasol.cloudetl.storage.FileFormat,
    filePath: Path,
    conf: Configuration,
    fileSystem: FileSystem
  ): _root_.com.exasol.cloudetl.source.Source =
    fileFormat match {
      case _root_.com.exasol.cloudetl.storage.FileFormat.AVRO =>
        new _root_.com.exasol.cloudetl.source.AvroSource(filePath, conf, fileSystem)
      case _root_.com.exasol.cloudetl.storage.FileFormat.ORC =>
        new _root_.com.exasol.cloudetl.source.OrcSource(filePath, conf, fileSystem)
      case _ => throw new IllegalArgumentException(s"Storage format $fileFormat is not supported.")
    }
}

object AvroSourceFactory {
  def apply(path: Path, conf: Configuration, fileSystem: FileSystem): _root_.com.exasol.cloudetl.source.AvroSource =
    new _root_.com.exasol.cloudetl.source.AvroSource(path, conf, fileSystem)
}

object OrcSourceFactory {
  def apply(path: Path, conf: Configuration, fileSystem: FileSystem): _root_.com.exasol.cloudetl.source.OrcSource =
    new _root_.com.exasol.cloudetl.source.OrcSource(path, conf, fileSystem)
}
