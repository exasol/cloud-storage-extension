package com.exasol.cloudetl.util

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo

import org.apache.parquet.schema.LogicalTypeAnnotation._
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema.Types

object SchemaUtil {

  val DECIMAL_MAX_PRECISION: Int = 38
  val DECIMAL_MAX_INT_DIGITS: Int = 9
  val DECIMAL_MAX_LONG_DIGITS: Int = 18

  // Maps the precision value into the number of bytes
  // Adapted from:
  //  - org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe.java
  val PRECISION_TO_BYTE_SIZE: Seq[Int] = {
    for {
      prec <- 1 to 38 // [1 .. 38]
      power = Math.pow(10, prec.toDouble) // scalastyle:ignore magic.number
      size = Math.ceil((Math.log(power - 1) / Math.log(2) + 1) / 8)
    } yield size.toInt
  }

  /**
   * Given the Exasol column information returns Parquet
   * [[org.apache.parquet.schema.MessageType]].
   */
  def createParquetMessageType(columns: Seq[ExaColumnInfo], schemaName: String): MessageType = {
    val types = columns.map(columnToParquetType(_))
    new MessageType(schemaName, types: _*)
  }

  // In below several lines, I try to pattern match on Class[X] of Java
  // types. Please also read:
  // https://stackoverflow.com/questions/7519140/pattern-matching-on-class-type
  private[this] object JTypes {
    val jInteger: Class[java.lang.Integer] = classOf[java.lang.Integer]
    val jLong: Class[java.lang.Long] = classOf[java.lang.Long]
    val jBigDecimal: Class[java.math.BigDecimal] = classOf[java.math.BigDecimal]
    val jDouble: Class[java.lang.Double] = classOf[java.lang.Double]
    val jBoolean: Class[java.lang.Boolean] = classOf[java.lang.Boolean]
    val jString: Class[java.lang.String] = classOf[java.lang.String]
    val jSqlDate: Class[java.sql.Date] = classOf[java.sql.Date]
    val jSqlTimestamp: Class[java.sql.Timestamp] = classOf[java.sql.Timestamp]
  }

  /**
   * Given Exasol column [[com.exasol.cloudetl.data.ExaColumnInfo]]
   * information convert it into Parquet schema type.
   */
  private[this] def columnToParquetType(colInfo: ExaColumnInfo): Type = {
    val colName = colInfo.name
    val colType = colInfo.`type`
    val repetition = if (colInfo.isNullable) Repetition.OPTIONAL else Repetition.REQUIRED

    import JTypes._

    colType match {
      case `jInteger` =>
        if (colInfo.precision == 0) {
          Types
            .primitive(PrimitiveTypeName.INT32, repetition)
            .named(colName)
        } else {
          require(
            colInfo.precision <= DECIMAL_MAX_INT_DIGITS,
            s"Got an 'Integer' type with more than '$DECIMAL_MAX_INT_DIGITS' precision."
          )
          Types
            .primitive(PrimitiveTypeName.INT32, repetition)
            .as(decimalType(colInfo.scale, colInfo.precision))
            .named(colName)
        }

      case `jLong` =>
        if (colInfo.precision == 0) {
          Types
            .primitive(PrimitiveTypeName.INT64, repetition)
            .named(colName)
        } else {
          require(
            colInfo.precision <= DECIMAL_MAX_LONG_DIGITS,
            s"Got a 'Long' type with more than '$DECIMAL_MAX_LONG_DIGITS' precision."
          )
          Types
            .primitive(PrimitiveTypeName.INT64, repetition)
            .as(decimalType(colInfo.scale, colInfo.precision))
            .named(colName)
        }

      case `jBigDecimal` =>
        Types
          .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, repetition)
          .length(PRECISION_TO_BYTE_SIZE(colInfo.precision - 1))
          .as(decimalType(colInfo.scale, colInfo.precision))
          .named(colName)

      case `jDouble` =>
        Types
          .primitive(PrimitiveTypeName.DOUBLE, repetition)
          .named(colName)

      case `jString` =>
        if (colInfo.length > 0) {
          Types
            .primitive(PrimitiveTypeName.BINARY, repetition)
            .length(colInfo.length)
            .as(stringType())
            .named(colName)
        } else {
          Types
            .primitive(PrimitiveTypeName.BINARY, repetition)
            .as(stringType())
            .named(colName)
        }

      case `jBoolean` =>
        Types
          .primitive(PrimitiveTypeName.BOOLEAN, repetition)
          .named(colName)

      case `jSqlDate` =>
        Types
          .primitive(PrimitiveTypeName.INT32, repetition)
          .as(dateType())
          .named(colName)

      case `jSqlTimestamp` =>
        Types
          .primitive(PrimitiveTypeName.INT96, repetition)
          .named(colName)

      case _ =>
        throw new IllegalArgumentException(
          s"Cannot convert Exasol type '$colType' to Parquet type."
        )
    }
  }

  /**
   * Returns a value from Exasol [[ExaIterator]] iterator on given index
   * which have [[com.exasol.cloudetl.data.ExaColumnInfo]] column type.
   */
  def exaColumnToValue(iter: ExaIterator, idx: Int, colInfo: ExaColumnInfo): Any = {
    val colType = colInfo.`type`
    import JTypes._

    colType match {
      case `jInteger`      => iter.getInteger(idx)
      case `jLong`         => iter.getLong(idx)
      case `jBigDecimal`   => iter.getBigDecimal(idx)
      case `jDouble`       => iter.getDouble(idx)
      case `jString`       => iter.getString(idx)
      case `jBoolean`      => iter.getBoolean(idx)
      case `jSqlDate`      => iter.getDate(idx)
      case `jSqlTimestamp` => iter.getTimestamp(idx)
      case _ =>
        throw new IllegalArgumentException(s"Cannot get Exasol value for column type '$colType'.")
    }
  }

}
