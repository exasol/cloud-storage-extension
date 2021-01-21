package com.exasol.cloudetl.orc.converter

import org.apache.hadoop.hive.common.`type`.HiveDecimal
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.TypeDescription._

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
class OrcConverterPrimitiveTypesTest extends BaseOrcConverterTest {

  test("reads BOOLEAN as long value") {
    val schema = createStruct().addField("boolean", createBoolean())
    orcWriter.write[Any](schema, List(true, false, null))
    val records = getRecords()
    assert(records(0).get(0) === true)
    assert(records(1).get(0) === false)
    assert(records(2).isNullAt(0) === true)
  }

  test("reads BYTE as byte value") {
    val schema = createStruct().addField("byte", createByte())
    orcWriter.write[Any](schema, List(13, null))
    val records = getRecords()
    assert(records(0).get(0) === 13)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads SHORT as short value") {
    val schema = createStruct().addField("short", createShort())
    orcWriter.write[Any](schema, List(314, null))
    val records = getRecords()
    assert(records(0).get(0) === 314)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads INT as integer value") {
    val schema = createStruct().addField("int", createInt())
    orcWriter.write[Any](schema, List(314, null))
    val records = getRecords()
    assert(records(0).get(0) === 314)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads LONG as long value") {
    val schema = createStruct().addField("long", createLong())
    orcWriter.write[Any](schema, List(3L, null))
    val records = getRecords()
    assert(records(0).get(0) === 3L)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads FLOAT as float value") {
    val schema = createStruct().addField("float", createFloat())
    orcWriter.write[Any](schema, List(3.14F, null))
    val records = getRecords()
    assert(records(0).get(0) === 3.14f)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads DOUBLE as double value") {
    val schema = createStruct().addField("double", createDouble())
    orcWriter.write[Any](schema, List(2.71, null))
    val records = getRecords()
    assert(records(0).get(0) === 2.71)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads Decimal value as java.math.decimal") {
    val schema = createStruct().addField("decimal", createDecimal())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val decimalVector = batch.cols(0).asInstanceOf[DecimalColumnVector]
      decimalVector.noNulls = false
      decimalVector.vector(0).set(HiveDecimal.create("173.433"))
      decimalVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0).isInstanceOf[java.math.BigDecimal])
    assert(records(0).getAs[java.math.BigDecimal](0).doubleValue() === 173.433)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads DATE as java.sql.date value") {
    val schema = createStruct().addField("date", createDate())
    orcWriter.write[Any](schema, List(0, 1, null))
    val records = getRecords()
    assert(records(0).get(0) === java.sql.Date.valueOf("1970-01-01"))
    assert(records(1).get(0) === java.sql.Date.valueOf("1970-01-02"))
    assert(records(2).isNullAt(0) === true)
  }

  test("reads TIMESTAMP as java.sql.timestamp value") {
    val timestamp1 = java.sql.Timestamp.from(java.time.Instant.EPOCH)
    val timestamp2 = new java.sql.Timestamp(System.currentTimeMillis())
    val schema = createStruct().addField("timestamp", createTimestamp())
    orcWriter.write[Any](schema, List(timestamp1, timestamp2, null))
    val records = getRecords()
    assert(records(0).get(0) === timestamp1)
    assert(records(1).get(0) === timestamp2)
    assert(records(2).isNullAt(0) === true)
  }

  test("reads CHAR as string value") {
    val schema = createStruct().addField("string", createChar())
    orcWriter.write[Any](schema, List("value", null))
    val records = getRecords()
    assert(records(0).get(0) === "value")
    assert(records(1).isNullAt(0) === true)
  }

  // scalastyle:off nonascii
  test("reads STRING as string value") {
    val schema = createStruct().addField("string", createString())
    orcWriter.write[Any](schema, List("välue", null))
    val records = getRecords()
    assert(records(0).get(0) === "välue")
    assert(records(1).isNullAt(0) === true)
  }

  test("reads VARCHAR as string value") {
    val schema = createStruct().addField("string", createVarchar())
    orcWriter.write[Any](schema, List("smiley ☺", null))
    val records = getRecords()
    assert(records(0).get(0) === "smiley ☺")
    assert(records(1).isNullAt(0) === true)
  }
  // scalastyle:on nonascii

  test("reads BINARY as string value") {
    val schema = createStruct().addField("string", createBinary())
    orcWriter.write[Any](schema, List("str", null))
    val records = getRecords()
    assert(records(0).get(0) === "str")
    assert(records(1).isNullAt(0) === true)
  }

}
