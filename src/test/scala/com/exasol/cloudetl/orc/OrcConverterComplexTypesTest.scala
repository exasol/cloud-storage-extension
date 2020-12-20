package com.exasol.cloudetl.orc

import java.nio.file.Path

import com.exasol.cloudetl.FileManager
import com.exasol.cloudetl.source.OrcSource
import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.orc.TypeDescription._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class OrcConverterComplexTypesTest extends AnyFunSuite with BeforeAndAfterEach with FileManager {

  private[this] var conf: Configuration = _
  private[this] var fileSystem: FileSystem = _
  private[this] var outputDirectory: Path = _
  private[this] var path: HPath = _
  private[this] var orcWriter: OrcWriter = _

  override final def beforeEach(): Unit = {
    conf = new Configuration
    fileSystem = FileSystem.get(conf)
    outputDirectory = createTemporaryFolder("orcRowConverterTest")
    path = new HPath(outputDirectory.toUri.toString, "part-00000.orc")
    orcWriter = new OrcWriter(path, conf)
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    ()
  }

  test("reads list of strings as JSON string") {
    val schema = createStruct()
      .addField("column", createList(createString()))
    orcWriter.write(schema, Map("column" -> Seq("John", "Jana", null)))
    assert(getRecords()(0) === Row(Seq("""["John","Jana",null]""")))
  }

  test("reads list of doubles as JSON string") {
    val schema = createStruct()
      .addField("column", createList(createDouble()))
    orcWriter.write(schema, Map("column" -> Seq(3.14, 2.71)))
    assert(getRecords()(0) === Row(Seq("[3.14,2.71]")))
  }

  test("reads list of list ints as JSON string") {
    val list = createList(createList(createInt()))
    val schema = createStruct().addField("column", list)
    orcWriter.write(schema, Map("column" -> Seq(Seq(314, 271), Seq(1, 2, 3))))
    assert(getRecords()(0) === Row(Seq("[[314,271],[1,2,3]]")))
  }

  test("reads list of maps as JSON string") {
    val inner = createList(createMap(createString(), createInt()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write(schema, Map("column" -> Seq(Map("pi" -> 314), Map("a" -> 1, "c" -> 3))))
    assert(getRecords()(0) === Row(Seq("""[{"pi":314},{"a":1,"c":3}]""")))
  }

  test("reads map as JSON string") {
    val map = createMap(createString(), createInt())
    val schema = createStruct().addField("column", map)
    orcWriter.write(schema, Map("column" -> Map("a" -> 1, "b" -> 2, "c" -> 3)))
    assert(getRecords()(0) === Row(Seq("""{"b":2,"a":1,"c":3}""")))
  }

  test("reads map with list values as JSON string") {
    val inner = createMap(createString(), createList(createDouble()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write(
      schema,
      Map("column" -> Map("consts" -> List(3.14, 2.71), "nums" -> List(1.0, 0.5)))
    )
    assert(getRecords()(0) === Row(Seq("""{"consts":[3.14,2.71],"nums":[1.0,0.5]}""")))
  }

  test("reads nested struct as JSON string") {
    val inner = createStruct()
      .addField("name", createString())
      .addField("phoneNumber", createInt())
      .addField("pets", createList(createString()))
    val schema = createStruct().addField("column", inner)
    orcWriter.write[Map[String, Any]](
      schema,
      Map("column" -> Map("name" -> "John", "phoneNumber" -> 1337, "pets" -> Seq("cat", "dog")))
    )
    val expected = Row(Seq("""{"phoneNumber":1337,"name":"John","pets":["cat","dog"]}"""))
    assert(getRecords()(0) === expected)
  }

  protected final def getRecords(): Seq[Row] =
    OrcSource(path, conf, fileSystem)
      .stream()
      .toSeq
}
