package com.exasol.cloudetl.it.scriptclasses

import java.util.List
import java.util.stream.Stream
import com.exasol.cloudetl.BaseS3IntegrationTest
import com.exasol.dbbuilder.dialects.Table
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.scalatest.{BeforeAndAfterEach, Ignore}

@Ignore
class ExportParallelismIT extends BaseS3IntegrationTest with BeforeAndAfterEach {
  val SCHEMA_NAME: String = "EXPORT_PARALLELISM"
  val bucketName: String = "export-parallelism"
  var exportTable: Table = _

  override final def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(SCHEMA_NAME)
    createS3ConnectionObject()
    createBucket(bucketName)
    exportTable = schema
      .createTable("T1_PARALLELISM", "C1", "DECIMAL(18,0)")
      .bulkInsert(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(List.of(_)))
  }

  override final def afterEach(): Unit =
    deleteBucketObjects(bucketName)

  test("exports using user provided parallelism") {
    exportData("PARALLELISM = 'iproc()'")
    assertThat(listObjects(bucketName).size(), equalTo(1))
  }

  test("exports using user provided parallelism as iproc() combination") {
    exportData("PARALLELISM = 'iproc(), mod(rownum,3)'")
    assertThat(listObjects(bucketName).size(), equalTo(3))
  }

  test("exports using auto parallelism") {
    exportData("")
    assertThat(listObjects(bucketName).size(), equalTo(2))
  }

  test("exports using auto parallelism with udf memory parameter") {
    exportData("UDF_MEMORY = '250'")
    // export does not have to use all instances, therefore comparing with lessThanOrEqualTo
    assertThat(listObjects(bucketName).size(): Integer, lessThanOrEqualTo(Integer.valueOf(5)))
  }

  def exportData(parallelism: String): Unit =
    executeStmt(
      s"""|EXPORT ${exportTable.getFullyQualifiedName()}
          |INTO SCRIPT $SCHEMA_NAME.EXPORT_PATH WITH
          |BUCKET_PATH     = 's3a://$bucketName/'
          |DATA_FORMAT     = 'PARQUET'
          |S3_ENDPOINT     = '$s3Endpoint'
          |CONNECTION_NAME = 'S3_CONNECTION'
          |$parallelism
          |;
      """.stripMargin
    )

}
