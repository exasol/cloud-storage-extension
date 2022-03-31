package com.exasol.cloudetl.timestamp

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Path
import java.sql.ResultSet
import java.sql.Timestamp
import java.time._
import java.time.format.DateTimeFormatter

import com.exasol.cloudetl.BaseS3IntegrationTest
import com.exasol.cloudetl.TestFileManager
import com.exasol.cloudetl.helper.DateTimeConverter._
import com.exasol.cloudetl.parquet.ParquetTestDataWriter
import com.exasol.dbbuilder.dialects.Table
import com.exasol.matcher.ResultSetStructureMatcher.table

import org.apache.hadoop.fs.{Path => HPath}
import org.apache.parquet.schema.MessageTypeParser
import org.apache.parquet.io.api.Binary
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.scalatest.BeforeAndAfterEach

class TimestampWithUTCImportExportIT extends BaseS3IntegrationTest with BeforeAndAfterEach with TestFileManager {
  val SCHEMA_NAME = "TIMESTAMP_SCHEMA"

  var outputDirectory: Path = _
  var path: HPath = _

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder("timestamp-tests-")
    path = new HPath(outputDirectory.toUri().toString(), "part-00000")
    ()
  }

  override final def afterEach(): Unit =
    deletePathFiles(outputDirectory)

  override final def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(SCHEMA_NAME)
    createS3ConnectionObject()
  }

  def importFromS3Bucket(table: Table, bucket: String, dataFormat: String): Unit =
    executeStmt(
      s"""|IMPORT INTO ${table.getFullyQualifiedName()}
          |FROM SCRIPT $SCHEMA_NAME.IMPORT_PATH WITH
          |BUCKET_PATH              = 's3a://$bucket/*'
          |DATA_FORMAT              = '$dataFormat'
          |S3_ENDPOINT              = '$s3Endpoint'
          |S3_CHANGE_DETECTION_MODE = 'none'
          |CONNECTION_NAME          = 'S3_CONNECTION'
          |TIMEZONE_UTC             = 'true'
          |PARALLELISM              = 'nproc()';
        """.stripMargin
    )

  def exportIntoS3Bucket(table: Table, bucket: String): Unit =
    executeStmt(
      s"""|EXPORT ${table.getFullyQualifiedName()}
          |INTO SCRIPT $SCHEMA_NAME.EXPORT_PATH WITH
          |BUCKET_PATH     = 's3a://$bucket/'
          |DATA_FORMAT     = 'PARQUET'
          |S3_ENDPOINT     = '$s3Endpoint'
          |CONNECTION_NAME = 'S3_CONNECTION'
          |TIMEZONE_UTC    = 'true'
          |PARALLELISM     = 'iproc()';
      """.stripMargin
    )

  def verifyTable(query: String, matcher: Matcher[ResultSet]): Unit = {
    val rs = executeQuery(query)
    assertThat(rs, matcher)
    rs.close()
    ()
  }

  test("parquet imports int64 (timestamp millis)") {
    val millis1 = Instant.EPOCH.toEpochMilli()
    val millis2 = System.currentTimeMillis()

    ParquetTimestampWriter("optional int64 column (TIMESTAMP_MILLIS);")
      .withBucketName("int64-timestamp-millis")
      .withTableColumnType("int64_timestamp_millis", "TIMESTAMP")
      .withFilePath(path)
      .withInputValues[Any](List(millis1, millis2, null))
      .verify(
        table()
          .row(new Timestamp(millis1))
          .row(new Timestamp(millis2))
          .row(null)
          .withUtcCalendar()
          .matches()
      )
  }

  test("parquet imports int64 (timestamp micros)") {
    val timestamp = Timestamp.valueOf("2022-01-12 10:28:53.123456")
    val millis = timestamp.getTime()
    val micros = millis * 1000L + (timestamp.getNanos().toLong / 1000) % 1000L

    ParquetTimestampWriter("optional int64 column (TIMESTAMP_MICROS);")
      .withBucketName("int64-timestamp-micros")
      .withTableColumnType("int64_timestamp_micros", "TIMESTAMP")
      .withFilePath(path)
      .withInputValues[Any](List(micros, null))
      .verify(
        table()
          .row(new Timestamp(millis))
          .row(null)
          .withUtcCalendar()
          .matches()
      )
  }

  test("parquet imports int96 (timestamp nanos)") {
    val millis = System.currentTimeMillis()
    val timestamp = new Timestamp(millis)
    val buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
    val micros = getMicrosFromTimestamp(timestamp)
    val (days, nanos) = getJulianDayAndNanos(micros)
    buffer.putLong(nanos).putInt(days)

    ParquetTimestampWriter("optional int96 column;")
      .withBucketName("int96-timestamp")
      .withTableColumnType("int96_timestamp", "TIMESTAMP")
      .withFilePath(path)
      .withInputValues[Any](List(Binary.fromConstantByteArray(buffer.array()), null))
      .verify(
        table()
          .row(timestamp)
          .row(null)
          .withUtcCalendar()
          .matches()
      )
  }

  test("parquet export timestamp") {
    val millis = System.currentTimeMillis()
    val timestampString = DateTimeFormatter //
      .ofPattern("yyyy-MM-dd HH:mm:ss.SSS") //
      .withZone(ZoneId.of("UTC")) //
      .format(Instant.ofEpochMilli(millis))
    val bucket = "export-timestamp-utc"

    val importTable = schema
      .createTable("TIMESTAMP_IMPORT_TABLE", "ID", "DECIMAL(18,0)", "COLUMN", "TIMESTAMP")
    val exportTable = schema
      .createTable("TIMESTAMP_EXPORT_TABLE", "ID", "DECIMAL(18,0)", "COLUMN", "TIMESTAMP")
      .insert(1L, timestampString)
      .insert(2L, null)

    createBucket(bucket)
    exportIntoS3Bucket(exportTable, bucket)
    importFromS3Bucket(importTable, bucket, "PARQUET")
    verifyTable(
      s"SELECT * FROM ${importTable.getFullyQualifiedName()} ORDER BY ID ASC",
      table()
        .row(1L, new Timestamp(millis))
        .row(2L, null)
        .withUtcCalendar()
        .matches()
    )
  }

  case class ParquetTimestampWriter(parquetType: String) extends ParquetTestDataWriter {
    private val parquetSchema = MessageTypeParser.parseMessageType(s"message test { $parquetType }")
    private var bucketName: String = _
    private var path: HPath = _
    private var table: Table = _
    def withBucketName(bucketName: String): this.type = {
      this.bucketName = bucketName
      this
    }
    def withFilePath(path: HPath): this.type = {
      this.path = path
      this
    }
    def withTableColumnType(tableName: String, columnType: String): this.type = {
      this.table = schema
        .createTableBuilder(tableName.toUpperCase(java.util.Locale.ENGLISH))
        .column("COLUMN", columnType)
        .build()
      this
    }
    def withInputValues[T](values: List[T]): this.type = {
      writeDataValues(values, this.path, this.parquetSchema)
      this
    }
    def verify(matcher: Matcher[ResultSet]): Unit = {
      uploadFileToS3(this.bucketName, this.path)
      importFromS3Bucket(this.table, this.bucketName, "PARQUET")
      verifyTable(s"SELECT * FROM ${this.table.getFullyQualifiedName()}", matcher)
      ()
    }
  }

}
