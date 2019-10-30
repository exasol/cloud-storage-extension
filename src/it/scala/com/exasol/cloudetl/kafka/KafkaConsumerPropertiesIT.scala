package com.exasol.cloudetl.kafka

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class KafkaConsumerPropertiesIT extends KafkaIntegrationTest {

  test("build returns a KafkaConsumer[String, GenericRecord]") {
    val kafkaConsumer = KafkaConsumerProperties(properties).build()
    assert(kafkaConsumer.isInstanceOf[KafkaConsumer[String, GenericRecord]])
  }

}
