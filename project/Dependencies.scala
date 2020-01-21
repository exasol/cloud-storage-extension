package com.exasol.cloudetl.sbt

import sbt._
import sbt.librarymanagement.InclExclRule

/** A list of required dependencies */
object Dependencies {

  // Versions
  private val ExasolVersion = "6.1.7"
  private val HadoopVersion = "3.2.1"
  private val OrcVersion = "1.6.2"
  private val ParquetVersion = "1.10.1"
  private val AzureStorageVersion = "8.6.0"
  private val GoogleStorageVersion = "1.9.4-hadoop3"
  private val KafkaClientsVersion = "2.4.0"
  private val KafkaAvroSerializerVersion = "5.4.0"
  private val TypesafeLoggingVersion = "3.9.2"

  val Resolvers: Seq[Resolver] = Seq(
    "Confluent Maven Repo" at "http://packages.confluent.io/maven/",
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  /** Core dependencies needed for connector */
  private val CoreDependencies: Seq[ModuleID] = Seq(
    "com.exasol" % "exasol-script-api" % ExasolVersion,
    "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-azure-datalake" % HadoopVersion,
    "org.apache.hadoop" % "hadoop-client" % HadoopVersion
      exclude ("org.apache.avro", "avro")
      exclude ("org.apache.hadoop", "hadoop-yarn-api")
      exclude ("org.apache.hadoop", "hadoop-yarn-client")
      exclude ("org.apache.hadoop", "hadoop-yarn-common")
      excludeAll (
        ExclusionRule(organization = "org.apache.curator"),
        ExclusionRule(organization = "org.apache.kerby")
    ),
    "org.apache.orc" % "orc-core" % OrcVersion,
    "org.apache.parquet" % "parquet-hadoop" % ParquetVersion,
    "com.microsoft.azure" % "azure-storage" % AzureStorageVersion,
    "com.google.cloud.bigdataoss" % "gcs-connector" % GoogleStorageVersion,
    "org.apache.kafka" % "kafka-clients" % KafkaClientsVersion,
    "io.confluent" % "kafka-avro-serializer" % KafkaAvroSerializerVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % TypesafeLoggingVersion
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
