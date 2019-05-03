package com.exasol.cloudetl.data

/**
 * The internal class that holds column data in an array.
 */
@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
final case class Row(protected[data] val values: Seq[Any]) {

  /** Checks whether the value at position {@code index} is null. */
  def isNullAt(index: Int): Boolean = get(index) == null

  /**
   * Returns the value at position {@code index}.
   *
   * If the value is null, null is returned.
   */
  def get(index: Int): Any = values(index)

  /** Returns the value at position {@code index} casted to the type. */
  @throws[ClassCastException]("When data type does not match")
  def getAs[T](index: Int): T = get(index).asInstanceOf[T]

  /** Returns the value array. */
  def getValues(): Seq[Any] =
    values

}
