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
    .settings(moduleName := "cloud-storage-etl-udfs")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.projectSettings(scalaVersion))
    .enablePlugins(IntegrationTestPlugin, GitVersioning)

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
