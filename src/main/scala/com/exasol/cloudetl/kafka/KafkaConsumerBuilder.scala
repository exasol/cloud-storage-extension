package com.exasol.cloudetl.kafka

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

/**
 * A companion object to the
 * [[org.apache.kafka.clients.consumer.KafkaConsumer]] class.
 */
object KafkaConsumerBuilder {

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def apply(params: Map[String, String]): KafkaConsumer[String, GenericRecord] = {
    val configs = KafkaConsumerProperties.fromImportParameters[GenericRecord](params)
    new KafkaConsumer[String, GenericRecord](
      configs.getProperties(),
      configs.keyDeserializerOpt.orNull,
      configs.valueDeserializerOpt.orNull
    )
  }

}
