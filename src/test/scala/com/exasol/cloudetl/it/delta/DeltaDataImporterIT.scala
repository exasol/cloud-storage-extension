package com.exasol.cloudetl.it.delta

import java.lang.Long
import java.sql.ResultSet

import com.exasol.cloudetl.BaseS3IntegrationTest
import com.exasol.cloudetl.helper.StringGenerator
import com.exasol.matcher.ResultSetStructureMatcher.table

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

class DeltaDataImporterIT extends BaseS3IntegrationTest {

  @transient private[this] var spark: SparkSession = _
  @transient private[this] var sqlContext: SQLContext = _
  private[this] val schemaName = "DELTA_SCHEMA"
  private[this] val dataFormat = "delta"

  override def beforeAll(): Unit = {
    super.beforeAll()
    prepareExasolDatabase(schemaName)
    createS3ConnectionObject()
    spark = SparkSession
      .builder()
      .appName("DeltaFormatIT")
      .master("local[2]")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.hadoop.fs.s3a.endpoint", s3Endpoint)
      .config("spark.hadoop.fs.s3a.access.key", getAWSAccessKey())
      .config("spark.hadoop.fs.s3a.secret.key", getAWSSecretKey())
      .getOrCreate()
    sqlContext = spark.sqlContext
  }

  override def afterAll(): Unit = {
    spark.stop()
    super.afterAll()
  }

  test("imports delta format data") {
    val bucketName = "delta-table-long"
    val testTable = schema.createTable("DELTA_TABLE_LONG", "C1", "INTEGER")

    createBucket(bucketName)
    writeSparkDataset(spark.range(1, 4), bucketName)
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

  test("import truncates long string values") {
    val sqlContext = this.sqlContext
    import sqlContext.implicits._

    val bucketName = "delta-table-string"
    val testTable = schema.createTable("DELTA_TABLE_STRING", "C1", "VARCHAR(2000000)")
    createBucket(bucketName)

    val longString = StringGenerator.getRandomString(2000005)
    writeSparkDataset(spark.sparkContext.parallelize(Seq(longString)).toDS(), bucketName)
    importFromS3IntoExasol(schemaName, testTable, bucketName, "*", dataFormat)
    verifyImport(
      s"SELECT * FROM ${testTable.getFullyQualifiedName()}",
      table()
        .row(longString.substring(0, 2000000))
        .matches()
    )
  }

  private[this] def writeSparkDataset[T](ds: Dataset[T], bucketName: String): Unit =
    ds.write.format("delta").mode("overwrite").save(s"s3a://$bucketName/")

  private[this] def verifyImport(query: String, matcher: Matcher[ResultSet]): Unit = {
    val resultSet = executeQuery(query)
    assertThat(resultSet, matcher)
    resultSet.close()
  }

}
