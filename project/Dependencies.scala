package com.exasol.cloudetl.sbt

import sbt.{ExclusionRule, _}
import sbt.librarymanagement.InclExclRule

/** A list of required dependencies */
object Dependencies {

  // Runtime dependencies versions
  private val ImportExportUDFVersion = "0.2.0"
  private val HadoopVersion = "3.3.0"
  private val DeltaVersion = "0.7.0"
  private val OrcVersion = "1.6.7"
  private val ParquetVersion = "1.12.0"
  private val GoogleStorageVersion = "1.9.4-hadoop3"
  private val SparkSQLVersion = "3.0.1"
  private val AlluxioCoreHDFSVersion = "2.5.0"

  // Test dependencies versions
  private val ScalaTestVersion = "3.2.6"
  private val ScalaTestPlusVersion = "1.0.0-M2"
  private val MockitoCoreVersion = "3.8.0"
  private val HamcrestVersion = "2.2"
  private val ExasolHamcrestMatcherVersion = "1.4.0"
  private val ExasolTestDBBuilderVersion = "3.1.1"
  private val ExasolTestContainersVersion = "3.5.1"
  private val TestContainersLocalstackVersion = "1.15.2"
  private val TestContainersScalaVersion = "0.39.3"

  val Resolvers: Seq[Resolver] = Seq(
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  lazy val StorageDependencies: Seq[ModuleID] = Seq(
    "com.exasol" %% "import-export-udf-common-scala" % ImportExportUDFVersion,
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
    "org.apache.hadoop" % "hadoop-hdfs" % HadoopVersion,
    "org.alluxio" % "alluxio-core-client-hdfs" % AlluxioCoreHDFSVersion,
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

  lazy val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % ScalaTestVersion,
    "org.scalatestplus" %% "scalatestplus-mockito" % ScalaTestPlusVersion,
    "org.mockito" % "mockito-core" % MockitoCoreVersion,
    "com.exasol" % "exasol-testcontainers" % ExasolTestContainersVersion,
    "com.exasol" % "test-db-builder-java" % ExasolTestDBBuilderVersion,
    "com.exasol" % "hamcrest-resultset-matcher" % ExasolHamcrestMatcherVersion,
    "org.hamcrest" % "hamcrest" % HamcrestVersion,
    "com.dimafeng" %% "testcontainers-scala-scalatest" % TestContainersScalaVersion,
    "org.testcontainers" % "localstack" % TestContainersLocalstackVersion
  ).map(_ % Test)

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

}
