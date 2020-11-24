package com.exasol.cloudetl.parquet.converter

import scala.collection.mutable.ArrayBuffer

/**
 * An interface for the storing converted Parquet values.
 *
 * Implementations of this interface choose internal structure depending
 * on the value converter.
 */
trait ValueHolder {

  /**
   * Reset the internal value holder data structure.
   */
  def reset(): Unit

  /**
   * Return the values as immutable sequence.
   */
  def getValues(): Seq[Any]

  def put(index: Int, value: Any): Unit
}

/**
 * An indexed value holder.
 *
 * It sets converted Parquet values to a positional cell in the
 * provided array.
 *
 * @param size the size of the internal structure
 */
final case class IndexedValueHolder(size: Int) extends ValueHolder {
  private[this] var array = Array.ofDim[Any](size)

  override def reset(): Unit = array = Array.ofDim[Any](size)
  override def getValues(): Seq[Any] = array.toSeq
  override def put(index: Int, value: Any): Unit = array.update(index, value)
}

/**
 * An appending value holder.
 *
 * It append converted Parquet values to the end of the values array.
 */
final case class AppendedValueHolder() extends ValueHolder {
  private[this] var array = ArrayBuffer.empty[Any]

  override def reset(): Unit = array.clear()
  override def getValues(): Seq[Any] = array.clone().toSeq
  override def put(index: Int, value: Any): Unit = {
    val _ = array += value
  }
}

/**
 * An empty value holder used for top-level parent converter.
 */
object EmptyValueHolder extends ValueHolder {
  override def reset(): Unit = ()
  override def getValues(): Seq[Any] = Seq.empty[Any]
  override def put(index: Int, value: Any): Unit = ()
}
