package com.exasol.cloudetl

import java.nio.file.Files
import java.nio.file.Path

import com.exasol.matcher.ResultSetStructureMatcher.table

import org.apache.hadoop.fs.{Path => HPath}
import org.hamcrest.MatcherAssert.assertThat
import org.scalatest.BeforeAndAfterEach

class FilesMetadataReaderIT extends BaseS3IntegrationTest with BeforeAndAfterEach with TestFileManager {

  val schemaName = "DATA_SCHEMA"
  val tableName = "DATA_TABLE"
  val bucketName: String = "filesmetadata"
  var outputDirectory: Path = _

  override final def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(schemaName)
    createS3ConnectionObject()
  }

  override final def beforeEach(): Unit =
    outputDirectory = createTemporaryFolder(s"data-files-")

  override final def afterEach(): Unit =
    deletePathFiles(outputDirectory)

  def getTableName(): String = s""""$schemaName"."$tableName""""

  test("filters hidden and metadata files") {
    val hidden = Seq("_SUCCESS", ".METADATA").map(outputDirectory.resolve(_))
    hidden.foreach { case f => Files.createFile(f) }

    val file1 = new HPath(outputDirectory.toUri.toString, "_SUCCESS")
    uploadFileToS3(bucketName, file1)
    val file2 = new HPath(outputDirectory.toUri.toString, ".METADATA")
    uploadFileToS3(bucketName, file2)

    val exasolTable = schema
      .createTableBuilder(tableName)
      .column("COLUMN", "VARCHAR(5)")
      .build()
    importFromS3IntoExasol(schemaName, exasolTable, bucketName, "*", "parquet")

    val resultSet = executeQuery(s"SELECT * FROM ${getTableName()}")
    assertThat(resultSet, table("VARCHAR").matches())
    resultSet.close()
  }

}
