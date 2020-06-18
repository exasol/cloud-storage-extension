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
    .enablePlugins(IntegrationTestPlugin)
    .disablePlugins(AssemblyPlugin)
    .aggregate(common, storage, streamingkafka, streamingkinesis)

lazy val common =
  project
    .in(file("common"))
    .settings(moduleName := "common")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(
      resolvers ++= Dependencies.ExasolResolvers,
      libraryDependencies ++= Dependencies.CommonDependencies,
      libraryDependencies ++= Dependencies.JacksonDependencies,
      dependencyOverrides ++= Dependencies.JacksonDependencies
    )
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .disablePlugins(AssemblyPlugin)

lazy val storage =
  project
    .in(file("storage"))
    .settings(moduleName := "storage")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.assemblySettings)
    .settings(libraryDependencies ++= Dependencies.StorageDependencies)
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")

lazy val streamingkafka =
  project
    .in(file("streaming-kafka"))
    .settings(moduleName := "streaming-kafka")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.assemblySettings)
    .settings(
      resolvers ++= Dependencies.ConfluentResolvers,
      libraryDependencies ++= Dependencies.KafkaDependencies,
      excludeDependencies ++= Dependencies.KafkaExcludedDependencies
    )
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")

lazy val streamingkinesis =
  project
    .in(file("streaming-kinesis"))
    .settings(moduleName := "streaming-kinesis")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.commonSettings(scalaVersion))
    .settings(Settings.assemblySettings)
    .settings(libraryDependencies ++= Dependencies.KinesisDependencies)
    .enablePlugins(IntegrationTestPlugin, GitVersioning)
    .dependsOn(common % "compile->compile;test->test")

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
