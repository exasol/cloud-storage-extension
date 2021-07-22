package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.fs.Path

/**
 * A SQL query generator that call the exporter classes.
 */
object TableExportQueryGenerator {

  /**
   * Generates an Exasol SQL for the data export query.
   *
   * @param metadata an Exasol metadata object
   * @param exportSpecification an Exasol export specification object
   */
  def generateSqlForExportSpec(metadata: ExaMetadata, exportSpec: ExaExportSpecification): String = {
    val storageProperties = StorageProperties(exportSpec.getParameters.asScala.toMap, metadata)
    val bucket = Bucket(storageProperties)
    bucket.validate()
    deleteBucketPathIfRequired(bucket)

    val bucketPath = bucket.bucketPath
    val parallelism = storageProperties.getParallelism("iproc()")
    val storagePropertiesStr = storageProperties.mkString()
    val scriptSchema = metadata.getScriptSchema()
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

  private[this] def deleteBucketPathIfRequired(bucket: Bucket): Unit = {
    if (bucket.properties.isOverwrite()) {
      val fileSystem = bucket.fileSystem
      val bucketPath = new Path(bucket.bucketPath)
      if (fileSystem.exists(bucketPath)) {
        val _ = fileSystem.delete(bucketPath, true)
      }
    }
    ()
  }

  /** Returns source column names with quotes removed. */
  private[this] def getSourceColumns(spec: ExaExportSpecification): Seq[String] =
    spec.getSourceColumnNames.asScala.map(getColumnName(_))

  /**
   * Given a table name dot column name syntax (myTable.colInt), return
   * the column name.
   */
  private[this] def getColumnName(columnName: String): String = columnName.split("\\.") match {
    case Array(column)            => column
    case Array(table @ _, column) => column
    case _                        => throw new RuntimeException(s"Could not parse the column name from '$columnName'!")
  }

}
