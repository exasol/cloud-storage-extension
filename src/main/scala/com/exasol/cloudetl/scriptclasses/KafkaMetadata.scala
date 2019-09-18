package com.exasol.cloudetl.scriptclasses

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.kafka.Consumer

import com.typesafe.scalalogging.LazyLogging

object KafkaMetadata extends LazyLogging {

  @SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
  def run(meta: ExaMetadata, iter: ExaIterator): Unit = {
    val rest = iter.getString(0)
    val params = Bucket.keyValueStringToMap(rest)

    val idOffsetPairs: HashMap[JInt, JLong] = HashMap.empty[JInt, JLong]
    do {
      val partitionId = iter.getInteger(1)
      val partitionOffset = iter.getLong(2)
      idOffsetPairs += (partitionId -> partitionOffset)
    } while (iter.next())

    val brokers = Bucket.requiredParam(params, "BROKER_ADDRESS")
    val groupId = Bucket.requiredParam(params, "GROUP_ID")
    val schemaRegistryUrl = Bucket.requiredParam(params, "SCHEMA_REGISTRY_URL")
    val topics = Bucket.requiredParam(params, "TOPICS")

    val kafkaConsumer = Consumer(brokers, groupId, schemaRegistryUrl)
    try {
      val topicPartitions = kafkaConsumer.partitionsFor(topics).asScala.toList.map(_.partition())
      topicPartitions.foreach { partitionId =>
        val offset: JLong = idOffsetPairs.getOrElse(partitionId, -1)
        iter.emit(new Integer(partitionId), offset)
      }
    } finally {
      kafkaConsumer.close()
    }
  }

}
