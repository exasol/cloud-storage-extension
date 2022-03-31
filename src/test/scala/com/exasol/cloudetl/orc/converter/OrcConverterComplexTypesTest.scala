package com.exasol.cloudetl.orc.converter

import java.nio.charset.StandardCharsets.UTF_8

import com.exasol.common.data.Row

import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription._

class OrcConverterComplexTypesTest extends BaseOrcConverterTest {

  test("reads list of strings as JSON string") {
    val schema = createStruct()
      .addField("column", createList(createString()))
    orcWriter.write(schema, List(Seq("John", "Jana", null)))
    assert(getRecords()(0) === Row(Seq("""["John","Jana",null]""")))
  }

  test("reads list of doubles as JSON string") {
    val schema = createStruct()
      .addField("column", createList(createDouble()))
    orcWriter.write(schema, List(Seq(3.14, 2.71)))
    assert(getRecords()(0) === Row(Seq("[3.14,2.71]")))
  }

  test("reads null list as JSON string") {
    val schema = createStruct()
      .addField("column", createList(createDouble()))
    orcWriter.write(schema, List(null))
    assert(getRecords()(0) === Row(Seq("[]")))
  }

  test("reads list of list ints as JSON string") {
    val list = createList(createList(createInt()))
    val schema = createStruct().addField("column", list)
    orcWriter.write(schema, List(Seq(Seq(314, 271), Seq(1, 2, 3))))
    assert(getRecords()(0) === Row(Seq("[[314,271],[1,2,3]]")))
  }

  test("reads list of maps as JSON string") {
    val inner = createList(createMap(createString(), createInt()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write(schema, List(Seq(Map("pi" -> 314), Map("a" -> 1, "c" -> 3))))
    assert(getRecords()(0) === Row(Seq("""[{"pi":314},{"a":1,"c":3}]""")))
  }

  test("reads map as JSON string") {
    val map = createMap(createString(), createInt())
    val schema = createStruct().addField("column", map)
    orcWriter.write(schema, List(Map("a" -> 1, "b" -> 2, "c" -> 3)))
    assert(getRecords()(0) === Row(Seq("""{"a":1,"b":2,"c":3}""")))
  }

  test("reads map with list values as JSON string") {
    val inner = createMap(createString(), createList(createDouble()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write(
      schema,
      List(Map("consts" -> List(3.14, 2.71), "nums" -> List(1.0, 0.5)))
    )
    assert(getRecords()(0) === Row(Seq("""{"nums":[1.0,0.5],"consts":[3.14,2.71]}""")))
  }

  test("reads nested struct as JSON string") {
    val inner = createStruct()
      .addField("name", createString())
      .addField("phoneNumber", createInt())
      .addField("pets", createList(createString()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write[Map[String, Any]](
      schema,
      List(Map("name" -> "John", "phoneNumber" -> 1337, "pets" -> Seq("cat", "dog")))
    )
    val expected = Row(Seq("""{"pets":["cat","dog"],"phoneNumber":1337,"name":"John"}"""))
    assert(getRecords()(0) === expected)
  }

  test("reads union as JSON string") {
    val schema = createStruct()
      .addField("column", createUnion().addUnionChild(createInt()).addUnionChild(createString()))
    val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(schema))
    val batch = schema.createRowBatch()
    batch.size = 3

    val unionVector = batch.cols(0).asInstanceOf[UnionColumnVector]
    unionVector.noNulls = false
    // Set int type for the first row
    unionVector.tags(0) = 0
    unionVector.fields(0).asInstanceOf[LongColumnVector].vector(0) = 23
    // Set string type for the second row
    unionVector.tags(1) = 1
    unionVector.fields(1).asInstanceOf[BytesColumnVector].setVal(1, "str".getBytes(UTF_8))
    // Set null for the third row
    unionVector.isNull(2) = true
    writer.addRowBatch(batch)
    writer.close()

    val records = getRecords()
    assert(records(0) === Row(Seq("""{"STRING":null,"INT":23}""")))
    assert(records(1) === Row(Seq("""{"STRING":"str","INT":null}""")))
    assert(records(2) === Row(Seq("""{"STRING":null,"INT":null}""")))
  }

}
