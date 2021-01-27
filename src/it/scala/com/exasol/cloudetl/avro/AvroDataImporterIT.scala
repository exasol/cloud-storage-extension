package com.exasol.cloudetl.avro

import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Path
import java.math.BigDecimal
import java.sql.ResultSet
import java.sql.Timestamp

import com.exasol.cloudetl.BaseIntegrationTest
import com.exasol.cloudetl.TestFileManager
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.avro.file.DataFileWriter
import org.apache.avro.Schema
import org.apache.avro.Conversions
import org.apache.avro.LogicalTypes
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.EnumSymbol
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificDatumWriter
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.scalatest.BeforeAndAfterEach

class AvroDataImporterIT
    extends BaseIntegrationTest
    with BeforeAndAfterEach
    with TestFileManager {

  val schemaName = "AVRO_SCHEMA"
  val tableName = "AVRO_TABLE"
  val bucketName: String = "avrobucket"
  var outputDirectory: Path = _
  var conf: Configuration = _
  var path: HPath = _
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

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder("avro-tests-")
    path = new HPath(outputDirectory.toUri.toString, "avro-00000.avro")
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    execute(s"DROP TABLE IF EXISTS ${getTableName()}")
  }

  def getTableName(): String = s""""$schemaName"."$tableName""""

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
    AvroChecker(getBasicSchema("\"int\""), "DECIMAL(9,0)")
      .withInputValues(List(1, 13, 5))
      .assertResultSet(
        table()
          .row(java.lang.Integer.valueOf(1))
          .row(java.lang.Integer.valueOf(13))
          .row(java.lang.Integer.valueOf(5))
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
    AvroChecker(getBasicSchema("\"long\""), "DECIMAL(18,0)")
      .withInputValues(List(7L, 77L))
      .assertResultSet(
        table()
          .row(java.lang.Long.valueOf(7))
          .row(java.lang.Long.valueOf(77))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports long (timestamp-millis)") {
    val schema = getBasicSchema("""{"type":"long","logicalType":"timestamp-millis"}""")
    val millis = System.currentTimeMillis()
    AvroChecker(schema, "TIMESTAMP")
      .withInputValues(List(millis, 0L))
      .assertResultSet(
        table()
          .row(new Timestamp(millis))
          .row(new Timestamp(0L))
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
      .withInputValues(List(new EnumSymbol(enumSchema, "B")))
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

  case class AvroChecker(avroSchemaString: String, exasolColumnType: String) {

    private val avroSchema = new Schema.Parser().parse(avroSchemaString)

    def withAvroWriter(block: DataFileWriter[GenericRecord] => Unit): AvroChecker = {
      val writer = new DataFileWriter[GenericRecord](new SpecificDatumWriter[GenericRecord]())
      writer.create(avroSchema, new File(path.toUri))
      block(writer)
      writer.close()
      this
    }

    def withInputValues[T](values: List[T]): AvroChecker = withAvroWriter { writer =>
      values.foreach { value =>
        val record = new GenericData.Record(avroSchema)
        record.put("column", value)
        writer.append(record)
      }
      writer.close()
    }

    def withResultSet(block: ResultSet => Unit): AvroChecker = {
      uploadDataFile()
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

    private[this] def uploadDataFile(): Unit = {
      s3.createBucket(new CreateBucketRequest(bucketName))
      val request = new PutObjectRequest(bucketName, path.getName(), new File(path.toUri()))
      s3.putObject(request)
      ()
    }

    private[this] def importIntoExasol(): Unit = {
      val table = schema.createTableBuilder(tableName).column("COLUMN", exasolColumnType).build()
      Thread.sleep(3 * 1000)
      val s3Endpoint = s3Container
        .getEndpointConfiguration(S3)
        .getServiceEndpoint()
        .replaceAll("127.0.0.1", "172.17.0.1")
      execute(
        s"""|IMPORT INTO ${table.getFullyQualifiedName()}
            |FROM SCRIPT $schemaName.IMPORT_PATH WITH
            |BUCKET_PATH     = 's3a://$bucketName/${path.getName()}'
            |DATA_FORMAT     = 'AVRO'
            |S3_ENDPOINT     = '$s3Endpoint'
            |CONNECTION_NAME = 'S3_CONNECTION'
            |PARALLELISM     = 'nproc()';
        """.stripMargin
      )
    }

  }

}
