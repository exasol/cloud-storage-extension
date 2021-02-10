package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import org.mockito.Mockito._

class ImportPathTest extends PathTest {

  test("generateSqlForImportSpec throws with message to the new scriptclass name") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(importSpec.getParameters()).thenReturn(properties.asJava)
    val thrown = intercept[IllegalArgumentException] {
      ImportPath.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage().contains("Please use the FilesImportQueryGenerator"))
  }

}
