package com.exasol.cloudetl.source

import java.nio.file.Paths

import com.exasol.cloudetl.util.FsUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.schema.MessageTypeParser
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

  private val resourcesDir = salesPosParquetFile.getParent
  private val pattern = s"${resourcesDir.toUri.toString}/*.parquet"

  test("reads multiple parquet files") {
    val iters = ParquetSource(pattern, fs, conf).stream()
    assert(iters.map(_.size).sum === 1005)
  }

  test("reads parquet files schema") {
    val schema = ParquetSource(pattern, fs, conf).getSchema()
    val expectedMsgType = MessageTypeParser
      .parseMessageType("""message spark_schema {
                          |  optional int64 sales_id;
                          |  optional int32 position_id;
                          |  optional int32 article_id;
                          |  optional int32 amount;
                          |  optional double price;
                          |  optional int32 voucher_id;
                          |  optional boolean canceled;
                          |}
      """.stripMargin)

    assert(schema.isDefined)
    schema.foreach { case msgType => assert(msgType === expectedMsgType) }
  }

}
