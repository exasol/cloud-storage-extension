package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata

import org.mockito.Mockito._

class ExportPathSuite extends BaseSuite {

  test("`generateSqlForExportSpec` should create a sql statement") {
    val exaMeta = mock[ExaMetadata]
    val exaSpec = mock[ExaExportSpecification]

    when(exaMeta.getScriptSchema()).thenReturn(testSchema)
    when(exaSpec.getParameters()).thenReturn(params.asJava)

    val srcCols = Seq("tbl.col_int", "c_bool", "c_char")
    when(exaSpec.getSourceColumnNames).thenReturn(srcCols.asJava)

    val sqlExpected =
      s"""SELECT
         |  $testSchema.EXPORT_TABLE(
         |    '$s3BucketPath', '$rest', 'col_int.c_bool.c_char', col_int, c_bool, c_char
         |)
         |FROM
         |  DUAL
         |GROUP BY
         |  iproc();
         |""".stripMargin

    assert(ExportPath.generateSqlForExportSpec(exaMeta, exaSpec) === sqlExpected)
    verify(exaMeta, atLeastOnce).getScriptSchema
    verify(exaSpec, times(1)).getParameters
    verify(exaSpec, times(1)).getSourceColumnNames
  }

  test("`generateSqlForExportSpec` should throw an exception if any required param is missing") {
    val exaMeta = mock[ExaMetadata]
    val exaSpec = mock[ExaExportSpecification]

    val newParams = params - ("S3_ACCESS_KEY")

    when(exaMeta.getScriptSchema()).thenReturn(testSchema)
    when(exaSpec.getParameters()).thenReturn(newParams.asJava)

    val thrown = intercept[IllegalArgumentException] {
      ExportPath.generateSqlForExportSpec(exaMeta, exaSpec)
    }

    assert(thrown.getMessage === "The required parameter S3_ACCESS_KEY is not defined!")
    verify(exaSpec, times(1)).getParameters
    verify(exaSpec, never).getSourceColumnNames
  }

  test("`generateSqlForExportSpec` throws if column cannot be parsed (contains extra '.')") {
    val exaMeta = mock[ExaMetadata]
    val exaSpec = mock[ExaExportSpecification]

    when(exaMeta.getScriptSchema()).thenReturn(testSchema)
    when(exaSpec.getParameters()).thenReturn(params.asJava)

    val srcCols = Seq("tbl.c_int.integer")
    when(exaSpec.getSourceColumnNames).thenReturn(srcCols.asJava)

    val thrown = intercept[RuntimeException] {
      ExportPath.generateSqlForExportSpec(exaMeta, exaSpec)
    }

    assert(thrown.getMessage === "Could not parse the column name from 'tbl.c_int.integer'!")
    verify(exaMeta, atLeastOnce).getScriptSchema
    verify(exaSpec, times(1)).getParameters
    verify(exaSpec, times(1)).getSourceColumnNames
  }

}
