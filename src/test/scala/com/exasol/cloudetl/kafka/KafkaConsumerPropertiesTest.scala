package com.exasol.cloudetl.kafka

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class KafkaConsumerPropertiesTest extends FunSuite with BeforeAndAfterEach {

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

  test("build throws if required properties are not provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).build()
    }
    assert(thrown.getMessage === errorMessage("BOOTSTRAP_SERVERS"))
  }

  ignore("build returns a KafkaConsumer[String, GenericRecord]") {
    properties = Map(
      "BOOTSTRAP_SERVERS" -> "kafka01.internal:9092",
      "SCHEMA_REGISTRY_URL" -> "https://schema-registry.internal.com"
    )
    val kafkaConsumer = BaseProperties(properties).build()
    assert(kafkaConsumer.isInstanceOf[KafkaConsumer[String, GenericRecord]])
  }

  private[this] case class BaseProperties(val params: Map[String, String])
      extends KafkaConsumerProperties(params)

}
