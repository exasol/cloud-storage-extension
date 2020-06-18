package com.exasol.cloudetl.avro

import scala.collection.JavaConverters._

import com.exasol.cloudetl.data.Row

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.scalatest.funsuite.AnyFunSuite

class AvroRowTest extends AnyFunSuite {

  test("apply returns Row from GenericRecord with ByteBuffer column") {
    val recordSchema = createRecord(
      "record",
      createField("col_str", Schema.create(Schema.Type.STRING))
    )
    val record = new GenericData.Record(recordSchema)
    record.put("col_str", java.nio.ByteBuffer.wrap(Array[Byte](104, 101, 108, 108, 111)))
    assert(AvroRow(record) === Row(Seq("hello")))
  }

  test("apply returns Row GenericRecord with single type in Union type") {
    val schemaTypes = Schema.createUnion(Schema.create(Schema.Type.LONG))
    val longUnionSchema = createUnion("col_union", schemaTypes)
    val recordSchema = createRecord(
      "record",
      createField("col_str", Schema.create(Schema.Type.STRING)),
      longUnionSchema
    )
    val record = new GenericData.Record(recordSchema)
    record.put("col_str", "123")
    record.put("col_union", 13L)
    assert(AvroRow(record) === Row(Seq("123", 13L)))
  }

  test("apply returns Row from GenericRecord with many columns") {
    val fixedSchema = createFixedSchema("fixedSchema", 5)
    val enumSchema = createEnumSchema("enumSchema", Seq("A", "B"))
    val recordSchema = createRecord(
      "record",
      createField("col_str", Schema.create(Schema.Type.STRING)),
      createPrimitiveUnionField("col_str_union", Schema.Type.STRING),
      createField("col_int", Schema.create(Schema.Type.INT)),
      createPrimitiveUnionField("col_int_union", Schema.Type.INT),
      createField("col_long", Schema.create(Schema.Type.LONG)),
      createPrimitiveUnionField("col_long_union", Schema.Type.LONG),
      createField("col_double", Schema.create(Schema.Type.DOUBLE)),
      createPrimitiveUnionField("col_double_union", Schema.Type.DOUBLE),
      createField("col_float", Schema.create(Schema.Type.FLOAT)),
      createPrimitiveUnionField("col_float_union", Schema.Type.FLOAT),
      createField("col_bool", Schema.create(Schema.Type.BOOLEAN)),
      createPrimitiveUnionField("col_bool_union", Schema.Type.BOOLEAN),
      createField("col_bytes", Schema.create(Schema.Type.BYTES)),
      createPrimitiveUnionField("col_bytes_union", Schema.Type.BYTES),
      createField("col_fixed", fixedSchema),
      createUnionField("col_fixed_union", fixedSchema),
      createField("col_enum", enumSchema),
      createUnionField("col_enum_union", enumSchema),
      createField("col_null", Schema.create(Schema.Type.NULL))
    )

    def getFixedData(fixedSchema: Schema, bytes: Array[Byte]): GenericData.Fixed = {
      val data = new GenericData.Fixed(fixedSchema)
      data.bytes(bytes)
      data
    }

    val record = new GenericData.Record(recordSchema)
    record.put("col_str", "hello")
    record.put("col_str_union", "hello")
    record.put("col_int", 1)
    record.put("col_int_union", 1)
    record.put("col_long", 1L)
    record.put("col_long_union", 1L)
    record.put("col_double", 1.0)
    record.put("col_double_union", 1.0)
    record.put("col_float", 1.0f)
    record.put("col_float_union", 1.0f)
    record.put("col_bool", true)
    record.put("col_bool_union", false)
    record.put("col_bytes", "bytes".getBytes())
    record.put("col_bytes_union", null)
    record.put("col_fixed", getFixedData(fixedSchema, Array[Byte](102, 105, 120, 101, 100)))
    record.put("col_fixed_union", getFixedData(fixedSchema, Array[Byte](104, 101, 108, 108, 111)))
    record.put("col_enum", new GenericData.EnumSymbol(enumSchema, "A"))
    record.put("col_enum_union", new GenericData.EnumSymbol(enumSchema, "B"))
    record.put("col_null", null)

    val expectedRow = Row(
      Seq(
        "hello",
        "hello",
        1,
        1,
        1L,
        1L,
        1.0,
        1.0,
        1.0f,
        1.0f,
        true,
        false,
        "bytes",
        null,
        "fixed",
        "hello",
        "A",
        "B",
        null
      )
    )

    assert(AvroRow(record) === expectedRow)
  }

  test("apply throws if Avro type (nested record) is not supported") {
    val innerRecordSchema = createRecord(
      "innerRecord",
      createPrimitiveUnionField("inner_field", Schema.Type.STRING)
    )

    val recordSchema = createRecord(
      "record",
      createPrimitiveUnionField("col_str", Schema.Type.STRING),
      createField("col_record", innerRecordSchema)
    )

    val innerRecord = new GenericData.Record(innerRecordSchema)
    innerRecord.put("inner_field", "abc")

    val record = new GenericData.Record(recordSchema)
    record.put("col_str", "xyz")
    record.put("col_record", innerRecord)

    val thrown = intercept[IllegalArgumentException] {
      AvroRow(record)
    }
    assert(thrown.getMessage === "Avro record type is not supported!")
  }

  test("apply throws if GenericRecord value cannot be cast as string") {
    val recordSchema = createRecord(
      "record",
      createField("col_str", Schema.create(Schema.Type.STRING))
    )
    val record = new GenericData.Record(recordSchema)
    record.put("col_str", 1L)
    val thrown = intercept[IllegalArgumentException] {
      AvroRow(record)
    }
    assert(thrown.getMessage === "Avro string type with value 1 cannot be converted to string!")
  }

  test("apply throws if GenericRecord Union type is not primitive and null") {
    val unionSchemaTypes =
      Schema.createUnion(Seq(Schema.Type.STRING, Schema.Type.INT).map(Schema.create(_)).asJava)
    val unionSchema = createUnion("col_union", unionSchemaTypes)
    val recordSchema = createRecord(
      "record",
      createField("col_str", Schema.create(Schema.Type.STRING)),
      unionSchema
    )
    val record = new GenericData.Record(recordSchema)
    record.put("col_str", "abc")
    record.put("col_union", 1)
    val thrown = intercept[IllegalArgumentException] {
      AvroRow(record)
    }
    assert(thrown.getMessage === "Avro Union type should contain a primitive and null!")
  }

  private[this] final def createRecord(name: String, fields: Schema.Field*): Schema = {
    val schema = Schema.createRecord(name, name, "com.exasol.cloudetl.avro", false)
    schema.setFields(fields.asJava)
    schema
  }

  private[this] final def createField(name: String, schema: Schema): Schema.Field =
    new Schema.Field(name, schema, "", null, Schema.Field.Order.ASCENDING)

  private[this] final def createUnionField(name: String, schemas: Schema*): Schema.Field = {
    val schemaTypes = Seq(Schema.create(Schema.Type.NULL)) ++ schemas
    val unionSchema = Schema.createUnion(schemaTypes.asJava)
    createUnion(name, unionSchema)
  }

  private[this] final def createPrimitiveUnionField(
    name: String,
    types: Schema.Type*
  ): Schema.Field = {
    val schemaTypes = (Seq(Schema.Type.NULL) ++ types).map(Schema.create(_))
    val unionSchema = Schema.createUnion(schemaTypes.asJava)
    createUnion(name, unionSchema)
  }

  private[this] final def createUnion(name: String, unionSchema: Schema): Schema.Field =
    new Schema.Field(name, unionSchema, null, null, Schema.Field.Order.ASCENDING)

  private[this] final def createFixedSchema(name: String, size: Int): Schema =
    Schema.createFixed(name, "", "com.exasol.cloudetl.avro", size)

  private[this] final def createEnumSchema(name: String, ordinals: Seq[String]): Schema =
    Schema.createEnum(name, "", "com.exasol.cloudetl.avro", ordinals.asJava)

}
