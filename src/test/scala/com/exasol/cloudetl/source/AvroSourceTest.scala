package com.exasol.cloudetl.source

import java.nio.file.Paths

class AvroSourceTest extends SourceTest {

  override val format: String = "avro"

  test("stream returns count of records from AVRO files") {
    val filePath = Paths.get(s"$resourceDir/sales1*.avro")
    assert(getRecordsCount(filePath) === 1998)
  }

}
