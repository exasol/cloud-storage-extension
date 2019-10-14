package com.exasol.cloudetl.kafka

import scala.collection.JavaConverters._
import scala.collection.mutable.{Map => MMap}

import com.exasol.cloudetl.common.AbstractProperties

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer

/**
 * A specific implementation of
 * [[com.exasol.cloudetl.common.AbstractProperties]] that handles user
 * provided key-value parameters for import user-defined-functions
 * (udfs) as Kafka consumer application.
 *
 * This class also provides builder methods for Kafka consumers.
 */
class KafkaConsumerProperties(private val properties: Map[String, String])
    extends AbstractProperties(properties) {

  import KafkaConsumerProperties._

  final def getBootstrapServers(): String =
    getAs[String](BOOTSTRAP_SERVERS.userPropertyName)

  final def getGroupId(): String =
    get(GROUP_ID.userPropertyName).fold(GROUP_ID.defaultValue)(identity)

  /** Returns the user provided topic name. */
  final def getTopics(): String =
    getAs[String](TOPICS)

  /** Returns the user provided Exasol table name. */
  final def getTableName(): String =
    getAs[String](TABLE_NAME)

  final def getPollTimeoutMs(): Long =
    get(POLL_TIMEOUT_MS).fold(POLL_TIMEOUT_MS_DEFAULT_VALUE)(_.asInstanceOf[Long])

  final def getMaxRecordsPerRun(): Int =
    get(MAX_RECORDS_PER_RUN).fold(MAX_RECORDS_PER_RUN_DEFAULT_VALUE)(_.asInstanceOf[Int])

  final def getMinRecordsPerRun(): Int =
    get(MIN_RECORDS_PER_RUN).fold(MIN_RECORDS_PER_RUN_DEFAULT_VALUE)(_.asInstanceOf[Int])

  /** Checks if the {@code SSL_ENABLED} property is set. */
  final def isSSLEnabled(): Boolean =
    isEnabled(SSL_ENABLED)

  /** Checks if the Schema Registry URL property is set. */
  final def hasSchemaRegistryUrl(): Boolean =
    containsKey(SCHEMA_REGISTRY_URL.userPropertyName)

  /** Returns the user provided schema registry url property. */
  final def getSchemaRegistryUrl(): String =
    getAs[String](SCHEMA_REGISTRY_URL.userPropertyName)

  /**
   * Returns {@code MAX_POLL_RECORDS} property value if provided,
   * otherwise returns default value.
   */
  final def getMaxPollRecords(): String =
    get(MAX_POLL_RECORDS.userPropertyName).fold(MAX_POLL_RECORDS.defaultValue)(identity)

  /**
   * Returns {@code FETCH_MIN_BYTES} property value if provided,
   * otherwise returns the default value.
   */
  final def getFetchMinBytes(): String =
    get(FETCH_MIN_BYTES.userPropertyName).fold(FETCH_MIN_BYTES.defaultValue)(identity)

  // Secure Connection Related Properties

  /**
   * Returns {@code SECURITY_PROTOCOL} property value if provided,
   * otherwise returns the default value.
   */
  final def getSecurityProtocol(): String =
    get(SECURITY_PROTOCOL.userPropertyName).fold(SECURITY_PROTOCOL.defaultValue)(identity)

  /**
   * Returns the user provided {@code SSL_KEY_PASSWORD} property value.
   */
  final def getSSLKeyPassword(): String =
    getAs[String](SSL_KEY_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_KEYSTORE_PASSWORD} property
   * value.
   */
  final def getSSLKeystorePassword(): String =
    getAs[String](SSL_KEYSTORE_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_KEYSTORE_LOCATION} property
   * value.
   */
  final def getSSLKeystoreLocation(): String =
    getAs[String](SSL_KEYSTORE_LOCATION.userPropertyName)

  /**
   * Returns the user provided {@code SSL_TRUSTSTORE_PASSWORD} property
   * value.
   */
  final def getSSLTruststorePassword(): String =
    getAs[String](SSL_TRUSTSTORE_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_TRUSTSTORE_LOCATION} property
   * value.
   */
  final def getSSLTruststoreLocation(): String =
    getAs[String](SSL_TRUSTSTORE_LOCATION.userPropertyName)

  /**
   * Returns {@code SSL_ENDPOINT_IDENTIFICATION_ALGORITHM} property
   * value if provided, otherwise returns the default value.
   */
  final def getSSLEndpointIdentificationAlgorithm(): String =
    get(SSL_ENDPOINT_IDENTIFICATION_ALGORITHM.userPropertyName)
      .fold(SSL_ENDPOINT_IDENTIFICATION_ALGORITHM.defaultValue)(identity)

  /**
   * Returns a [[org.apache.kafka.clients.consumer.KafkaConsumer]] class.
   *
   * At the moment Avro based specific {@code KafkaConsumer[String,
   * GenericRecord]} consumer is returned. Therefore, in order to define
   * the schem of [[org.apache.avro.generic.GenericRecord]] the {@code
   * SCHEMA_REGISTRY_URL} value should be provided.
   */
  final def build(): KafkaConsumer[String, GenericRecord] =
    KafkaConsumerProperties.createKafkaConsumer(this)

  /** Returns the Kafka consumer properties as Java map. */
  @SuppressWarnings(
    Array(
      "org.wartremover.warts.AsInstanceOf",
      "org.wartremover.warts.MutableDataStructures",
      "org.wartremover.warts.NonUnitStatements"
    )
  )
  final def getProperties(): java.util.Map[String, AnyRef] = {
    val props = MMap.empty[String, String]
    props.put(ENABLE_AUTO_COMMIT.kafkaPropertyName, ENABLE_AUTO_COMMIT.defaultValue)
    props.put(BOOTSTRAP_SERVERS.kafkaPropertyName, getBootstrapServers())
    props.put(GROUP_ID.kafkaPropertyName, getGroupId())
    props.put(SCHEMA_REGISTRY_URL.kafkaPropertyName, getSchemaRegistryUrl())
    props.put(MAX_POLL_RECORDS.kafkaPropertyName, getMaxPollRecords())
    props.put(FETCH_MIN_BYTES.kafkaPropertyName, getFetchMinBytes())
    if (isSSLEnabled()) {
      props.put(SECURITY_PROTOCOL.kafkaPropertyName, getSecurityProtocol())
      props.put(SSL_KEY_PASSWORD.kafkaPropertyName, getSSLKeyPassword())
      props.put(SSL_KEYSTORE_PASSWORD.kafkaPropertyName, getSSLKeystorePassword())
      props.put(SSL_KEYSTORE_LOCATION.kafkaPropertyName, getSSLKeystoreLocation())
      props.put(SSL_TRUSTSTORE_PASSWORD.kafkaPropertyName, getSSLTruststorePassword())
      props.put(SSL_TRUSTSTORE_LOCATION.kafkaPropertyName, getSSLTruststoreLocation())
      props.put(
        SSL_ENDPOINT_IDENTIFICATION_ALGORITHM.kafkaPropertyName,
        getSSLEndpointIdentificationAlgorithm()
      )
    }
    props.toMap.asInstanceOf[Map[String, AnyRef]].asJava
  }

  final def getAs[T](key: String): T =
    get(key).fold {
      throw new IllegalArgumentException(s"Please provide a value for the $key property!")
    }(_.asInstanceOf[T])

  /**
   * Returns a string value of key-value property pairs.
   *
   * The resulting string is sorted by keys ordering.
   */
  @SuppressWarnings(Array("org.wartremover.warts.Overloading"))
  final def mkString(): String =
    mkString(KEY_VALUE_SEPARATOR, PROPERTY_SEPARATOR)

}

/**
 * A companion object for [[KafkaConsumerProperties]] class.
 */
object KafkaConsumerProperties {

  /**
   * A line separator string used for creating key-value property
   * strings.
   */
  private[kafka] final val PROPERTY_SEPARATOR: String = ";"

  /**
   * A default separator string used for concatenate key-value pairs.
   */
  private[kafka] final val KEY_VALUE_SEPARATOR: String = " -> "

  /**
   * A required property key name for a Kafka topic name to import data
   * from.
   */
  private[kafka] final val TOPICS: String = "TOPICS"

  /**
   * A required property key name for a Exasol table name to import data
   * into.
   */
  private[kafka] final val TABLE_NAME: String = "TABLE_NAME"

  private[kafka] final val POLL_TIMEOUT_MS: String = "POLL_TIMEOUT_MS"
  private[kafka] final val POLL_TIMEOUT_MS_DEFAULT_VALUE: Long = 30000L

  private[kafka] final val MAX_RECORDS_PER_RUN: String = "MAX_RECORDS_PER_RUN"
  private[kafka] final val MAX_RECORDS_PER_RUN_DEFAULT_VALUE: Int = 1000000

  private[kafka] final val MIN_RECORDS_PER_RUN: String = "MIN_RECORDS_PER_RUN"
  private[kafka] final val MIN_RECORDS_PER_RUN_DEFAULT_VALUE: Int = 100

  /**
   * An optional property key name to set SSL secure connections to
   * Kafka cluster.
   */
  private[kafka] val SSL_ENABLED: String = "SSL_ENABLED"

  /**
   * Below are relavant Kafka consumer configuration parameters are
   * defined.
   *
   * See [[https://kafka.apache.org/documentation.html#consumerconfigs]]
   */
  /**
   * Internal configuration helper class.
   *
   * @param userPropertyName A UDF user provided property key name
   * @param kafkaPropertyName An equivalent property in Kafka
   *        configuration that maps user property key name
   * @param defaultValue A default value for the property key name
   */
  private[kafka] final case class Config(
    val userPropertyName: String,
    val kafkaPropertyName: String,
    val defaultValue: String
  )

  /**
   * This is the {@code enable.auto.commit} configuration setting.
   *
   * If set to true the offset of consumer will be periodically
   * committed to the Kafka cluster in the background. This is `false`
   * by default, since we manage the offset commits ourselves in the
   * Exasol table.
   */
  private[kafka] val ENABLE_AUTO_COMMIT: Config = Config(
    "ENABLE_AUTO_COMMIT",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
    "false"
  )

  /**
   * This is the `bootstrap.servers` configuration setting.
   *
   * A list of host and port pairs to use for establishing the initial
   * connection to the Kafka cluster.
   *
   * It is a required property that should be provided by the user.
   */
  private[kafka] val BOOTSTRAP_SERVERS: Config = Config(
    "BOOTSTRAP_SERVERS",
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
    ""
  )

  /**
   * This is the {@code group.id} configuration setting.
   *
   * It is a unique string that identifies the consumer group this
   * consumer belongs to.
   */
  private[kafka] val GROUP_ID: Config = Config(
    "GROUP_ID",
    ConsumerConfig.GROUP_ID_CONFIG,
    "EXASOL_KAFKA_UDFS_CONSUMERS"
  )

  /**
   * This is the {@code max.poll.records} configuration setting.
   *
   * It is the maximum number of records returned in a single call to
   * poll() function. Default value is `500`.
   */
  private[kafka] val MAX_POLL_RECORDS: Config = Config(
    "MAX_POLL_RECORDS",
    ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
    "500"
  )

  /**
   * This is the {@code fetch.min.bytes} configuration setting.
   *
   * It is the minimum amount of data the server should return for a
   * fetch request. Default value is `1`.
   */
  private[kafka] val FETCH_MIN_BYTES: Config = Config(
    "FETCH_MIN_BYTES",
    ConsumerConfig.FETCH_MIN_BYTES_CONFIG,
    "1"
  )

  /**
   * An optional schema registry url.
   *
   * The Avro value deserializer will be used when user sets this
   * property value.
   */
  private[kafka] val SCHEMA_REGISTRY_URL: Config = Config(
    "SCHEMA_REGISTRY_URL",
    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
    ""
  )

  /**
   * This is the {@code security.protocol} configuration setting.
   *
   * It is the protocol used to communicate with brokers, when
   * [[SSL_ENABLED]] is set to {@code true}. Default value is
   * [[SslConfigs.DEFAULT_SSL_PROTOCOL]].
   */
  private[kafka] val SECURITY_PROTOCOL: Config = Config(
    "SECURITY_PROTOCOL",
    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
    SslConfigs.DEFAULT_SSL_PROTOCOL
  )

  /**
   * This is the {@code ssl.key.password} configuration setting.
   *
   * It represents the password of the private key in the key store
   * file. It is required property when [[SSL_ENABLED]] is set to {@code
   * true}.
   */
  private[kafka] val SSL_KEY_PASSWORD: Config = Config(
    "SSL_KEY_PASSWORD",
    SslConfigs.SSL_KEY_PASSWORD_CONFIG,
    ""
  )

  /**
   * This is the {@code ssl.keystore.password} confguration setting.
   *
   * It the store password for the keystore file. It is required
   * property when [[SSL_ENABLED]] is set to {@code true}.
   */
  private[kafka] val SSL_KEYSTORE_PASSWORD: Config = Config(
    "SSL_KEYSTORE_PASSWORD",
    SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
    ""
  )

  /**
   * This is the {@code ssl.keystore.location} configuration setting.
   *
   * It represents the location of the keystore file. It is required
   * property when [[SSL_ENABLED]] is set to {@code true} and can be
   * used for two-way authentication for the clients.
   */
  private[kafka] val SSL_KEYSTORE_LOCATION: Config = Config(
    "SSL_KEYSTORE_LOCATION",
    SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
    ""
  )

  /**
   * This is the {@code ssl.truststore.password} configuration setting.
   *
   * It is the password for the truststore file, and required property
   * when [[SSL_ENABLED]] is set to {@code true}.
   */
  private[kafka] val SSL_TRUSTSTORE_PASSWORD: Config = Config(
    "SSL_TRUSTSTORE_PASSWORD",
    SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
    ""
  )

  /**
   * This is the {@code ssl.truststore.location} configuration setting.
   *
   * It is the location of the truststore file, and required property
   * when [[SSL_ENABLED]] is set to {@code true}.
   */
  private[kafka] val SSL_TRUSTSTORE_LOCATION: Config = Config(
    "SSL_TRUSTSTORE_LOCATION",
    SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
    ""
  )

  /**
   * This is the {@code ssl.endpoint.identification.algorithm}
   * configuration setting.
   *
   * It is the endpoint identification algorithm to validate server
   * hostname using server certificate. It is used when [[SSL_ENABLED]]
   * is set to {@code true}. Default value is
   * [[SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM]].
   */
  private[kafka] val SSL_ENDPOINT_IDENTIFICATION_ALGORITHM: Config = Config(
    "SSL_ENDPOINT_IDENTIFICATION_ALGORITHM",
    SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG,
    SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM
  )

  /**
   * Returns [[KafkaConsumerProperties]] from user provided key value
   * properties.
   */
  def apply(params: Map[String, String]): KafkaConsumerProperties =
    new KafkaConsumerProperties(params)

  /**
   * Creates [[KafkaConsumerProperties]] from properly separated string.
   */
  def fromString(string: String): KafkaConsumerProperties = {
    if (!string.contains(PROPERTY_SEPARATOR)) {
      throw new IllegalArgumentException(
        s"The input string is not separated by '$PROPERTY_SEPARATOR'!"
      )
    }
    val properties = string
      .split(PROPERTY_SEPARATOR)
      .map { word =>
        val pairs = word.split(KEY_VALUE_SEPARATOR)
        pairs(0) -> pairs(1)
      }
      .toMap

    new KafkaConsumerProperties(properties)
  }

  /**
   * Creates a [[org.apache.kafka.clients.consumer.KafkaConsumer]] from
   * [[KafkaConsumerProperties]].
   */
  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def createKafkaConsumer(
    properties: KafkaConsumerProperties
  ): KafkaConsumer[String, GenericRecord] = {
    validate(properties)
    new KafkaConsumer[String, GenericRecord](
      properties.getProperties(),
      new StringDeserializer,
      getAvroDeserializer(properties.getSchemaRegistryUrl())
        .asInstanceOf[Deserializer[GenericRecord]]
    )
  }

  private[this] def validate(properties: KafkaConsumerProperties): Unit = {
    if (!properties.containsKey(BOOTSTRAP_SERVERS.userPropertyName)) {
      throw new IllegalArgumentException(
        s"Please provide a value for the "
          + s"${BOOTSTRAP_SERVERS.userPropertyName} property!"
      )
    }
    if (!properties.hasSchemaRegistryUrl()) {
      throw new IllegalArgumentException(
        s"Please provide a value for the "
          + s"${SCHEMA_REGISTRY_URL.userPropertyName} property!"
      )
    }
  }

  private[this] def getAvroDeserializer(schemaRegistryUrl: String): KafkaAvroDeserializer = {
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
