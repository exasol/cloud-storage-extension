package com.exasol.cloudetl.export

import java.sql.Date
import java.sql.ResultSet
import java.sql.Timestamp

import scala.collection.mutable.LinkedHashMap

import com.exasol.cloudetl.BaseS3IntegrationTest
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
    val tableValues = Stream[Array[Any]](
      Array(1L, null),
      Array(2L, true),
      Array(3L, false)
    )
    ExportImportChecker(columns, tableValues, "boolean-bucket").assert()
  }

  test("exports and imports varchar") {
    val columns = LinkedHashMap(
      "NAME" -> "VARCHAR(40)"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, "Cat"),
      Array(2L, "Dog")
    )
    ExportImportChecker(columns, tableValues, "varchar-bucket").assert()
  }

  test("exports and imports character") {
    val columns = LinkedHashMap(
      "C_CHAR20" -> "CHAR(20)"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, null),
      Array(2L, "foo                 "),
      Array(3L, "0123456789abcdefghij")
    )
    ExportImportChecker(columns, tableValues, "character-bucket").assert()
  }

  test("exports and imports numeric") {
    val columns = LinkedHashMap(
      "C_DECIMAL" -> "DECIMAL(3,2)",
      "C_DOUBLE" -> "DOUBLE PRECISION"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, null, null),
      Array(2L, 1.23, 3.14159265358979323846264338327950288),
      Array(3L, 0.0, 0.0),
      Array(4L, -9.99, -5.555555555555),
      Array(5L, -1.11, -111.11111111111),
      Array(6L, 9.99, 9.9999999999999),
      Array(7L, -9.99, -9.9999999999999)
    )
    ExportImportChecker(columns, tableValues, "numeric-bucket").assert(TypeMatchMode.NO_JAVA_TYPE_CHECK)
  }

  test("exports and imports numeric minimum and maximum") {
    val columns = LinkedHashMap(
      "C_INT" -> "INTEGER",
      "C_LONG" -> "BIGINT"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, null, null),
      Array(2L, INT_MIN, LONG_MIN),
      Array(3L, INT_MAX, LONG_MAX)
    )
    ExportImportChecker(columns, tableValues, "numeric-min-max-bucket").assert(TypeMatchMode.NO_JAVA_TYPE_CHECK)
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
    val tableValues = Stream[Array[Any]](
      Array(1L, null, null, null, null, null, null),
      Array(2L, 100, 3.1415, 1.0f, 7, 12, 5)
    )
    ExportImportChecker(columns, tableValues, "numeric-alias-bucket").assert(TypeMatchMode.NO_JAVA_TYPE_CHECK)
  }

  test("exports and imports date timestamp") {
    val columns = LinkedHashMap(
      "C_DATE" -> "DATE",
      "C_TIMESTAMP" -> "TIMESTAMP"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, Date.valueOf("0001-01-01"), Timestamp.valueOf("0001-01-01 01:01:01.0")),
      Array(2L, Date.valueOf("1970-01-01"), Timestamp.valueOf("2001-01-01 01:01:01")),
      Array(3L, Date.valueOf("9999-12-31"), Timestamp.valueOf("9999-12-31 23:59:59"))
    )
    ExportImportChecker(columns, tableValues, "date-timestamp-bucket").assert()
  }

  test("exports and imports identifier cases") {
    val columns = LinkedHashMap(
      "C_VARCHAR_mixedcase" -> "VARCHAR(20)",
      "C_VARCHAR_Mixed_Case" -> "VARCHAR(20)",
      "C_VARCHAR_REGULAR" -> "VARCHAR(50)"
    )
    val tableValues = Stream[Array[Any]](
      Array(1L, "Quoted, lower case", "Quoted, mixed case", "Not quoted, automatically turned to upper case"),
      Array(2L, "Cats", "Dogs", "Ducks")
    )
    ExportImportChecker(columns, tableValues, "delimited-bucket").assert()
  }

  test("exports and imports decimals with trailing zeros") {
    val columns = LinkedHashMap("C_DECIMAL" -> "DECIMAL(18, 4)")
    val tableValues = Stream[Array[Any]](Array(1L, 238316.38))
    ExportImportChecker(columns, tableValues, "decimal-trailing-zeros-bucket")
      .assertWithMatcher(
        table()
          .row(java.lang.Long.valueOf(1), java.math.BigDecimal.valueOf(238316.3800))
          .matches()
      )
  }

  test("exports and imports from view") {
    val bucket = "decimal-cast-view"
    val importTable = schema
      .createTable("DECIMAL_IMPORT_TABLE", "ID", "DECIMAL(18,0)", "C_DECIMAL", "DECIMAL(18,2)")
    val decimalTable = schema
      .createTable("DECIMAL_EXPORT_TABLE", "ID", "DECIMAL(18,0)", "C_DECIMAL", "DECIMAL(18,2)")
      .insert("1", java.lang.Double.valueOf(238316.38))
    val viewName = s"$SCHEMA_NAME.DECIMAL_TABLE_V"
    executeStmt(
      s"""|CREATE OR REPLACE VIEW $viewName
          |AS SELECT
          |   ID AS ID,
          |   CAST(C_DECIMAL AS DECIMAL(18,4)) AS C_DECIMAL
          |FROM ${decimalTable.getFullyQualifiedName()}
      """.stripMargin
    )

    createBucket(bucket)
    exportIntoS3(SCHEMA_NAME, viewName, bucket)
    importFromS3IntoExasol(SCHEMA_NAME, importTable, bucket, "*", "PARQUET")

    val resultSet = executeQuery(s"SELECT * FROM ${importTable.getFullyQualifiedName()}")
    assertThat(
      resultSet,
      table()
        .row(java.lang.Long.valueOf(1), java.lang.Double.valueOf(238316.3800))
        .matches(TypeMatchMode.NO_JAVA_TYPE_CHECK)
    )
    resultSet.close()
  }

  case class ExportImportChecker(columns: LinkedHashMap[String, String], input: Stream[Array[Any]], bucket: String) {
    val tableValues = input.map(_.map(_.asInstanceOf[AnyRef]))
    val exportTable = {
      var table = createTable(getTableName("EXPORT"))
      tableValues.foreach { case rows => table = table.insert(rows: _*) }
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
      tableValues.foreach { case rows => matcher = matcher.row(rows: _*) }
      matcher.matches(typeMatchMode)
    }

    def withResultSet(block: ResultSet => Unit): Unit = {
      val resultSet = executeQuery(s"SELECT * FROM ${importTable.getFullyQualifiedName()} ORDER BY ID ASC")
      block(resultSet)
      resultSet.close()
    }

    def exportAndImport(): Unit = {
      createBucket(bucket)
      exportIntoS3(SCHEMA_NAME, exportTable.getFullyQualifiedName(), bucket)
      importFromS3IntoExasol(SCHEMA_NAME, importTable, bucket, "*", "PARQUET")
    }

    def assert(typeMatchMode: TypeMatchMode = TypeMatchMode.STRICT): Unit = {
      exportAndImport()
      withResultSet(assertThat(_, getMatcher(typeMatchMode)))
    }

    def assertWithMatcher(matcher: Matcher[ResultSet]): Unit = {
      exportAndImport()
      withResultSet(assertThat(_, matcher))
    }
  }

}
