package com.exasol.cloudetl.source

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.cloudetl.util.FileSystemUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.schema.MessageTypeParser
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.Var"))
class ParquetSourceSuite extends FunSuite with BeforeAndAfterAll with Matchers {

  private var conf: Configuration = _
  private var fileSystem: FileSystem = _
  private var parquetResourceFolder: Path = _

  override final def beforeAll(): Unit = {
    conf = new Configuration()
    fileSystem = FileSystem.get(conf)
    parquetResourceFolder =
      Paths.get(getClass.getResource("/data/import/parquet/").toURI).toAbsolutePath
    ()
  }

  test("reads a single sales_positions parquet format file") {
    val filePath = Paths.get(s"$parquetResourceFolder/sales_positions1.snappy.parquet")
    FileSystemUtil.globWithLocal(filePath, fileSystem).foreach { file =>
      val source = ParquetSource(file, conf, fileSystem)
      assert(source.stream().size === 500)
    }
  }

  test("reads multiple sales_positions parquet format files") {
    val filePattern = Paths.get(s"$parquetResourceFolder/sales_positions*.parquet")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePattern, fileSystem)
    val result = globbedFilePath.map { file =>
      val source = ParquetSource(file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum

    assert(result === 1005)
  }

  test("reads sales_positions parquet format files schema") {
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

    val filePattern = Paths.get(s"$parquetResourceFolder/sales_pos*.parquet")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePattern, fileSystem)
    globbedFilePath.foreach { file =>
      val schema = ParquetSource(file, conf, fileSystem).getSchema()
      assert(schema.isDefined)
      schema.foreach { case msgType => assert(msgType === expectedMsgType) }
    }
  }

  test("reads a sales parquet format file with richer types") {
    val filePath = Paths.get(s"$parquetResourceFolder/sales1.snappy.parquet")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    val result = globbedFilePath.map { file =>
      val source = ParquetSource(file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum

    assert(result === 999)
  }

}
