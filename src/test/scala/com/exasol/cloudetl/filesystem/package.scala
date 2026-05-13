package com.exasol.cloudetl

import org.apache.hadoop.fs.FileSystem

package object filesystem {
  val FileSystemManager: FileSystemManagerFactory.type = FileSystemManagerFactory
}

object FileSystemManagerFactory {
  def apply(fileSystem: FileSystem): _root_.com.exasol.cloudetl.filesystem.FileSystemManager =
    new _root_.com.exasol.cloudetl.filesystem.FileSystemManager(fileSystem)
}
