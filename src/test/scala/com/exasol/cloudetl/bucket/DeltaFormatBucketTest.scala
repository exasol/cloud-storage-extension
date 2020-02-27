package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat

import org.apache.spark.sql.SparkSession

@SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
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

  test("getPaths returns delta log files with trailing star bucket path") {
    properties = Map(PATH -> s"${path.dropRight(1)}/*", FORMAT -> "DELTA")
    spark.range(1, 5).toDF("id").coalesce(1).write.format("delta").save(path)

    val bucket = getBucket(properties)
    assert(bucket.getPaths().size === 1)
    assert(bucket.getPaths().exists(path => path.toUri.toString.contains("/*")) === false)
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

  test("stream returns records from the latest delta snapshot") {
    spark.range(1, 6).toDF("id").write.format("delta").save(path)
    val bucket = getBucket(properties)
    val set = collectToSet[Int](bucket)
    assert(set.size === 5)
    assert(set === Set(1, 2, 3, 4, 5))
  }

  test("stream returns records from the overwite delta snapshot") {
    spark.range(1, 6).toDF("id").write.format("delta").save(path)
    spark.range(10, 13).toDF("id").write.format("delta").mode("overwrite").save(path)
    val bucket = getBucket(properties)
    val set = collectToSet[Int](bucket)
    assert(set.size === 3)
    assert(set === Set(10, 11, 12))
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
