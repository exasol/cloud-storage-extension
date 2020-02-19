package com.exasol.cloudetl.sbt

import sbt._
import sbt.librarymanagement.InclExclRule

/** A list of required dependencies */
object Dependencies {

  // Versions
  private val ExasolVersion = "6.1.7"
  private val HadoopVersion = "3.2.1"
  private val AvroVersion = "1.9.2"
  private val OrcVersion = "1.6.2"
  private val ParquetVersion = "1.10.1"
  private val AzureStorageVersion = "8.6.0"
  private val GoogleStorageVersion = "1.9.4-hadoop3"
  private val KafkaClientsVersion = "2.4.0"
  private val KafkaAvroSerializerVersion = "5.4.0"
  private val SLF4JApiVersion = "1.7.30"
  private val TypesafeLoggingVersion = "3.9.2"

  val Resolvers: Seq[Resolver] = Seq(
    "Confluent Maven Repo" at "https://packages.confluent.io/maven/",
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  /** Core dependencies needed for connector */
  private val CoreDependencies: Seq[ModuleID] = Seq(
    "com.exasol" % "exasol-script-api" % ExasolVersion,
    "org.slf4j" % "slf4j-api" % SLF4JApiVersion,
    "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure" % HadoopVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.eclipse.jetty", "jetty-util-ajax")
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
      excludeAll (
        ExclusionRule(organization = "org.eclipse.jetty"),
        ExclusionRule(organization = "org.apache.kerby"),
        ExclusionRule(organization = "org.apache.curator"),
        ExclusionRule(organization = "org.apache.zookeeper")
    ),
    "com.google.cloud.bigdataoss" % "gcs-connector" % GoogleStorageVersion
      exclude ("com.google.guava", "guava")
      exclude ("org.apache.httpcomponents", "httpclient"),
    "org.apache.avro" % "avro" % AvroVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("com.fasterxml.jackson.core", "jackson-core"),
    "org.apache.orc" % "orc-core" % OrcVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("javax.xml.bind", "jaxb-api"),
    "org.apache.parquet" % "parquet-hadoop" % ParquetVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("commons-codec", "commons-codec")
      exclude ("org.xerial.snappy", "snappy-java"),
    "org.apache.kafka" % "kafka-clients" % KafkaClientsVersion,
    "io.confluent" % "kafka-avro-serializer" % KafkaAvroSerializerVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.apache.avro", "avro")
      exclude ("com.google.guava", "guava")
      exclude ("org.apache.commons", "commons-lang3"),
    "com.typesafe.scala-logging" %% "scala-logging" % TypesafeLoggingVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.scala-lang", "scala-library")
      exclude ("org.scala-lang", "scala-reflect")
  )

  /** Test dependencies only required in `test` */
  private val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.1.0",
    "org.scalatestplus" %% "scalatestplus-mockito" % "1.0.0-M2",
    "org.mockito" % "mockito-core" % "3.2.4",
    "io.github.embeddedkafka" %% "embedded-kafka-schema-registry" % "5.4.0"
  ).map(_ % Test)

  lazy val ExcludedDependencies: Seq[InclExclRule] = Seq(
    ExclusionRule("org.ow2.asm", "asm"),
    ExclusionRule("javax.ws.rs", "jsr311-api"),
    ExclusionRule("com.sun.jersey", "jersey-core"),
    ExclusionRule("com.sun.jersey", "jersey-server"),
    ExclusionRule("com.sun.jersey", "jersey-json"),
    ExclusionRule("javax.servlet", "servlet-api"),
    ExclusionRule("javax.servlet.jsp", "jsp-api")
  )

  /** The list of all dependencies for the connector */
  lazy val AllDependencies: Seq[ModuleID] = CoreDependencies ++ TestDependencies

}
