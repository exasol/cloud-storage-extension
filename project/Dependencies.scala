package com.exasol.cloudetl.sbt

import sbt._

/** A list of required dependencies */
object Dependencies {

  // Versions
  private val ExasolJDBCVersion = "6.0.13"
  private val HadoopVersion = "2.9.2"
  private val ParquetVersion = "1.8.1"
  private val AzureStorageVersion = "2.2.0"
  private val GoogleStorageVersion = "hadoop2-1.9.10"
  private val TypesafeLoggingVersion = "3.9.0"

  val Resolvers: Seq[Resolver] = Seq(
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
    "org.apache.parquet" % "parquet-avro" % ParquetVersion,
    "com.microsoft.azure" % "azure-storage" % AzureStorageVersion,
    "com.google.cloud.bigdataoss" % "gcs-connector" % GoogleStorageVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % TypesafeLoggingVersion
  )

  /** Test dependencies only required in `test` */
  private val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.mockito" % "mockito-core" % "2.23.4"
  ).map(_ % Test)

  /** The list of all dependencies for the connector */
  lazy val AllDependencies: Seq[ModuleID] = CoreDependencies ++ TestDependencies

}
