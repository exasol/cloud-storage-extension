package com.exasol.cloudetl.scriptclasses

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap
import scala.util.{Failure, Success, Try}

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.kafka.KafkaConsumerProperties

import com.typesafe.scalalogging.LazyLogging

object KafkaMetadata extends LazyLogging {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val kafkaProperties = KafkaConsumerProperties(iterator.getString(0))

    val idOffsetPairs: HashMap[JInt, JLong] = HashMap.empty[JInt, JLong]
    do {
      val partitionId = iterator.getInteger(1)
      val partitionOffset = iterator.getLong(2)
      idOffsetPairs += (partitionId -> partitionOffset)
    } while (iterator.next())

    val kafkaConsumerTry = Try(kafkaProperties.build(metadata))
    kafkaConsumerTry match {

      case Failure(ex) =>
        logger.error("Unable to create consumer", ex)
        throw ex

      case Success(kafkaConsumer) =>
        val topics = kafkaProperties.getTopics()
        val topicPartitions =
          kafkaConsumer.partitionsFor(topics).asScala.toList.map(_.partition())

        try {
          topicPartitions.foreach { partitionId =>
            val offset: JLong = idOffsetPairs.getOrElse(partitionId, -1)
            iterator.emit(Integer.valueOf(partitionId), offset)
          }
        } finally {
          kafkaConsumer.close()
        }
    }

  }

}
