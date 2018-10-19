import com.exasol.s3etl.sbt.Settings
import com.exasol.s3etl.sbt.IntegrationTestPlugin

lazy val orgSettings = Seq(
  name := "s3etl",
  description := "Exasol S3 ETL User Defined Functions",
  organization := "com.exasol",
  organizationHomepage := Some(url("http://www.exasol.com"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.10.6", "2.11.12")
)

lazy val root =
  project
    .in(file("."))
    .settings(moduleName := "s3etl")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.projectSettings(scalaVersion))
    .enablePlugins(IntegrationTestPlugin, GitVersioning)

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
