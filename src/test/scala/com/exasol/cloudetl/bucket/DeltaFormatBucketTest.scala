package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.FileManager
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

@SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
class DeltaFormatBucketTest extends AbstractBucketTest with FileManager {

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
      .master("local[2]")
      .getOrCreate()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(tmpDir)
    spark.stop()
  }

  test("getPaths throws if the path is not delta format") {
    val thrown = intercept[IllegalArgumentException] {
      getBucket(properties).getPaths()
    }
    val expectedMessage = s"The provided path: '$path' is not a Delta formatted directory!"
    assert(thrown.getMessage.contains(expectedMessage))
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
    val set = collectToSet[Int](bucket)
    assert(set === Set(1, 2, 3, 4, 5))
  }

  test("stream returns records from the overwrite delta snapshot") {
    saveSparkDataset(spark.range(1, 6), None)
    saveSparkDataset(spark.range(10, 13), Option("overwrite"))
    val bucket = getBucket(properties)
    val set = collectToSet[Int](bucket)
    assert(set === Set(10, 11, 12))
  }

  private[this] def saveSparkDataset[T](df: Dataset[T], saveMode: Option[String]): Unit =
    saveMode.fold {
      df.write.format("delta").save(path)
    } { mode =>
      df.write.format("delta").mode(mode).save(path)
    }

  private[this] def collectToSet[T](bucket: Bucket): Set[T] = {
    val set = scala.collection.mutable.Set[T]()
    val conf = bucket.getConfiguration()
    val fileSystem = bucket.fileSystem
    bucket.getPaths().map { path =>
      val source = Source(FileFormat("delta"), path, conf, fileSystem)
      source.stream().foreach { row =>
        set.add(row.getAs[T](0))
      }
      source.close()
    }
    set.toSet
  }

}
