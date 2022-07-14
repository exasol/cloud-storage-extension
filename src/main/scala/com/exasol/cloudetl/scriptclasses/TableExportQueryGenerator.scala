package com.exasol.cloudetl.scriptclasses

import scala.jdk.CollectionConverters._

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.cloudetl.parallelism.FixedUdfCountCalculator
import com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator
import com.exasol.errorreporting.ExaError

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
    val parallelism = getParallelism(metadata, storageProperties)
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

  private[this] def getParallelism(metadata: ExaMetadata, storageProperties: StorageProperties): String =
    storageProperties
      .getParallelism()
      .fold {
        val multiplier = getCoresPerNode(metadata, storageProperties)
        String.format("iproc(), mod(rownum,%d)", multiplier)
      }(identity)

  private[this] def getCoresPerNode(metadata: ExaMetadata, storageProperties: StorageProperties): Int = {
    val coresPerNode = Runtime.getRuntime().availableProcessors()
    math.min(coresPerNode, getCoresLimitedByMemory(metadata, storageProperties))
  }

  private[this] def getCoresLimitedByMemory(metadata: ExaMetadata, storageProperties: StorageProperties): Int =
    if (storageProperties.hasUdfMemory()) {
      MemoryUdfCountCalculator(metadata, storageProperties.getUdfMemory()).getNumberOfCoresLimitedByMemoryPerNode()
    } else {
      FixedUdfCountCalculator(metadata).getNumberOfCoresLimitedByMemoryPerNode()
    }

  /** Returns source column names with quotes removed. */
  private[this] def getSourceColumns(spec: ExaExportSpecification): Seq[String] =
    spec.getSourceColumnNames().asScala.map(getColumnName(_)).toSeq

  /**
   * Given a table name dot column name syntax (myTable.colInt), return
   * the column name.
   */
  private[this] def getColumnName(columnName: String): String = columnName.split("\\.") match {
    case Array(column)            => column
    case Array(table @ _, column) => column
    case _ =>
      throw new TableExporterException(
        ExaError
          .messageBuilder("E-CSE-17")
          .message("Could not parse the column name from given column syntax {{COLUMN}}.")
          .parameter("COLUMN", columnName)
          .toString()
      )
  }

}
