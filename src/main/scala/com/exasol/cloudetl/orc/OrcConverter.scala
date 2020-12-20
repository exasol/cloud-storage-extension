package com.exasol.cloudetl.orc

import scala.collection.mutable.{Map => MMap}
import scala.util.control.NonFatal

import com.exasol.cloudetl.util.DateTimeUtil

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.TypeDescription
import org.apache.orc.TypeDescription.Category

/**
 * An interface for all type converters.
 */
sealed trait OrcConverter[T <: ColumnVector] {

  /**
   * Reads the row at provided index from the vector.
   *
   * @param vector The Orc
   *        [[org.apache.hadoop.hive.ql.exec.vector.ColumnVector]] vector
   * @param index The index to read at
   */
  def readAt(vector: T, index: Int): Any
}

/**
 * A companion object for [[OrcConverter]] interface.
 */
object OrcConverterFactory {

  /**
   * Given the Orc [[org.apache.orc.TypeDescription$]] types creates a
   * converter that reads the type value into Java objects.
   */
  def apply(orcType: TypeDescription): OrcConverter[_ <: ColumnVector] =
    if (orcType.getCategory().isPrimitive()) {
      createPrimitiveConverter(orcType)
    } else {
      createComplexConverter(orcType)
    }

  private[this] def createPrimitiveConverter(
    orcType: TypeDescription
  ): OrcConverter[_ <: ColumnVector] =
    orcType.getCategory() match {
      case Category.BOOLEAN   => BooleanConverter
      case Category.BYTE      => LongConverter
      case Category.CHAR      => StringConverter
      case Category.STRING    => StringConverter
      case Category.VARCHAR   => StringConverter
      case Category.SHORT     => IntConverter
      case Category.INT       => IntConverter
      case Category.LONG      => LongConverter
      case Category.DECIMAL   => DecimalConverter
      case Category.FLOAT     => FloatConverter
      case Category.DOUBLE    => DoubleConverter
      case Category.DATE      => DateConverter
      case Category.TIMESTAMP => TimestampConverter
      case _ =>
        throw new IllegalArgumentException(
          s"Found orc unsupported type, '${orcType.getCategory}'."
        )
    }

  private[this] def createComplexConverter(
    orcType: TypeDescription
  ): OrcConverter[_ <: ColumnVector] =
    orcType.getCategory() match {
      case Category.LIST =>
        val listChildType = orcType.getChildren().get(0)
        ListConverter(apply(listChildType))
      case Category.MAP =>
        val mapKeyType = orcType.getChildren().get(0)
        val mapValueType = orcType.getChildren().get(1)
        MapConverter(apply(mapKeyType), apply(mapValueType))
      case Category.STRUCT =>
        new StructConverter(orcType)
      case _ =>
        throw new IllegalArgumentException(
          s"Found orc unsupported type, '${orcType.getCategory}'."
        )
    }

}

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
   * @param list the ORC list column vector
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

final case class MapConverter[T <: ColumnVector, U <: ColumnVector](
  keyConverter: OrcConverter[T],
  valueConverter: OrcConverter[U]
) extends OrcConverter[MapColumnVector] {

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

final class StructConverter(schema: TypeDescription) extends OrcConverter[StructColumnVector] {

  private[this] val fields = schema.getChildren()
  private[this] val fieldNames = schema.getFieldNames()

  final def getColumnName(index: Int): String = fieldNames.get(index)

  final def readFromColumn[T <: ColumnVector](
    struct: StructColumnVector,
    rowIndex: Int,
    columnIndex: Int
  ): Any = {
    val converter = OrcConverterFactory(fields.get(columnIndex)).asInstanceOf[OrcConverter[T]]
    val vector = struct.fields(columnIndex).asInstanceOf[T]
    val newRowIndex = if (vector.isRepeating) 0 else rowIndex
    converter.readAt(vector, newRowIndex)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Nothing"))
  override final def readAt(vector: StructColumnVector, rowIndex: Int): Map[String, Any] = {
    val size = fields.size()
    val values = MMap.empty[String, Any]
    for { columnIndex <- 0 until size } {
      values.put(getColumnName(columnIndex), readFromColumn(vector, rowIndex, columnIndex))
    }
    values.toMap
  }

}

object BooleanConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Boolean =
    vector.vector(index) == 1
}

object IntConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toInt
    }
}

object LongConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index)
    }
}

object DoubleConverter extends OrcConverter[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index)
    }
}

object FloatConverter extends OrcConverter[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).toFloat
    }
}

object DateConverter extends OrcConverter[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): java.sql.Date =
    if (vector.isNull(index)) {
      null
    } else {
      val daysSinceEpoch = vector.vector(index)
      DateTimeUtil.daysToDate(daysSinceEpoch)
    }
}

object TimestampConverter extends OrcConverter[TimestampColumnVector] {
  override def readAt(vector: TimestampColumnVector, index: Int): java.sql.Timestamp =
    if (vector.isNull(index)) {
      null
    } else {
      new java.sql.Timestamp(vector.getTime(index))
    }
}

object DecimalConverter extends OrcConverter[DecimalColumnVector] {
  override def readAt(vector: DecimalColumnVector, index: Int): java.math.BigDecimal =
    if (vector.isNull(index)) {
      null
    } else {
      vector.vector(index).getHiveDecimal.bigDecimalValue()
    }
}

object StringConverter extends OrcConverter[BytesColumnVector] with LazyLogging {
  override def readAt(vector: BytesColumnVector, index: Int): String =
    if (vector.isNull(index)) {
      null
    } else {
      try {
        val bytes = vector.vector.headOption.fold(Array.empty[Byte])(
          _.slice(vector.start(index), vector.start(index) + vector.length(index))
        )
        new String(bytes, "UTF8")
      } catch {
        case NonFatal(exception) =>
          logger.error(s"Could not read string bytes from orc vector.", exception)
          throw exception
      }
    }
}
