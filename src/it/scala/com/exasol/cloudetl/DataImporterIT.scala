package com.exasol.cloudetl

import java.io.File
import java.math._
import java.nio.file.Path
import java.nio.ByteOrder
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.Timestamp
import java.time._

import com.exasol.cloudetl.orc.OrcTestDataWriter
import com.exasol.cloudetl.parquet.ParquetTestDataWriter
import com.exasol.cloudetl.util.DateTimeUtil
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.avro.file.DataFileWriter
import org.apache.avro._
import org.apache.avro.generic._
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.io.api.Binary
import org.apache.parquet.schema._
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class DataImporterIT extends BaseIntegrationTest {

  val INT_MIN = -2147483648
  val INT_MAX = 2147483647
  val LONG_MIN = -9223372036854775808L
  val LONG_MAX = 9223372036854775807L
  val schemaName = "DATA_SCHEMA"
  val bucketName: String = "databucket"
  var conf: Configuration = _
  var s3: AmazonS3 = _

  override final def beforeAll(): Unit = {
    startContainers()
    prepareExasolDatabase(schemaName)
    s3 = AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(s3Container.getEndpointConfiguration(S3))
      .withCredentials(s3Container.getDefaultCredentialsProvider())
      .disableChunkedEncoding()
      .build()
    conf = new Configuration
  }

  override def nestedSuites = Vector(
    new AvroDataImporter(),
    new OrcDataImporter(),
    new ParquetDataImporter()
  )

  class AvroDataImporter extends BaseDataImporter {
    val dataFormat = "avro"

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
      AvroChecker(getBasicSchema("\"boolean\""), "BOOLEAN")
        .withInputValues(List(true, false))
        .assertResultSet(
          table()
            .row(java.lang.Boolean.TRUE)
            .row(java.lang.Boolean.FALSE)
            .matches()
        )
    }

    test("imports int") {
      AvroChecker(getBasicSchema("\"int\""), "DECIMAL(10,0)")
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
      AvroChecker(avroSchema, "DATE")
        .withInputValues(List(0, 1))
        .assertResultSet(
          table()
            .row(java.sql.Date.valueOf("1970-01-01"))
            .row(java.sql.Date.valueOf("1970-01-02"))
            .matches()
        )
    }

    test("imports long") {
      AvroChecker(getBasicSchema("\"long\""), "DECIMAL(19,0)")
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

      AvroChecker(schema, "TIMESTAMP")
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
      AvroChecker(getBasicSchema("\"float\""), "FLOAT")
        .withInputValues(List(3.14f, 2.71F))
        .assertResultSet(
          table()
            .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
            .row(CellMatcherFactory.cellMatcher(2.71, STRICT, EPS))
            .matches()
        )
    }

    test("imports double") {
      AvroChecker(getBasicSchema("\"double\""), "DOUBLE")
        .withInputValues(List(3.14, 2.71))
        .assertResultSet(
          table()
            .row(java.lang.Double.valueOf(3.14))
            .row(java.lang.Double.valueOf(2.71))
            .matches()
        )
    }

    test("imports bytes") {
      AvroChecker(getBasicSchema("\"bytes\""), "VARCHAR(20)")
        .withInputValues(List(ByteBuffer.wrap("hello".getBytes("UTF-8"))))
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
      AvroChecker(getBasicSchema(inner), "DECIMAL(8,3)")
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
      fixedData.bytes("fixed".getBytes("UTF-8"))
      AvroChecker(schema, "VARCHAR(20)")
        .withInputValues(List(fixedData))
        .assertResultSet(
          table()
            .row("fixed")
            .matches()
        )
    }

    test("imports fixed (decimal)") {
      val inner =
        """{"type":"fixed","name":"fx","size":7,"logicalType":"decimal","precision":7,"scale":4}"""
      val schema = getBasicSchema(inner)
      val fixedSchema = new Schema.Parser().parse(schema).getField("column").schema()
      val fixedData = new Conversions.DecimalConversion().toFixed(
        new BigDecimal("0.0123"),
        fixedSchema,
        LogicalTypes.decimal(7, 4)
      )
      AvroChecker(schema, "DECIMAL(7,4)")
        .withInputValues(List(fixedData))
        .assertResultSet(
          table()
            .row(java.lang.Double.valueOf(0.0123))
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports string") {
      AvroChecker(getBasicSchema("\"string\""), "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(20)")
        .withInputValues(List(new GenericData.EnumSymbol(enumSchema, "B")))
        .assertResultSet(
          table()
            .row("B")
            .matches()
        )
    }

    test("imports union") {
      AvroChecker(getBasicSchema("""["string", "null"]"""), "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(20)")
        .withInputValues(List(java.util.List.of(4, 5, 6)))
        .assertResultSet(
          table()
            .row("[4,5,6]")
            .matches()
        )
    }

    test("imports array of doubles") {
      val schema = getBasicSchema("""{"type":"array","items":"double"}""")
      AvroChecker(schema, "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(40)")
        .withInputValues(List(input))
        .assertResultSet(
          table()
            .row("""[{"k1":1.1,"k2":3.14},{"k1":0.1}]""")
            .matches()
        )
    }

    test("imports map") {
      val schema = getBasicSchema("""{"type":"map","values":"int"}""")
      AvroChecker(schema, "VARCHAR(30)")
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
      AvroChecker(schema, "VARCHAR(20)")
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
      AvroChecker(schema, "VARCHAR(65)")
        .withInputValues(List(java.util.List.of(recordOne, recordTwo)))
        .assertResultSet(
          table()
            .row("""[{"name":"one","weight":67.14},{"name":"two","weight":78.71}]""")
            .matches()
        )
    }
  }

  class OrcDataImporter extends BaseDataImporter {
    val dataFormat = "orc"

    test("imports boolean") {
      OrcChecker("struct<f:boolean>", "BOOLEAN")
        .withInputValues(List(true, false, null))
        .assertResultSet(
          table()
            .row(java.lang.Boolean.TRUE)
            .row(java.lang.Boolean.FALSE)
            .row(null)
            .matches()
        )
    }

    test("imports byte") {
      OrcChecker("struct<f:tinyint>", "DECIMAL(9,0)")
        .withInputValues(List(11, null))
        .assertResultSet(
          table().row(java.lang.Byte.valueOf("11")).row(null).matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports short") {
      OrcChecker("struct<f:smallint>", "DECIMAL(9,0)")
        .withInputValues(List(13, null))
        .assertResultSet(
          table().row(java.lang.Short.valueOf("13")).row(null).matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports int") {
      OrcChecker("struct<f:int>", "DECIMAL(10,0)")
        .withInputValues(List(INT_MIN, 999, null, INT_MAX))
        .assertResultSet(
          table()
            .row(java.lang.Integer.valueOf(INT_MIN))
            .row(java.lang.Integer.valueOf(999))
            .row(null)
            .row(java.lang.Integer.valueOf(INT_MAX))
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports long") {
      OrcChecker("struct<f:bigint>", "DECIMAL(19,0)")
        .withInputValues(List(LONG_MIN, 1234L, null, LONG_MAX))
        .assertResultSet(
          table()
            .row(java.lang.Long.valueOf(LONG_MIN))
            .row(java.lang.Long.valueOf(1234))
            .row(null)
            .row(java.lang.Long.valueOf(LONG_MAX))
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports float") {
      val EPS = java.math.BigDecimal.valueOf(0.0001)
      OrcChecker("struct<f:float>", "FLOAT")
        .withInputValues(List(3.14F, null))
        .assertResultSet(
          table()
            .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
            .row(null)
            .matches()
        )
    }

    test("imports double") {
      OrcChecker("struct<f:double>", "DOUBLE")
        .withInputValues(List(2.71, null))
        .assertResultSet(table().row(java.lang.Double.valueOf(2.71)).row(null).matches())
    }

    test("imports char") {
      OrcChecker("struct<f:char(3)>", "VARCHAR(3)")
        .withInputValues(List("a✅a", null))
        .assertResultSet(table().row("a✅a").row(null).matches())
    }

    test("imports varchar") {
      OrcChecker("struct<f:varchar(5)>", "VARCHAR(5)")
        .withInputValues(List("hello", "world", null))
        .assertResultSet(table().row("hello").row("world").row(null).matches())
    }

    test("imports binary") {
      OrcChecker("struct<f:string>", "VARCHAR(10)")
        .withInputValues(List("中文", null))
        .assertResultSet(table().row("中文").row(null).matches())
    }

    test("imports string") {
      OrcChecker("struct<f:string>", "VARCHAR(10)")
        .withInputValues(List("value", null))
        .assertResultSet(table().row("value").row(null).matches())
    }

    test("imports decimal") {
      OrcChecker("struct<f:decimal(6,3)>", "DECIMAL(6,3)")
        .withInputValues(List("333.333", "0.666", null))
        .assertResultSet(
          table()
            .row(java.lang.Double.valueOf(333.333))
            .row(java.lang.Double.valueOf(0.666))
            .row(null)
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports date") {
      OrcChecker("struct<f:date>", "DATE")
        .withInputValues(List(0, 1, null))
        .assertResultSet(
          table()
            .row(java.sql.Date.valueOf("1970-01-01"))
            .row(java.sql.Date.valueOf("1970-01-02"))
            .row(null)
            .matches()
        )
    }

    test("imports timestamp") {
      val timestamp1 = Timestamp.from(java.time.Instant.EPOCH)
      val timestamp2 = new Timestamp(System.currentTimeMillis())
      OrcChecker("struct<f:timestamp>", "TIMESTAMP")
        .withInputValues(List(timestamp1, timestamp2, null))
        .assertResultSet(
          table()
            .row(timestamp1)
            .row(timestamp2)
            .row(null)
            .matches()
        )
    }

    test("imports list of strings") {
      OrcChecker("struct<f:array<string>>", "VARCHAR(30)")
        .withInputValues(List(Seq("Brandon", "Eddard", null)))
        .assertResultSet(table().row("""["Brandon","Eddard",null]""").matches())
    }

    test("imports list of doubles") {
      OrcChecker("struct<f:array<double>>", "VARCHAR(20)")
        .withInputValues(List(Seq(1.11, 2.22, null)))
        .assertResultSet(table().row("[1.11,2.22,null]").matches())
    }

    test("imports list of ints") {
      OrcChecker("struct<f:array<int>>", "VARCHAR(10)")
        .withInputValues(List(Seq(3, 7, 1)))
        .assertResultSet(table().row("[3,7,1]").matches())
    }

    test("imports empty list") {
      OrcChecker("struct<f:array<string>>", "VARCHAR(10)")
        .withInputValues(List(null))
        .assertResultSet(table().row("[]").matches())
    }

    test("imports list of lists") {
      OrcChecker("struct<f:array<array<int>>>", "VARCHAR(30)")
        .withInputValues(List(Seq(List(314, 271), List(1, 2, 4))))
        .assertResultSet(table().row("[[314,271],[1,2,4]]").matches())
    }

    test("imports list of maps") {
      OrcChecker("struct<f:array<map<string,int>>>", "VARCHAR(30)")
        .withInputValues(List(Seq(Map("p" -> 314), Map("a" -> 1, "b" -> 2))))
        .assertResultSet(table().row("""[{"p":314},{"b":2,"a":1}]""").matches())
    }

    test("imports map") {
      OrcChecker("struct<f:map<string,int>>", "VARCHAR(30)")
        .withInputValues(List(Map("a" -> 3, "b" -> 5, "c" -> 7)))
        .assertResultSet(table().row("""{"b":5,"a":3,"c":7}""").matches())
    }

    test("imports map with list values") {
      val input = Map("consts" -> List(3.14, 2.71), "nums" -> List(1.0, 0.5))
      OrcChecker("struct<f:map<string,array<double>>>", "VARCHAR(40)")
        .withInputValues(List(input))
        .assertResultSet(table().row("""{"consts":[3.14,2.71],"nums":[1.0,0.5]}""").matches())
    }

    test("imports nested struct") {
      val input: Map[String, Any] =
        Map("name" -> "Jon", "phoneNumber" -> 1337, "pets" -> Seq("cat", "direwolf"))
      OrcChecker(
        "struct<f:struct<name:string,phoneNumber:int,pets:array<string>>>",
        "VARCHAR(60)"
      ).withInputValues(List(input))
        .assertResultSet(
          table()
            .row("""{"phoneNumber":1337,"name":"Jon","pets":["cat","direwolf"]}""")
            .matches()
        )
    }

    test("imports union") {
      val orcType = "struct<f:uniontype<int,string>>"
      val orcSchema = TypeDescription.fromString(orcType)
      val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(orcSchema))
      val batch = orcSchema.createRowBatch()
      batch.size = 3
      val unionVector = batch.cols(0).asInstanceOf[UnionColumnVector]
      unionVector.noNulls = false
      // Set string type for the first row
      unionVector.tags(1) = 0
      unionVector.fields(1).asInstanceOf[BytesColumnVector].setVal(0, "str".getBytes("UTF-8"))
      // Set int type for the second row
      unionVector.tags(0) = 1
      unionVector.fields(0).asInstanceOf[LongColumnVector].vector(1) = 23
      // Set null for the third row
      unionVector.isNull(2) = true
      writer.addRowBatch(batch)
      writer.close()

      OrcChecker(orcType, "VARCHAR(30)")
        .assertResultSet(
          table()
            .row("""{"INT":null,"STRING":"str"}""")
            .row("""{"INT":23,"STRING":null}""")
            .row("""{"INT":null,"STRING":null}""")
            .matches()
        )
    }
  }

  class ParquetDataImporter extends BaseDataImporter {
    val dataFormat = "parquet"

    test("imports boolean") {
      ParquetChecker("optional boolean column;", "BOOLEAN")
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
      ParquetChecker("optional int32 column;", "DECIMAL(10,0)")
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
      ParquetChecker("required int32 column (DATE);", "DATE")
        .withInputValues[Any](List(0, 1, 54, 567, 1234))
        .assertResultSet(
          table()
            .row(DateTimeUtil.daysToDate(0))
            .row(DateTimeUtil.daysToDate(1))
            .row(DateTimeUtil.daysToDate(54))
            .row(DateTimeUtil.daysToDate(567))
            .row(DateTimeUtil.daysToDate(1234))
            .matches()
        )
    }

    test("imports int32 (decimal)") {
      ParquetChecker("required int32 column (DECIMAL(8,3));", "DECIMAL(18,3)")
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
      ParquetChecker("optional int64 column;", "DECIMAL(19,0)")
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
      ParquetChecker("optional int64 column (DECIMAL(12,2));", "DECIMAL(27,2)")
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
      val zdt1 =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.of("Europe/Berlin"))
      val zdt2 =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.of("Europe/Berlin"))
      val expectedTimestamp1 = Timestamp.valueOf(zdt1.toLocalDateTime())
      val expectedTimestamp2 = Timestamp.valueOf(zdt2.toLocalDateTime())

      ParquetChecker("optional int64 column (TIMESTAMP_MILLIS);", "TIMESTAMP")
        .withInputValues[Any](List(millis1, millis2, null))
        .assertResultSet(
          table()
            .row(expectedTimestamp1)
            .row(expectedTimestamp2)
            .row(null)
            .matches()
        )
    }

    test("imports float") {
      val EPS = java.math.BigDecimal.valueOf(0.0001)
      ParquetChecker("optional float column;", "FLOAT")
        .withInputValues[Any](List(2.71f, 3.14F, null))
        .assertResultSet(
          table()
            .row(CellMatcherFactory.cellMatcher(2.71, STRICT, EPS))
            .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
            .row(null)
            .matches()
        )
    }

    test("imports double") {
      ParquetChecker("optional double column;", "DOUBLE")
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
      ParquetChecker("optional binary column;", "VARCHAR(20)")
        .withInputValues[Any](List("hello", "world", null))
        .assertResultSet(
          table()
            .row("hello")
            .row("world")
            .row(null)
            .matches()
        )
    }

    test("imports binary (utf8)") {
      ParquetChecker("required binary column (UTF8);", "VARCHAR(20)")
        .withInputValues[String](List("ÄäÖöÜüß / ☺", "world", ""))
        .assertResultSet(
          table()
            .row("ÄäÖöÜüß / ☺")
            .row("world")
            .row(null)
            .matches()
        )
    }

    test("imports binary (decimal)") {
      val decimal1 = Binary
        .fromConstantByteArray(new BigDecimal("12345").unscaledValue().toByteArray())
      val decimal2 = Binary
        .fromConstantByteArray(new BigDecimal("123456").unscaledValue().toByteArray())
      ParquetChecker("required binary column (DECIMAL(8,3));", "DECIMAL(18,3)")
        .withInputValues[Binary](List(decimal1, decimal2))
        .assertResultSet(
          table()
            .row(java.lang.Double.valueOf(12.345))
            .row(java.lang.Double.valueOf(123.456))
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports fixed_len_byte_array") {
      ParquetChecker("required fixed_len_byte_array(5) column;", "VARCHAR(5)")
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
      val decimal1 = Binary
        .fromConstantByteArray(new BigDecimal(decimalValueString).unscaledValue().toByteArray())
      val decimal2 = Binary.fromConstantByteArray(Array.fill[Byte](9)(0x0), 0, 9)
      ParquetChecker("required fixed_len_byte_array(9) column (DECIMAL(20,5));", "DECIMAL(20,5)")
        .withInputValues[Binary](List(decimal1, decimal2))
        .assertResultSet(
          table()
            .row(new BigDecimal(new BigInteger(decimalValueString), 5, new MathContext(20)))
            .row(java.lang.Double.valueOf(0.00))
            .matches(NO_JAVA_TYPE_CHECK)
        )
    }

    test("imports int96 (timestamp nanos)") {
      val millis = System.currentTimeMillis()
      val timestamp = new Timestamp(millis)
      val buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
      val micros = DateTimeUtil.getMicrosFromTimestamp(timestamp)
      val (days, nanos) = DateTimeUtil.getJulianDayAndNanos(micros)
      buffer.putLong(nanos).putInt(days)
      val zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin"))
      val expectedTimestamp = Timestamp.valueOf(zdt.toLocalDateTime())

      ParquetChecker("required int96 column;", "TIMESTAMP")
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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
      ParquetChecker(parquetType, "VARCHAR(35)")
        .withWriter {
          case (writer, schema) =>
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
      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
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

      ParquetChecker(parquetType, "VARCHAR(20)")
        .withWriter {
          case (writer, schema) =>
            val record = new SimpleGroup(schema)
            var person = record.addGroup(0)
            person.append("name", "John")
            person = record.addGroup(0)
            person.append("name", "Jane")
            writer.write(record)
        }
        .assertResultSet(table().row("""["John","Jane"]""").matches())
    }

    test("imports repeated group with multips fields") {
      val parquetType =
        """|repeated group person {
           |  required binary name (UTF8);
           |  optional int32 age;
           |}
      """.stripMargin

      ParquetChecker(parquetType, "VARCHAR(60)")
        .withWriter {
          case (writer, schema) =>
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

  }

  trait BaseDataImporter extends AnyFunSuite with BeforeAndAfterEach with TestFileManager {
    val dataFormat: String
    var outputDirectory: Path = _
    var path: HPath = _
    def tableName(): String = s"DATA_TABLE_${dataFormat.toUpperCase()}"

    def getTableName(): String = s""""$schemaName"."$tableName""""

    override final def beforeEach(): Unit = {
      outputDirectory = createTemporaryFolder(s"$dataFormat-tests-")
      path = new HPath(outputDirectory.toUri.toString, s"part-00000.$dataFormat")
      ()
    }

    override final def afterEach(): Unit = {
      deletePathFiles(outputDirectory)
      executeStmt(s"DROP TABLE IF EXISTS ${getTableName()}")
    }

    abstract class AbstractChecker(exaColumnType: String) {

      def withResultSet(block: ResultSet => Unit): this.type = {
        uploadParquetFile()
        importIntoExasol()
        val rs = query(s"SELECT * FROM ${getTableName()}")
        block(rs)
        rs.close()
        this
      }

      def assertResultSet(matcher: Matcher[ResultSet]): Unit = {
        withResultSet(assertThat(_, matcher))
        ()
      }

      private[this] def uploadParquetFile(): Unit = {
        s3.createBucket(new CreateBucketRequest(bucketName))
        val request = new PutObjectRequest(bucketName, path.getName(), new File(path.toUri()))
        s3.putObject(request)
        Thread.sleep(1 * 1000)
        ()
      }

      private[this] def importIntoExasol(): Unit = {
        val table = schema.createTableBuilder(tableName).column("COLUMN", exaColumnType).build()
        val s3Endpoint = s3Container
          .getEndpointConfiguration(S3)
          .getServiceEndpoint()
          .replaceAll("127.0.0.1", "172.17.0.1")
        executeStmt(
          s"""|IMPORT INTO ${table.getFullyQualifiedName()}
              |FROM SCRIPT $schemaName.IMPORT_PATH WITH
              |BUCKET_PATH              = 's3a://$bucketName/${path.getName()}'
              |DATA_FORMAT              = '$dataFormat'
              |S3_ENDPOINT              = '$s3Endpoint'
              |S3_CHANGE_DETECTION_MODE = 'none'
              |CONNECTION_NAME          = 'S3_CONNECTION'
              |PARALLELISM              = 'nproc()';
        """.stripMargin
        )
      }
    }

    case class OrcChecker(orcColumn: String, exaColumn: String)
        extends AbstractChecker(exaColumn) {

      def withWriter(block: OrcTestDataWriter => Unit): OrcChecker = {
        val writer = new OrcTestDataWriter(path, conf)
        block(writer)
        this
      }

      def withInputValues[T](values: List[T]): OrcChecker = withWriter { writer =>
        val schema = TypeDescription.fromString(orcColumn)
        writer.write(schema, values)
      }
    }

    case class AvroChecker(avroSchemaStr: String, exaColumn: String)
        extends AbstractChecker(exaColumn) {
      private val avroSchema = new Schema.Parser().parse(avroSchemaStr)

      def withWriter(block: DataFileWriter[GenericRecord] => Unit): AvroChecker = {
        val writer = new DataFileWriter[GenericRecord](new SpecificDatumWriter[GenericRecord]())
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

    case class ParquetChecker(parquetColumn: String, exaColumn: String)
        extends AbstractChecker(exaColumn)
        with ParquetTestDataWriter {
      private val parquetSchema =
        MessageTypeParser.parseMessageType(s"message test { $parquetColumn }")

      def withWriter(block: (ParquetWriter[Group], MessageType) => Unit): ParquetChecker = {
        val writer = getParquetWriter(path, parquetSchema, true)
        block(writer, parquetSchema)
        writer.close()
        this
      }

      def withInputValues[T](values: List[T]): ParquetChecker = withWriter { (writer, schema) =>
        values.foreach { value =>
          val record = new SimpleGroup(schema)
          if (!isNull(value)) {
            appendValue(value, record)
          }
          writer.write(record)
        }
      }

      private[this] def isNull(obj: Any): Boolean = !Option(obj).isDefined

      private def appendValue(value: Any, record: SimpleGroup): Unit = {
        value match {
          case v: Boolean => record.append("column", v)
          case v: Int     => record.append("column", v)
          case v: Long    => record.append("column", v)
          case v: Float   => record.append("column", v)
          case v: Double  => record.append("column", v)
          case v: String  => record.append("column", v)
          case v: Binary  => record.append("column", v)
        }
        ()
      }
    }
  }

}
