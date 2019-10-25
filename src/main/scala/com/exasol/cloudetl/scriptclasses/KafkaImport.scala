package com.exasol.cloudetl.scriptclasses

import java.time.Duration
import java.util.Arrays

import scala.collection.JavaConverters._

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.kafka.KafkaConsumerProperties

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.TopicPartition

object KafkaImport extends LazyLogging {

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val kafkaProperties = KafkaConsumerProperties(iterator.getString(0))
    val partitionId = iterator.getInteger(1)
    val partitionOffset = iterator.getLong(2)
    val partitionNextOffset = partitionOffset + 1L
    val nodeId = metadata.getNodeId
    val vmId = metadata.getVmId
    logger.info(
      s"Kafka consumer for node=$nodeId, vm=$vmId using " +
        s"partition=$partitionId and startOffset=$partitionNextOffset"
    )

    val topics = kafkaProperties.getTopics()
    val timeout = kafkaProperties.getPollTimeoutMs()
    val maxRecords = kafkaProperties.getMaxRecordsPerRun()
    val minRecords = kafkaProperties.getMinRecordsPerRun()
    val topicPartition = new TopicPartition(topics, partitionId)

    val kafkaConsumer = kafkaProperties.build()
    kafkaConsumer.assign(Arrays.asList(topicPartition))
    kafkaConsumer.seek(topicPartition, partitionNextOffset)

    try {
      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      var recordsCount = 0

      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      var total = 0

      do {
        val records = kafkaConsumer.poll(Duration.ofMillis(timeout))
        recordsCount = records.count()
        total += recordsCount
        records.asScala.foreach { record =>
          logger.debug(
            s"Read partition=${record.partition()} offset=${record.offset()} " +
              s"key=${record.key()} value=${record.value()}"
          )
          val metadata: Seq[Object] =
            Seq(record.partition().asInstanceOf[AnyRef], record.offset().asInstanceOf[AnyRef])
          val row = Row.fromAvroGenericRecord(record.value())
          val allColumns: Seq[Object] = metadata ++ row.getValues().map(_.asInstanceOf[AnyRef])
          iterator.emit(allColumns: _*)
        }
        logger.info(
          s"Emitted total=$total records in node=$nodeId, vm=$vmId, partition=$partitionId"
        )

      } while (recordsCount >= minRecords && total < maxRecords)
    } finally {
      kafkaConsumer.close();
    }
  }

}
