package com.exasol.cloudetl.helper

import java.util.Locale.ENGLISH

import com.exasol.cloudetl.data.ExaColumnInfo

import org.apache.parquet.schema._
import org.apache.parquet.schema.LogicalTypeAnnotation._
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._

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
    val repetition = if (columnInfo.isNullable) Type.Repetition.OPTIONAL else Type.Repetition.REQUIRED
    columnType match {
      case `jInteger` =>
        if (columnInfo.precision == 0) {
          getPrimitiveType(columnName, INT32, repetition, None, None)
        } else {
          require(
            columnInfo.precision <= DECIMAL_MAX_INT_DIGITS,
            s"Got an 'Integer' type with more than '$DECIMAL_MAX_INT_DIGITS' precision."
          )
          getPrimitiveType(
            columnName,
            INT32,
            repetition,
            None,
            Option(decimalType(columnInfo.scale, columnInfo.precision))
          )
        }
      case `jLong` =>
        if (columnInfo.precision == 0) {
          getPrimitiveType(columnName, INT64, repetition, None, None)
        } else {
          require(
            columnInfo.precision <= DECIMAL_MAX_LONG_DIGITS,
            s"Got a 'Long' type with more than '$DECIMAL_MAX_LONG_DIGITS' precision."
          )
          getPrimitiveType(
            columnName,
            INT64,
            repetition,
            None,
            Option(decimalType(columnInfo.scale, columnInfo.precision))
          )
        }
      case `jBigDecimal` =>
        getPrimitiveType(
          columnName,
          FIXED_LEN_BYTE_ARRAY,
          repetition,
          Option(PRECISION_TO_BYTE_SIZE(columnInfo.precision - 1)),
          Option(decimalType(columnInfo.scale, columnInfo.precision))
        )
      case `jDouble` => getPrimitiveType(columnName, DOUBLE, repetition, None, None)
      case `jString` =>
        if (columnInfo.length > 0) {
          getPrimitiveType(columnName, BINARY, repetition, Option(columnInfo.length), Option(stringType()))
        } else {
          getPrimitiveType(columnName, BINARY, repetition, None, Option(stringType()))
        }
      case `jBoolean`      => getPrimitiveType(columnName, BOOLEAN, repetition, None, None)
      case `jSqlDate`      => getPrimitiveType(columnName, INT32, repetition, None, Option(dateType()))
      case `jSqlTimestamp` => getPrimitiveType(columnName, INT96, repetition, None, None)
      case _ => throw new IllegalArgumentException(s"Cannot convert Exasol type '$columnType' to Parquet type.")
    }
  }

  private[this] def getPrimitiveType(
    name: String,
    primitiveType: PrimitiveType.PrimitiveTypeName,
    repetition: Type.Repetition,
    lengthOption: Option[Int],
    logicalTypeOption: Option[LogicalTypeAnnotation]
  ): Type = {
    var resultType = Types.primitive(primitiveType, repetition)
    resultType = lengthOption.fold(resultType)(len => resultType.length(len))
    resultType = logicalTypeOption.fold(resultType)(logicalType => resultType.as(logicalType))
    resultType.named(name)
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
