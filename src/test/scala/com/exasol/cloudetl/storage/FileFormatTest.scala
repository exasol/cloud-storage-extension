package com.exasol.cloudetl.storage

import com.exasol.cloudetl.storage.FileFormat._

import org.scalatest.funsuite.AnyFunSuite

class FileFormatTest extends AnyFunSuite {

  test("apply returns supported file formats") {
    val testData = Map(
      "AVro" -> AVRO,
      "delta" -> DELTA,
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
