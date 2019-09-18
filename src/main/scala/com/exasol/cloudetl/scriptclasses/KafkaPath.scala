package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket

object KafkaPath {

  def generateSqlForImportSpec(exaMeta: ExaMetadata, exaSpec: ExaImportSpecification): String = {
    val params = exaSpec.getParameters.asScala.toMap
    val tableName = Bucket.requiredParam(params, "TABLE_NAME")
    val topics = Bucket.requiredParam(params, "TOPICS")
    if (topics.contains(",")) {
      throw new IllegalArgumentException("Only single topic can be consumed using Kafka import!")
    }
    val rest = Bucket.keyValueMapToString(params)
    val scriptSchema = exaMeta.getScriptSchema

    s"""SELECT
       |  $scriptSchema.KAFKA_CONSUME(
       |    '$rest', partition_index, max_offset
       |)
       |FROM (
       |  SELECT $scriptSchema.KAFKA_METADATA(
       |    '$rest', kafka_partition, kafka_offset
       |  ) FROM (
       |    SELECT kafka_partition, MAX(kafka_offset) AS kafka_offset
       |    FROM $tableName
       |    GROUP BY kafka_partition
       |    UNION ALL
       |    SELECT 0, -1
       |    FROM DUAL
       |    WHERE NOT EXISTS (SELECT * FROM $tableName)
       |  )
       |)
       |GROUP BY
       |  partition_index;
       |""".stripMargin
  }

}
