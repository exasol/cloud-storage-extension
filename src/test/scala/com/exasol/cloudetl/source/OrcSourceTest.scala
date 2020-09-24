package com.exasol.cloudetl.source

import java.nio.file.Paths

class OrcSourceTest extends AbstractSourceTest {

  override val format: String = "orc"

  test("stream returns count of records from single ORC file") {
    val filePath = Paths.get(s"$resourceDir/sales*.orc")
    assert(getRecordsCount(filePath) === 1998)
  }

  test("stream returns count of records from ORC files") {
    val filePath = Paths.get(s"$resourceDir/employee*.orc")
    assert(getRecordsCount(filePath) === 438304)
  }

  test("stream throws if it cannot create ORC reader") {
    val nonPath = new org.apache.hadoop.fs.Path(s"$resourceDir/notFile.orc")
    val thrown = intercept[java.io.FileNotFoundException] {
      getSource(nonPath).stream().size
    }
    assert(thrown.getMessage === s"File $nonPath does not exist")
  }

}
