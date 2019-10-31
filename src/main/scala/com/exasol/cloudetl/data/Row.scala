package com.exasol.cloudetl.data

import java.nio.ByteBuffer

import scala.collection.JavaConverters._

import org.apache.avro.Schema
import org.apache.avro.generic.GenericFixed
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
    record.getSchema.getFields.asScala.zipWithIndex.foreach {
      case (field, index) =>
        values.update(index, getAvroRecordValue(record.get(index), field.schema))
    }
    Row(values.toSeq)
  }

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.AsInstanceOf",
      "org.wartremover.warts.Null",
      "org.wartremover.warts.Return",
      "org.wartremover.warts.ToString"
    )
  )
  def getAvroRecordValue(value: Any, field: Schema): Any = {
    // scalastyle:off
    if (value == null) {
      return null
    }
    // scalastyle:on
    field.getType match {
      case Schema.Type.NULL    => value
      case Schema.Type.BOOLEAN => value
      case Schema.Type.INT     => value
      case Schema.Type.LONG    => value
      case Schema.Type.FLOAT   => value
      case Schema.Type.DOUBLE  => value
      case Schema.Type.STRING  => getAvroValueAsString(value, field)
      case Schema.Type.FIXED   => getAvroValueAsString(value, field)
      case Schema.Type.BYTES   => getAvroValueAsString(value, field)
      case Schema.Type.ENUM    => value.toString
      case Schema.Type.UNION   => getAvroUnionValue(value, field)
      case field =>
        throw new IllegalArgumentException(s"Avro ${field.getName} type is not supported!")
    }
  }

  def getAvroValueAsString(value: Any, field: Schema): String = value match {
    case str: String            => str
    case utf: Utf8              => utf.toString
    case byteBuffer: ByteBuffer => new String(byteBuffer.array)
    case arrayByte: Array[Byte] => new String(arrayByte)
    case fixed: GenericFixed    => new String(fixed.bytes())
    case other =>
      throw new IllegalArgumentException(
        s"Avro ${field.getName} type with value $other cannot be converted to string!"
      )
  }

  def getAvroUnionValue(value: Any, field: Schema): Any = field.getTypes.asScala.toSeq match {
    case Seq(f)                                     => getAvroRecordValue(value, f)
    case Seq(n, f) if n.getType == Schema.Type.NULL => getAvroRecordValue(value, f)
    case Seq(f, n) if n.getType == Schema.Type.NULL => getAvroRecordValue(value, f)
    case _ =>
      throw new IllegalArgumentException("Avro Union type should contain a primitive and null!")
  }

}
