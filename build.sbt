import com.exasol.cloudetl.sbt.Dependencies
import com.exasol.cloudetl.sbt.Settings
import com.exasol.cloudetl.sbt.IntegrationTestPlugin

lazy val orgSettings = Seq(
  name := "cloud-storage-etl-udfs",
  description := "Exasol Public Cloud Storage ETL User Defined Functions",
  organization := "com.exasol",
  organizationHomepage := Some(url("http://www.exasol.com"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.12.10",
  crossScalaVersions := Seq("2.11.12", "2.12.10")
)

lazy val root =
  project
    .in(file("."))
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .disablePlugins(AssemblyPlugin, IntegrationTestPlugin, GitVersioning)
    .aggregate(common, avro, storage, streamingkafka, streamingkinesis)

lazy val common =
  project
    .in(file("common"))
    .settings(moduleName := "import-export-udf-common-scala")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(
      resolvers ++= Dependencies.ExasolResolvers,
      libraryDependencies ++= Dependencies.CommonDependencies
    )
    .disablePlugins(AssemblyPlugin, IntegrationTestPlugin, GitVersioning)

lazy val avro =
  project
    .in(file("avro"))
    .settings(moduleName := "import-export-udf-avro-scala")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(libraryDependencies ++= Dependencies.AvroDependencies)
    .dependsOn(common % "compile->compile;test->test")
    .disablePlugins(AssemblyPlugin, IntegrationTestPlugin, GitVersioning)

lazy val storage =
  project
    .in(file("storage"))
    .settings(moduleName := "exasol-cloud-storage-extension")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.integrationTestSettings)
    .settings(Settings.assemblySettings)
    .settings(
      libraryDependencies ++= Dependencies.StorageDependencies,
      libraryDependencies ++= Dependencies.JacksonDependencies,
      dependencyOverrides ++= Dependencies.JacksonDependencies
    )
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")
    .dependsOn(avro % "compile->compile")

lazy val streamingkafka =
  project
    .in(file("streaming-kafka"))
    .settings(moduleName := "exasol-kafka-consumer-extension")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.integrationTestSettings)
    .settings(Settings.assemblySettings)
    .settings(
      resolvers ++= Dependencies.ConfluentResolvers,
      libraryDependencies ++= Dependencies.KafkaDependencies,
      excludeDependencies ++= Dependencies.KafkaExcludedDependencies
    )
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")
    .dependsOn(avro % "compile->compile")

lazy val streamingkinesis =
  project
    .in(file("streaming-kinesis"))
    .settings(moduleName := "exasol-kinesis-consumer-extension")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.integrationTestSettings)
    .settings(Settings.assemblySettings)
    .settings(libraryDependencies ++= Dependencies.KinesisDependencies)
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
