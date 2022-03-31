package com.exasol.cloudetl.orc.converter

import scala.collection.mutable.{Map => MMap}

import com.exasol.cloudetl.helper.DateTimeConverter

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.TypeDescription

/**
 * An interface for all type converters.
 */
sealed trait OrcConverter[T <: ColumnVector] {

  /**
   * Reads the record at provided index from the underlying vector.
   *
   * @param vector the Orc
   *        [[org.apache.hadoop.hive.ql.exec.vector.ColumnVector]] vector
   * @param index the index to read at
   */
  def readAt(vector: T, index: Int): Any
}

/**
 * A converter for the Orc {@code BOOLEAN} type.
 */
object BooleanConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index) == 1L
    }
}

/**
 * A converter for the Orc {@code BYTE} type.
 */
object ByteConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toByte
    }
}

/**
 * A converter for the Orc {@code SHORT} type.
 */
object ShortConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toShort
    }
}

/**
 * A converter for the Orc {@code INTEGER} type.
 */
object IntConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toInt
    }
}

/**
 * A converter for the Orc {@code LONG} type.
 */
object LongConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index)
    }
}

/**
 * A converter for the Orc {@code DOUBLE} type.
 */
object DoubleConverter extends OrcConverter[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index)
    }
}

/**
 * A converter for the Orc {@code FLOAT} type.
 */
object FloatConverter extends OrcConverter[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toFloat
    }
}

/**
 * A converter for the Orc {@code DATE} type.
 *
 * It reads the values as days sine the epoch.
 */
object DateConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): java.sql.Date =
    if (vector.isNull(index)) {
      null
    } else {
      val daysSinceEpoch = vector.vector(index)
      DateTimeConverter.daysToDate(daysSinceEpoch)
    }
}

/**
 * A converter for the Orc {@code TIMESTAMP} type.
 */
object TimestampConverter extends OrcConverter[TimestampColumnVector] {
  override def readAt(vector: TimestampColumnVector, index: Int): java.sql.Timestamp =
    if (vector.isNull(index)) {
      null
    } else {
      val timestamp = new java.sql.Timestamp(vector.getTime(index))
      timestamp.setNanos(vector.getNanos(index))
      timestamp
    }
}

/**
 * A converter for the Orc {@code DECIMAL} type.
 */
object DecimalConverter extends OrcConverter[DecimalColumnVector] {
  override def readAt(vector: DecimalColumnVector, index: Int): java.math.BigDecimal =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).getHiveDecimal.bigDecimalValue()
    }
}

/**
 * A converter for the Orc {@code CHAR, STRING and VARCHAR} types.
 */
object StringConverter extends OrcConverter[BytesColumnVector] with LazyLogging {
  override def readAt(vector: BytesColumnVector, index: Int): String =
    if (vector.isNull(index)) {
      null
    } else {
      vector.toString(index)
    }
}

/**
 * A converter for the Orc {@code BINARY} type.
 */
object BinaryConverter extends OrcConverter[BytesColumnVector] {
  override def readAt(vector: BytesColumnVector, index: Int): String =
    if (vector.isNull(index)) {
      null
    } else {
      val startOffset = vector.start(index)
      val endOffset = startOffset + vector.length(index)
      val bytesSlice = vector.vector(index).slice(startOffset, endOffset)
      new String(bytesSlice, "UTF8")
    }
}

/**
 * A converter for the Orc {@code LIST} type.
 *
 * @param elementConverter a converter for the list internal type
 */
final case class ListConverter[T <: ColumnVector](elementConverter: OrcConverter[T])
    extends OrcConverter[ListColumnVector] {

  /**
   * Reads from a list column vector.
   *
   * The row index is an index in the list column vector offsets and lengths array. The length
   * indicates how many records to read from the offset value.
   *
   * The pointer iterates from {@code offset} till {@code offset + length} end value. We also keep
   * another index ({@code [0 ..  length)})for the values array that indexes read objects.
   *
   * @param list the Orc list column vector
   * @param rowIndex the index into the offsets and lengths array of the list column vector
   * @return the values object
   */
  override def readAt(list: ListColumnVector, rowIndex: Int): Any = {
    val offset = list.offsets(rowIndex).toInt
    val length = list.lengths(rowIndex).toInt
    val values = Array.ofDim[Any](length)
    val pointerEnd = offset + length
    var pointer = offset
    var idx = 0
    while (pointer < pointerEnd) {
      if (!list.noNulls && list.isNull(pointer)) {
        values(idx) = null
      } else if (!list.noNulls && list.isRepeating && list.isNull(0)) {
        values(idx) = null
      } else {
        values(idx) = elementConverter.readAt(list.child.asInstanceOf[T], pointer)
      }
      pointer += 1
      idx += 1
    }
    values.toSeq
  }
}

/**
 * A converter for the Orc {@code MAP} type.
 *
 * @param keyConverter a converter for the map key type
 * @param valueConverter a converter for the map value type
 */
final case class MapConverter[T <: ColumnVector, U <: ColumnVector](
  keyConverter: OrcConverter[T],
  valueConverter: OrcConverter[U]
) extends OrcConverter[MapColumnVector] {

  /**
   * Reads from a map column vector.
   *
   * Similar to reading from list, the offsets and lengths array provide
   * pointers into keys and values vectors of the map.
   */
  override def readAt(vector: MapColumnVector, rowIndex: Int): Map[Any, Any] = {
    val offset = vector.offsets(rowIndex).toInt
    val length = vector.lengths(rowIndex).toInt
    var pointer = offset
    val pointerEnd = offset + length
    val values = MMap.empty[Any, Any]
    while (pointer < pointerEnd) {
      val key = keyConverter.readAt(vector.keys.asInstanceOf[T], pointer)
      val value = valueConverter.readAt(vector.values.asInstanceOf[U], pointer)
      val _ = values.put(key, value)
      pointer += 1
    }
    values.toMap
  }
}

/**
 * An abstract class for Orc {@code STRUCT} or {@code UNION} converters.
 *
 * @param schema the schema with field names and types
 */
sealed abstract class AbstractStructLikeConverter(schema: TypeDescription) {

  protected val fields = schema.getChildren()
  protected val size = fields.size()

  /**
   * Returns the name of the field given the position in the Orc schema.
   *
   * In the {@code STRUCT} schema, the field names are defined. So it is
   * easy to name for given field index. However, {@code UNION} schema
   * does not define field names, they are indexed from {@code 0 ..
   * fieldSize}. In this case, we use the field category name, for
   * example, "INT" for integer types or "STRING" for string (VARCHAR,
   * BINARY, CHAR) types.
   *
   * @param fieldIndex the positional index of the field
   * @return name of the field
   */
  def getFieldName(fieldIndex: Int): String

  final def readFromFieldColumn[T <: ColumnVector](
    fieldVector: ColumnVector,
    rowIndex: Int,
    fieldIndex: Int
  ): Any = {
    val converter = OrcConverterFactory(fields.get(fieldIndex)).asInstanceOf[OrcConverter[T]]
    val newRowIndex = if (fieldVector.isRepeating) 0 else rowIndex
    converter.readAt(fieldVector.asInstanceOf[T], newRowIndex)
  }

  final def readFields(fieldVectors: Array[ColumnVector], rowIndex: Int): Map[String, Any] = {
    val values = MMap.empty[String, Any]
    for { fieldIndex <- 0 until size } {
      val key = getFieldName(fieldIndex)
      val value = readFromFieldColumn(fieldVectors(fieldIndex), rowIndex, fieldIndex)
      values.put(key, value)
    }
    values.toMap
  }

}

/**
 * A converter for the Orc {@code STRUCT} type.
 */
final case class StructConverter(schema: TypeDescription)
    extends AbstractStructLikeConverter(schema)
    with OrcConverter[StructColumnVector] {

  private[this] val fieldNames = schema.getFieldNames()

  override final def getFieldName(fieldIndex: Int): String = fieldNames.get(fieldIndex)

  override final def readAt(struct: StructColumnVector, rowIndex: Int): Map[String, Any] =
    readFields(struct.fields, rowIndex)

}

/**
 * A converter for the Orc {@code UNION} type.
 */
final case class UnionConverter(schema: TypeDescription)
    extends AbstractStructLikeConverter(schema)
    with OrcConverter[UnionColumnVector] {

  override final def getFieldName(index: Int): String = fields.get(index).getCategory().name

  override final def readAt(union: UnionColumnVector, rowIndex: Int): Map[String, Any] =
    readFields(union.fields, rowIndex)

}
