package com.exasol.cloudetl.transform

/**
 * An interface for implementing data transformations before importing into a database or exporting to storage.
 */
trait Transformation {

  /**
   * Applies transformations to the list of objects.
   *
   * @param values a row of objects
   * @return tranformed list of objects
   */
  def transform(values: Array[Object]): Array[Object]
}
