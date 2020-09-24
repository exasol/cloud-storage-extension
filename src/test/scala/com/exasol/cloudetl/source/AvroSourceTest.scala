package com.exasol.cloudetl.source

import java.nio.file.Paths

class AvroSourceTest extends AbstractSourceTest {

  override val format: String = "avro"

  test("stream returns count of records from AVRO files") {
    val filePath = Paths.get(s"$resourceDir/sales1*.avro")
    assert(getRecordsCount(filePath) === 1998)
  }

  test("stream throws if it cannot create AVRO reader") {
    val nonPath = new org.apache.hadoop.fs.Path(s"$resourceDir/notFile.avro")
    val thrown = intercept[java.io.FileNotFoundException] {
      getSource(nonPath).stream().size
    }
    assert(thrown.getMessage === s"File $nonPath does not exist")
  }

}
