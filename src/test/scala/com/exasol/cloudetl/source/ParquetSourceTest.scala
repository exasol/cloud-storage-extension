package com.exasol.cloudetl.source

import java.nio.file.Paths

import com.exasol.cloudetl.filesystem.FileSystemManager

import org.apache.parquet.schema.MessageTypeParser

class ParquetSourceTest extends AbstractSourceTest {

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
    val globbedFilePath = FileSystemManager.globWithLocal(filePattern, getFileSystem())
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

  test("stream returns count of records from PARQUET files") {
    val filePattern = Paths.get(s"$resourceDir/sales_positions*.parquet")
    assert(getRecordsCount(filePattern) === 1005)
  }

  test("stream returns count of records from PARQUET files with richer types") {
    val filePath = Paths.get(s"$resourceDir/sales1.snappy.parquet")
    assert(getRecordsCount(filePath) === 999)
  }

  test("stream throws if it cannot create PARQUET reader") {
    val nonPath = new org.apache.hadoop.fs.Path(s"$resourceDir/notFile.parquet")
    val thrown = intercept[java.io.FileNotFoundException] {
      getSource(nonPath).stream().size
    }
    assert(thrown.getMessage === s"File $nonPath does not exist")
  }

}
