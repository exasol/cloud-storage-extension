package com.exasol.cloudetl.scriptclasses

import java.time.Duration
import java.util.Arrays

import scala.collection.JavaConverters._

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.kafka.KafkaConsumerBuilder

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.TopicPartition

object KafkaImport extends LazyLogging {

  private[this] val POLL_TIMEOUT_MS: Int = 30000

  private[this] val MAX_RECORDS_PER_RUN = 1000000

  private[this] val MIN_RECORDS_PER_RUN = 100

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
    val topics = Bucket.requiredParam(params, "TOPICS")
    val timeout = Bucket.optionalIntParameter(params, "POLL_TIMEOUT_MS", POLL_TIMEOUT_MS)
    val maxRecords =
      Bucket.optionalIntParameter(params, "MAX_RECORDS_PER_RUN", MAX_RECORDS_PER_RUN)
    val minRecords =
      Bucket.optionalIntParameter(params, "MIN_RECORDS_PER_RUN", MIN_RECORDS_PER_RUN)
    val topicPartition = new TopicPartition(topics, partitionId)

    val kafkaConsumer = KafkaConsumerBuilder(params)
    kafkaConsumer.assign(Arrays.asList(topicPartition))
    kafkaConsumer.seek(topicPartition, partitionNextOffset)

    try {
      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      var recordsCount = 0

      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      var total = 0

      do {
        val records = kafkaConsumer.poll(Duration.ofMillis(timeout.toLong))
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
          ctx.emit(
            allColumns: _*
          )
        }
        logger.info(
          s"Emitted total=$recordsCount records in node=$nodeId, vm=$vmId, partition=$partitionId"
        )

      } while (recordsCount >= minRecords && total < maxRecords)
    } finally {
      kafkaConsumer.close();
    }
  }

}
