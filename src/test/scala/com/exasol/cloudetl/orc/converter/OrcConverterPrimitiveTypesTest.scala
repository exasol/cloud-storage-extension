package com.exasol.cloudetl.orc.converter

import org.apache.hadoop.hive.common.`type`.HiveDecimal
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.TypeDescription._

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
class OrcConverterPrimitiveTypesTest extends BaseOrcConverterTest {

  test("reads BOOLEAN as long value") {
    val schema = createStruct().addField("boolean", createBoolean())
    val batch = schema.createRowBatch()
    batch.size = 3
    withWriter(schema) { writer =>
      val boolVector = batch.cols(0).asInstanceOf[LongColumnVector]
      boolVector.noNulls = false
      boolVector.vector(0) = 1L
      boolVector.vector(1) = 0L
      boolVector.isNull(2) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === true)
    assert(records(1).get(0) === false)
    assert(records(2).isNullAt(0) === true)
  }

  test("reads BYTE as byte value") {
    val schema = createStruct().addField("byte", createByte())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val byte = java.lang.Byte.valueOf("13")
      val byteVector = batch.cols(0).asInstanceOf[LongColumnVector]
      byteVector.noNulls = false
      byteVector.vector(0) = byte.byteValue().toLong
      byteVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === 13)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads SHORT as short value") {
    val schema = createStruct().addField("short", createShort())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val shortVector = batch.cols(0).asInstanceOf[LongColumnVector]
      shortVector.noNulls = false
      shortVector.vector(0) = 314
      shortVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === 314)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads INT as integer value") {
    val schema = createStruct().addField("int", createInt())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val intVector = batch.cols(0).asInstanceOf[LongColumnVector]
      intVector.noNulls = false
      intVector.vector(0) = 314
      intVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === 314)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads LONG as long value") {
    val schema = createStruct().addField("long", createLong())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val longVector = batch.cols(0).asInstanceOf[LongColumnVector]
      longVector.noNulls = false
      longVector.vector(0) = 3L
      longVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === 3L)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads FLOAT as float value") {
    val schema = createStruct().addField("float", createFloat())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val floatVector = batch.cols(0).asInstanceOf[DoubleColumnVector]
      floatVector.noNulls = false
      floatVector.vector(0) = 3.14F
      floatVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === 3.14f)
    assert(records(1).isNullAt(0) === true)
  }

  test("reads DOUBLE as double value") {
    val schema = createStruct().addField("double", createDouble())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val floatVector = batch.cols(0).asInstanceOf[DoubleColumnVector]
      floatVector.noNulls = false
      floatVector.vector(0) = 2.71
      floatVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
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
    val batch = schema.createRowBatch()
    batch.size = 3
    withWriter(schema) { writer =>
      val dateVector = batch.cols(0).asInstanceOf[LongColumnVector]
      dateVector.noNulls = false
      dateVector.vector(0) = 0
      dateVector.vector(1) = 1
      dateVector.isNull(2) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === java.sql.Date.valueOf("1970-01-01"))
    assert(records(1).get(0) === java.sql.Date.valueOf("1970-01-02"))
    assert(records(2).isNullAt(0) === true)
  }

  test("reads TIMESTAMP as java.sql.timestamp value") {
    val schema = createStruct().addField("timestamp", createTimestamp())
    val batch = schema.createRowBatch()
    batch.size = 3
    val timestamp1 = java.sql.Timestamp.from(java.time.Instant.EPOCH)
    val timestamp2 = new java.sql.Timestamp(System.currentTimeMillis())
    withWriter(schema) { writer =>
      val timestampVector = batch.cols(0).asInstanceOf[TimestampColumnVector]
      timestampVector.noNulls = false
      timestampVector.set(0, timestamp1)
      timestampVector.set(1, timestamp2)
      timestampVector.isNull(2) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === timestamp1)
    assert(records(1).get(0) === timestamp2)
    assert(records(2).isNullAt(0) === true)
  }

  test("reads CHAR as string value") {
    val schema = createStruct().addField("string", createChar())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val charVector = batch.cols(0).asInstanceOf[BytesColumnVector]
      charVector.noNulls = false
      charVector.setVal(0, "value".getBytes("UTF-8"))
      charVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === "value")
    assert(records(1).isNullAt(0) === true)
  }

  test("reads STRING as string value") {
    val schema = createStruct().addField("string", createString())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val stringVector = batch.cols(0).asInstanceOf[BytesColumnVector]
      stringVector.noNulls = false
      stringVector.setVal(0, "value".getBytes("UTF-8"))
      stringVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === "value")
    assert(records(1).isNullAt(0) === true)
  }

  test("reads VARCHAR as string value") {
    val schema = createStruct().addField("string", createVarchar())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val varcharVector = batch.cols(0).asInstanceOf[BytesColumnVector]
      varcharVector.noNulls = false
      varcharVector.setVal(0, "value".getBytes("UTF-8"))
      varcharVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === "value")
    assert(records(1).isNullAt(0) === true)
  }

  test("reads BINARY as string value") {
    val schema = createStruct().addField("string", createBinary())
    val batch = schema.createRowBatch()
    batch.size = 2
    withWriter(schema) { writer =>
      val binaryVector = batch.cols(0).asInstanceOf[BytesColumnVector]
      binaryVector.noNulls = false
      binaryVector.setVal(0, "value".getBytes("UTF-8"))
      binaryVector.isNull(1) = true
      writer.addRowBatch(batch)
    }
    val records = getRecords()
    assert(records(0).get(0) === "value")
    assert(records(1).isNullAt(0) === true)
  }

}
