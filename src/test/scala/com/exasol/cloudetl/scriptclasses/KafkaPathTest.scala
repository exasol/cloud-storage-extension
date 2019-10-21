package com.exasol.cloudetl.scriptclasses

import scala.collection.JavaConverters._

import com.exasol.cloudetl.kafka.KafkaConsumerProperties

import org.mockito.Mockito._

class KafkaPathTest extends PathTest {

  private[this] val kafkaConsumerProperties = Map(
    "BOOTSTRAP_SERVERS" -> "kafka.broker01.example.com:9092",
    "TOPICS" -> "kafkaTopic",
    "TABLE_NAME" -> "exasolTable"
  )

  override final def beforeEach(): Unit = {
    super.beforeEach()
    properties = kafkaConsumerProperties
    when(metadata.getScriptSchema()).thenReturn(schema)
    ()
  }

  test("generateSqlForImportSpec returns SQL statement") {
    when(importSpec.getParameters()).thenReturn(properties.asJava)
    val propertyPairs = KafkaConsumerProperties(properties).mkString()

    val expectedSQLStatement =
      s"""SELECT
         |  $schema.KAFKA_IMPORT(
         |    '$propertyPairs', partition_index, max_offset
         |)
         |FROM (
         |  SELECT $schema.KAFKA_METADATA(
         |    '$propertyPairs', kafka_partition, kafka_offset
         |  ) FROM (
         |    SELECT kafka_partition, MAX(kafka_offset) AS kafka_offset
         |    FROM exasolTable
         |    GROUP BY kafka_partition
         |    UNION ALL
         |    SELECT 0, -1
         |    FROM DUAL
         |    WHERE NOT EXISTS (SELECT * FROM exasolTable LIMIT 2)
         |  )
         |)
         |GROUP BY
         |  partition_index;
         |""".stripMargin

    assert(KafkaPath.generateSqlForImportSpec(metadata, importSpec) === expectedSQLStatement)
    verify(metadata, atLeastOnce).getScriptSchema
    verify(importSpec, times(1)).getParameters
  }

  test("generateSqlForImportSpec throws if table name property is not set") {
    properties -= ("TABLE_NAME")
    when(importSpec.getParameters()).thenReturn(properties.asJava)
    val thrown = intercept[IllegalArgumentException] {
      KafkaPath.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage === "Please provide a value for the TABLE_NAME property!")
  }

  test("generateSqlForImportSpec throws if topics property is not set") {
    properties -= "TOPICS"
    when(importSpec.getParameters()).thenReturn(properties.asJava)
    val thrown = intercept[IllegalArgumentException] {
      KafkaPath.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage === "Please provide a value for the TOPICS property!")
  }

  test("generateSqlForImportSpec throws if topics contains more than one topic") {
    properties += ("TOPICS" -> "topic1,topic2,topic3")
    when(importSpec.getParameters()).thenReturn(properties.asJava)
    val thrown = intercept[IllegalArgumentException] {
      KafkaPath.generateSqlForImportSpec(metadata, importSpec)
    }
    assert(thrown.getMessage === "Only single topic can be consumed using Kafka import!")
  }

}
