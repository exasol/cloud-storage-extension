package com.exasol.cloudetl

import java.nio.file.Path
import java.sql.ResultSet

import org.apache.hadoop.fs.{Path => HPath}
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.scalatest.BeforeAndAfterEach

trait BaseDataImporter extends BaseS3IntegrationTest with BeforeAndAfterEach with TestFileManager {
  val INT_MIN = -2147483648
  val INT_MAX = 2147483647
  val LONG_MIN = -9223372036854775808L
  val LONG_MAX = 9223372036854775807L

  val schemaName: String
  val bucketName: String
  val dataFormat: String

  var outputDirectory: Path = _
  var path: HPath = _

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder(s"$dataFormat-tests-")
    path = new HPath(outputDirectory.toUri.toString, s"part-00000.$dataFormat")
    ()
  }

  override final def afterEach(): Unit =
    deletePathFiles(outputDirectory)

  override final def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(schemaName)
    createS3ConnectionObject()
  }

  override final def afterAll(): Unit = {
    executeStmt(s"DROP SCHEMA IF EXISTS $schemaName CASCADE;")
    super.afterAll()
  }

  abstract class AbstractChecker(exaColumnType: String, tableName: String) {
    def withResultSet(block: ResultSet => Unit): this.type = {
      uploadFileToS3(bucketName, path)
      val table = schema
        .createTableBuilder(tableName.toUpperCase(java.util.Locale.ENGLISH))
        .column("COLUMN", exaColumnType)
        .build()
      importFromS3IntoExasol(schemaName, table, bucketName, path.getName(), dataFormat)
      val rs = executeQuery(s"SELECT * FROM ${table.getFullyQualifiedName()}")
      block(rs)
      rs.close()
      this
    }

    def assertResultSet(matcher: Matcher[ResultSet]): Unit = {
      withResultSet(assertThat(_, matcher))
      ()
    }
  }

}
