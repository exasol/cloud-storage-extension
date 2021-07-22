package com.exasol.cloudetl

import java.sql.Date
import java.sql.ResultSet
import java.sql.Timestamp

import scala.collection.mutable.LinkedHashMap

import com.exasol.dbbuilder.dialects.Table
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

// Exports to parquet and imports exported files into another table, and check two tables are similar.
class DataExporterIT extends BaseS3IntegrationTest {

  val SCHEMA_NAME = "DATA_SCHEMA"

  override final def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(SCHEMA_NAME)
    createS3ConnectionObject()
  }

  test("exports and imports boolean") {
    val columns = LinkedHashMap(
      "C_BOOLEAN" -> "BOOLEAN"
    )
    val values = Stream[Array[Any]](
      Array(1L, null),
      Array(2L, true),
      Array(3L, false)
    )
    ExportImportChecker(columns, values, "boolean-bucket").assert()
  }

  test("exports and imports varchar") {
    val columns = LinkedHashMap(
      "NAME" -> "VARCHAR(40)"
    )
    val values = Stream[Array[Any]](
      Array(1L, "Cat"),
      Array(2L, "Dog")
    )
    ExportImportChecker(columns, values, "varchar-bucket").assert()
  }

  test("exports and imports character") {
    val columns = LinkedHashMap(
      "C_CHAR20" -> "CHAR(20)",
      "C_VARCHAR20" -> "VARCHAR(20)"
    )
    val values = Stream[Array[Any]](
      Array(1L, null, null),
      Array(2L, "foo                 ", "bar"),
      Array(3L, "0123456789abcdefghij", "ABCEDFGHIJ0123456789")
    )
    ExportImportChecker(columns, values, "character-bucket").assert()
  }

  test("exports and imports numeric") {
    val columns = LinkedHashMap(
      "C_DECIMAL" -> "DECIMAL(3,2)",
      "C_DOUBLE" -> "DOUBLE PRECISION"
    )
    val values = Stream[Array[Any]](
      Array(1L, null, null),
      Array(2L, 1.23, 3.14159265358979323846264338327950288),
      Array(3L, 0.0, 0.0),
      Array(4L, -9.99, -5.555555555555),
      Array(5L, -1.11, -111.11111111111),
      Array(6L, 9.99, 9.9999999999999),
      Array(7L, -9.99, -9.9999999999999)
    )
    ExportImportChecker(columns, values, "numeric-bucket").assert(TypeMatchMode.NO_JAVA_TYPE_CHECK)
  }

  test("exports and imports numeric alias") {
    val columns = LinkedHashMap(
      "C_INTEGER" -> "INTEGER",
      "C_DOUBLE" -> "DOUBLE",
      "C_FLOAT" -> "FLOAT",
      "C_SHORTINT" -> "SHORTINT",
      "C_SMALLINT" -> "SMALLINT",
      "C_TINYINT" -> "TINYINT"
    )
    val values = Stream[Array[Any]](
      Array(1L, null, null, null, null, null, null),
      Array(2L, 100, 3.1415, 1.0f, 7, 12, 5)
    )
    ExportImportChecker(columns, values, "numeric-alias-bucket").assert(TypeMatchMode.NO_JAVA_TYPE_CHECK)
  }

  test("exports and imports date timestamp") {
    val columns = LinkedHashMap(
      "C_DATE" -> "DATE",
      "C_TIMESTAMP" -> "TIMESTAMP"
    )
    val values = Stream[Array[Any]](
      Array(1L, Date.valueOf("0001-01-01"), Timestamp.valueOf("0001-01-01 01:01:01.0")),
      Array(2L, Date.valueOf("1970-01-01"), Timestamp.valueOf("2001-01-01 01:01:01")),
      Array(3L, Date.valueOf("9999-12-31"), Timestamp.valueOf("2001-01-01 01:01:01.0"))
    )
    ExportImportChecker(columns, values, "date-timestamp-bucket").assert()
  }

  test("exports and imports identifier cases") {
    val columns = LinkedHashMap(
      "C_VARCHAR_mixedcase" -> "VARCHAR(20)",
      "C_VARCHAR_MixedCase" -> "VARCHAR(20)",
      "C_VARCHAR_REGULAR" -> "VARCHAR(50)"
    )
    val values = Stream[Array[Any]](
      Array(1L, "Quoted, lower case", "Quoted, mixed case", "Not quoted, automatically turned to upper case"),
      Array(2L, "Cats", "Dogs", "Ducks")
    )
    ExportImportChecker(columns, values, "delimited-bucket").assert()
  }

  case class ExportImportChecker(
    columns: LinkedHashMap[String, String],
    tableValues: Stream[Array[Any]],
    bucket: String
  ) {
    val values = tableValues.map(_.map(_.asInstanceOf[AnyRef]))
    val exportTable = {
      var table = createTable(getTableName("EXPORT"))
      values.foreach { case rows => table = table.insert(rows: _*) }
      table
    }
    val importTable = createTable(getTableName("IMPORT"))

    def createTable(name: String): Table = {
      val builder = schema.createTableBuilder(name).column("ID", "DECIMAL(18,0)")
      columns.foreach { case (columnName, columnType) => builder.column(columnName, columnType) }
      builder.build()
    }

    def getTableName(suffix: String): String =
      bucket.replace("bucket", suffix).replace("-", "_").toUpperCase

    def getMatcher(typeMatchMode: TypeMatchMode): Matcher[ResultSet] = {
      var matcher = table()
      values.foreach { case rows => matcher = matcher.row(rows: _*) }
      matcher.matches(typeMatchMode)
    }

    def withResultSet(block: ResultSet => Unit): Unit = {
      val resultSet = executeQuery(s"SELECT * FROM ${importTable.getFullyQualifiedName()} ORDER BY ID ASC")
      block(resultSet)
      resultSet.close()
    }

    def exportIntoS3(): Unit =
      executeStmt(
        s"""|EXPORT ${exportTable.getFullyQualifiedName()}
            |INTO SCRIPT $SCHEMA_NAME.EXPORT_PATH WITH
            |BUCKET_PATH     = 's3a://$bucket/'
            |DATA_FORMAT     = 'PARQUET'
            |S3_ENDPOINT     = '$s3Endpoint'
            |CONNECTION_NAME = 'S3_CONNECTION'
            |PARALLELISM     = 'iproc()';
      """.stripMargin
      )

    def assert(typeMatchMode: TypeMatchMode = TypeMatchMode.STRICT): Unit = {
      createBucket(bucket)
      exportIntoS3()
      importFromS3IntoExasol(SCHEMA_NAME, importTable, bucket, "*", "PARQUET")
      withResultSet(assertThat(_, getMatcher(typeMatchMode)))
    }
  }

}
