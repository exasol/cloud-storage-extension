package com.exasol.cloudetl.storage

import org.scalatest.FunSuite

class FileFormatTest extends FunSuite {

  test("apply returns supported file formats") {
    val testData = Map(
      "AVro" -> AVRO,
      "ORC" -> ORC,
      "fiLE" -> FILE,
      "parquet" -> PARQUET
    )
    testData.foreach {
      case (given, expected) =>
        assert(FileFormat(given) === expected)
    }
  }

  test("apply throws if file format is not supported") {
    val thrown = intercept[IllegalArgumentException] {
      FileFormat("CsV")
    }
    assert(thrown.getMessage === s"Unsupported file format CsV!")
  }

}
