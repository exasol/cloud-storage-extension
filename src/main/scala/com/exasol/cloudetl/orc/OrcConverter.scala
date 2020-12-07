package com.exasol.cloudetl.orc

import scala.util.control.NonFatal

import com.exasol.cloudetl.util.DateTimeUtil

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.TypeDescription
import org.apache.orc.TypeDescription.Category

/**
 * An interface for all type deserializers.
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
object OrcConverter {

  /**
   * Given the Orc [[org.apache.orc.TypeDescription$]] types creates a
   * deserializer that reads the type value into Java objects.
   */
  def apply(orcType: TypeDescription): OrcConverter[_ <: ColumnVector] =
    orcType.getCategory match {
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
      case Category.LIST =>
        throw new IllegalArgumentException("Orc list type is not supported.")
      case Category.MAP =>
        throw new IllegalArgumentException("Orc map type is not supported.")
      case Category.STRUCT =>
        throw new IllegalArgumentException("Orc nested struct type is not supported.")
      case _ =>
        throw new IllegalArgumentException(
          s"Found orc unsupported type, '${orcType.getCategory}'."
        )
    }

}

final class StructConverter(fieldTypes: Seq[TypeDescription])
    extends OrcConverter[StructColumnVector] {

  def readFromColumn[T <: ColumnVector](
    struct: StructColumnVector,
    rowIndex: Int,
    columnIndex: Int
  ): Any = {
    val deserializer = OrcConverter(fieldTypes(columnIndex)).asInstanceOf[OrcConverter[T]]
    val vector = struct.fields(columnIndex).asInstanceOf[T]
    val newRowIndex = if (vector.isRepeating) 0 else rowIndex
    deserializer.readAt(vector, newRowIndex)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Nothing"))
  override final def readAt(vector: StructColumnVector, rowIndex: Int): Seq[Any] = {
    val size = fieldTypes.size
    val values = Array.ofDim[Any](size)
    for { columnIndex <- 0 until size } {
      values.update(columnIndex, readFromColumn(vector, rowIndex, columnIndex))
    }
    values.toSeq
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
