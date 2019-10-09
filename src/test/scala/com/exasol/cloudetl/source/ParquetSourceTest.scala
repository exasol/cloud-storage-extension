package com.exasol.cloudetl.source

import java.nio.file.Paths

import com.exasol.cloudetl.util.FileSystemUtil

import org.apache.parquet.schema.MessageTypeParser

class ParquetSourceTest extends SourceTest {

  override val format: String = "parquet"

  test("getSchema returns parquet schema") {
    val expectedMessageType = MessageTypeParser
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

    val filePattern = Paths.get(s"$resourceDir/sales_pos*.parquet")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePattern, getFileSystem())
    globbedFilePath.foreach { file =>
      val schema = ParquetSource(file, getConf(), getFileSystem()).getSchema()
      assert(schema.isDefined)
      schema.foreach { case messageType => assert(messageType === expectedMessageType) }
    }
  }

  test("stream returns count of records from single PARQUET file") {
    val filePath = Paths.get(s"$resourceDir/sales_positions1.snappy.parquet")
    assert(getRecordsCount(filePath) === 500)
  }

  test("stream reaturns count of records from PARQUET files") {
    val filePattern = Paths.get(s"$resourceDir/sales_positions*.parquet")
    assert(getRecordsCount(filePattern) === 1005)
  }

  test("stream returns count of records from PARQUET files with richer types") {
    val filePath = Paths.get(s"$resourceDir/sales1.snappy.parquet")
    assert(getRecordsCount(filePath) === 999)
  }

}
