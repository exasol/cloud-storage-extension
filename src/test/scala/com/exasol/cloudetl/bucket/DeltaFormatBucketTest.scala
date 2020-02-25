package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.DummyRecordsTest

import org.apache.spark.sql.SparkSession

class DeltaFormatBucketTest extends AbstractBucketTest with DummyRecordsTest {

  private[this] var path: String = _
  private[this] var tmpDir: java.nio.file.Path = _
  private[this] var spark: SparkSession = _

  override final def beforeEach(): Unit = {
    super.beforeEach()
    tmpDir = createTemporaryFolder("deltaPath")
    path = tmpDir.toUri.toString
    properties = Map(PATH -> path, FORMAT -> "DELTA")
    spark = SparkSession
      .builder()
      .appName("DeltaFormatTest")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
  }

  override final def afterEach(): Unit = {
    deleteFiles(tmpDir)
    spark.stop()
  }

  test("getPaths throws if the path is not delta format") {
    val thrown = intercept[IllegalArgumentException] {
      getBucket(properties).getPaths()
    }
    assert(thrown.getMessage.contains(s"The provided path: '$path' is not a Delta format!"))
  }

  test("getPaths returns delta log files with latest snapshot") {
    spark.range(1, 101).toDF("id").repartition(20).write.format("delta").save(path)

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 20)
  }

  test("getPaths returns delta log files with overwrite snapshot") {
    spark
      .range(1, 101)
      .toDF("id")
      .repartition(20) // creates 20 files
      .write
      .format("delta")
      .save(path)

    spark
      .range(101, 110)
      .toDF("id")
      .coalesce(2)
      .write
      .format("delta")
      .mode("overwrite")
      .save(path)

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 2)
  }

}
