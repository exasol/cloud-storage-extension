package com.exasol.cloudetl.util

import com.exasol.cloudetl.data.ExaColumnInfo

import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.OriginalType
import org.apache.parquet.schema.PrimitiveType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema.Types
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SchemaUtilSuite extends FunSuite with Matchers {

  test("`createParquetMessageType` throws an exception for unknown type") {
    val thrown = intercept[RuntimeException] {
      SchemaUtil.createParquetMessageType(
        Seq(ExaColumnInfo("c_short", classOf[java.lang.Short], 0, 0, 0, false)),
        "test_schema"
      )
    }
    val expectedMsg = s"Cannot convert Exasol type '${classOf[java.lang.Short]}' to Parquet type."
    assert(thrown.getMessage === expectedMsg)
  }

  test("`createParquetMessageType` creates parquet message type from list of exa columns") {

    val exaColumns = Seq(
      ExaColumnInfo("c_int", classOf[java.lang.Integer], 0, 0, 0, true),
      ExaColumnInfo("c_int", classOf[java.lang.Integer], 1, 0, 1, true),
      ExaColumnInfo("c_long", classOf[java.lang.Long], 0, 0, 0, false),
      ExaColumnInfo("c_long", classOf[java.lang.Long], 7, 3, 4, true),
      ExaColumnInfo("c_decimal", classOf[java.math.BigDecimal], 38, 10, 16, false),
      ExaColumnInfo("c_double", classOf[java.lang.Double], 0, 0, 0, true),
      ExaColumnInfo("c_string", classOf[java.lang.String], 0, 0, 0, false),
      ExaColumnInfo("c_string", classOf[java.lang.String], 0, 0, 20, false),
      ExaColumnInfo("c_boolean", classOf[java.lang.Boolean], 0, 0, 0, false),
      ExaColumnInfo("c_date", classOf[java.sql.Date], 0, 0, 0, false),
      ExaColumnInfo("c_timestamp", classOf[java.sql.Timestamp], 0, 0, 0, false)
    )

    val schemaName = "exasol_export_schema"

    val messageType = new MessageType(
      schemaName,
      new PrimitiveType(Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT32, "c_int"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.OPTIONAL)
        .precision(1)
        .scale(0)
        .length(1)
        .as(OriginalType.DECIMAL)
        .named("c_int"),
      new PrimitiveType(Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT64, "c_long"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.OPTIONAL)
        .precision(7)
        .scale(3)
        .length(4)
        .as(OriginalType.DECIMAL)
        .named("c_long"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED)
        .precision(38)
        .scale(10)
        .length(16)
        .as(OriginalType.DECIMAL)
        .named("c_decimal"),
      new PrimitiveType(Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.DOUBLE, "c_double"),
      new PrimitiveType(
        Repetition.REQUIRED,
        PrimitiveType.PrimitiveTypeName.BINARY,
        "c_string",
        OriginalType.UTF8
      ),
      Types
        .primitive(PrimitiveTypeName.BINARY, Repetition.REQUIRED)
        .length(20)
        .as(OriginalType.UTF8)
        .named("c_string"),
      new PrimitiveType(
        Repetition.REQUIRED,
        PrimitiveType.PrimitiveTypeName.BOOLEAN,
        "c_boolean"
      ),
      Types
        .primitive(PrimitiveTypeName.INT32, Repetition.REQUIRED)
        .as(OriginalType.DATE)
        .named("c_date"),
      new PrimitiveType(Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT96, "c_timestamp")
    )

    assert(SchemaUtil.createParquetMessageType(exaColumns, schemaName) === messageType)
  }
}
