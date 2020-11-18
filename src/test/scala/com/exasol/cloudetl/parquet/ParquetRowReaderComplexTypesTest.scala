package com.exasol.cloudetl.parquet

import java.io.Closeable
import java.nio.file.Path

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.source.ParquetSource
import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class ParquetRowReaderComplexTypesTest
    extends AnyFunSuite
    with BeforeAndAfterEach
    with DummyRecordsTest {

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

  test("reads array of strings as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group names (LIST) {
         |    repeated group list {
         |      required binary name (UTF8);
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val names = record.addGroup(0)
      names.addGroup(0).append("name", "John")
      names.addGroup(0).append("name", "Jane")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""["John","Jane"]""")))
  }

  test("reads array of ints as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group names (LIST) {
         |    repeated group list {
         |      required int32 age;
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val ages = record.addGroup(0)
      ages.addGroup(0).append("age", 3)
      ages.addGroup(0).append("age", 4)
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("[3,4]")))
  }

  test("reads array of doubles as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group prices (LIST) {
         |    repeated group list {
         |      required double price;
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val prices = record.addGroup(0)
      prices.addGroup(0).append("price", 3.14)
      prices.addGroup(0).append("price", 2.71)
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("[3.14,2.71]")))
  }

  test("reads non-standard array as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group prices (LIST) {
         |    repeated int32 price;
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val prices = record.addGroup(0)
      prices.append("price", 314)
      prices.append("price", 271)
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads arrays of arrays as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group arrays (LIST) {
         |    repeated group list {
         |      required group inner (LIST) {
         |        repeated group list {
         |          required int32 element;
         |        }
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val arrays = record.addGroup(0).addGroup(0)
      var inner = arrays.addGroup("inner")
      inner.addGroup(0).append("element", 1)
      inner.addGroup(0).append("element", 2)
      inner = arrays.addGroup("inner")
      inner.addGroup(0).append("element", 3)
      println(s"RECORD: $record")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads arrays of maps as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group maps (LIST) {
         |    repeated group list {
         |      optional group map (MAP) {
         |        repeated group key_value {
         |          required binary key (UTF8);
         |          optional double price;
         |        }
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val maps = record.addGroup(0).addGroup(0).addGroup("map")
      maps.addGroup("key_value").append("key", "key1").append("price", 3.14)
      maps.addGroup("key_value").append("key", "key2").append("price", 2.71)
      // map.addGroup("key_value").append("key", "key2").append("value", 271L)
      println(s"RECORD: $record")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads map as key value JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group map (MAP) {
         |    repeated group key_value {
         |      required binary key (UTF8);
         |      required int64 value;
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val map = record.addGroup(0)
      map.addGroup("key_value").append("key", "key1").append("value", 314L)
      map.addGroup("key_value").append("key", "key2").append("value", 271L)
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""{"key1":314,"key2":271}""")))
  }

  test("reads map of arrays as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group map (MAP) {
         |    repeated group key_value {
         |      required binary key (UTF8);
         |      optional group prices (LIST) {
         |        repeated group list {
         |          required double price;
         |        }
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val map = record.addGroup(0).addGroup("key_value")
      val prices = map.append("key", "key1").addGroup("prices")
      prices.addGroup(0).append("price", 3.14)
      prices.addGroup(0).append("price", 2.71)
      // map.addGroup("key_value").append("key", "key2").append("value", 271L)
      println(s"RECORD: $record")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads map with group key as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group maps (MAP) {
         |    repeated group key_value {
         |      required group key {
         |        optional binary name;
         |        optional binary surname;
         |      }
         |      optional double price;
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val maps = record.addGroup(0).addGroup("key_value")
      maps.addGroup("key").append("name", "John").append("surname", "Doe")
      maps.append("price", 2.71)
      // map.addGroup("key_value").append("key", "key2").append("value", 271L)
      println(s"RECORD: $record")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads struct record as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
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
      record.add(0, "John")
      val contacts = record.addGroup(1)
      contacts.addGroup(0).append("name", "Jane").append("phoneNumber", "1337")
      contacts.addGroup(0).append("name", "Jake")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  test("reads nested struct record as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary name (UTF8);
         |  optional group contacts (MAP) {
         |    repeated group key_value {
         |      required binary name (UTF8);
         |      optional group numbers (LIST) {
         |        repeated group list {
         |          optional binary phoneNumber (UTF8);
         |        }
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.add(0, "John")
      val contacts = record.addGroup(1)
      val phoneNumbers = contacts.addGroup(0).append("name", "Jane").addGroup("numbers")
      phoneNumbers.addGroup(0).append("phoneNumber", "1337")
      writer.write(record)
    }
    getRecords().foreach(println(_))
  }

  /*
  private[this] def writeValues[T](schema: MessageType, values: Seq[Any]): Unit =
    values.foreach {
      case value =>
        val v = getValue(value)
        withResource(getParquetWriter(schema, true)) { writer =>
          val record = new SimpleGroup(schema)
          val names = record.addGroup(0)
          names.addGroup(0).append("name", value.asInstanceOf[T])
          names.addGroup(0).append("name", v)
          writer.write(record)
        }
    }

  private[this] def getValue[T](value: T): Any = value match {
    case value: String => value.asInstanceOf[String]
    case value: Int => value.asInstanceOf[Int]
    case value: Long => value.asInstanceOf[Long]
  }
   */

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
