package com.exasol.cloudetl.source

import com.exasol.cloudetl.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

/**
 * An abstract representation of a distributed Hadoop compatible file
 * system source.
 */
abstract class Source {

  /**
   * The file [[org.apache.hadoop.fs.Path]] paths in the file system.
   */
  def paths: Seq[Path]

  /** The Hadoop [[org.apache.hadoop.fs.FileSystem$]] file system. */
  def fileSystem: FileSystem

  /**
   * The Hadoop [[org.apache.hadoop.conf.Configuration]] configuration.
   */
  def conf: Configuration

  /**
   * Returns sequence of internal [[com.exasol.cloudetl.data.Row]] row
   * iterators per each paths file.
   */
  def stream(): Seq[Iterator[Row]]

}
