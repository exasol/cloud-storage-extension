package com.exasol.cloudetl.scriptclasses

import scala.jdk.CollectionConverters._

import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.Mockito._

class FilesImportQueryGeneratorTest extends PathTest {

  test("generateSqlForImportSpec returns SQL statement") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(importSpec.getParameters()).thenReturn(properties.asJava)

    val storageProperties = StorageProperties(properties)
    val bucketPath = storageProperties.getStoragePath()
    val storagePropertyPairs = storageProperties.mkString()

    val expectedSQLStatement =
      s"""|SELECT
          |  $schema.IMPORT_FILES(
          |    '$bucketPath', '$storagePropertyPairs', filename, start_index, end_index
          |)
          |FROM (
          |  SELECT $schema.IMPORT_METADATA(
          |    '$bucketPath', '$storagePropertyPairs', 65536
          |  )
          |)
          |GROUP BY
          |  partition_index;
          |""".stripMargin

    assert(FilesImportQueryGenerator.generateSqlForImportSpec(metadata, importSpec) === expectedSQLStatement)
    verify(metadata, atLeastOnce).getScriptSchema()
    verify(importSpec, times(1)).getParameters()
  }

  test("generateSqlForImportSpec throws if required property is not set") {
    val newProperties = properties - "S3_ENDPOINT"
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(importSpec.getParameters()).thenReturn(newProperties.asJava)

    val thrown = intercept[IllegalArgumentException] {
      FilesImportQueryGenerator.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage().startsWith("E-CSE-2"))
    assert(thrown.getMessage().contains("'S3_ENDPOINT' property value is missing."))
    verify(importSpec, times(1)).getParameters()
  }

}
