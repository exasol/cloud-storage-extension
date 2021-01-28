package com.exasol.cloudetl.parquet

import java.io.File
import java.math._
import java.nio.file.Path
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.sql.ResultSet
import java.sql.Timestamp
import java.time._

import com.exasol.cloudetl.BaseIntegrationTest
import com.exasol.cloudetl.TestFileManager
import com.exasol.cloudetl.util.DateTimeUtil
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema._
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.scalatest.BeforeAndAfterEach

class ParquetDataImporterIT
    extends BaseIntegrationTest
    with BeforeAndAfterEach
    with TestFileManager {

  val schemaName = "PARQUET_SCHEMA"
  val tableName = "PARQUET_TABLE"
  val bucketName: String = "parquetbucket"
  var outputDirectory: Path = _
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
  }

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder("parquet-tests-")
    path = new HPath(outputDirectory.toUri.toString, "part-00000.parquet")
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    execute(s"DROP TABLE IF EXISTS ${getTableName()}")
  }

  def getTableName(): String = s""""$schemaName"."$tableName""""

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
    ParquetChecker("optional int32 column;", "DECIMAL(9,0)")
      .withInputValues[Any](List(1, 666, null))
      .assertResultSet(
        table()
          .row(java.lang.Integer.valueOf(1))
          .row(java.lang.Integer.valueOf(666))
          .row(null)
          .matches()
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
    ParquetChecker("optional int64 column;", "DECIMAL(18,0)")
      .withInputValues[Any](List(999L, null))
      .assertResultSet(
        table()
          .row(java.lang.Long.valueOf(999))
          .row(null)
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
    val zdt1 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.of("Europe/Berlin"))
    val zdt2 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.of("Europe/Berlin"))
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
      .withParquetWriter {
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
      .withParquetWriter {
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
      .withParquetWriter {
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
      .withParquetWriter {
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
      .withParquetWriter {
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
      .withParquetWriter {
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
      .withParquetWriter {
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

  case class ParquetChecker(parquetColumnType: String, exasolColumnType: String) {

    def withParquetWriter(block: (ParquetWriter[Group], MessageType) => Unit): ParquetChecker = {
      val schema = getSchema()
      val writer = getParquetWriter(schema, true)
      block(writer, schema)
      writer.close()
      this
    }

    def withInputValues[T](values: List[T]): ParquetChecker = withParquetWriter {
      (writer, schema) =>
        values.foreach { value =>
          val record = new SimpleGroup(schema)
          if (!isNull(value)) {
            appendValue(value, record)
          }
          writer.write(record)
        }
    }

    def withResultSet(block: ResultSet => Unit): ParquetChecker = {
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

    private[this] def isNull(obj: Any): Boolean = !Option(obj).isDefined

    private[this] def uploadParquetFile(): Unit = {
      s3.createBucket(new CreateBucketRequest(bucketName))
      val request = new PutObjectRequest(bucketName, path.getName(), new File(path.toUri()))
      s3.putObject(request)
      ()
    }

    private[this] def importIntoExasol(): Unit = {
      val table = schema.createTableBuilder(tableName).column("COLUMN", exasolColumnType).build()
      Thread.sleep(1 * 1000)
      val s3Endpoint = s3Container
        .getEndpointConfiguration(S3)
        .getServiceEndpoint()
        .replaceAll("127.0.0.1", "172.17.0.1")
      execute(
        s"""|IMPORT INTO ${table.getFullyQualifiedName()}
            |FROM SCRIPT $schemaName.IMPORT_PATH WITH
            |BUCKET_PATH     = 's3a://$bucketName/${path.getName()}'
            |DATA_FORMAT     = 'PARQUET'
            |S3_ENDPOINT     = '$s3Endpoint'
            |CONNECTION_NAME = 'S3_CONNECTION'
            |PARALLELISM     = 'nproc()';
        """.stripMargin
      )
    }

    private[this] def appendValue(value: Any, record: SimpleGroup): Unit = {
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

    private[this] def getSchema(): MessageType =
      MessageTypeParser.parseMessageType(s"message test { $parquetColumnType }")

  }

  protected final def getParquetWriter(
    schema: MessageType,
    dictionaryEncoding: Boolean
  ): ParquetWriter[Group] =
    BaseGroupWriterBuilder(path, schema)
      .withDictionaryEncoding(dictionaryEncoding)
      .build()

  private[this] case class BaseGroupWriteSupport(schema: MessageType)
      extends WriteSupport[Group] {
    var writer: GroupWriter = null

    override def prepareForWrite(recordConsumer: RecordConsumer): Unit =
      writer = new GroupWriter(recordConsumer, schema)

    override def init(configuration: Configuration): WriteSupport.WriteContext =
      new WriteSupport.WriteContext(schema, new java.util.HashMap[String, String]())

    override def write(record: Group): Unit =
      writer.write(record)
  }

  private[this] case class BaseGroupWriterBuilder(path: HPath, schema: MessageType)
      extends ParquetWriter.Builder[Group, BaseGroupWriterBuilder](path) {
    override def getWriteSupport(conf: Configuration): WriteSupport[Group] =
      BaseGroupWriteSupport(schema)
    override def self(): BaseGroupWriterBuilder = this
  }

}
