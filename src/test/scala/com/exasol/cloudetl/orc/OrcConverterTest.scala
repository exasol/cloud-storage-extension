package com.exasol.cloudetl.orc

import java.nio.file.Path

import com.exasol.cloudetl.FileManager
import com.exasol.cloudetl.source.OrcSource

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.hive.common.`type`.HiveDecimal
import org.apache.hadoop.hive.ql.exec.vector.DecimalColumnVector
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
class OrcConverterTest extends AnyFunSuite with BeforeAndAfterEach with FileManager {

  private[this] var conf: Configuration = _
  private[this] var fileSystem: FileSystem = _
  private[this] var outputDirectory: Path = _
  private[this] var path: HPath = _

  override final def beforeEach(): Unit = {
    conf = new Configuration
    fileSystem = FileSystem.get(conf)
    outputDirectory = createTemporaryFolder("orcRowConverterTest")
    path = new HPath(outputDirectory.toUri.toString, "part-00000.orc")
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    ()
  }

  test("apply throws if orc type is unsupported") {
    val orcUnion = TypeDescription.createUnion()
    val thrown = intercept[IllegalArgumentException] {
      OrcConverterFactory(orcUnion)
    }
    assert(thrown.getMessage === "Found orc unsupported type, 'UNION'.")
  }

  test("reads Decimal value as java.math.decimal") {
    val schema =
      TypeDescription.createStruct().addField("col_decimal", TypeDescription.createDecimal())
    val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(schema))
    val batch = schema.createRowBatch()
    batch.size = 2

    val decimalVector = batch.cols(0).asInstanceOf[DecimalColumnVector]
    decimalVector.noNulls = false
    decimalVector.vector(0).set(HiveDecimal.create("173.433"))
    decimalVector.isNull(1) = true
    writer.addRowBatch(batch)
    writer.close()

    val src = OrcSource(path, conf, fileSystem)
    val rows = src.stream().toList
    assert(rows(0).get(0).isInstanceOf[java.math.BigDecimal])
    assert(rows(0).getAs[java.math.BigDecimal](0).doubleValue() === 173.433)
    assert(rows(1).isNullAt(0) === true)
  }

}
