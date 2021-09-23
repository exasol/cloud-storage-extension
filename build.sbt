import com.exasol.cloudetl.sbt.Dependencies
import com.exasol.cloudetl.sbt.Settings
import com.exasol.cloudetl.sbt.IntegrationTestPlugin

// Adding this since dependency excludes are not properly handled using coursier.
// See: https://github.com/sbt/sbt/issues/5170
// Enable coursier once the issue is resolved.
ThisBuild / useCoursier := false

lazy val orgSettings = Seq(
  name := "cloud-storage-extension",
  description := "Exasol Cloud Storage Import and Export Extension",
  organization := "com.exasol",
  organizationHomepage := Some(url("http://www.exasol.com"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.12.15",
  crossScalaVersions := Seq("2.11.12", "2.12.15")
)

lazy val root =
  project
    .in(file("."))
    .settings(moduleName := "exasol-cloud-storage-extension")
    .settings(version := "2.0.0")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.projectSettings(scalaVersion))
    .settings(
      resolvers ++= Dependencies.Resolvers,
      libraryDependencies ++= Dependencies.StorageDependencies,
      libraryDependencies ++= Dependencies.TestDependencies,
      excludeDependencies ++= Dependencies.ExcludedDependencies
    )
    .enablePlugins(IntegrationTestPlugin, ReproducibleBuildsPlugin)

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")
