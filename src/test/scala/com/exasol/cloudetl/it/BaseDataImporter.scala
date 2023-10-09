package com.exasol.cloudetl

import java.nio.file.Path
import java.sql.ResultSet

import org.apache.hadoop.fs.{Path => HPath}
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.scalatest.BeforeAndAfterEach

trait BaseDataImporter extends BaseS3IntegrationTest with BeforeAndAfterEach with TestFileManager {
  val schemaName: String
  val bucketName: String
  val dataFormat: String

  var outputDirectory: Path = _
  var paths: List[HPath] = List()
  val baseFileName = "part-"

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder(s"$dataFormat-tests-")
    ()
  }

  override final def afterEach(): Unit = {
    paths = List()
    deletePathFiles(outputDirectory)
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    createBucket(bucketName)
    prepareExasolDatabase(schemaName)
    createS3ConnectionObject()
  }

  override def afterAll(): Unit = {
    executeStmt(s"DROP SCHEMA IF EXISTS $schemaName CASCADE;")
    super.afterAll()
  }

  def addFile(): HPath = {
    val fileCounter = String.format("%04d", paths.length)
    val newPath = new HPath(outputDirectory.toUri.toString, s"$baseFileName$fileCounter.$dataFormat")
    paths = paths.appended(newPath)
    return newPath
  }

  abstract class AbstractChecker(exaColumnType: String, tableName: String)
      extends AbstractMultiColChecker(Map("COLUMN" -> exaColumnType), tableName)

  abstract class AbstractMultiColChecker(columns: Map[String, String], tableName: String) {
    def withResultSet(block: ResultSet => Unit): this.type = {
      paths.foreach(path => uploadFileToS3(bucketName, path))
      val tableBuilder = schema
        .createTableBuilder(tableName.toUpperCase(java.util.Locale.ENGLISH))
      columns.foreach { case (colName, colType) =>
        tableBuilder.column(colName, colType)
      }

      val table = tableBuilder.build()
      importFromS3IntoExasol(schemaName, table, bucketName, s"$baseFileName*", dataFormat)
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
