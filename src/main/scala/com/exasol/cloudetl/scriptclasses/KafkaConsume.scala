package com.exasol.cloudetl.scriptclasses

import java.time.Duration
import java.util.Arrays

import scala.collection.JavaConverters._

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.kafka.Consumer

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.TopicPartition

object KafkaConsume extends LazyLogging {

  private[this] val POLL_TIMEOUT_MS: Long = 2000L

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def run(meta: ExaMetadata, ctx: ExaIterator): Unit = {
    val rest = ctx.getString(0)
    val partitionId = ctx.getInteger(1)
    val partitionOffset = ctx.getLong(2)
    val partitionNextOffset = partitionOffset + 1L
    val nodeId = meta.getNodeId
    val vmId = meta.getVmId
    logger.info(
      s"Kafka consumer for node=$nodeId, vm=$vmId using " +
        s"partition=$partitionId and startOffset=$partitionNextOffset"
    )

    val params = Bucket.keyValueStringToMap(rest)
    val brokers = Bucket.requiredParam(params, "BROKER_ADDRESS")
    val groupId = Bucket.requiredParam(params, "GROUP_ID")
    val schemaRegistryUrl = Bucket.requiredParam(params, "SCHEMA_REGISTRY_URL")
    val topics = Bucket.requiredParam(params, "TOPICS")

    val topicPartition = new TopicPartition(topics, partitionId)
    val kafkaConsumer = Consumer(brokers, groupId, schemaRegistryUrl)
    kafkaConsumer.assign(Arrays.asList(topicPartition))
    kafkaConsumer.seek(topicPartition, partitionNextOffset)

    try {
      val records = kafkaConsumer.poll(Duration.ofMillis(POLL_TIMEOUT_MS))
      records.asScala.foreach { record =>
        logger.debug(
          s"Emitting partition=${record.partition()} offset=${record.offset()} " +
            s"key=${record.key()} value=${record.value()}"
        )
        val metadata: Seq[Object] =
          Seq(record.partition().asInstanceOf[AnyRef], record.offset().asInstanceOf[AnyRef])
        val row = Row.fromAvroGenericRecord(record.value())
        val allColumns: Seq[Object] = metadata ++ row.getValues().map(_.asInstanceOf[AnyRef])
        ctx.emit(
          allColumns: _*
        )
      }
    } finally {
      kafkaConsumer.close();
    }
  }

}
