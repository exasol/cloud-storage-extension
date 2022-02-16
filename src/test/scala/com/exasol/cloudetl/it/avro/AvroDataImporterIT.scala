package com.exasol.cloudetl.avro

import java.io.File
import java.math._
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets.UTF_8
import java.sql.Timestamp
import java.time._

import com.exasol.cloudetl.BaseDataImporter
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import org.apache.avro._
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic._
import org.apache.avro.specific.SpecificDatumWriter

class AvroDataImporterIT extends BaseDataImporter {

  override val schemaName = "AVRO_SCHEMA"
  override val bucketName = "avro-bucket"
  override val dataFormat = "avro"

  private[this] def getBasicSchema(avroType: String): String =
    s"""|{
        |  "type": "record",
        |  "namespace": "avro.types",
        |  "name": "basic",
        |  "fields": [{
        |    "name": "column",
        |    "type": $avroType
        |  }]
        |}
        """.stripMargin

  test("imports boolean") {
    AvroChecker(getBasicSchema("\"boolean\""), "BOOLEAN", "bool")
      .withInputValues(List(true, false))
      .assertResultSet(
        table()
          .row(java.lang.Boolean.TRUE)
          .row(java.lang.Boolean.FALSE)
          .matches()
      )
  }

  test("imports int") {
    AvroChecker(getBasicSchema("\"int\""), "DECIMAL(10,0)", "int")
      .withInputValues(List(INT_MIN, 13, INT_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Integer.valueOf(INT_MIN))
          .row(java.lang.Integer.valueOf(13))
          .row(java.lang.Integer.valueOf(INT_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int (date)") {
    val avroSchema = getBasicSchema("""{"type":"int","logicalType":"date"}""")
    AvroChecker(avroSchema, "DATE", "int_date")
      .withInputValues(List(0, 1))
      .assertResultSet(
        table()
          .row(java.sql.Date.valueOf("1970-01-01"))
          .row(java.sql.Date.valueOf("1970-01-02"))
          .matches()
      )
  }

  test("imports long") {
    AvroChecker(getBasicSchema("\"long\""), "DECIMAL(19,0)", "long")
      .withInputValues(List(LONG_MIN, 77L, LONG_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Long.valueOf(LONG_MIN))
          .row(java.lang.Long.valueOf(77))
          .row(java.lang.Long.valueOf(LONG_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports long (timestamp-millis)") {
    val schema = getBasicSchema("""{"type":"long","logicalType":"timestamp-millis"}""")
    val millis = System.currentTimeMillis()
    val zdt1 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin"))
    val zdt2 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("Europe/Berlin"))
    val expectedTimestamp1 = Timestamp.valueOf(zdt1.toLocalDateTime())
    val expectedTimestamp2 = Timestamp.valueOf(zdt2.toLocalDateTime())

    AvroChecker(schema, "TIMESTAMP", "long_timestamp")
      .withInputValues(List(millis, 0L))
      .assertResultSet(
        table()
          .row(expectedTimestamp1)
          .row(expectedTimestamp2)
          .matches()
      )
  }

  test("imports float") {
    val EPS = java.math.BigDecimal.valueOf(0.0001)
    AvroChecker(getBasicSchema("\"float\""), "FLOAT", "float")
      .withInputValues(List(3.14f, 2.71f))
      .assertResultSet(
        table()
          .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
          .row(CellMatcherFactory.cellMatcher(2.71, STRICT, EPS))
          .matches()
      )
  }

  test("imports double") {
    AvroChecker(getBasicSchema("\"double\""), "DOUBLE", "double")
      .withInputValues(List(3.14, 2.71))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(3.14))
          .row(java.lang.Double.valueOf(2.71))
          .matches()
      )
  }

  test("imports bytes") {
    AvroChecker(getBasicSchema("\"bytes\""), "VARCHAR(20)", "bytes")
      .withInputValues(List(ByteBuffer.wrap("hello".getBytes(UTF_8))))
      .assertResultSet(
        table()
          .row("hello")
          .matches()
      )
  }

  test("imports bytes (decimal)") {
    val decimal1 = ByteBuffer.wrap(new BigDecimal("123456").unscaledValue().toByteArray())
    val decimal2 = ByteBuffer.wrap(new BigDecimal("12345678").unscaledValue().toByteArray())
    val inner = """{"type":"bytes","logicalType":"decimal","precision":8,"scale":3}"""
    AvroChecker(getBasicSchema(inner), "DECIMAL(8,3)", "bytes_decimal")
      .withInputValues(List(decimal1, decimal2))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(123.456))
          .row(java.lang.Double.valueOf(12345.678))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports fixed") {
    val schema = getBasicSchema("""{"type":"fixed","name":"fixed", "size":5}""")
    val fixedSchema = new Schema.Parser().parse(schema).getField("column").schema()
    val fixedData = new GenericData.Fixed(fixedSchema)
    fixedData.bytes("fixed".getBytes(UTF_8))
    AvroChecker(schema, "VARCHAR(20)", "fixed")
      .withInputValues(List(fixedData))
      .assertResultSet(
        table()
          .row("fixed")
          .matches()
      )
  }

  test("imports fixed (decimal)") {
    val inner = """{"type":"fixed","name":"fx","size":7,"logicalType":"decimal","precision":7,"scale":4}"""
    val schema = getBasicSchema(inner)
    val fixedSchema = new Schema.Parser().parse(schema).getField("column").schema()
    val fixedData = new Conversions.DecimalConversion().toFixed(
      new BigDecimal("0.0123"),
      fixedSchema,
      LogicalTypes.decimal(7, 4)
    )
    AvroChecker(schema, "DECIMAL(7,4)", "fixed_decimal")
      .withInputValues(List(fixedData))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(0.0123))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports string") {
    AvroChecker(getBasicSchema("\"string\""), "VARCHAR(20)", "string_table")
      .withInputValues(List("hello", "worldÜüß"))
      .assertResultSet(
        table()
          .row("hello")
          .row("worldÜüß")
          .matches()
      )
  }

  test("imports enum") {
    val schema = getBasicSchema("""{"type":"enum","name":"lttrs","symbols":["A","B","C"]}""")
    val enumSchema = new Schema.Parser().parse(schema).getField("column").schema()
    AvroChecker(schema, "VARCHAR(20)", "enum_table")
      .withInputValues(List(new GenericData.EnumSymbol(enumSchema, "B")))
      .assertResultSet(
        table()
          .row("B")
          .matches()
      )
  }

  test("imports union") {
    AvroChecker(getBasicSchema("""["string", "null"]"""), "VARCHAR(20)", "union_table")
      .withInputValues(List("str-value", null))
      .assertResultSet(
        table()
          .row("str-value")
          .row(null)
          .matches()
      )
  }

  test("imports array of strings") {
    val schema = getBasicSchema("""{"type":"array","items":"string"}""")
    AvroChecker(schema, "VARCHAR(20)", "array_strings")
      .withInputValues(List(java.util.List.of("a", "b"), java.util.List.of("c", "d")))
      .assertResultSet(
        table()
          .row("""["a","b"]""")
          .row("""["c","d"]""")
          .matches()
      )
  }

  test("imports array of ints") {
    val schema = getBasicSchema("""{"type":"array","items":"int"}""")
    AvroChecker(schema, "VARCHAR(20)", "array_ints")
      .withInputValues(List(java.util.List.of(4, 5, 6)))
      .assertResultSet(
        table()
          .row("[4,5,6]")
          .matches()
      )
  }

  test("imports array of doubles") {
    val schema = getBasicSchema("""{"type":"array","items":"double"}""")
    AvroChecker(schema, "VARCHAR(20)", "array_doubles")
      .withInputValues(List(java.util.List.of(1.01, 3.14, 2.71)))
      .assertResultSet(
        table()
          .row("[1.01,3.14,2.71]")
          .matches()
      )
  }

  test("imports array of arrays") {
    val inner = """{"type":"array","items":"int"}"""
    val schema = getBasicSchema(s"""{"type":"array","items":$inner}""")
    val input = java.util.List.of(java.util.List.of(1, 2, 3), java.util.List.of(5, 6))
    AvroChecker(schema, "VARCHAR(20)", "array_arrays")
      .withInputValues(List(input))
      .assertResultSet(
        table()
          .row("[[1,2,3],[5,6]]")
          .matches()
      )
  }

  test("imports array of maps") {
    val inner = """{"type":"map","values":"double"}"""
    val schema = getBasicSchema(s"""{"type":"array","items":$inner}""")
    val input =
      java.util.List.of(java.util.Map.of("k1", 1.1, "k2", 3.14), java.util.Map.of("k1", 0.1))
    AvroChecker(schema, "VARCHAR(40)", "array_maps")
      .withInputValues(List(input))
      .assertResultSet(
        table()
          .row("""[{"k1":1.1,"k2":3.14},{"k1":0.1}]""")
          .matches()
      )
  }

  test("imports map") {
    val schema = getBasicSchema("""{"type":"map","values":"int"}""")
    AvroChecker(schema, "VARCHAR(30)", "map_table")
      .withInputValues(List(java.util.Map.of("key1", 3, "key2", 6, "key3", 9)))
      .assertResultSet(
        table()
          .row("""{"key1":3,"key2":6,"key3":9}""")
          .matches()
      )
  }

  test("imports map with array values") {
    val inner = """{"type":"array","items":"int"}"""
    val schema = getBasicSchema(s"""{"type":"map","values":$inner}""")
    val input = java.util.Map.of("k1", java.util.List.of(1, 2, 3))
    AvroChecker(schema, "VARCHAR(20)", "map_array_values")
      .withInputValues(List(input))
      .assertResultSet(
        table()
          .row("""{"k1":[1,2,3]}""")
          .matches()
      )
  }

  test("imports nested record") {
    val inner =
      s"""|{
          |"type":"record",
          |"name":"Record",
          |"fields":[
          |   {"name":"name","type":"string"},
          |   {"name":"weight","type":"double"}
          |]}
      """.stripMargin
    val innerSchema = new Schema.Parser().parse(inner)
    val schema = getBasicSchema(s"""{"type":"array","items":$inner}""")

    val recordOne = new GenericData.Record(innerSchema)
    recordOne.put("name", "one")
    recordOne.put("weight", 67.14)
    val recordTwo = new GenericData.Record(innerSchema)
    recordTwo.put("name", "two")
    recordTwo.put("weight", 78.71)
    AvroChecker(schema, "VARCHAR(65)", "nested_records_table")
      .withInputValues(List(java.util.List.of(recordOne, recordTwo)))
      .assertResultSet(
        table()
          .row("""[{"name":"one","weight":67.14},{"name":"two","weight":78.71}]""")
          .matches()
      )
  }

  case class AvroChecker(avroSchemaStr: String, exaColumn: String, tableName: String)
      extends AbstractChecker(exaColumn, tableName) {
    val AVRO_SYNC_INTERVAL_SIZE = 64 * 1024 * 1024
    val avroSchema = new Schema.Parser().parse(avroSchemaStr)

    def withWriter(block: DataFileWriter[GenericRecord] => Unit): AvroChecker = {
      val writer = new DataFileWriter[GenericRecord](new SpecificDatumWriter[GenericRecord]())
      writer.setFlushOnEveryBlock(false)
      writer.setSyncInterval(AVRO_SYNC_INTERVAL_SIZE)
      writer.create(avroSchema, new File(path.toUri))
      block(writer)
      writer.close()
      this
    }

    def withInputValues[T](values: List[T]): AvroChecker = withWriter { writer =>
      values.foreach { value =>
        val record = new GenericData.Record(avroSchema)
        record.put("column", value)
        writer.append(record)
      }
      writer.close()
    }
  }

}
