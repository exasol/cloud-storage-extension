package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket._

object ImportPath {

  def generateSqlForImportSpec(exaMeta: ExaMetadata, exaSpec: ExaImportSpecification): String = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    val params = exaSpec.getParameters.asScala.toMap

    val bucket = Bucket(params)

    bucket.validate()

    val bucketPath = bucket.bucketPath
    val parallelism = Bucket.optionalParameter(params, "PARALLELISM", "nproc()")

    val rest = Bucket.keyValueMapToString(params)

    val scriptSchema = exaMeta.getScriptSchema

    s"""SELECT
       |  $scriptSchema.IMPORT_FILES(
       |    '$bucketPath', '$rest', filename
       |)
       |FROM (
       |  SELECT $scriptSchema.IMPORT_METADATA(
       |    '$bucketPath', '$rest', $parallelism
       |  )
       |)
       |GROUP BY
       |  partition_index;
       |""".stripMargin
  }

}
