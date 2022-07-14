package com.exasol.cloudetl.scriptclasses

import java.nio.file.Files
import java.nio.file.Path

import com.exasol.cloudetl.BaseS3IntegrationTest
import com.exasol.cloudetl.TestFileManager
import com.exasol.matcher.ResultSetStructureMatcher.table

import org.apache.hadoop.fs.{Path => HPath}
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.scalatest.BeforeAndAfterEach
import java.sql.ResultSet

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

  override final def afterAll(): Unit =
    deleteBucket(bucketName)

  override final def beforeEach(): Unit =
    outputDirectory = createTemporaryFolder(s"data-files-")

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    deleteBucketObjects(bucketName)
  }

  def getTableName(): String = s""""$schemaName"."$tableName""""

  def getPropertiesString(): String =
    "BUCKET_PATH -> s3a://filesmetadata/*;DATA_FORMAT -> ORC;" +
      s"S3_ENDPOINT -> $s3Endpoint;CONNECTION_NAME -> S3_CONNECTION"

  test("partitions files using user provided parallelism value") {
    createAndUploadFiles(3)
    verify(
      runMetadataScript("2", getPropertiesString()),
      table()
        .row("s3a://filesmetadata/part-001", "0")
        .row("s3a://filesmetadata/part-002", "1")
        .row("s3a://filesmetadata/part-003", "0")
        .matches()
    )
  }

  test("partitions files using user provided parallelism value as nproc() combination") {
    createAndUploadFiles(3)
    verify(
      runMetadataScript("nproc()*1", getPropertiesString()),
      table()
        .row("s3a://filesmetadata/part-001", "0")
        .row("s3a://filesmetadata/part-002", "0")
        .row("s3a://filesmetadata/part-003", "0")
        .matches()
    )
  }

  test("partitions files using automatically") {
    createAndUploadFiles(3)
    // Memory limit: (1342177280 MB memory per node / 500000000 MB UDF memory = 2).
    // Calculated parallelism: 1 node * min(6 cores per node, 2 cores limited by memory) = 2.
    verify(
      runMetadataScript("65536", getPropertiesString()), // use large default
      table()
        .row("s3a://filesmetadata/part-001", "0")
        .row("s3a://filesmetadata/part-002", "1")
        .row("s3a://filesmetadata/part-003", "0")
        .matches()
    )
  }

  test("partitions files using udf memory parameter value") {
    createAndUploadFiles(6)
    val properties = getPropertiesString() + ";UDF_MEMORY -> 250"
    // Memory limit: (1342177280 MB memory per node / 250000000 MB UDF memory = 5).
    // Calculated parallelism: 1 node * min(6 cores per node, 5 cores limited by memory) = 5
    verify(
      runMetadataScript("65536", properties),
      table()
        .row("s3a://filesmetadata/part-001", "0")
        .row("s3a://filesmetadata/part-002", "1")
        .row("s3a://filesmetadata/part-003", "2")
        .row("s3a://filesmetadata/part-004", "3")
        .row("s3a://filesmetadata/part-005", "4")
        .row("s3a://filesmetadata/part-006", "0")
        .matches()
    )
  }

  test("filters hidden and metadata files") {
    uploadFiles(Seq("_SUCCESS", ".METADATA"))
    importFromS3IntoExasol(schemaName, schema.createTable(tableName, "C1", "VARCHAR(5)"), bucketName, "*", "parquet")
    verify(executeQuery(s"SELECT * FROM ${getTableName()}"), table("VARCHAR").matches())
  }

  private[this] def verify(resultSet: ResultSet, matcher: Matcher[ResultSet]): Unit = {
    assertThat(resultSet, matcher)
    resultSet.close()
  }

  private[this] def runMetadataScript(parallelism: String, properties: String): ResultSet =
    executeQuery(
      s"SELECT filename, partition_index FROM (SELECT IMPORT_METADATA('$bucketName', '$properties', $parallelism))"
    )

  private[this] def createAndUploadFiles(numOfFiles: Int): Unit = {
    val files = Seq.range(1, numOfFiles + 1).map(fileId => String.format("part-%03d", fileId))
    uploadFiles(files)
  }

  private[this] def uploadFiles(files: Seq[String]): Unit =
    files.foreach { case filename =>
      Files.createFile(outputDirectory.resolve(filename))
      uploadFileToS3(bucketName, new HPath(outputDirectory.toUri().toString(), filename))
    }

}
