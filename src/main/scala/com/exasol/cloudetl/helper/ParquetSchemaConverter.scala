package com.exasol.cloudetl.helper

import java.util.Locale.ENGLISH

import com.exasol.cloudetl.data.ExaColumnInfo

import org.apache.parquet.schema.LogicalTypeAnnotation._
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema.Types

/**
 * A class that converts Exasol column names to Parquet schema.
 *
 * @param isLowercaseSchemaEnabled a boolean flag to make Parquet schema fields lowercase
 */
final case class ParquetSchemaConverter(isLowercaseSchemaEnabled: Boolean) extends JavaClassTypes {
  import ParquetSchemaConverter._

  /**
   * Given the Exasol column information returns Parquet [[org.apache.parquet.schema.MessageType]].
   *
   * @param columns a list of Exasol column information
   * @param schemaName a Parquet schema name
   * @return a Parquet message type
   */
  def createParquetMessageType(columns: Seq[ExaColumnInfo], schemaName: String): MessageType = {
    val types = columns.map(columnToParquetField(_))
    new MessageType(schemaName, types: _*)
  }

  private[this] def columnToParquetField(columnInfo: ExaColumnInfo): Type = {
    val columnName = convertIdentifier(columnInfo.name)
    getParquetType(columnName, columnInfo)
  }

  private[this] def convertIdentifier(columnName: String): String = {
    val convertedName = columnName.replace("\"", "")
    if (isLowercaseSchemaEnabled) {
      convertedName.toLowerCase(ENGLISH)
    } else {
      convertedName
    }
  }

  private[this] def getParquetType(columnName: String, columnInfo: ExaColumnInfo): Type = {
    val columnType = columnInfo.`type`
    val repetition = if (columnInfo.isNullable) Repetition.OPTIONAL else Repetition.REQUIRED
    columnType match {
      case `jInteger` =>
        if (columnInfo.precision == 0) {
          Types
            .primitive(PrimitiveTypeName.INT32, repetition)
            .named(columnName)
        } else {
          require(
            columnInfo.precision <= DECIMAL_MAX_INT_DIGITS,
            s"Got an 'Integer' type with more than '$DECIMAL_MAX_INT_DIGITS' precision."
          )
          Types
            .primitive(PrimitiveTypeName.INT32, repetition)
            .as(decimalType(columnInfo.scale, columnInfo.precision))
            .named(columnName)
        }
      case `jLong` =>
        if (columnInfo.precision == 0) {
          Types
            .primitive(PrimitiveTypeName.INT64, repetition)
            .named(columnName)
        } else {
          require(
            columnInfo.precision <= DECIMAL_MAX_LONG_DIGITS,
            s"Got a 'Long' type with more than '$DECIMAL_MAX_LONG_DIGITS' precision."
          )
          Types
            .primitive(PrimitiveTypeName.INT64, repetition)
            .as(decimalType(columnInfo.scale, columnInfo.precision))
            .named(columnName)
        }
      case `jBigDecimal` =>
        Types
          .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, repetition)
          .length(PRECISION_TO_BYTE_SIZE(columnInfo.precision - 1))
          .as(decimalType(columnInfo.scale, columnInfo.precision))
          .named(columnName)
      case `jDouble` =>
        Types
          .primitive(PrimitiveTypeName.DOUBLE, repetition)
          .named(columnName)
      case `jString` =>
        if (columnInfo.length > 0) {
          Types
            .primitive(PrimitiveTypeName.BINARY, repetition)
            .length(columnInfo.length)
            .as(stringType())
            .named(columnName)
        } else {
          Types
            .primitive(PrimitiveTypeName.BINARY, repetition)
            .as(stringType())
            .named(columnName)
        }
      case `jBoolean` =>
        Types
          .primitive(PrimitiveTypeName.BOOLEAN, repetition)
          .named(columnName)

      case `jSqlDate` =>
        Types
          .primitive(PrimitiveTypeName.INT32, repetition)
          .as(dateType())
          .named(columnName)

      case `jSqlTimestamp` =>
        Types
          .primitive(PrimitiveTypeName.INT96, repetition)
          .named(columnName)
      case _ =>
        throw new IllegalArgumentException(s"Cannot convert Exasol type '$columnType' to Parquet type.")
    }
  }

}

object ParquetSchemaConverter {
  val DECIMAL_MAX_PRECISION: Int = 38
  val DECIMAL_MAX_INT_DIGITS: Int = 9
  val DECIMAL_MAX_LONG_DIGITS: Int = 18

  // Maps the precision value into the number of bytes.
  // Adapted from:
  //  - org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe.java
  val PRECISION_TO_BYTE_SIZE: Seq[Int] = {
    for {
      prec <- 1 to 38 // [1 .. 38]
      power = Math.pow(10, prec.toDouble) // scalastyle:ignore magic.number
      size = Math.ceil((Math.log(power - 1) / Math.log(2) + 1) / 8)
    } yield size.toInt
  }

}
