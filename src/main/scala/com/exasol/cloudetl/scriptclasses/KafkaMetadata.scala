package com.exasol.cloudetl.scriptclasses

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap
import scala.util.{Failure, Success, Try}

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.kafka.KafkaConsumerBuilder

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

    val kafkaConsumerTry = Try(KafkaConsumerBuilder(params))
    kafkaConsumerTry match {

      case Failure(ex) =>
        logger.error("Unable to create consumer", ex)
        throw ex

      case Success(kafkaConsumer) =>
        val topics = Bucket.requiredParam(params, "TOPICS")
        val topicPartitions =
          kafkaConsumer.partitionsFor(topics).asScala.toList.map(_.partition())

        try {
          topicPartitions.foreach { partitionId =>
            val offset: JLong = idOffsetPairs.getOrElse(partitionId, -1)
            iter.emit(new Integer(partitionId), offset)
          }
        } finally {
          kafkaConsumer.close()
        }
    }

  }

}
