package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

/**
 * A SQL query generator class that facilitates the metadata reading and file data importing.
 */
object FilesImportQueryGenerator {

  private[this] val DEFAULT_PARALLELISM = "nproc()"

  /**
   * Generates an Exasol SQL for the data import query.
   *
   * @param metadata an Exasol metadata object
   * @param importSpecification an Exasol import specification object
   */
  def generateSqlForImportSpec(metadata: ExaMetadata, importSpecification: ExaImportSpecification): String = {
    val storageProperties = StorageProperties(importSpecification.getParameters())
    val bucket = Bucket(storageProperties)
    bucket.validate()

    val scriptSchema = metadata.getScriptSchema()
    val bucketPath = bucket.bucketPath
    val parallelism = storageProperties.getParallelism(DEFAULT_PARALLELISM)
    val storagePropertiesAsString = storageProperties.mkString()

    s"""|SELECT
        |  $scriptSchema.IMPORT_FILES(
        |    '$bucketPath', '$storagePropertiesAsString', filename, start_index, end_index
        |)
        |FROM (
        |  SELECT $scriptSchema.IMPORT_METADATA(
        |    '$bucketPath', '$storagePropertiesAsString', $parallelism
        |  )
        |)
        |GROUP BY
        |  partition_index;
        |""".stripMargin
  }

}
