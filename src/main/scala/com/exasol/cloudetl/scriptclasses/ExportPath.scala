package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

object ExportPath {

  def generateSqlForExportSpec(
    metadata: ExaMetadata,
    exportSpec: ExaExportSpecification
  ): String = {
    val storageProperties = StorageProperties(exportSpec.getParameters.asScala.toMap)
    val bucket = Bucket(storageProperties)
    bucket.validate()

    val bucketPath = bucket.bucketPath
    val parallelism = storageProperties.getParallelism("iproc()")
    val storagePropertiesStr = storageProperties.mkString()
    val scriptSchema = metadata.getScriptSchema

    val srcColumns = getSourceColumns(exportSpec)
    val srcColumnsStr = srcColumns.mkString(".")

    s"""SELECT
       |  $scriptSchema.EXPORT_TABLE(
       |    '$bucketPath', '$storagePropertiesStr', '$srcColumnsStr', ${srcColumns.mkString(", ")}
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
