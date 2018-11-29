package com.exasol.cloudetl.scriptclasses

import java.util.{Map => JMap}

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata

object ImportS3Path {

  def generateSqlForImportSpec(exaMeta: ExaMetadata, exaSpec: ExaImportSpecification): String = {
    val params = exaSpec.getParameters
    val s3Bucket = requiredParam(params, "S3_BUCKET_PATH")
    val s3Endpoint = requiredParam(params, "S3_ENDPOINT")
    val s3AccessKey = requiredParam(params, "S3_ACCESS_KEY")
    val s3SecretKey = requiredParam(params, "S3_SECRET_KEY")
    val parallelism = optionalParam(params, "PARALLELISM", "nproc()")

    val scriptSchema = exaMeta.getScriptSchema

    s"""
       |SELECT
       |  $scriptSchema.IMPORT_S3_FILES(
       |    '$s3Bucket', '$s3Endpoint', '$s3AccessKey', '$s3SecretKey', s3_filename
       |)
       |FROM (
       |  SELECT $scriptSchema.IMPORT_S3_METADATA(
       |    '$s3Bucket', '$s3Endpoint', '$s3AccessKey', '$s3SecretKey', $parallelism
       |  )
       |)
       |GROUP BY
       |  partition_index;
    """.stripMargin
  }

  private[this] def requiredParam(params: JMap[String, String], key: String): String =
    if (!params.containsKey(key)) {
      throw new IllegalArgumentException(s"The required parameter $key is not defined!")
    } else {
      params.get(key)
    }

  private[this] def optionalParam(
    params: JMap[String, String],
    key: String,
    defaultValue: String
  ): String =
    if (!params.containsKey(key)) {
      defaultValue
    } else {
      params.get(key)
    }

}
