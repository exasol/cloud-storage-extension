package com.exasol.cloudetl.source

import java.nio.file.Paths

import com.exasol.cloudetl.util.FsUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.Any"))
class ParquetSourceSuite extends FunSuite with Matchers {

  private val conf = new Configuration()
  private val fs = FileSystem.get(conf)

  private val salesPosParquetFile =
    Paths.get(getClass.getResource("/parquet/sales_positions1.snappy.parquet").toURI)

  test("reads a single parquet file") {
    val data = ParquetSource(FsUtil.globWithLocal(salesPosParquetFile, fs), fs, conf)
    val iters = data.stream
    assert(iters.map(_.size).sum === 500)
  }

  test("reads multiple parquet files") {
    val resourcesDir = salesPosParquetFile.getParent
    val pattern = s"${resourcesDir.toUri.toString}/*.parquet"
    val data = ParquetSource(pattern, fs, conf)
    val iters = data.stream
    assert(iters.map(_.size).sum === 1000)
  }

}
