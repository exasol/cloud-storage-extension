package com.exasol.cloudetl.sbt

import sbt._
import sbt.Keys._

import sbtassembly.MergeStrategy
import sbtassembly.PathList
import sbtassembly.AssemblyPlugin.autoImport._

import com.typesafe.sbt.SbtGit.git
import scoverage.ScoverageSbtPlugin.autoImport._
import org.scalastyle.sbt.ScalastylePlugin.autoImport._
import wartremover.WartRemover.autoImport.wartremoverErrors
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._

/** A list of (boilerplate) settings for build process */
object Settings {

  def projectSettings(scalaVersion: SettingKey[String]): Seq[Setting[_]] =
    buildSettings(scalaVersion) ++ miscSettings ++ scalaStyleSettings ++ assemblySettings

  def buildSettings(scalaVersion: SettingKey[String]): Seq[Setting[_]] = Seq(
    // Compiler settings
    scalacOptions ++= Compilation.compilerFlagsFn(scalaVersion.value),
    scalacOptions in (Compile, console) := Compilation.consoleFlagsFn(scalaVersion.value),
    javacOptions ++= Compilation.JavacCompilerFlags,
    compileOrder in Compile := CompileOrder.JavaThenScala
  )

  def miscSettings(): Seq[Setting[_]] = Seq(
    // Wartremover settings
    wartremoverErrors in (Compile, compile) := Compilation.WartremoverFlags,
    wartremoverErrors in (Test, compile) := Compilation.WartremoverTestFlags,
    // General settings
    cancelable in Global := true,
    // ScalaFmt settings
    scalafmtOnCompile := true,
    // Scoverage settings
    coverageMinimum := 50,
    coverageOutputHTML := true,
    coverageOutputXML := true,
    coverageOutputCobertura := true,
    coverageFailOnMinimum := false,
    // Git versioning, use git describe
    git.useGitDescribe := true
  )

  /** Creates a Scalastyle tasks that run with unit and integration tests. */
  def scalaStyleSettings(): Seq[Setting[_]] = {
    lazy val mainScalastyle = taskKey[Unit]("mainScalastyle")
    lazy val testScalastyle = taskKey[Unit]("testScalastyle")
    Seq(
      scalastyleFailOnError := true,
      (scalastyleConfig in Compile) := (baseDirectory in ThisBuild).value / "project" / "scalastyle-config.xml",
      (scalastyleConfig in Test) := (baseDirectory in ThisBuild).value / "project" / "scalastyle-test-config.xml",
      mainScalastyle := scalastyle.in(Compile).toTask("").value,
      testScalastyle := scalastyle.in(Test).toTask("").value,
      (test in Test) := ((test in Test) dependsOn mainScalastyle).value,
      (test in Test) := ((test in Test) dependsOn testScalastyle).value,
    )
  }

  /**
   * Creates settings for integration tests.
   *
   * Use only when [[IntegrationTestPlugin]] is enabled.
   */
  def integrationTestSettings(): Seq[Setting[_]] = {
    lazy val mainScalastyle = taskKey[Unit]("mainScalastyle")
    lazy val itTestScalastyle = taskKey[Unit]("itTestScalastyle")
    Seq(
      (scalastyleConfig in IntegrationTest) := (scalastyleConfig in Test).value,
      (scalastyleSources in IntegrationTest) := Seq((scalaSource in IntegrationTest).value),
      mainScalastyle := scalastyle.in(Compile).toTask("").value,
      itTestScalastyle := scalastyle.in(IntegrationTest).toTask("").value,
      (test in IntegrationTest) := ((test in IntegrationTest) dependsOn mainScalastyle).value,
      (test in IntegrationTest) := ((test in IntegrationTest) dependsOn itTestScalastyle).value
    )
  }

  def assemblySettings(): Seq[Setting[_]] = Seq(
    test in assembly := {},
    logLevel in assembly := Level.Info,
    assemblyJarName in assembly := moduleName.value + "-" + version.value + ".jar",
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    },
    assemblyExcludedJars in assembly := {
      val cp = (fullClasspath in assembly).value
      val exludeSet = Set.empty[String]
      cp.filter { jar =>
        exludeSet(jar.data.getName)
      }
    }
  )

}
