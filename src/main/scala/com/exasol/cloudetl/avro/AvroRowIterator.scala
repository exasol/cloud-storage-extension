package com.exasol.cloudetl.avro

import com.exasol.cloudetl.data.Row

import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8

/**
 * An object that creates a [[com.exasol.cloudetl.data.Row]] iterator
 * given the Avro [[org.apache.avro.file.DataFileReader]] with
 * [[org.apache.avro.generic.GenericRecord]].
 *
 * Each next record is converted into an internal Row class.
 */
object AvroRowIterator {

  def apply(reader: DataFileReader[GenericRecord]): Iterator[Row] =
    new Iterator[Row] {
      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      private[this] var isCompleted = false

      override def hasNext: Boolean =
        if (isCompleted) {
          false
        } else {
          val hasNext = reader.hasNext
          if (!hasNext) {
            reader.close()
            isCompleted = true
          }
          hasNext
        }

      override def next(): Row = {
        if (!hasNext) {
          throw new NoSuchElementException("Avro reader next on empty iterator")
        }
        val record = reader.next()
        recordToRow(record)
      }
    }

  private[this] def recordToRow(record: GenericRecord): Row = {
    val size = record.getSchema.getFields.size
    val values = Array.ofDim[Any](size)
    for { index <- 0 until size } {
      values.update(index, convertRecordValue(record.get(index)))
    }
    Row(values.toSeq)
  }

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  private[this] def convertRecordValue(value: Any): Any = value match {
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
