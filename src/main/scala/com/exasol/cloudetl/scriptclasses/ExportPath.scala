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
    val parallelism = Bucket.optionalParam(params, "PARALLELISM", "nproc()")

    val rest = Bucket.mapToStr(params)

    val scriptSchema = exaMeta.getScriptSchema

    s"""
       |SELECT
       |  $scriptSchema.EXPORT_TABLE('$bucketPath', '$rest')
       |FROM
       |  DUAL
       |GROUP BY
       |  $parallelism;
    """.stripMargin
  }

}
