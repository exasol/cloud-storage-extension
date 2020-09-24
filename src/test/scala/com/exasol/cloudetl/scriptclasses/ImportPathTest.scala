package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.Mockito._

class ImportPathTest extends PathTest {

  test("generateSqlForImportSpec returns SQL statement") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(importSpec.getParameters()).thenReturn(properties.asJava)

    val storageProperties = StorageProperties(properties)
    val bucketPath = storageProperties.getStoragePath()
    val storagePropertyPairs = storageProperties.mkString()

    val expectedSQLStatement =
      s"""SELECT
         |  $schema.IMPORT_FILES(
         |    '$bucketPath', '$storagePropertyPairs', filename
         |)
         |FROM (
         |  SELECT $schema.IMPORT_METADATA(
         |    '$bucketPath', '$storagePropertyPairs', nproc()
         |  )
         |)
         |GROUP BY
         |  partition_index;
         |""".stripMargin

    assert(ImportPath.generateSqlForImportSpec(metadata, importSpec) === expectedSQLStatement)
    verify(metadata, atLeastOnce).getScriptSchema
    verify(importSpec, times(1)).getParameters
  }

  test("generateSqlForImportSpec throws if required property is not set") {
    val newProperties = properties - ("S3_ENDPOINT")
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(importSpec.getParameters()).thenReturn(newProperties.asJava)

    val thrown = intercept[IllegalArgumentException] {
      ImportPath.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage === "Please provide a value for the S3_ENDPOINT property!")
    verify(importSpec, times(1)).getParameters
  }

}
