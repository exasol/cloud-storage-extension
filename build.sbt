import com.exasol.cloudetl.sbt.Dependencies
import com.exasol.cloudetl.sbt.Settings
import com.exasol.cloudetl.sbt.IntegrationTestPlugin

lazy val orgSettings = Seq(
  name := "cloud-storage-extension",
  description := "Exasol Cloud Storage Import and Export Extension",
  organization := "com.exasol",
  organizationHomepage := Some(url("http://www.exasol.com"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.12.12",
  crossScalaVersions := Seq("2.11.12", "2.12.12")
)

lazy val root =
  project
    .in(file("."))
    .settings(moduleName := "cloud-storage-extension")
    .settings(version := "1.1.0")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.projectSettings(scalaVersion))
    .settings(
      resolvers ++= Dependencies.Resolvers,
      libraryDependencies ++= Dependencies.StorageDependencies,
      libraryDependencies ++= Dependencies.TestDependencies,
      excludeDependencies ++= Dependencies.ExcludedDependencies
    )
    .enablePlugins(IntegrationTestPlugin, GitVersioning)

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
