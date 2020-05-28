package com.exasol.cloudetl.benchmarks

import java.util.concurrent.TimeUnit

import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.fs.Path
import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder

/**
 * Steps to run:
 * {{{
 *   $ ./sbtx
 *   > project benchmarks
 *   > set javaOptions += "-Dparquet-file-path=path/to/parquet/file"
 *   > jmh:runMain com.exasol.cloudetl.benchmarks.ParquetReadBenchmark
 * }}}
 */
@State(Scope.Benchmark)
class ParquetReadBenchmark {
  private[this] val parquetFilePath = System.getProperty("parquet-file-path")

  // Add AWS credentials to authenticate with S3 bucket. If you do not
  // use the session token, simply remove the key-value line from the
  // map.
  private[this] val awsCredentials = Map(
    "S3_ACCESS_KEY" -> "<AWS_ACCESS_KEY>",
    "S3_SECRET_KEY" -> "<AWS_SECRET_KEY>"
    "S3_SESSION_TOKEN" -> "<AWS_SESSION_TOKEN>"
    "S3_ENDPOINT" -> "s3.eu-central-1.amazonaws.com"
  )

  private[this] def createParquetSource(): Source = {
    val path = new Path(parquetFilePath)
    val properties = StorageProperties(Map("BUCKET_PATH" -> normalize(path)) ++ awsCredentials)
    val bucket = Bucket(properties)
    Source(FileFormat("PARQUET"), path, bucket.getConfiguration(), bucket.fileSystem)
  }

  private[this] final def normalize(path: Path): String =
    path.toUri.toString.replaceAll("/$", "").replaceAll("///", "/")

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime))
  @OutputTimeUnit(TimeUnit.SECONDS)
  def parquetDeserializationBeforeEmit(blackhole: Blackhole): Unit = {
    val source = createParquetSource()
    source.stream().foreach { row =>
      val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
      blackhole.consume(columns.toArray)
    }
  }
}

object ParquetReadBenchmark {
  def main(args: Array[String]) {
    new Runner(
      new OptionsBuilder()
        .include(classOf[ParquetReadBenchmark].getSimpleName)
        .warmupIterations(1)
        .measurementIterations(5)
        .forks(1)
        .build()
    ).run()
  }
}
