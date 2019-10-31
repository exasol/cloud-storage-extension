package com.exasol.cloudetl.sbt

import sbt._
import sbt.librarymanagement.InclExclRule

/** A list of required dependencies */
object Dependencies {

  // Versions
  private val ExasolJDBCVersion = "6.0.13"
  private val HadoopVersion = "2.9.2"
  private val OrcVersion = "1.5.5"
  private val ParquetVersion = "1.8.1"
  private val AzureStorageVersion = "2.2.0"
  private val GoogleStorageVersion = "hadoop2-1.9.10"
  private val KafkaClientsVersion = "2.3.0"
  private val KafkaAvroSerializerVersion = "5.2.1"
  private val TypesafeLoggingVersion = "3.9.0"

  val Resolvers: Seq[Resolver] = Seq(
    "Confluent Maven Repo" at "http://packages.confluent.io/maven/",
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  /** Core dependencies needed for connector */
  private val CoreDependencies: Seq[ModuleID] = Seq(
    "com.exasol" % "exasol-jdbc" % ExasolJDBCVersion,
    "com.exasol" % "exasol-script-api" % ExasolJDBCVersion,
    "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure-datalake" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-common" % HadoopVersion exclude ("org.slf4j", "slf4j-log4j12"),
    "org.apache.hadoop" % "hadoop-hdfs" % HadoopVersion,
    "org.apache.orc" % "orc-core" % OrcVersion,
    "org.apache.parquet" % "parquet-avro" % ParquetVersion,
    "com.microsoft.azure" % "azure-storage" % AzureStorageVersion,
    "com.google.cloud.bigdataoss" % "gcs-connector" % GoogleStorageVersion,
    "org.apache.kafka" % "kafka-clients" % KafkaClientsVersion,
    "io.confluent" % "kafka-avro-serializer" % KafkaAvroSerializerVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % TypesafeLoggingVersion
  )

  /** Test dependencies only required in `test` */
  private val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.mockito" % "mockito-core" % "2.23.4",
    "org.apache.kafka" %% "kafka" % "2.3.0" exclude ("org.slf4j", "slf4j-log4j12") exclude ("org.apache.kafka", "kafka-clients"),
    "io.github.embeddedkafka" %% "embedded-kafka-schema-registry" % "5.3.0" exclude ("org.apacha.kafka", "kafka")
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
