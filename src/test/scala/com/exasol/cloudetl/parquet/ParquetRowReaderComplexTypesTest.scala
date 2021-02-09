package com.exasol.cloudetl.parquet

import com.exasol.common.data.Row

import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.schema.MessageTypeParser

class ParquetRowReaderComplexTypesTest extends BaseParquetReaderTest {

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
         |  optional group ages (LIST) {
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

  test("reads non-standard array as JSON array string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group heights (LIST) {
         |    repeated int32 height;
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val prices = record.addGroup(0)
      prices.append("height", 314)
      prices.append("height", 271)
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("[314,271]")))
  }

  test("reads repeated field as JSON array") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  repeated binary name (UTF8);
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      record.add(0, "John")
      record.add(0, "Jane")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""["John","Jane"]""")))
  }

  test("reads repeated group with single element as JSON array string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  repeated group person {
         |    required binary name (UTF8);
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      var person = record.addGroup(0)
      person.append("name", "John")
      person = record.addGroup(0)
      person.append("name", "Jane")
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""["John","Jane"]""")))
  }

  test("reads repeated group many elements as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  repeated group person {
         |    required binary name (UTF8);
         |    optional int32 age;
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      var person = record.addGroup(0)
      person.append("name", "John").append("age", 24)
      person = record.addGroup(0)
      person.append("name", "Jane").append("age", 22)
      writer.write(record)
    }
    val expected = Row(Seq("""[{"name":"John","age":24},{"name":"Jane","age":22}]"""))
    assert(getRecords()(0) === expected)
  }

  test("reads array of arrays as JSON string") {
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
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("[[1,2],[3]]")))
  }

  test("reads array of maps as JSON string") {
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
      val array = record.addGroup(0).addGroup(0)
      var map = array.addGroup("map")
      map.addGroup("key_value").append("key", "key1").append("price", 3.14)
      map.addGroup("key_value").append("key", "key2").append("price", 2.71)
      map = array.addGroup("map")
      map.addGroup("key_value").append("key", "a").append("price", 100.0)
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""[{"key1":3.14,"key2":2.71},{"a":100.0}]""")))
  }

  test("reads map as JSON string") {
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

  test("reads map with array values as JSON string") {
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
      writer.write(record)
    }
    assert(getRecords()(0) === Row(Seq("""{"key1":[3.14,2.71]}""")))
  }

  test("reads map with group values as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group maps (MAP) {
         |    repeated group key_value {
         |      required binary name (UTF8);
         |      required group values {
         |        optional int32 height;
         |        optional int32 weight;
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val maps = record.addGroup(0)
      var map = maps.addGroup("key_value")
      map.append("name", "John").addGroup("values").append("height", 170).append("weight", 70)
      map = maps.addGroup("key_value")
      map.append("name", "Jane").addGroup("values").append("height", 160).append("weight", 60)
      writer.write(record)
    }
    val expected =
      Row(Seq("""{"John":{"height":170,"weight":70},"Jane":{"height":160,"weight":60}}"""))
    assert(getRecords()(0) === expected)
  }

  test("reads array of repeated group as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group array (LIST) {
         |    repeated group list {
         |      repeated group values {
         |        required binary key (UTF8);
         |        optional double price;
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val array = record.addGroup(0).addGroup(0)
      array.addGroup("values").append("key", "key1").append("price", 3.14)
      array.addGroup("values").append("key", "key2").append("price", 2.71)
      array.addGroup("values").append("key", "a").append("price", 100.0)
      writer.write(record)
    }
    val expected = Row(
      Seq(
        """[[{"key":"key1","price":3.14},{"key":"key2","price":2.71},{"key":"a","price":100.0}]]"""
      )
    )
    assert(getRecords()(0) === expected)
  }

  test("reads map with repeated group values as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  optional group maps (MAP) {
         |    repeated group key_value {
         |      required binary name (UTF8);
         |      repeated group values {
         |        required int32 year;
         |        optional int32 height;
         |        optional int32 weight;
         |      }
         |    }
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, true)) { writer =>
      val record = new SimpleGroup(schema)
      val maps = record.addGroup(0)
      var map = maps.addGroup("key_value")
      map.append("name", "John")
      map.addGroup(1).append("year", 2019).append("height", 170).append("weight", 70)
      map.addGroup(1).append("year", 2020).append("height", 170).append("weight", 80)

      map = maps.addGroup("key_value")
      map.append("name", "Jane")
      map.addGroup(1).append("year", 2019).append("height", 160)
      writer.write(record)
    }
    val expected = Row(
      Seq(
        """|{
           |"John":[{"year":2019,"height":170,"weight":70},{"year":2020,"height":170,"weight":80}],
           |"Jane":[{"year":2019,"height":160,"weight":null}]
           |}
           |""".stripMargin.replaceAll("\n", "")
      )
    )
    assert(getRecords()(0) === expected)
  }

  test("reads group as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary name (UTF8);
         |  optional group contacts {
         |    required binary name (UTF8);
         |    optional binary phoneNumber (UTF8);
         |  }
         |}
         |""".stripMargin
    )
    withResource(getParquetWriter(schema, false)) { writer =>
      val record = new SimpleGroup(schema)
      record.add(0, "John")
      val contacts = record.addGroup(1)
      contacts.append("name", "Jane").append("phoneNumber", "1337")
      writer.write(record)
    }
    val expected = Row(Seq("John", """{"name":"Jane","phoneNumber":"1337"}"""))
    assert(getRecords()(0) === expected)
  }

  test("reads group with repeated group as JSON string") {
    val schema = MessageTypeParser.parseMessageType(
      """|message test {
         |  required binary name (UTF8);
         |  optional group contacts {
         |    repeated group person {
         |      required binary name (UTF8);
         |      optional binary phoneNumber (UTF8);
         |    }
         |    optional int32 count;
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
      contacts.append("count", 2)
      writer.write(record)
    }
    val expected = Row(
      Seq(
        "John",
        """|{"person":
           |  [
           |    {"name":"Jane","phoneNumber":"1337"},
           |    {"name":"Jake","phoneNumber":null}
           |  ],
           |"count":2
           |}
           |""".stripMargin.replaceAll("\\s+", "")
      )
    )
    assert(getRecords()(0) === expected)
  }

  test("reads nested groups as JSON string") {
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
    assert(getRecords()(0) === Row(Seq("John", """{"Jane":["1337"]}""")))
  }

}
