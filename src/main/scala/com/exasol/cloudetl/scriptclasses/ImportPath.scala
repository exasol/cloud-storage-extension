package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

object ImportPath {

<<<<<<< HEAD
  def generateSqlForImportSpec(
    metadata: ExaMetadata,
    importSpec: ExaImportSpecification
  ): String = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    val storageProperties = StorageProperties(importSpec.getParameters.asScala.toMap)
    val bucket = Bucket(storageProperties)
    bucket.validate()

    val bucketPath = bucket.bucketPath
    val parallelism = storageProperties.getParallelism("nproc()")
    val storagePropertiesStr = storageProperties.mkString()
    val scriptSchema = metadata.getScriptSchema

    s"""SELECT
       |  $scriptSchema.IMPORT_FILES(
       |    '$bucketPath', '$storagePropertiesStr', filename
       |)
       |FROM (
       |  SELECT $scriptSchema.IMPORT_METADATA(
       |    '$bucketPath', '$storagePropertiesStr', $parallelism
       |  )
       |)
       |GROUP BY
       |  partition_index;
       |""".stripMargin
  }

}
