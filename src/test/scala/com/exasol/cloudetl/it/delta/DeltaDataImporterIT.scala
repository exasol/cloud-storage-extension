package com.exasol.cloudetl.it.delta

import java.lang.Long
import java.sql.ResultSet

import com.exasol.cloudetl.BaseDataImporter
import com.exasol.matcher.ResultSetStructureMatcher.table

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

class DeltaDataImporterIT extends BaseDataImporter {

  private[this] var spark: SparkSession = _
  override val schemaName = "DELTA_SCHEMA"
  override val bucketName = "delta-bucket"
  override val dataFormat = "delta"

  override def beforeAll(): Unit = {
    super.beforeAll()
    spark = SparkSession
      .builder()
      .appName("DeltaFormatIT")
      .master("local[2]")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.hadoop.fs.s3a.endpoint", s3Endpoint)
      .config("spark.hadoop.fs.s3a.access.key", getAWSAccessKey())
      .config("spark.hadoop.fs.s3a.secret.key", getAWSSecretKey())
      .getOrCreate()
  }

  override def afterAll(): Unit = {
    spark.stop()
    super.afterAll()
  }

  test("imports delta format data") {
    val testTable = schema.createTable("DELTA_TABLE", "C1", "INTEGER")
    createBucket(bucketName)
    writeSparkDataset(spark.range(1, 4))
    importFromS3IntoExasol(schemaName, testTable, bucketName, "*", dataFormat)
    verifyImport(
      s"SELECT * FROM ${testTable.getFullyQualifiedName()} ORDER BY C1 ASC",
      table()
        .row(Long.valueOf(1))
        .row(Long.valueOf(2))
        .row(Long.valueOf(3))
        .matches()
    )
  }

  private[this] def writeSparkDataset(ds: Dataset[Long]): Unit =
    ds.write.format("delta").mode("overwrite").save(s"s3a://$bucketName/")

  private[this] def verifyImport(query: String, matcher: Matcher[ResultSet]): Unit = {
    val resultSet = executeQuery(query)
    assertThat(resultSet, matcher)
    resultSet.close()
  }

}
