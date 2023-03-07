package com.exasol.cloudetl.sink

import com.exasol.cloudetl.bucket.Bucket

/**
 * An abstract sink representation.
 */
abstract class Sink[T] extends AutoCloseable {

  /**
   * The specific [[com.exasol.cloudetl.bucket.Bucket]] where the files
   * will be exported.
   */
  val bucket: Bucket

  /**
   * Creates a format (parquet, avro, etc) specific writer.
   *
   * @param path The file path this writer going to write
   */
  def createWriter(path: String): Writer[T]

  /**
   * Writes the provided value.
   *
   * @param value The specific value to write
   */
  def write(value: T): Unit

}

/**
 * An interface for data writers.
 */
trait Writer[T] extends AutoCloseable {

  /**
   * Writes the provided value to the path.
   *
   * @param value The value to write
   */
  def write(value: T): Unit

}
