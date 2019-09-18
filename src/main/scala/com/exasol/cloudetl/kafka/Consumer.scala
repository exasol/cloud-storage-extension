package com.exasol.cloudetl.kafka

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

object Consumer {

  def apply(brokers: String, groupId: String): KafkaConsumer[String, String] = {
    val configs = getConfiguration(brokers, groupId)
    new KafkaConsumer[String, String](configs)
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  private[this] def getConfiguration(brokers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      classOf[StringDeserializer].getCanonicalName
    )
    props.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      classOf[StringDeserializer].getCanonicalName
    )
    props
  }
}
