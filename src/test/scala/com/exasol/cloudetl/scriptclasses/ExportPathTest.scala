package com.exasol.cloudetl.scriptclasses

import java.nio.file.Files

import scala.collection.JavaConverters._

import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.Mockito._

class ExportPathTest extends PathTest {

  test("generateSqlForExportSpec returns SQL statement") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(properties.asJava)
    val srcCols = Seq("tbl.col_int", "c_bool", "c_char")
    when(exportSpec.getSourceColumnNames).thenReturn(srcCols.asJava)

    val storageProperties = StorageProperties(properties)
    val bucketPath = storageProperties.getStoragePath()
    val stringPairs = storageProperties.mkString()

    val expectedSQLStatement =
      s"""SELECT
         |  $schema.EXPORT_TABLE(
         |    '$bucketPath', '$stringPairs', 'col_int.c_bool.c_char', col_int, c_bool, c_char
         |)
         |FROM
         |  DUAL
         |GROUP BY
         |  iproc();
         |""".stripMargin

    assert(ExportPath.generateSqlForExportSpec(metadata, exportSpec) === expectedSQLStatement)
    verify(metadata, atLeastOnce).getScriptSchema
    verify(exportSpec, times(1)).getParameters
    verify(exportSpec, times(1)).getSourceColumnNames
  }

  test("generateSqlForExportSpec throws if required property is not set") {
    val newProperties = properties - ("S3_ENDPOINT")
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(newProperties.asJava)

    val thrown = intercept[IllegalArgumentException] {
      ExportPath.generateSqlForExportSpec(metadata, exportSpec)
    }
    assert(thrown.getMessage === "Please provide a value for the S3_ENDPOINT property!")
    verify(exportSpec, times(1)).getParameters
    verify(exportSpec, never).getSourceColumnNames
  }

  test("generateSqlForExportSpec throws if columns cannot be parsed (e.g, contains extra '.')") {
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(properties.asJava)
    val srcCols = Seq("tbl.c_int.integer")
    when(exportSpec.getSourceColumnNames).thenReturn(srcCols.asJava)

    val thrown = intercept[RuntimeException] {
      ExportPath.generateSqlForExportSpec(metadata, exportSpec)
    }
    assert(thrown.getMessage === "Could not parse the column name from 'tbl.c_int.integer'!")
    verify(metadata, atLeastOnce).getScriptSchema
    verify(exportSpec, times(1)).getParameters
    verify(exportSpec, times(1)).getSourceColumnNames
  }

  private[this] def createDummyFiles(path: java.nio.file.Path): Seq[java.nio.file.Path] = {
    val files = Seq("a.parquet", "b.parquet", "c.parquet").map(path.resolve(_))
    files.foreach(Files.createFile(_))
    files
  }

  test("generateSqlForExportSpec keeps the path files if 'overwrite' parameter is not set") {
    val bucketPath = Files.createTempDirectory("bucketPath")
    val files = createDummyFiles(bucketPath)
    val newProperties = properties ++ Map(
      "BUCKET_PATH" -> s"file://${bucketPath.toUri.getRawPath}"
    )
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(newProperties.asJava)
    ExportPath.generateSqlForExportSpec(metadata, exportSpec)
    assert(Files.exists(bucketPath) === true)
    assert(Files.list(bucketPath).findAny().isPresent() === true)
    assert(Files.list(bucketPath).count() === 3)

    files.foreach(Files.deleteIfExists(_))
    Files.delete(bucketPath)
  }

  test("generateSqlForExportSpec deletes the path files if 'overwrite' parameter is set") {
    val bucketPath = Files.createTempDirectory("bucketPath")
    createDummyFiles(bucketPath)
    val newProperties = properties ++ Map(
      "BUCKET_PATH" -> s"file://${bucketPath.toUri.getRawPath}",
      "OVERWRITE" -> "true"
    )
    when(metadata.getScriptSchema()).thenReturn(schema)
    when(exportSpec.getParameters()).thenReturn(newProperties.asJava)
    ExportPath.generateSqlForExportSpec(metadata, exportSpec)
    assert(Files.exists(bucketPath) === false)
  }

}
