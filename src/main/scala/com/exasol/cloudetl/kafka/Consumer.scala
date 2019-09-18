package com.exasol.cloudetl.kafka

import java.util.Properties

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

object Consumer {

  def apply(
    brokers: String,
    groupId: String,
    schemaRegistryUrl: String
  ): KafkaConsumer[String, GenericRecord] = {
    val configs = getConfiguration(brokers, groupId, schemaRegistryUrl)
    new KafkaConsumer[String, GenericRecord](configs)
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  private[this] def getConfiguration(
    brokers: String,
    groupId: String,
    schemaRegistryUrl: String
  ): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      classOf[StringDeserializer].getCanonicalName
    )
    props.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      classOf[KafkaAvroDeserializer].getCanonicalName
    )
    props
  }
}
