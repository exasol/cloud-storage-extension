package com.exasol.cloudetl.kafka

import scala.collection.JavaConverters._
import scala.collection.mutable.{Map => MMap}

import com.exasol.cloudetl.bucket.Bucket

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer

/**
 * A companion object to the [[KafkaConsumerProperties]] class.
 *
 * It provides helper functions such as {@code apply} and {@code create}
 * for convenient properties construction.
 */
@SuppressWarnings(Array("org.wartremover.warts.Overloading", "org.wartremover.warts.Null"))
object KafkaConsumerProperties {

  /**
   * Creates Kafka consumer properties with optional key and value
   * deserializers.
   */
  def apply[K, V](
    properties: Map[String, String],
    keyDeserializer: Option[Deserializer[K]],
    valueDeserializer: Option[Deserializer[V]]
  ): KafkaConsumerProperties[K, V] = {
    require(
      keyDeserializer != null &&
        (keyDeserializer.isDefined || properties
          .contains(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)),
      "Key deserializer should be defined or specified in properties!"
    )
    require(
      valueDeserializer != null &&
        (valueDeserializer.isDefined || properties
          .contains(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)),
      "Value deserializer should be defined or specified in properties!"
    )
    new KafkaConsumerProperties[K, V](properties, keyDeserializer, valueDeserializer)
  }

  /**
   * Creates Kafka consumer properties with explicitly provided key and
   * value deserializers.
   */
  def apply[K, V](
    properties: Map[String, String],
    keyDeserializer: Deserializer[K],
    valueDeserializer: Deserializer[V]
  ): KafkaConsumerProperties[K, V] =
    apply(properties, Option(keyDeserializer), Option(valueDeserializer))

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.AsInstanceOf",
      "org.wartremover.warts.MutableDataStructures",
      "org.wartremover.warts.NonUnitStatements"
    )
  )
  def fromImportParameters[V](
    importParams: Map[String, String]
  ): KafkaConsumerProperties[String, V] = {
    val params = MMap.empty[String, String]
    params.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    params.put(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
      Bucket.requiredParam(importParams, "BROKER_ADDRESS")
    )
    params.put(ConsumerConfig.GROUP_ID_CONFIG, Bucket.requiredParam(importParams, "GROUP_ID"))
    val schemaRegistryUrl = Bucket.requiredParam(importParams, "SCHEMA_REGISTRY_URL")
    params.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
    params.put(
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
      Bucket.optionalParameter(importParams, "MAX_POLL_RECORDS", "500")
    )
    params.put(
      ConsumerConfig.FETCH_MIN_BYTES_CONFIG,
      Bucket.optionalParameter(importParams, "FETCH_MIN_BYTES", "1")
    )
    val sslEnabled = Bucket.optionalParameter(importParams, "SSL_ENABLED", "false")
    if (sslEnabled.equals("true")) {
      params.put(
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
        Bucket.requiredParam(importParams, "SECURITY_PROTOCOL")
      )
      params.put(
        SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
        Bucket.requiredParam(importParams, "SSL_KEYSTORE_LOCATION")
      )
      params.put(
        SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
        Bucket.requiredParam(importParams, "SSL_KEYSTORE_PASSWORD")
      )
      params.put(
        SslConfigs.SSL_KEY_PASSWORD_CONFIG,
        Bucket.requiredParam(importParams, "SSL_KEY_PASSWORD")
      )
      params.put(
        SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
        Bucket.requiredParam(importParams, "SSL_TRUSTSTORE_LOCATION")
      )
      params.put(
        SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
        Bucket.requiredParam(importParams, "SSL_TRUSTSTORE_PASSWORD")
      )
      val idAlgo = Bucket.optionalParameter(
        importParams,
        "SSL_ENDPOINT_IDENTIFICATION_ALGORITHM",
        SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM
      )
      params.put(
        SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG,
        if (idAlgo == "none") "" else idAlgo
      )
    }

    KafkaConsumerProperties(
      params.toMap,
      new StringDeserializer,
      createAvroDeserializer(schemaRegistryUrl).asInstanceOf[Deserializer[V]]
    )

  }

  def createAvroDeserializer(schemaRegistryUrl: String): KafkaAvroDeserializer = {
    // The schema registry url should be provided here since the one
    // configured in consumer properties is not for the deserializer.
    val deserializerConfig = Map(
      AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrl
    )
    val kafkaAvroDeserializer = new KafkaAvroDeserializer
    kafkaAvroDeserializer.configure(deserializerConfig.asJava, false)
    kafkaAvroDeserializer
  }

}

/**
 * A properties holder class for Kafka consumers.
 *
 * It is parameterized on key/value deserializer types.
 */
class KafkaConsumerProperties[K, V](
  val properties: Map[String, String],
  val keyDeserializerOpt: Option[Deserializer[K]],
  val valueDeserializerOpt: Option[Deserializer[V]]
) {

  /**
   * A comma-separated collection of host/port pairs in order to connect
   * to Kafka brokers.
   */
  final def withBootstrapServers(bootstrapServers: String): KafkaConsumerProperties[K, V] =
    withProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

  /**
   * A unique identifier the consumer group this consumer belongs.
   */
  final def withGroupId(groupId: String): KafkaConsumerProperties[K, V] =
    withProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId)

  /**
   * A schema registry url this consumer can use.
   */
  final def withSchemaRegistryUrl(schemaRegistryUrl: String): KafkaConsumerProperties[K, V] =
    withProperty(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)

  /**
   * Sets or updates key/value Kafka consumer property.
   */
  final def withProperty(key: String, value: String): KafkaConsumerProperties[K, V] =
    copy(properties = properties.updated(key, value))

  /**
   * Returns the Kafka consumer properties as Java map.
   */
  final def getProperties(): java.util.Map[String, AnyRef] =
    properties.asInstanceOf[Map[String, AnyRef]].asJava

  @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
  private[this] def copy(
    properties: Map[String, String],
    keyDeserializer: Option[Deserializer[K]] = keyDeserializerOpt,
    valueDeserializer: Option[Deserializer[V]] = valueDeserializerOpt
  ): KafkaConsumerProperties[K, V] =
    new KafkaConsumerProperties[K, V](
      properties,
      keyDeserializer,
      valueDeserializer
    )
}
