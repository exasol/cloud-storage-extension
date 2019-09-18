package com.exasol.cloudetl.data

import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8

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

/**
 * A companion object to the internal [[Row]] data structure with helper
 * functions.
 */
object Row {

  /**
   * Returns a [[Row]] from [[org.apache.avro.generic.GenericRecord]]
   * data.
   */
  def fromAvroGenericRecord(record: GenericRecord): Row = {
    val size = record.getSchema.getFields.size
    val values = Array.ofDim[Any](size)
    for { index <- 0 until size } {
      values.update(index, getAvroRecordValue(record.get(index)))
    }
    Row(values.toSeq)
  }

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  private[this] def getAvroRecordValue(value: Any): Any = value match {
    case _: GenericRecord =>
      throw new IllegalArgumentException("Avro nested record type is not supported yet!")
    case _: java.util.Collection[_] =>
      throw new IllegalArgumentException("Avro collection type is not supported yet!")
    case _: java.util.Map[_, _] =>
      throw new IllegalArgumentException("Avro map type is not supported yet!")
    case _: Utf8       => value.asInstanceOf[Utf8].toString
    case primitiveType => primitiveType
  }

}
