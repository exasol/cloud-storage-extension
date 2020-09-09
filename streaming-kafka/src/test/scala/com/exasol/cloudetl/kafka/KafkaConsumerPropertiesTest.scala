package com.exasol.cloudetl.kafka

import com.exasol.{ExaConnectionInformation, ExaMetadata}

import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class KafkaConsumerPropertiesTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

  private[this] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  private[this] def errorMessage(key: String): String =
    s"Please provide a value for the $key property!"

  test("getBootstrapServers returns bootstrap servers property value") {
    val bootstrapServers = "kafka01.example.com,kafka02.example.com"
    properties = Map("BOOTSTRAP_SERVERS" -> bootstrapServers)
    assert(BaseProperties(properties).getBootstrapServers() === bootstrapServers)
  }

  test("getBootstrapServers throws if bootstrap servers property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getBootstrapServers()
    }
    assert(thrown.getMessage === errorMessage("BOOTSTRAP_SERVERS"))
  }

  test("getGroupId returns user provided value") {
    properties = Map("GROUP_ID" -> "groupId")
    assert(BaseProperties(properties).getGroupId() === "groupId")
  }

  test("getGroupId returns default value if group id is not set") {
    assert(BaseProperties(properties).getGroupId() === "EXASOL_KAFKA_UDFS_CONSUMERS")
  }

  test("getTopics returns topics property value") {
    properties = Map("TOPICS" -> "Metamorphosis")
    assert(BaseProperties(properties).getTopics() === "Metamorphosis")
  }

  test("getTopics throws if topics property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getTopics()
    }
    assert(thrown.getMessage === errorMessage("TOPICS"))
  }

  test("getTableName returns Exasol table name property value") {
    properties = Map("TABLE_NAME" -> "table")
    assert(BaseProperties(properties).getTableName() === "table")
  }

  test("getTableName throws if table name property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getTableName()
    }
    assert(thrown.getMessage === errorMessage("TABLE_NAME"))
  }

  test("getPollTimeoutMs returns provided poll timeout value") {
    properties = Map("POLL_TIMEOUT_MS" -> "10")
    assert(BaseProperties(properties).getPollTimeoutMs() === 10L)
  }

  test("getPollTimeoutMs returns default value if poll timeout is not set") {
    assert(BaseProperties(properties).getPollTimeoutMs() === 30000L)
  }

  test("getPollTimeoutMs throws if value cannot be converted to long") {
    properties = Map("POLL_TIMEOUT_MS" -> "1l")
    intercept[NumberFormatException] {
      BaseProperties(properties).getPollTimeoutMs()
    }
  }

  test("getMinRecordsPerRun returns provided minimum value") {
    properties = Map("MIN_RECORDS_PER_RUN" -> "7")
    assert(BaseProperties(properties).getMinRecordsPerRun() === 7)
  }

  test("getMinRecordsPerRun returns default value if minimum value is not set") {
    assert(BaseProperties(properties).getMinRecordsPerRun() === 100)
  }

  test("getMinRecordsPerRun throws if value cannot be converted to int") {
    properties = Map("MIN_RECORDS_PER_RUN" -> "e")
    intercept[NumberFormatException] {
      BaseProperties(properties).getMinRecordsPerRun()
    }
  }

  test("getMaxRecordsPerRun returns provided maximum value") {
    properties = Map("MAX_RECORDS_PER_RUN" -> "43")
    assert(BaseProperties(properties).getMaxRecordsPerRun() === 43)
  }

  test("getMaxRecordsPerRun returns default value if maximum value is not set") {
    assert(BaseProperties(properties).getMaxRecordsPerRun() === 1000000)
  }

  test("getMaxRecordsPerRun throws if value cannot be converted to int") {
    properties = Map("MAX_RECORDS_PER_RUN" -> "max")
    intercept[NumberFormatException] {
      BaseProperties(properties).getMaxRecordsPerRun()
    }
  }

  test("isSSLEnabled returns true if it is set to true") {
    properties = Map("SSL_ENABLED" -> "true")
    assert(BaseProperties(properties).isSSLEnabled() === true)
  }

  test("isSSLEnabled returns false if it is not set") {
    assert(BaseProperties(properties).isSSLEnabled() === false)
  }

  test("hasSchemaRegistryUrl returns true if schema registry url is provided") {
    properties = Map("SCHEMA_REGISTRY_URL" -> "https://schema-registry.example.com")
    assert(BaseProperties(properties).hasSchemaRegistryUrl() === true)
  }

  test("hasSchemaRegistryUrl returns false if schema registry url is not set") {
    assert(BaseProperties(properties).hasSchemaRegistryUrl() === false)
  }

  test("getSchemaRegistryUrl returns schema registry url property value") {
    properties = Map("SCHEMA_REGISTRY_URL" -> "http://a-schema.url")
    assert(BaseProperties(properties).getSchemaRegistryUrl() === "http://a-schema.url")
  }

  test("getSchemaRegistryUrl throws if schema registry url property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSchemaRegistryUrl()
    }
    assert(thrown.getMessage === errorMessage("SCHEMA_REGISTRY_URL"))
  }

  test("getMaxPollRecords returns max poll records value") {
    properties = Map("MAX_POLL_RECORDS" -> "9")
    assert(BaseProperties(properties).getMaxPollRecords() === "9")
  }

  test("getMaxPollRecords returns default value if max poll records is not set") {
    assert(BaseProperties(properties).getMaxPollRecords() === "500")
  }

  test("getFetchMinBytes returns minimum fetch bytes property value") {
    properties = Map("FETCH_MIN_BYTES" -> "23")
    assert(BaseProperties(properties).getFetchMinBytes() === "23")
  }

  test("getFetchMinBytes returns default value if property is not set") {
    assert(BaseProperties(properties).getFetchMinBytes() === "1")
  }

  test("getFetchMaxBytes returns maximum fetch bytes property value") {
    properties = Map("FETCH_MAX_BYTES" -> "27")
    assert(BaseProperties(properties).getFetchMaxBytes() === "27")
  }

  test("getFetchMaxBytes returns default value if property is not set") {
    // intentionally hardcoded, get alert if it changes.
    assert(BaseProperties(properties).getFetchMaxBytes() === "52428800")
  }

  test("getMaxPartitionFetchBytes returns maximum partition fetch bytes property value") {
    properties = Map("MAX_PARTITION_FETCH_BYTES" -> "4")
    assert(BaseProperties(properties).getMaxPartitionFetchBytes() === "4")
  }

  test("getMaxPartitionFetchBytes returns default value if property is not set") {
    assert(BaseProperties(properties).getMaxPartitionFetchBytes() === "1048576")
  }

  test("getSecurityProtocol returns user provided security protocol property value") {
    properties = Map("SECURITY_PROTOCOL" -> "SSL")
    assert(BaseProperties(properties).getSecurityProtocol() === "SSL")
  }

  test("getSecurityProtocol returns default value if security protocol is not set") {
    // default value is intentionally hardcoded, should alert if things
    // change
    assert(BaseProperties(properties).getSecurityProtocol() === "TLS")
  }

  test("getSSLKeyPassword returns ssl key password property value") {
    properties = Map("SSL_KEY_PASSWORD" -> "1337")
    assert(BaseProperties(properties).getSSLKeyPassword() === "1337")
  }

  test("getSSLKeyPassword throws if ssl key password property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSSLKeyPassword()
    }
    assert(thrown.getMessage === errorMessage("SSL_KEY_PASSWORD"))
  }

  test("getSSLKeystorePassword returns ssl keystore password property value") {
    properties = Map("SSL_KEYSTORE_PASSWORD" -> "p@ss")
    assert(BaseProperties(properties).getSSLKeystorePassword() === "p@ss")
  }

  test("getSSLKeystorePassword throws if ssl keystore password property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSSLKeystorePassword()
    }
    assert(thrown.getMessage === errorMessage("SSL_KEYSTORE_PASSWORD"))
  }

  test("getSSLKeystoreLocation returns ssl keystore location property value") {
    properties = Map("SSL_KEYSTORE_LOCATION" -> "/path/keystore.jks")
    assert(BaseProperties(properties).getSSLKeystoreLocation() === "/path/keystore.jks")
  }

  test("getSSLKeystoreLocation throws if ssl keystore location property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSSLKeystoreLocation()
    }
    assert(thrown.getMessage === errorMessage("SSL_KEYSTORE_LOCATION"))
  }

  test("getSSLTruststorePassword returns ssl truststore password property value") {
    properties = Map("SSL_TRUSTSTORE_PASSWORD" -> "tp@ss")
    assert(BaseProperties(properties).getSSLTruststorePassword() === "tp@ss")
  }

  test("getSSLTruststorePassword throws if ssl truststore password property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSSLTruststorePassword()
    }
    assert(thrown.getMessage === errorMessage("SSL_TRUSTSTORE_PASSWORD"))
  }

  test("getSSLTruststoreLocation returns ssl truststore location property value") {
    properties = Map("SSL_TRUSTSTORE_LOCATION" -> "/path/truststore.jks")
    assert(BaseProperties(properties).getSSLTruststoreLocation() === "/path/truststore.jks")
  }

  test("getSSLTruststorePassword throws if ssl truststore location property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getSSLTruststoreLocation()
    }
    assert(thrown.getMessage === errorMessage("SSL_TRUSTSTORE_LOCATION"))
  }

  test("getSSLEndpointIdentificationAlgorithm returns user provided property value") {
    properties = Map("SSL_ENDPOINT_IDENTIFICATION_ALGORITHM" -> "none")
    assert(BaseProperties(properties).getSSLEndpointIdentificationAlgorithm() === "none")
  }

  test("getSSLEndpointIdentificationAlgorithm returns default value if it is not set") {
    // default value is intentionally hardcoded, should alert if things
    // change
    assert(BaseProperties(properties).getSSLEndpointIdentificationAlgorithm() === "https")
  }

  test("build throws if required BOOTSTRAP_SERVERS property is not provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).build(mock[ExaMetadata])
    }
    assert(thrown.getMessage === errorMessage("BOOTSTRAP_SERVERS"))
  }

  test("build throws if required SCHEMA_REGISTRY_URL property is not provided") {
    properties = Map("BOOTSTRAP_SERVERS" -> "kafka01.internal:9092")
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).build(mock[ExaMetadata])
    }
    assert(thrown.getMessage === errorMessage("SCHEMA_REGISTRY_URL"))
  }

  test("getProperties returns Java map properties") {
    import KafkaConsumerProperties._
    val requiredProperties = Map(
      BOOTSTRAP_SERVERS -> "kafka.broker.com:9092",
      SCHEMA_REGISTRY_URL -> "http://schema-registry.com:8080",
      SECURITY_PROTOCOL -> "SSL",
      SSL_KEY_PASSWORD -> "sslKeyPass",
      SSL_KEYSTORE_PASSWORD -> "sslKeystorePass",
      SSL_KEYSTORE_LOCATION -> "/bucket/keystore.JKS",
      SSL_TRUSTSTORE_PASSWORD -> "sslTruststorePass",
      SSL_TRUSTSTORE_LOCATION -> "/bucket/truststore.JKS"
    )
    val optionalProperties = Map(
      ENABLE_AUTO_COMMIT -> "false",
      GROUP_ID -> "EXASOL_KAFKA_UDFS_CONSUMERS",
      MAX_POLL_RECORDS -> "500",
      FETCH_MIN_BYTES -> "1",
      FETCH_MAX_BYTES -> "52428800",
      MAX_PARTITION_FETCH_BYTES -> "1048576"
    )

    properties = Map("SSL_ENABLED" -> "true") ++ requiredProperties.map {
      case (key, value) =>
        key.userPropertyName -> value
    }
    val javaProps = BaseProperties(properties).getProperties()
    assert(javaProps.isInstanceOf[java.util.Map[String, Object]])
    (requiredProperties ++ optionalProperties).foreach {
      case (key, value) =>
        assert(javaProps.get(key.kafkaPropertyName) === value)
    }
  }

  test("mergeWithConnectionObject returns new KafkaConsumerProperties") {
    val propertiesMap = Map(
      "TOPICS" -> "test-topic",
      "CONNECTION_NAME" -> "MY_CONNECTION"
    )
    val kafkaConsumerProperties = new BaseProperties(propertiesMap)
    val exaMetadata = mock[ExaMetadata]
    val exaConnectionInformation = mock[ExaConnectionInformation]
    when(exaMetadata.getConnection("MY_CONNECTION")).thenReturn(exaConnectionInformation)
    when(exaConnectionInformation.getUser()).thenReturn("")
    when(exaConnectionInformation.getPassword())
      .thenReturn(
        """BOOTSTRAP_SERVERS=MY_BOOTSTRAP_SERVERS;
          |SCHEMA_REGISTRY_URL=MY_SCHEMA_REGISTRY;
          |SECURITY_PROTOCOL=SSL;
          |SSL_KEYSTORE_LOCATION=MY_KEYSTORE_LOCATION;
          |SSL_KEYSTORE_PASSWORD=MY_KEYSTORE_PASSWORD;
          |SSL_KEY_PASSWORD=MY_SSL_KEY_PASSWORD;
          |SSL_TRUSTSTORE_LOCATION=MY_TRUSTSTORE_LOCATION;
          |SSL_TRUSTSTORE_PASSWORD=MY_TRUSTSTORE_PASSWORD""".stripMargin.replace("\n", "")
      )
    val mergedKafkaConsumerProperties =
      kafkaConsumerProperties.mergeWithConnectionObject(exaMetadata)
    assert(
      mergedKafkaConsumerProperties.mkString() ===
        """BOOTSTRAP_SERVERS -> MY_BOOTSTRAP_SERVERS;
          |CONNECTION_NAME -> MY_CONNECTION;
          |SCHEMA_REGISTRY_URL -> MY_SCHEMA_REGISTRY;
          |SECURITY_PROTOCOL -> SSL;
          |SSL_KEYSTORE_LOCATION -> MY_KEYSTORE_LOCATION;
          |SSL_KEYSTORE_PASSWORD -> MY_KEYSTORE_PASSWORD;
          |SSL_KEY_PASSWORD -> MY_SSL_KEY_PASSWORD;
          |SSL_TRUSTSTORE_LOCATION -> MY_TRUSTSTORE_LOCATION;
          |SSL_TRUSTSTORE_PASSWORD -> MY_TRUSTSTORE_PASSWORD;
          |TOPICS -> test-topic""".stripMargin
          .replace("\n", "")
    )
  }

  test("mergeWithConnectionObject throws exception during validation") {
    val conflictingProperties = Map(
      "BOOTSTRAP_SERVERS" -> "kafka.broker.com:9092",
      "CONNECTION_NAME" -> "MY_CONNECTION",
      "SSL_KEY_PASSWORD" -> "sslK3YP@ssword"
    )

    val kafkaConsumerProperties = new BaseProperties(conflictingProperties)
    val exaMetadata = mock[ExaMetadata]
    val thrown = intercept[KafkaConnectorException] {
      kafkaConsumerProperties.mergeWithConnectionObject(exaMetadata)
    }
    assert(
      thrown.getMessage ===
        """Please provide either CONNECTION_NAME property
          | or server / credentials parameters, but not both!""".stripMargin
          .replace("\n", "")
    )
  }

  private[this] case class BaseProperties(val params: Map[String, String])
      extends KafkaConsumerProperties(params)

}
