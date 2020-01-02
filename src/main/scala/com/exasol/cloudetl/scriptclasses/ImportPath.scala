package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

object ImportPath {

  def generateSqlForImportSpec(
    metadata: ExaMetadata,
    importSpec: ExaImportSpecification
  ): String = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    val storageProperties = StorageProperties(importSpec.getParameters.asScala.toMap)
    val bucket = Bucket(storageProperties)
    bucket.validate()

    val scriptSchema = metadata.getScriptSchema
    val bucketPath = bucket.bucketPath
    val parallelism = storageProperties.getParallelism("nproc()")
    val storagePropertiesAsString = storageProperties.mkString()

    s"""SELECT
       |  $scriptSchema.IMPORT_FILES(
       |    '$bucketPath', '$storagePropertiesAsString', filename
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
