package com.exasol.cloudetl.bucket

import scala.util.Using

import com.exasol.cloudetl.TestFileManager
import com.exasol.cloudetl.parquet.ParquetSourceTest

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll

class DeltaFormatBucketTest extends AbstractBucketTest with TestFileManager with BeforeAndAfterAll {

  private[this] var path: String = _
  private[this] var tmpDir: java.nio.file.Path = _
  private[this] var spark: SparkSession = _

  override final def beforeAll(): Unit =
    spark = SparkSession
      .builder()
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .appName("DeltaFormatTest")
      .master("local[2]")
      .getOrCreate()

  override final def beforeEach(): Unit = {
    super.beforeEach()
    tmpDir = createTemporaryFolder("deltaPath")
    path = tmpDir.toUri.toString
    properties = Map(PATH -> path, FORMAT -> "DELTA")
  }

  override final def afterEach(): Unit =
    deletePathFiles(tmpDir)

  override final def afterAll(): Unit =
    spark.close()

  test("getPaths throws if the path is not delta format") {
    val thrown = intercept[IllegalArgumentException] {
      getBucket(properties).getPaths()
    }
    assert(thrown.getMessage().startsWith("F-CSE-3"))
    assert(thrown.getMessage().contains(s"path '$path' is not a Delta formatted"))
  }

  test("getPaths returns delta log files with latest snapshot") {
    saveSparkDataset(spark.range(1, 101).repartition(20), None)

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 20)
  }

  test("getPaths returns delta log files with trailing star bucket path") {
    properties = Map(PATH -> s"${path.dropRight(1)}/*", FORMAT -> "DELTA")
    saveSparkDataset(spark.range(1, 5).coalesce(1), None)

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 1)
    assert(bucket.getPaths().exists(path => path.toUri.toString.contains("/*")) === false)
  }

  test("getPaths returns delta log files with overwrite snapshot") {
    saveSparkDataset(spark.range(1, 101).repartition(20), None)
    saveSparkDataset(spark.range(101, 110).coalesce(2), Option("overwrite"))

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 2)
  }

  test("stream returns records from the latest delta snapshot") {
    saveSparkDataset(spark.range(1, 6), None)
    val bucket = getBucket(properties)
    val set = collectToSet(bucket)
    assert(set === Set(1, 2, 3, 4, 5))
  }

  test("stream returns records from the overwrite delta snapshot") {
    saveSparkDataset(spark.range(1, 6), None)
    saveSparkDataset(spark.range(10, 13), Option("overwrite"))
    val bucket = getBucket(properties)
    val set = collectToSet(bucket)
    assert(set === Set(10, 11, 12))
  }

  private[this] def saveSparkDataset[T](df: Dataset[T], saveMode: Option[String]): Unit =
    saveMode.fold {
      df.write.format("delta").save(path)
    } { mode =>
      df.write.format("delta").mode(mode).save(path)
    }

  private[this] def collectToSet(bucket: Bucket): Set[Long] = {
    val set = scala.collection.mutable.Set[Long]()
    val conf = bucket.getConfiguration()
    bucket.getPaths().map { path =>
      Using(ParquetSourceTest(path, conf)) { source =>
        source.stream().foreach { row =>
          set.add(row.getAs[java.lang.Long](0))
        }
      }
    }
    set.toSet
  }

}
