package com.exasol.cloudetl.sbt

import sbt.{ExclusionRule, _}
import sbt.librarymanagement.InclExclRule

/** A list of required dependencies */
object Dependencies {

  // Runtime dependencies versions
  private val ExasolVersion = "6.1.7"
  private val HadoopVersion = "3.3.0"
  private val AvroVersion = "1.10.0"
  private val DeltaVersion = "0.7.0"
  private val OrcVersion = "1.6.3"
  private val ParquetVersion = "1.10.1"
  private val AzureStorageVersion = "8.6.0"
  private val GoogleStorageVersion = "1.9.4-hadoop3"
  private val KafkaClientsVersion = "2.5.0"
  private val KafkaAvroSerializerVersion = "5.4.0"
  private val SparkSQLVersion = "3.0.0"
  private val SLF4JApiVersion = "1.7.30"
  private val TypesafeLoggingVersion = "3.9.2"
  private val JacksonVersion = "2.11.2"

  // Test dependencies versions
  private val ScalaTestVersion = "3.2.2"
  private val ScalaTestPlusVersion = "1.0.0-M2"
  private val MockitoCoreVersion = "3.5.10"
  private val ExasolTestContainersVersion = "2.1.0"
  private val LocalStackVersion = "1.14.3"
  private val KafkaSchemaRegistryVersion = "5.4.0"

  val ExasolResolvers: Seq[Resolver] = Seq(
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  val ConfluentResolvers: Seq[Resolver] = Seq(
    "Confluent Maven Repo" at "https://packages.confluent.io/maven/",
  )

  lazy val JacksonDependencies: Seq[ModuleID] = Seq(
    "com.fasterxml.jackson.core" % "jackson-core" % JacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion
      exclude ("com.fasterxml.jackson.core", "jackson-annotations"),
    "com.fasterxml.jackson.core" % "jackson-annotations" % JacksonVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % JacksonVersion
      exclude ("com.fasterxml.jackson.core", "jackson-databind")
  )

  lazy val CommonDependencies: Seq[ModuleID] = Seq(
    "com.exasol" % "exasol-script-api" % ExasolVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % TypesafeLoggingVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.scala-lang", "scala-library")
      exclude ("org.scala-lang", "scala-reflect"),
    // Common test dependencies
    "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
    "org.scalatestplus" %% "scalatestplus-mockito" % ScalaTestPlusVersion % "test",
    "org.mockito" % "mockito-core" % MockitoCoreVersion % "test"
  )

  lazy val AvroDependencies: Seq[ModuleID] = Seq(
    "org.apache.avro" % "avro" % AvroVersion
      exclude ("org.slf4j", "slf4j-api")
      excludeAll (
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
    ),
    "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion
  )

  lazy val StorageDependencies: Seq[ModuleID] = Seq(
    "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure" % HadoopVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("com.fasterxml.jackson.core", "jackson-core")
      exclude ("com.microsoft.azure", "azure-keyvault-core"),
    "org.apache.hadoop" % "hadoop-azure-datalake" % HadoopVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("com.fasterxml.jackson.core", "jackson-core"),
    "org.apache.hadoop" % "hadoop-client" % HadoopVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.slf4j", "slf4j-log4j12")
      exclude ("commons-cli", "commons-cli")
      exclude ("commons-logging", "commons-logging")
      exclude ("com.google.code.findbugs", "jsr305")
      exclude ("org.apache.commons", "commons-compress")
      exclude ("org.apache.avro", "avro")
      exclude ("org.apache.hadoop", "hadoop-yarn-api")
      exclude ("org.apache.hadoop", "hadoop-yarn-client")
      exclude ("org.apache.hadoop", "hadoop-yarn-common")
      exclude ("com.fasterxml.jackson.core", "jackson-databind")
      excludeAll (
        ExclusionRule(organization = "org.eclipse.jetty"),
        ExclusionRule(organization = "org.apache.kerby"),
        ExclusionRule(organization = "org.apache.curator"),
        ExclusionRule(organization = "org.apache.zookeeper")
    ),
    "com.google.cloud.bigdataoss" % "gcs-connector" % GoogleStorageVersion
      exclude ("com.google.guava", "guava")
      exclude ("org.apache.httpcomponents", "httpclient"),
    "org.apache.orc" % "orc-core" % OrcVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("javax.xml.bind", "jaxb-api"),
    "org.apache.parquet" % "parquet-hadoop" % ParquetVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("commons-codec", "commons-codec")
      exclude ("org.xerial.snappy", "snappy-java"),
    "io.delta" %% "delta-core" % DeltaVersion,
    "org.apache.spark" %% "spark-sql" % SparkSQLVersion
      exclude ("org.apache.hadoop", "hadoop-client")
      exclude ("com.fasterxml.jackson.core", "jackson-annotations")
      exclude ("com.fasterxml.jackson.core", "jackson-core")
      exclude ("com.fasterxml.jackson.core", "jackson-databind")
      excludeAll (
        ExclusionRule(organization = "org.apache.arrow"),
        ExclusionRule(organization = "org.apache.avro"),
        ExclusionRule(organization = "org.apache.curator"),
        ExclusionRule(organization = "org.apache.orc"),
        ExclusionRule(organization = "org.apache.zookeeper")
    )
  )

  lazy val KafkaDependencies: Seq[ModuleID] = Seq(
    "org.apache.kafka" % "kafka-clients" % KafkaClientsVersion,
    "io.confluent" % "kafka-avro-serializer" % KafkaAvroSerializerVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.apache.avro", "avro")
      exclude ("org.apache.commons", "commons-lang3")
      exclude ("com.google.guava", "guava")
      exclude ("com.fasterxml.jackson.core", "jackson-databind")
      exclude ("io.swagger", "swagger-core")
      exclude ("io.swagger", "swagger-models"),
    // Tests
    "io.github.embeddedkafka" %% "embedded-kafka-schema-registry" % KafkaSchemaRegistryVersion % "test"
      exclude ("com.fasterxml.jackson.core", "jackson-annotations")
      exclude ("com.fasterxml.jackson.core", "jackson-core")
      exclude ("com.fasterxml.jackson.core", "jackson-databind")
  )

  lazy val KinesisDependencies: Seq[ModuleID] = Seq(
    "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % JacksonVersion,
    // Tests
    "com.exasol" % "exasol-testcontainers" % ExasolTestContainersVersion % "test",
    "org.testcontainers" % "localstack" % LocalStackVersion % "test"
  )

  lazy val ExcludedDependencies: Seq[InclExclRule] = Seq(
    ExclusionRule("org.ow2.asm", "asm"),
    ExclusionRule("javax.ws.rs", "jsr311-api"),
    ExclusionRule("com.sun.jersey", "jersey-core"),
    ExclusionRule("com.sun.jersey", "jersey-server"),
    ExclusionRule("com.sun.jersey", "jersey-json"),
    ExclusionRule("javax.servlet", "servlet-api"),
    ExclusionRule("javax.servlet.jsp", "jsp-api"),
    ExclusionRule("org.openjfx", "javafx.base")
  )

  lazy val KafkaExcludedDependencies: Seq[InclExclRule] = Seq(
    ExclusionRule("org.openjfx", "javafx.base")
  )

}
