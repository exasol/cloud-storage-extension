package com.exasol.cloudetl.kafka

import scala.collection.JavaConverters._
import scala.collection.mutable.{Map => MMap}

import com.exasol.ExaMetadata
import com.exasol.cloudetl.common.AbstractProperties
import com.exasol.cloudetl.common.CommonProperties

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

  /** Returns user provided Kafka bootstrap servers string. */
  final def getBootstrapServers(): String =
    getString(BOOTSTRAP_SERVERS.userPropertyName)

  /**
   * Returns user provided group id, if it is not provided by user
   * returns default value.
   */
  final def getGroupId(): String =
    get(GROUP_ID.userPropertyName).fold(GROUP_ID.defaultValue)(identity)

  /** Returns the user provided topic name. */
  final def getTopics(): String =
    getString(TOPICS)

  /**
   * Returns the user provided Exasol table name; otherwise returns
   * default value.
   */
  final def getTableName(): String =
    getString(TABLE_NAME)

  /**
   * Returns poll timeout millisecords if provided by user; otherwise
   * returns default value.
   *
   * throws java.lang.NumberFormatException If value is not a Long.
   */
  @throws[NumberFormatException]("If value is not a Long.")
  final def getPollTimeoutMs(): Long =
    get(POLL_TIMEOUT_MS.userPropertyName).fold(POLL_TIMEOUT_MS.defaultValue)(_.toLong)

  /**
   * Returns minimum records per run property value when provided by
   * user; otherwise returns default value.
   *
   * throws java.lang.NumberFormatException If value is not an Int.
   */
  @throws[NumberFormatException]("If value is not an Int.")
  final def getMinRecordsPerRun(): Int =
    get(MIN_RECORDS_PER_RUN.userPropertyName).fold(MIN_RECORDS_PER_RUN.defaultValue)(_.toInt)

  /**
   * Returns maximum records per run property value when provided by
   * user; otherwise returns default value.
   *
   * throws java.lang.NumberFormatException If value is not an Int.
   */
  @throws[NumberFormatException]("If value is not an Int.")
  final def getMaxRecordsPerRun(): Int =
    get(MAX_RECORDS_PER_RUN.userPropertyName).fold(MAX_RECORDS_PER_RUN.defaultValue)(_.toInt)

  /** Checks if the {@code SSL_ENABLED} property is set. */
  final def isSSLEnabled(): Boolean =
    isEnabled(SSL_ENABLED)

  /** Checks if the Schema Registry URL property is set. */
  final def hasSchemaRegistryUrl(): Boolean =
    containsKey(SCHEMA_REGISTRY_URL.userPropertyName)

  /** Returns the user provided schema registry url property. */
  final def getSchemaRegistryUrl(): String =
    getString(SCHEMA_REGISTRY_URL.userPropertyName)

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

  /**
   * Returns {@code FETCH_MAX_BYTES} property value if provided,
   * otherwise returns the default value.
   */
  final def getFetchMaxBytes(): String =
    get(FETCH_MAX_BYTES.userPropertyName).fold(FETCH_MAX_BYTES.defaultValue)(identity)

  /**
   * Returns {@code MAX_PARTITION_FETCH_BYTES} property value if
   * provided, otherwise returns the default value.
   */
  final def getMaxPartitionFetchBytes(): String =
    get(MAX_PARTITION_FETCH_BYTES.userPropertyName)
      .fold(MAX_PARTITION_FETCH_BYTES.defaultValue)(identity)

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
    getString(SSL_KEY_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_KEYSTORE_PASSWORD} property
   * value.
   */
  final def getSSLKeystorePassword(): String =
    getString(SSL_KEYSTORE_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_KEYSTORE_LOCATION} property
   * value.
   */
  final def getSSLKeystoreLocation(): String =
    getString(SSL_KEYSTORE_LOCATION.userPropertyName)

  /**
   * Returns the user provided {@code SSL_TRUSTSTORE_PASSWORD} property
   * value.
   */
  final def getSSLTruststorePassword(): String =
    getString(SSL_TRUSTSTORE_PASSWORD.userPropertyName)

  /**
   * Returns the user provided {@code SSL_TRUSTSTORE_LOCATION} property
   * value.
   */
  final def getSSLTruststoreLocation(): String =
    getString(SSL_TRUSTSTORE_LOCATION.userPropertyName)

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
   * the schema of [[org.apache.avro.generic.GenericRecord]] the {@code
   * SCHEMA_REGISTRY_URL} value should be provided.
   */
  final def build(exaMetadata: ExaMetadata): KafkaConsumer[String, GenericRecord] =
    KafkaConsumerProperties.createKafkaConsumer(this, exaMetadata)

  /** Returns the Kafka consumer properties as Java map. */
  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  final def getProperties(): java.util.Map[String, AnyRef] = {
    val props = MMap.empty[String, String]
    props.put(ENABLE_AUTO_COMMIT.kafkaPropertyName, ENABLE_AUTO_COMMIT.defaultValue)
    props.put(BOOTSTRAP_SERVERS.kafkaPropertyName, getBootstrapServers())
    props.put(GROUP_ID.kafkaPropertyName, getGroupId())
    props.put(SCHEMA_REGISTRY_URL.kafkaPropertyName, getSchemaRegistryUrl())
    props.put(MAX_POLL_RECORDS.kafkaPropertyName, getMaxPollRecords())
    props.put(FETCH_MIN_BYTES.kafkaPropertyName, getFetchMinBytes())
    props.put(FETCH_MAX_BYTES.kafkaPropertyName, getFetchMaxBytes())
    props.put(MAX_PARTITION_FETCH_BYTES.kafkaPropertyName, getMaxPartitionFetchBytes())
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

  /**
   * Returns a new [[KafkaConsumerProperties]] that merges the key-value pairs
   * parsed from user provided Exasol named connection object.
   */
  final def mergeWithConnectionObject(exaMetadata: ExaMetadata): KafkaConsumerProperties =
    if (hasNamedConnection()) {
      validateConnectionObject()
      val connectionParsedMap =
        parseConnectionInfo(BOOTSTRAP_SERVERS.userPropertyName, Option(exaMetadata))
      val newProperties = properties ++ connectionParsedMap
      new KafkaConsumerProperties(newProperties)
    } else {
      this
    }

  private[this] def validateConnectionObject(): Unit = {
    val connectionProperties = List(
      "SSL_KEYSTORE_LOCATION",
      "SSL_KEYSTORE_PASSWORD",
      "SSL_KEY_PASSWORD",
      "SSL_TRUSTSTORE_LOCATION",
      "SSL_TRUSTSTORE_PASSWORD"
    )

    if (connectionProperties.exists(p => containsKey(p))) {
      throw new KafkaConnectorException(
        "Please provide either CONNECTION_NAME property or " +
          "server / credentials parameters, but not both!"
      )
    }
  }

  /**
   * Returns a string value of key-value property pairs.
   *
   * The resulting string is sorted by keys ordering.
   */
  final def mkString(): String =
    mkString(KEY_VALUE_SEPARATOR, PROPERTY_SEPARATOR)

}

/**
 * A companion object for [[KafkaConsumerProperties]] class.
 */
object KafkaConsumerProperties extends CommonProperties {

  /**
   * Internal configuration helper class.
   *
   * @param userPropertyName A UDF user provided property key name
   * @param kafkaPropertyName An equivalent property in Kafka
   *        configuration that maps user property key name
   * @param defaultValue A default value for the property key name
   */
  private[kafka] final case class Config[T](
    val userPropertyName: String,
    val kafkaPropertyName: String,
    val defaultValue: T
  )

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

  /**
   * An optional property key name to set SSL secure connections to
   * Kafka cluster.
   */
  private[kafka] final val SSL_ENABLED: String = "SSL_ENABLED"

  /**
   * A number of milliseconds to wait for Kafka consumer {@code poll} to
   * return any data.
   */
  private[kafka] final val POLL_TIMEOUT_MS: Config[Long] = Config[Long](
    "POLL_TIMEOUT_MS",
    "",
    30000L // scalastyle:ignore magic.number
  )

  /**
   * An upper bound on the minimum number of records to consume per UDF
   * run.
   *
   * That is, if the {@code poll} returns fewer records than this
   * number, consume them and finish the process. Otherwise, continue
   * polling more data until the total number of records reaches
   * [[MAX_RECORDS_PER_RUN]].
   *
   * See [[MAX_RECORDS_PER_RUN]].
   */
  private[kafka] final val MIN_RECORDS_PER_RUN: Config[Int] = Config[Int](
    "MIN_RECORDS_PER_RUN",
    "",
    100 // scalastyle:ignore magic.number
  )

  /**
   * An lower bound on the maximum number of records to consumer per UDF
   * run.
   *
   * When the returned number of records from {@code poll} is more than
   * [[MIN_RECORDS_PER_RUN]], it continues polling for more records
   * until total number reaches this number.
   *
   * See [[MIN_RECORDS_PER_RUN]].
   */
  private[kafka] final val MAX_RECORDS_PER_RUN: Config[Int] = Config[Int](
    "MAX_RECORDS_PER_RUN",
    "",
    1000000 // scalastyle:ignore magic.number
  )

  /**
   * Below are relavant Kafka consumer configuration parameters are
   * defined.
   *
   * See [[https://kafka.apache.org/documentation.html#consumerconfigs]]
   */
  /**
   * This is the {@code enable.auto.commit} configuration setting.
   *
   * If set to true the offset of consumer will be periodically
   * committed to the Kafka cluster in the background. This is `false`
   * by default, since we manage the offset commits ourselves in the
   * Exasol table.
   */
  private[kafka] final val ENABLE_AUTO_COMMIT: Config[String] = Config[String](
    "ENABLE_AUTO_COMMIT",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
    "false"
  )

  /**
   * This is the {@code bootstrap.servers} configuration setting.
   *
   * A list of host and port pairs to use for establishing the initial
   * connection to the Kafka cluster.
   *
   * It is a required property that should be provided by the user.
   */
  private[kafka] final val BOOTSTRAP_SERVERS: Config[String] = Config[String](
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
  private[kafka] final val GROUP_ID: Config[String] = Config[String](
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
  private[kafka] final val MAX_POLL_RECORDS: Config[String] = Config[String](
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
  private[kafka] final val FETCH_MIN_BYTES: Config[String] = Config[String](
    "FETCH_MIN_BYTES",
    ConsumerConfig.FETCH_MIN_BYTES_CONFIG,
    "1"
  )

  /**
   * This is the {@code fetch.max.bytes} configuration setting.
   *
   * It is the maximum amount of data the server should return for a
   * fetch request. Default value is
   * [[ConsumerConfig.DEFAULT_FETCH_MAX_BYTES]].
   */
  private[kafka] final val FETCH_MAX_BYTES: Config[String] = Config[String](
    "FETCH_MAX_BYTES",
    ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
    s"${ConsumerConfig.DEFAULT_FETCH_MAX_BYTES}"
  )

  /**
   * This is the {@code max.partition.fetch.bytes} configuration
   * setting.
   *
   * It is the maximum amount of data the server will return per
   * partition. Default value is
   * [[ConsumerConfig.DEFAULT_MAX_PARTITION_FETCH_BYTES]].
   */
  private[kafka] final val MAX_PARTITION_FETCH_BYTES: Config[String] = Config[String](
    "MAX_PARTITION_FETCH_BYTES",
    ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
    s"${ConsumerConfig.DEFAULT_MAX_PARTITION_FETCH_BYTES}"
  )

  /**
   * An optional schema registry url.
   *
   * The Avro value deserializer will be used when user sets this
   * property value.
   */
  private[kafka] final val SCHEMA_REGISTRY_URL: Config[String] = Config[String](
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
  private[kafka] final val SECURITY_PROTOCOL: Config[String] = Config[String](
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
  private[kafka] final val SSL_KEY_PASSWORD: Config[String] = Config[String](
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
  private[kafka] final val SSL_KEYSTORE_PASSWORD: Config[String] = Config[String](
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
  private[kafka] final val SSL_KEYSTORE_LOCATION: Config[String] = Config[String](
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
  private[kafka] final val SSL_TRUSTSTORE_PASSWORD: Config[String] = Config[String](
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
  private[kafka] final val SSL_TRUSTSTORE_LOCATION: Config[String] = Config[String](
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
  private[kafka] final val SSL_ENDPOINT_IDENTIFICATION_ALGORITHM: Config[String] = Config[String](
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
   * Returns [[KafkaConsumerProperties]] from properly separated string.
   */
  def apply(string: String): KafkaConsumerProperties =
    apply(mapFromString(string))

  /**
   * Creates a [[org.apache.kafka.clients.consumer.KafkaConsumer]] from
   * [[KafkaConsumerProperties]].
   */
  def createKafkaConsumer(
    kafkaConsumerProperties: KafkaConsumerProperties,
    exaMetadata: ExaMetadata
  ): KafkaConsumer[String, GenericRecord] = {
    val properties = kafkaConsumerProperties.mergeWithConnectionObject(exaMetadata)
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
