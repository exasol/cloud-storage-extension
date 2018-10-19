package com.exasol.s3etl.sbt

import sbt._

/** A list of required dependencies */
object Dependencies {

  val Resolvers: Seq[Resolver] = Seq(
    "Exasol Releases" at "https://maven.exasol.com/artifactory/exasol-releases"
  )

  /** Core dependencies needed for connector */
  private val CoreDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
    "com.exasol" % "exasol-jdbc" % "6.0.8",
    "com.exasol" % "exasol-script-api" % "6.0.8",
    "org.apache.hadoop" % "hadoop-aws" % "3.1.1",
    "org.apache.hadoop" % "hadoop-common" % "3.1.1"
  )

  /** Test dependencies only required in `test` */
  private val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.mockito" % "mockito-core" % "2.23.0"
  ).map(_ % Test)

  /** The list of all dependencies for the connector */
  lazy val AllDependencies: Seq[ModuleID] = CoreDependencies ++ TestDependencies

}
