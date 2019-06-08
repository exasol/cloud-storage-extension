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
sealed trait OrcDeserializer[T <: ColumnVector] {

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
 * A companion object for [[OrcDeserializer]] interface.
 */
object OrcDeserializer {

  /**
   * Given the Orc [[org.apache.orc.TypeDescription$]] types creates a
   * deserializer that reads the type value into Java objects.
   */
  def apply(orcType: TypeDescription): OrcDeserializer[_ <: ColumnVector] =
    orcType.getCategory match {
      case Category.BOOLEAN   => BooleanDeserializer
      case Category.BYTE      => LongDeserializer
      case Category.CHAR      => StringDeserializer
      case Category.STRING    => StringDeserializer
      case Category.VARCHAR   => StringDeserializer
      case Category.SHORT     => IntDeserializer
      case Category.INT       => IntDeserializer
      case Category.LONG      => LongDeserializer
      case Category.DECIMAL   => DecimalDeserializer
      case Category.FLOAT     => FloatDeserializer
      case Category.DOUBLE    => DoubleDeserializer
      case Category.DATE      => DateDeserializer
      case Category.TIMESTAMP => TimestampDeserializer
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

final class StructDeserializer(fieldTypes: Seq[TypeDescription])
    extends OrcDeserializer[StructColumnVector] {

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def readFromColumn[T <: ColumnVector](
    struct: StructColumnVector,
    rowIndex: Int,
    columnIndex: Int
  ): Any = {
    val deserializer = OrcDeserializer(fieldTypes(columnIndex)).asInstanceOf[OrcDeserializer[T]]
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

object BooleanDeserializer extends OrcDeserializer[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Boolean =
    vector.vector(index) == 1
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object IntDeserializer extends OrcDeserializer[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      vector.vector(index).toInt
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object LongDeserializer extends OrcDeserializer[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      vector.vector(index)
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object DoubleDeserializer extends OrcDeserializer[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      vector.vector(index)
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object FloatDeserializer extends OrcDeserializer[DoubleColumnVector] {
  override def readAt(vector: DoubleColumnVector, index: Int): Any =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      vector.vector(index).toFloat
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object DateDeserializer extends OrcDeserializer[LongColumnVector] {
  override def readAt(vector: LongColumnVector, index: Int): java.sql.Date =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      val daysSinceEpoch = vector.vector(index)
      DateTimeUtil.daysToDate(daysSinceEpoch)
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object TimestampDeserializer extends OrcDeserializer[TimestampColumnVector] {
  override def readAt(vector: TimestampColumnVector, index: Int): java.sql.Timestamp =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      new java.sql.Timestamp(vector.getTime(index))
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object DecimalDeserializer extends OrcDeserializer[DecimalColumnVector] {
  override def readAt(vector: DecimalColumnVector, index: Int): BigDecimal =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
    } else {
      BigDecimal(vector.vector(index).getHiveDecimal.bigDecimalValue)
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object StringDeserializer extends OrcDeserializer[BytesColumnVector] with LazyLogging {
  override def readAt(vector: BytesColumnVector, index: Int): String =
    if (vector.isNull(index)) {
      null // scalastyle:ignore null
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
