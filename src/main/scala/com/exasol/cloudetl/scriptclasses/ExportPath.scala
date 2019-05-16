package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket._

object ExportPath {

  def generateSqlForExportSpec(exaMeta: ExaMetadata, exaSpec: ExaExportSpecification): String = {
    val params = exaSpec.getParameters.asScala.toMap
    val bucket = Bucket(params)

    bucket.validate()

    val bucketPath = bucket.bucketPath
    val parallelism = Bucket.optionalParameter(params, "PARALLELISM", "iproc()")
    val rest = Bucket.keyValueMapToString(params)

    val scriptSchema = exaMeta.getScriptSchema

    val srcColumns = getSourceColumns(exaSpec)
    val srcColumnsParam = srcColumns.mkString(".")

    s"""SELECT
       |  $scriptSchema.EXPORT_TABLE(
       |    '$bucketPath', '$rest', '$srcColumnsParam', ${srcColumns.mkString(", ")}
       |)
       |FROM
       |  DUAL
       |GROUP BY
       |  $parallelism;
       |""".stripMargin
  }

  /** Returns source column names with quotes removed. */
  private[this] def getSourceColumns(spec: ExaExportSpecification): Seq[String] =
    spec.getSourceColumnNames.asScala
      .map {
        case value =>
          getColumnName(value).replaceAll("\"", "")
      }

  /**
   * Given a table name dot column name syntax (myTable.colInt), return
   * the column name.
   */
  private[this] def getColumnName(str: String): String = str.split("\\.") match {
    case Array(colName)              => colName
    case Array(tblName @ _, colName) => colName
    case _ =>
      throw new RuntimeException(s"Could not parse the column name from '$str'!")
  }

}
