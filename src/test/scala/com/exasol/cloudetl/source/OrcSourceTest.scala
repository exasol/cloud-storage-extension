package com.exasol.cloudetl.source

import java.nio.file.Paths

class OrcSourceTest extends SourceTest {

  override val format: String = "orc"

  test("stream returns count of records from single ORC file") {
    val filePath = Paths.get(s"$resourceDir/sales*.orc")
    assert(getRecordsCount(filePath) === 1998)
  }

  test("stream returns count of records from ORC files") {
    val filePath = Paths.get(s"$resourceDir/employee*.orc")
    assert(getRecordsCount(filePath) === 438304)
  }

}
