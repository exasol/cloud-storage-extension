package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.kafka.KafkaConsumerProperties

object KafkaPath {

  def generateSqlForImportSpec(
    metadata: ExaMetadata,
    importSpec: ExaImportSpecification
  ): String = {
    val kafkaProperties = KafkaConsumerProperties(importSpec.getParameters.asScala.toMap)
    val tableName = kafkaProperties.getTableName()
    val topics = kafkaProperties.getTopics()
    if (topics.contains(",")) {
      throw new IllegalArgumentException("Only single topic can be consumed using Kafka import!")
    }
    val kvPairs = kafkaProperties.mkString()
    val scriptSchema = metadata.getScriptSchema

    s"""SELECT
       |  $scriptSchema.KAFKA_IMPORT(
       |    '$kvPairs', partition_index, max_offset
       |)
       |FROM (
       |  SELECT $scriptSchema.KAFKA_METADATA(
       |    '$kvPairs', kafka_partition, kafka_offset
       |  ) FROM (
       |    SELECT kafka_partition, MAX(kafka_offset) AS kafka_offset
       |    FROM $tableName
       |    GROUP BY kafka_partition
       |    UNION ALL
       |    SELECT 0, -1
       |    FROM DUAL
       |    WHERE NOT EXISTS (SELECT * FROM $tableName LIMIT 2)
       |  )
       |)
       |GROUP BY
       |  partition_index;
       |""".stripMargin
  }

}
