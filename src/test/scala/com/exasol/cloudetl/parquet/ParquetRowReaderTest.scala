package com.exasol.cloudetl.parquet

import java.nio.file.Path

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.source.ParquetSource

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.MessageTypeParser
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

class ParquetRowReaderTest extends FunSuite with BeforeAndAfterEach with DummyRecordsTest {

  private[this] var conf: Configuration = _
  private[this] var fileSystem: FileSystem = _
  private[this] var outputDirectory: Path = _
  private[this] var path: HPath = _

  override final def beforeEach(): Unit = {
    conf = new Configuration
    fileSystem = FileSystem.get(conf)
    outputDirectory = createTemporaryFolder("parquetRowReaderTest")
    path = new HPath(outputDirectory.toUri.toString, "part-00000.parquet")
    ()
  }

  override final def afterEach(): Unit = {
    deleteFiles(outputDirectory)
    ()
  }

  private[this] def write(schema: MessageType, record: SimpleGroup): Unit = {
    val writer = BaseGroupWriterBuilder(path, schema).build()
    writer.write(record)
    writer.close()
  }

  test("read throws if parquet record has complex types") {
    val schema = MessageTypeParser
      .parseMessageType("""message user {
                          |  required binary name (UTF8);
                          |  optional group contacts {
                          |    repeated group array {
                          |      required binary name (UTF8);
                          |      optional binary phoneNumber (UTF8);
                          |    }
                          |  }
                          |}
                        """.stripMargin)
    val record = new SimpleGroup(schema)
    record.add(0, "A. Name")
    val contacts = record.addGroup(1)
    contacts.addGroup(0).append("name", "A. Contact").append("phoneNumber", "1337")
    contacts.addGroup(0).append("name", "Second Contact")
    write(schema, record)

    val thrown = intercept[UnsupportedOperationException] {
      ParquetSource(path, conf, fileSystem).stream().size
    }
    assert(thrown.getMessage === "Currently only primitive types are supported")
  }

  test("reads INT64 (TIMESTAMP_MILLIS) as Timestamp value") {

    val schema = MessageTypeParser
      .parseMessageType("""message test {
                          |  required int64 col_long;
                          |  required int64 col_timestamp (TIMESTAMP_MILLIS);
                          |}
                        """.stripMargin)
    val record = new SimpleGroup(schema)
    record.append("col_long", 153L)
    record.append("col_timestamp", TIMESTAMP_VALUE1.getTime())
    write(schema, record)

    val src = ParquetSource(path, conf, fileSystem)
    assert(src.stream().toSeq(0) === Row(Seq(153L, TIMESTAMP_VALUE1)))
  }

  test("reads non-decimal FIXED_LEN_BYTE_ARRAY as String value") {
    val size = 5
    val schema = MessageTypeParser
      .parseMessageType(s"""message test {
                           |  required fixed_len_byte_array($size) col_byte_array;
                           |}
                        """.stripMargin)
    val record = new SimpleGroup(schema)
    record.append("col_byte_array", "hello")
    write(schema, record)

    val src = ParquetSource(path, conf, fileSystem)
    assert(src.stream().toSeq(0) === Row(Seq("hello")))
  }

  test("reads non-UTF8 BINARY as String value") {
    val schema = MessageTypeParser
      .parseMessageType(s"""message test {
                           |  required binary col_binary;
                           |}
                        """.stripMargin)
    val record = new SimpleGroup(schema)
    record.append("col_binary", "test")
    write(schema, record)

    val src = ParquetSource(path, conf, fileSystem)
    assert(src.stream().toSeq(0) === Row(Seq("test")))
  }

  private[this] case class BaseGroupWriteSupport(schema: MessageType)
      extends WriteSupport[Group] {
    var writer: GroupWriter = null

    override def prepareForWrite(recordConsumer: RecordConsumer): Unit =
      writer = new GroupWriter(recordConsumer, schema)

    override def init(configuration: Configuration): WriteSupport.WriteContext =
      new WriteSupport.WriteContext(schema, new java.util.HashMap[String, String]())

    override def write(record: Group): Unit =
      writer.write(record)
  }

  private[this] case class BaseGroupWriterBuilder(path: HPath, schema: MessageType)
      extends ParquetWriter.Builder[Group, BaseGroupWriterBuilder](path) {
    override def getWriteSupport(conf: Configuration): WriteSupport[Group] =
      BaseGroupWriteSupport(schema)
    override def self(): BaseGroupWriterBuilder = this
  }

}
