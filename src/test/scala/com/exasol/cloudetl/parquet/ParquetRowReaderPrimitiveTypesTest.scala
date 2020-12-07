package com.exasol.cloudetl.parquet

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.sql.Timestamp

import com.exasol.cloudetl.parquet.converter.ParquetDecimalConverter
import com.exasol.common.data.Row

import org.apache.parquet.column.Dictionary
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.io.api.Binary
import org.apache.parquet.schema.LogicalTypeAnnotation.decimalType
import org.apache.parquet.schema.MessageTypeParser
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema.Types

class ParquetRowReaderPrimitiveTypesTest extends BaseParquetReaderTest {

  test("reads INT64 (TIMESTAMP_MILLIS) as timestamp value") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int64 col_long;
         |  required int64 col_timestamp (TIMESTAMP_MILLIS);
         |}
         |""".stripMargin
    )
    val timestamp = new Timestamp(System.currentTimeMillis())
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_long", 153L)
      record.append("col_timestamp", timestamp.getTime())
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq(153L, timestamp)))
  }

  test("reads FIXED_LEN_BYTE_ARRAY as string value") {
    val size = 5
    val schema = MessageTypeParser.parseMessageType(
      s"""|message test {
          |  required fixed_len_byte_array($size) col_byte_array;
          |}
          |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_byte_array", "hello")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("hello")))
  }

  test("reads BINARY as string value") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary col_binary;
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_binary", "test")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("test")))
  }

  test("reads BINARY (UTF8) as string value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary col_binary (UTF8);
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq("test1", "test2", "test1", "test2").foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("col_binary", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq("test1")))
  }

  test("reads INT32 (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int32 col_int (DECIMAL(9,2));
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(123456789, 0, 0, 123456789).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("col_int", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(BigDecimal.valueOf(123456789, 2))))
  }

  test("reads INT64 (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int64 column (DECIMAL(18,2));
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(1234567890123456L, 1L, 1L, 1234567890123456L).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(BigDecimal.valueOf(1234567890123456L, 2))))
  }

  test("reads BINARY (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary column (DECIMAL(30,2));
         |}
         |""".stripMargin
    )
    val decimalValue = "123456789012345678901234567890"
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(decimalValue, decimalValue).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)
      }
    }
    val expected = new BigDecimal(new BigInteger(decimalValue.getBytes()), 2, new MathContext(30))
    val records = getRecords()
    assert(records.size === 2)
    assert(records(0) === Row(Seq(expected)))
  }

  test("reads FIXED_LEN_BYTE_ARRAY (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required fixed_len_byte_array(20) column (DECIMAL(20,2));
         |}
         |""".stripMargin
    )
    val decimalValue = "12345678901234567890"
    val bytes = Array.fill[Byte](30)(0x1)
    val zeros = Binary.fromConstantByteArray(bytes, 0, 20)
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(decimalValue, decimalValue).foreach { value =>
        var record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)

        record = new SimpleGroup(schema)
        record.append("column", zeros)
        writer.write(record)
      }
    }
    val expected = new BigDecimal(new BigInteger(decimalValue.getBytes()), 2, new MathContext(20))
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(expected)))
  }

  test("read throws when decimal converter has unsupported base primitive type") {
    final case class DictionaryEncoding() extends Dictionary(null) {
      override def getMaxId(): Int = 4
    }
    val parquetType = Types
      .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.OPTIONAL)
      .length(4)
      .as(decimalType(0, 9))
      .named("bytes")
    val thrown = intercept[UnsupportedOperationException] {
      ParquetDecimalConverter(parquetType, 0, null).setDictionary(DictionaryEncoding())
    }
    assert(thrown.getMessage.contains("Cannot convert parquet type to decimal type"))
  }

}
