package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import org.mockito.Mockito._

class ExportPathTest extends PathTest {

  test("generateSqlForExportSpec throws with message to the new scriptclass name") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(properties.asJava)
    val thrown = intercept[IllegalArgumentException] {
      ExportPath.generateSqlForExportSpec(metadata, exportSpec)
    }
    assert(thrown.getMessage().contains("Please use the ExportTableQueryGenerator"))
  }

}
