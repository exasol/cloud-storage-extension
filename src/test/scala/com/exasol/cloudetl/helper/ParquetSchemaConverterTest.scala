package com.exasol.cloudetl.util

import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.helper.ParquetSchemaConverter

import org.apache.parquet.schema.LogicalTypeAnnotation._
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class ParquetSchemaConverterTest extends AnyFunSuite with MockitoSugar {

  test("createParquetMessageType throws if type is unknown") {
    val converter = ParquetSchemaConverter(false)
    val columns = Seq(ExaColumnInfo("c_short", classOf[java.lang.Short], 0, 0, 0, false))
    val thrown = intercept[IllegalArgumentException] {
      converter.createParquetMessageType(columns, "test_schema")
    }
    assert(thrown.getMessage().startsWith("F-CSE-22"))
    assert(thrown.getMessage().contains("Exasol type 'class java.lang.Short' to Parquet type."))
  }

  test("createParquetMessageType returns Parquet schema from Exasol columns") {
    val converter = ParquetSchemaConverter(true)
    val columns = Seq(
      ExaColumnInfo("c_int", classOf[java.lang.Integer], 0, 0, 0, true),
      ExaColumnInfo("c_int", classOf[java.lang.Integer], 1, 0, 0, true),
      ExaColumnInfo("c_int", classOf[java.lang.Integer], 9, 0, 0, true),
      ExaColumnInfo("c_long", classOf[java.lang.Long], 0, 0, 0, false),
      ExaColumnInfo("c_long", classOf[java.lang.Long], 18, 0, 0, true),
      ExaColumnInfo("c_decimal_int", classOf[java.math.BigDecimal], 9, 0, 0, false),
      ExaColumnInfo("c_decimal_long", classOf[java.math.BigDecimal], 17, 0, 0, false),
      ExaColumnInfo("c_decimal", classOf[java.math.BigDecimal], 38, 10, 16, false),
      ExaColumnInfo("c_double", classOf[java.lang.Double], 0, 0, 0, true),
      ExaColumnInfo("c_string", classOf[java.lang.String], 0, 0, 0, false),
      ExaColumnInfo("c_string", classOf[java.lang.String], 0, 0, 20, false),
      ExaColumnInfo("c_boolean", classOf[java.lang.Boolean], 0, 0, 0, false),
      ExaColumnInfo("c_date", classOf[java.sql.Date], 0, 0, 0, false),
      ExaColumnInfo("c_timestamp", classOf[java.sql.Timestamp], 0, 0, 0, false)
    )
    val schemaName = "exasol_export_schema"
    val expectedMessageType = new MessageType(
      schemaName,
      new PrimitiveType(Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT32, "c_int"),
      Types
        .primitive(PrimitiveTypeName.INT32, Repetition.OPTIONAL)
        .as(decimalType(0, 1))
        .named("c_int"),
      Types
        .primitive(PrimitiveTypeName.INT32, Repetition.OPTIONAL)
        .as(decimalType(0, 9))
        .named("c_int"),
      new PrimitiveType(Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT64, "c_long"),
      Types
        .primitive(PrimitiveTypeName.INT64, Repetition.OPTIONAL)
        .as(decimalType(0, 18))
        .named("c_long"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED)
        .length(4)
        .as(decimalType(0, 9))
        .named("c_decimal_int"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED)
        .length(8)
        .as(decimalType(0, 17))
        .named("c_decimal_long"),
      Types
        .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED)
        .length(16)
        .as(decimalType(10, 38))
        .named("c_decimal"),
      new PrimitiveType(Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.DOUBLE, "c_double"),
      Types
        .primitive(PrimitiveTypeName.BINARY, Repetition.REQUIRED)
        .as(stringType())
        .named("c_string"),
      Types
        .primitive(PrimitiveTypeName.BINARY, Repetition.REQUIRED)
        .length(20)
        .as(stringType())
        .named("c_string"),
      new PrimitiveType(
        Repetition.REQUIRED,
        PrimitiveType.PrimitiveTypeName.BOOLEAN,
        "c_boolean"
      ),
      Types
        .primitive(PrimitiveTypeName.INT32, Repetition.REQUIRED)
        .as(dateType())
        .named("c_date"),
      new PrimitiveType(Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT96, "c_timestamp")
    )
    assert(converter.createParquetMessageType(columns, schemaName) === expectedMessageType)
  }

  test("createParquetMessageType throws if integer precision is larger than allowed") {
    val converter = ParquetSchemaConverter(false)
    val columns = Seq(ExaColumnInfo("c_int", classOf[java.lang.Integer], 10, 0, 0, true))
    val thrown = intercept[IllegalArgumentException] {
      converter.createParquetMessageType(columns, "test")
    }
    val expectedMessage = "requirement failed: Got an 'Integer' type with more than '9' precision."
    assert(thrown.getMessage() === expectedMessage)
  }

  test("createParquetMessageType throws if long precision is larger than allowed") {
    val converter = ParquetSchemaConverter(false)
    val columns = Seq(ExaColumnInfo("c_long", classOf[java.lang.Long], 20, 0, 0, true))
    val thrown = intercept[IllegalArgumentException] {
      converter.createParquetMessageType(columns, "test")
    }
    val expectedMessage = "requirement failed: Got a 'Long' type with more than '18' precision."
    assert(thrown.getMessage() === expectedMessage)
  }

  test("createParquetMessageType removes quotes from field names") {
    Seq(true, false).foreach { case isLowercase =>
      val converter = ParquetSchemaConverter(isLowercase)
      val columns = Seq(
        ExaColumnInfo("\"column int\"", classOf[java.lang.Integer], 9, 0, 0, true),
        ExaColumnInfo("\"c_long\"", classOf[java.lang.Long], 12, 0, 0, true)
      )
      val schema = converter.createParquetMessageType(columns, "test")
      assert(schema.getFieldName(0) === "column int")
      assert(schema.getFieldName(1) === "c_long")
    }
  }

  test("createParquetMessageType converts field names to lowercase when enabled") {
    val tests = Map(
      true -> "c_long",
      false -> "c_LONG"
    )
    tests.foreach { case (isLowercase, expectedFieldName) =>
      val converter = ParquetSchemaConverter(isLowercase)
      val columns = Seq(ExaColumnInfo("\"c_LONG\"", classOf[java.lang.Long], 12, 0, 0, true))
      val schema = converter.createParquetMessageType(columns, "test")
      assert(schema.getFieldName(0) === expectedFieldName)
    }
  }

}
