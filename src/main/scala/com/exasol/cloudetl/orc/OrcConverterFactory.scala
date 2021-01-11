package com.exasol.cloudetl.orc

import org.apache.hadoop.hive.ql.exec.vector.ColumnVector
import org.apache.orc.TypeDescription
import org.apache.orc.TypeDescription.Category

/**
 * An Orc data types converter factory class.
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
      case Category.BYTE      => ByteConverter
      case Category.SHORT     => ShortConverter
      case Category.INT       => IntConverter
      case Category.LONG      => LongConverter
      case Category.FLOAT     => FloatConverter
      case Category.DOUBLE    => DoubleConverter
      case Category.DECIMAL   => DecimalConverter
      case Category.DATE      => DateConverter
      case Category.TIMESTAMP => TimestampConverter
      case Category.BINARY    => BinaryConverter
      case Category.CHAR      => StringConverter
      case Category.STRING    => StringConverter
      case Category.VARCHAR   => StringConverter
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
        val listElementType = orcType.getChildren().get(0)
        ListConverter(apply(listElementType))
      case Category.MAP =>
        val mapKeyType = orcType.getChildren().get(0)
        val mapValueType = orcType.getChildren().get(1)
        MapConverter(apply(mapKeyType), apply(mapValueType))
      case Category.STRUCT => StructConverter(orcType)
      case Category.UNION  => UnionConverter(orcType)
      case _ =>
        throw new IllegalArgumentException(
          s"Found orc unsupported type, '${orcType.getCategory}'."
        )
    }
}
