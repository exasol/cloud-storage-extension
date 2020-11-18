package com.exasol.cloudetl.parquet

import java.io.Closeable
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.nio.file.Path

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.parquet.converter.ParquetDecimalConverter
import com.exasol.cloudetl.source.ParquetSource
import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.column.Dictionary
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema._
import org.apache.parquet.schema.LogicalTypeAnnotation.decimalType
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class ParquetRowReaderTest extends AnyFunSuite with BeforeAndAfterEach with DummyRecordsTest {

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

  test("read throws if parquet record has complex types") {
    val schema = MessageTypeParser.parseMessageType(
      """|message user {
         |  required binary name (UTF8);
         |  optional group contacts {
         |    repeated group array {
         |      required binary name (UTF8);
         |      optional binary phoneNumber (UTF8);
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.add(0, "A. Name")
      val contacts = record.addGroup(1)
      contacts.addGroup(0).append("name", "A. Contact").append("phoneNumber", "1337")
      contacts.addGroup(0).append("name", "Second Contact")
      writer.write(record)
    }

    val thrown = intercept[UnsupportedOperationException] {
      getRecords()
    }
    assert(thrown.getMessage === "Currently only primitive types are supported")
  }

  test("reads INT64 (TIMESTAMP_MILLIS) as timestamp value") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int64 col_long;
         |  required int64 col_timestamp (TIMESTAMP_MILLIS);
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_long", 153L)
      record.append("col_timestamp", TIMESTAMP_VALUE1.getTime())
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq(153L, TIMESTAMP_VALUE1)))
  }

  test("reads FIXED_LEN_BYTE_ARRAY as string value") {
    val size = 5
    val schema = MessageTypeParser.parseMessageType(
      s"""|message test {
          |  required fixed_len_byte_array($size) col_byte_array;
          |}
          |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_byte_array", "hello")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("hello")))
  }

  test("reads BINARY as string value") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary col_binary;
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.append("col_binary", "test")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("test")))
  }

  test("reads BINARY (UTF8) as string value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary col_binary (UTF8);
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq("test1", "test2", "test1", "test2").foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("col_binary", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq("test1")))
  }

  test("reads INT32 (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int32 col_int (DECIMAL(9,2));
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(123456789, 0, 0, 123456789).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("col_int", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(BigDecimal.valueOf(123456789, 2))))
  }

  test("reads INT64 (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required int64 column (DECIMAL(18,2));
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(1234567890123456L, 1L, 1L, 1234567890123456L).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)
      }
    }
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(BigDecimal.valueOf(1234567890123456L, 2))))
  }

  test("reads BINARY (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary column (DECIMAL(30,2));
         |}
         |""".stripMargin
    )
    val decimalValue = "123456789012345678901234567890"
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(decimalValue, decimalValue).foreach { value =>
        val record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)
      }
    }
    val expected = new BigDecimal(new BigInteger(decimalValue.getBytes()), 2, new MathContext(30))
    val records = getRecords()
    assert(records.size === 2)
    assert(records(0) === Row(Seq(expected)))
  }

  test("reads FIXED_LEN_BYTE_ARRAY (decimal) as big decimal value using dictionary encoding") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required fixed_len_byte_array(20) column (DECIMAL(20,2));
         |}
         |""".stripMargin
    )
    val decimalValue = "12345678901234567890"
    val bytes = Array.fill[Byte](30)(0x1)
    val zeros = Binary.fromConstantByteArray(bytes, 0, 20)
    withResource(getParquetWriter(schema, true)) { writer =>
      Seq(decimalValue, decimalValue).foreach { value =>
        var record = new SimpleGroup(schema)
        record.append("column", value)
        writer.write(record)

        record = new SimpleGroup(schema)
        record.append("column", zeros)
        writer.write(record)
      }
    }
    val expected = new BigDecimal(new BigInteger(decimalValue.getBytes()), 2, new MathContext(20))
    val records = getRecords()
    assert(records.size === 4)
    assert(records(0) === Row(Seq(expected)))
  }

  test("read throws when decimal converter has unsupported base primitive type") {
    final case class DictionaryEncoding() extends Dictionary(null) {
      override def getMaxId(): Int = 4
    }
    val parquetType = Types
      .primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.OPTIONAL)
      .length(4)
      .as(decimalType(0, 9))
      .named("bytes")
    val thrown = intercept[UnsupportedOperationException] {
      ParquetDecimalConverter(parquetType, 0, null).setDictionary(DictionaryEncoding())
    }
    assert(thrown.getMessage.contains("Cannot convert parquet type to decimal type"))
  }

  private[this] def withResource[T <: Closeable](writer: T)(block: T => Unit): Unit = {
    block(writer)
    writer.close()
  }

  private[this] def getRecords(): Seq[Row] =
    ParquetSource(path, conf, fileSystem)
      .stream()
      .toSeq

  private[this] def getParquetWriter(
    schema: MessageType,
    dictionaryEncoding: Boolean
  ): ParquetWriter[Group] =
    BaseGroupWriterBuilder(path, schema)
      .withDictionaryEncoding(dictionaryEncoding)
      .build()

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
