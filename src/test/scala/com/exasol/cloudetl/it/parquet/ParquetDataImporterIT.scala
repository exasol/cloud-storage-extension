package com.exasol.cloudetl.parquet

import java.math._
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.sql.Timestamp
import java.time._
import java.util.UUID

import com.exasol.cloudetl.BaseDataImporter
import com.exasol.cloudetl.helper.DateTimeConverter._
import com.exasol.cloudetl.helper.UUIDConverter
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.io.api.Binary
import org.apache.parquet.schema._

class ParquetDataImporterIT extends BaseDataImporter {

  override val schemaName = "PARQUET_SCHEMA"
  override val bucketName = "parquet-bucket"
  override val dataFormat = "parquet"

  test("imports boolean") {
    ParquetChecker("optional boolean column;", "BOOLEAN", "boolean_table")
      .withInputValues[Any](List(true, false, null))
      .assertResultSet(
        table()
          .row(java.lang.Boolean.TRUE)
          .row(java.lang.Boolean.FALSE)
          .row(null)
          .matches()
      )
  }

  test("imports int32") {
    ParquetChecker("optional int32 column;", "DECIMAL(10,0)", "int32")
      .withInputValues[Any](List(INT_MIN, 666, null, INT_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Integer.valueOf(INT_MIN))
          .row(java.lang.Integer.valueOf(666))
          .row(null)
          .row(java.lang.Integer.valueOf(INT_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int32 (date)") {
    ParquetChecker("required int32 column (DATE);", "DATE", "int32_date")
      .withInputValues[Any](List(0, 1, 54, 567, 1234))
      .assertResultSet(
        table()
          .row(daysToDate(0))
          .row(daysToDate(1))
          .row(daysToDate(54))
          .row(daysToDate(567))
          .row(daysToDate(1234))
          .matches()
      )
  }

  test("imports int32 (decimal)") {
    ParquetChecker("required int32 column (DECIMAL(8,3));", "DECIMAL(18,3)", "int32_decimal")
      .withInputValues[Any](List(1, 34, 567, 1234))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(0.001))
          .row(java.lang.Double.valueOf(0.034))
          .row(java.lang.Double.valueOf(0.567))
          .row(java.lang.Double.valueOf(1.234))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int64") {
    ParquetChecker("optional int64 column;", "DECIMAL(19,0)", "int64")
      .withInputValues[Any](List(LONG_MIN, 999L, null, LONG_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Long.valueOf(LONG_MIN))
          .row(java.lang.Long.valueOf(999))
          .row(null)
          .row(java.lang.Long.valueOf(LONG_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int64 (decimal)") {
    ParquetChecker("optional int64 column (DECIMAL(12,2));", "DECIMAL(27,2)", "int64_decimal")
      .withInputValues[Any](List(271717171717L, 314141414141L, null))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(2717171717.17))
          .row(java.lang.Double.valueOf(3141414141.41))
          .row(null)
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int64 (timestamp millis)") {
    val millis1 = Instant.EPOCH.toEpochMilli()
    val millis2 = System.currentTimeMillis()
    val zdt1 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.of("Europe/Berlin"))
    val zdt2 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.of("Europe/Berlin"))
    val expectedTimestamp1 = Timestamp.valueOf(zdt1.toLocalDateTime())
    val expectedTimestamp2 = Timestamp.valueOf(zdt2.toLocalDateTime())

    ParquetChecker("optional int64 column (TIMESTAMP_MILLIS);", "TIMESTAMP", "int64_timestamp_millis")
      .withInputValues[Any](List(millis1, millis2, null))
      .assertResultSet(
        table()
          .row(expectedTimestamp1)
          .row(expectedTimestamp2)
          .row(null)
          .matches()
      )
  }

  test("imports int64 (timestamp micros)") {
    val timestamp = Timestamp.valueOf("2022-01-12 10:28:53.123456")
    val millis = timestamp.getTime()
    val micros = timestamp.getTime() * 1000L + (timestamp.getNanos().toLong / 1000) % 1000L
    val zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin"))
    val expectedTimestamp = Timestamp.valueOf(zdt.toLocalDateTime())

    ParquetChecker("optional int64 column (TIMESTAMP_MICROS);", "TIMESTAMP", "int64_timestamp_micros")
      .withInputValues[Any](List(micros, null))
      .assertResultSet(
        table()
          .row(expectedTimestamp)
          .row(null)
          .matches()
      )
  }

  test("imports float") {
    val EPS = java.math.BigDecimal.valueOf(0.0001)
    ParquetChecker("optional float column;", "FLOAT", "float_table")
      .withInputValues[Any](List(2.71f, 3.14f, null))
      .assertResultSet(
        table()
          .row(CellMatcherFactory.cellMatcher(2.71, STRICT, EPS))
          .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
          .row(null)
          .matches()
      )
  }

  test("imports double") {
    ParquetChecker("optional double column;", "DOUBLE", "double_table")
      .withInputValues[Any](List(20.21, 1.13, null))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(20.21))
          .row(java.lang.Double.valueOf(1.13))
          .row(null)
          .matches()
      )
  }

  test("imports binary") {
    ParquetChecker("optional binary column;", "VARCHAR(20)", "binary_table")
      .withInputValues[Any](List("hello", "world", null))
      .assertResultSet(
        table()
          .row("hello")
          .row("world")
          .row(null)
          .matches()
      )
  }

  // scalastyle:off nonascii
  test("imports binary (utf8)") {
    ParquetChecker("required binary column (UTF8);", "VARCHAR(20)", "binary_utf8_table")
      .withInputValues[String](List("ÄäÖöÜüß / ☺", "world", ""))
      .assertResultSet(
        table()
          .row("ÄäÖöÜüß / ☺")
          .row("world")
          .row(null)
          .matches()
      )
  }
  // scalastyle:on

  test("imports binary (decimal)") {
    val decimal1 = Binary.fromConstantByteArray(new BigDecimal("12345").unscaledValue().toByteArray())
    val decimal2 = Binary.fromConstantByteArray(new BigDecimal("123456").unscaledValue().toByteArray())
    ParquetChecker("required binary column (DECIMAL(8,3));", "DECIMAL(18,3)", "binary_decimal")
      .withInputValues[Binary](List(decimal1, decimal2))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(12.345))
          .row(java.lang.Double.valueOf(123.456))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports fixed_len_byte_array") {
    ParquetChecker("required fixed_len_byte_array(5) column;", "VARCHAR(5)", "fixed")
      .withInputValues[String](List("abcde", "world", "     "))
      .assertResultSet(
        table()
          .row("abcde")
          .row("world")
          .row("     ")
          .matches()
      )
  }

  test("imports fixed_len_byte_array (decimal)") {
    // Length n can store <= floor(log_10(2^(8*n - 1) - 1)) base-10 digits
    val decimalValueString = "12345678901234567890"
    val decimal1 = Binary.fromConstantByteArray(new BigDecimal(decimalValueString).unscaledValue().toByteArray())
    val decimal2 = Binary.fromConstantByteArray(Array.fill[Byte](9)(0x0), 0, 9)
    ParquetChecker("required fixed_len_byte_array(9) column (DECIMAL(20,5));", "DECIMAL(20,5)", "fixed_decimal")
      .withInputValues[Binary](List(decimal1, decimal2))
      .assertResultSet(
        table()
          .row(new BigDecimal(new BigInteger(decimalValueString), 5, new MathContext(20)))
          .row(java.lang.Double.valueOf(0.00))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports fixed_len_byte_array (uuid)") {
    val uuid = UUID.randomUUID()
    val uuidBinary = Binary.fromConstantByteArray(UUIDConverter.toByteArray(uuid))
    val uuidZeros = Binary.fromConstantByteArray(Array.fill[Byte](16)(0x0), 0, 16)
    ParquetChecker("required fixed_len_byte_array(16) column (UUID);", "VARCHAR(36)", "fixed_uuid")
      .withInputValues[Binary](List(uuidBinary, uuidZeros))
      .assertResultSet(
        table()
          .row(uuid.toString())
          .row("00000000-0000-0000-0000-000000000000")
          .matches()
      )
  }

  test("imports int96 (timestamp nanos)") {
    val millis = System.currentTimeMillis()
    val timestamp = new Timestamp(millis)
    val buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
    val micros = getMicrosFromTimestamp(timestamp)
    val (days, nanos) = getJulianDayAndNanos(micros)
    buffer.putLong(nanos).putInt(days)
    val zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin"))
    val expectedTimestamp = Timestamp.valueOf(zdt.toLocalDateTime())

    ParquetChecker("required int96 column;", "TIMESTAMP", "int96_timestamp")
      .withInputValues[Binary](List(Binary.fromConstantByteArray(buffer.array())))
      .assertResultSet(
        table()
          .row(expectedTimestamp)
          .matches()
      )
  }

  test("imports list of strings") {
    val parquetType =
      """|optional group names (LIST) {
         |  repeated group list {
         |    required binary name (UTF8);
         |  }
         |}
      """.stripMargin

    ParquetChecker(parquetType, "VARCHAR(20)", "list_strings")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val names = record.addGroup(0)
        names.addGroup(0).append("name", "John")
        names.addGroup(0).append("name", "Jana")
        writer.write(record)
      }
      .assertResultSet(table().row("""["John","Jana"]""").matches())
  }

  test("imports list of doubles") {
    val parquetType =
      """|optional group prices (LIST) {
         |  repeated group list {
         |    required double price;
         |  }
         |}
      """.stripMargin

    ParquetChecker(parquetType, "VARCHAR(20)", "list_doubles")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val prices = record.addGroup(0)
        prices.addGroup(0).append("price", 0.14)
        prices.addGroup(0).append("price", 1.234)
        writer.write(record)
      }
      .assertResultSet(table().row("[0.14,1.234]").matches())
  }

  test("imports list of ints") {
    val parquetType =
      """|optional group ages (LIST) {
         |  repeated group list {
         |    required int32 age;
         |  }
         |}
      """.stripMargin

    ParquetChecker(parquetType, "VARCHAR(20)", "list_ints")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val ages = record.addGroup(0)
        ages.addGroup(0).append("age", 21)
        ages.addGroup(0).append("age", 12)
        writer.write(record)
      }
      .assertResultSet(table().row("[21,12]").matches())
  }

  test("imports list of lists") {
    val parquetType =
      """|optional group arrays (LIST) {
         |  repeated group list {
         |    required group inner (LIST) {
         |      repeated group list {
         |        required binary element;
         |      }
         |    }
         |  }
         |}
      """.stripMargin

    ParquetChecker(parquetType, "VARCHAR(20)", "list_lists")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val arrays = record.addGroup(0).addGroup(0)
        var inner = arrays.addGroup("inner")
        inner.addGroup(0).append("element", "a")
        inner.addGroup(0).append("element", "b")
        inner = arrays.addGroup("inner")
        inner.addGroup(0).append("element", "c")
        writer.write(record)
      }
      .assertResultSet(table().row("""[["a","b"],["c"]]""").matches())
  }

  test("imports list of maps") {
    val parquetType =
      """|optional group maps (LIST) {
         |  repeated group list {
         |    optional group map (MAP) {
         |      repeated group key_value {
         |        required binary name (UTF8);
         |        optional int32 age;
         |      }
         |    }
         |  }
         |}
      """.stripMargin
    ParquetChecker(parquetType, "VARCHAR(35)", "list_maps")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val array = record.addGroup(0).addGroup(0)
        var map = array.addGroup("map")
        map.addGroup("key_value").append("name", "bob").append("age", 14)
        map.addGroup("key_value").append("name", "jon").append("age", 12)
        map = array.addGroup("map")
        map.addGroup("key_value").append("name", "ted").append("age", 20)
        writer.write(record)
      }
      .assertResultSet(table().row("""[{"bob":14,"jon":12},{"ted":20}]""").matches())
  }

  test("imports map") {
    val parquetType =
      """|optional group map (MAP) {
         |  repeated group key_value {
         |    required binary key (UTF8);
         |    required int64 value;
         |  }
         |}
      """.stripMargin
    ParquetChecker(parquetType, "VARCHAR(20)", "map_table")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val map = record.addGroup(0)
        map.addGroup("key_value").append("key", "key1").append("value", 3L)
        map.addGroup("key_value").append("key", "key2").append("value", 7L)
        writer.write(record)
      }
      .assertResultSet(table().row("""{"key1":3,"key2":7}""").matches())
  }

  test("imports map with list values") {
    val parquetType =
      """|optional group map (MAP) {
         |  repeated group key_value {
         |    required binary brand (UTF8);
         |    optional group prices (LIST) {
         |      repeated group list {
         |        required double price;
         |      }
         |    }
         |  }
         |}
      """.stripMargin

    ParquetChecker(parquetType, "VARCHAR(20)", "map_list_values")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        val map = record.addGroup(0).addGroup("key_value")
        val prices = map.append("brand", "nike").addGroup("prices")
        prices.addGroup(0).append("price", 0.14)
        prices.addGroup(0).append("price", 5.11)
        writer.write(record)
      }
      .assertResultSet(table().row("""{"nike":[0.14,5.11]}""").matches())
  }

  test("imports repeated field") {
    val parquetType = "repeated binary name (UTF8);"
    ParquetChecker(parquetType, "VARCHAR(20)", "repeated_field")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        record.add(0, "John")
        record.add(0, "Jane")
        writer.write(record)
      }
      .assertResultSet(table().row("""["John","Jane"]""").matches())
  }

  test("imports repeated group with single field") {
    val parquetType =
      """|repeated group person {
         |  required binary name (UTF8);
         |}
      """.stripMargin
    ParquetChecker(parquetType, "VARCHAR(20)", "repeated_group_single_field")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        var person = record.addGroup(0)
        person.append("name", "John")
        person = record.addGroup(0)
        person.append("name", "Jane")
        writer.write(record)
      }
      .assertResultSet(table().row("""["John","Jane"]""").matches())
  }

  test("imports repeated group with multiple fields") {
    val parquetType =
      """|repeated group person {
         |  required binary name (UTF8);
         |  optional int32 age;
         |}
      """.stripMargin
    ParquetChecker(parquetType, "VARCHAR(60)", "repeated_group_many_fields")
      .withWriter { case (writer, schema) =>
        val record = new SimpleGroup(schema)
        var person = record.addGroup(0)
        person.append("name", "John").append("age", 24)
        person = record.addGroup(0)
        person.append("name", "Jane").append("age", 22)
        writer.write(record)
      }
      .assertResultSet(
        table()
          .row("""[{"name":"John","age":24},{"name":"Jane","age":22}]""")
          .matches()
      )
  }

  test("imports multiple columns") {
    MultiParquetChecker(
      "required binary name (UTF8); required int32 age;",
      Map("NAME" -> "VARCHAR(60)", "AGE" -> "INTEGER"),
      "multi_col"
    )
      .withWriter { case (writer, schema) =>
        writer.write(new SimpleGroup(schema).append("name", "John").append("age", 24))
        writer.write(new SimpleGroup(schema).append("name", "Jane").append("age", 22))
      }
      .assertResultSet(
        table()
          .row("John", 24)
          .row("Jane", 22)
          .matches()
      )
  }

  case class ParquetChecker(parquetColumn: String, exaColumn: String, tableName: String)
      extends AbstractChecker(exaColumn, tableName)
      with ParquetTestDataWriter {
    private val parquetSchema = MessageTypeParser.parseMessageType(s"message test { $parquetColumn }")

    def withWriter(block: (ParquetWriter[Group], MessageType) => Unit): ParquetChecker = {
      val writer = getParquetWriter(path, parquetSchema, true)
      block(writer, parquetSchema)
      writer.close()
      this
    }

    def withInputValues[T](values: List[T]): ParquetChecker = {
      writeDataValues(values, path, parquetSchema)
      this
    }
  }

  case class MultiParquetChecker(parquetColumn: String, exaColumns: Map[String, String], tableName: String)
      extends AbstractMultiColChecker(exaColumns, tableName)
      with ParquetTestDataWriter {
    private val parquetSchema = MessageTypeParser.parseMessageType(s"message test { $parquetColumn }")

    def withWriter(block: (ParquetWriter[Group], MessageType) => Unit): MultiParquetChecker = {
      val writer = getParquetWriter(path, parquetSchema, true)
      block(writer, parquetSchema)
      writer.close()
      this
    }

    def withInputValues[T](values: List[T]): MultiParquetChecker = {
      writeDataValues(values, path, parquetSchema)
      this
    }
  }

}
