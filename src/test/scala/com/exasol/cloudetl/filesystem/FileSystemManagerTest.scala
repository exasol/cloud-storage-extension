package com.exasol.cloudetl.filesystem

import java.io.FileNotFoundException
import java.nio.file.{Path => FPath}
import java.nio.file.Files
import java.nio.file.Paths

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class FileSystemManagerTest extends AnyFunSuite with BeforeAndAfterEach {

  private[this] var hidden: Seq[FPath] = _
  private[this] var files: Seq[FPath] = _
  private[this] var fileSystem: FileSystem = _
  private[this] var temporaryDirectory: FPath = _

  override final def beforeEach(): Unit = {
    fileSystem = FileSystem.get(new Configuration())
    temporaryDirectory = Files.createTempDirectory("tempdir")
    files = Seq("a", "b", "c", "a.parquet", "b.parquet").map(temporaryDirectory.resolve(_))
    hidden = Seq("_SUCCESS", ".hidden").map(temporaryDirectory.resolve(_))
    (files ++ hidden).foreach { case f => Files.createFile(f) }
    ()
  }

  override final def afterEach(): Unit = {
    files.foreach(Files.deleteIfExists(_))
    hidden.foreach(Files.deleteIfExists(_))
    Files.deleteIfExists(temporaryDirectory)
    ()
  }

  private[this] def getDirPath(): String = temporaryDirectory.toUri().getRawPath()
  private[this] def getFiles(pattern: String): Set[Path] =
    FileSystemManager(fileSystem).getFiles(pattern).toSet
  private[this] def getDefaultExpectedPaths(): Set[Path] =
    files.map(f => new Path(s"file:${f.toUri.getRawPath}")).toSet

  test("getFiles returns paths from a pattern") {
    val pathPattern = s"${getDirPath()}/*"
    assert(getFiles(pathPattern) === getDefaultExpectedPaths())
  }

  test("getFiles returns paths from a regular path without glob") {
    assert(getFiles(getDirPath()) === getDefaultExpectedPaths)
  }

  test("getFiles returns paths from sub directories") {
    val subDirectory = Files.createDirectories(Paths.get(s"${getDirPath()}/subDir/"))
    val subDirectoryFile = Files.createFile(subDirectory.resolve("aa.parquet"))
    val expectedPaths = getDefaultExpectedPaths ++ Set(
      new Path(s"file:${subDirectoryFile.toUri().getRawPath()}")
    )
    assert(getFiles(getDirPath()) === expectedPaths)
    Files.deleteIfExists(subDirectoryFile)
    Files.deleteIfExists(subDirectory)
  }

  test("getFiles returns paths from a pattern with file extensions") {
    val pathPattern = s"${getDirPath()}/*.parquet"
    val pathsWithExtensions = getFiles(pathPattern)
    assert(pathsWithExtensions.map(_.toUri().getRawPath()).forall(_.contains("parquet")))
  }

  test("getFiles throws if no path exists") {
    val thrown = intercept[FileNotFoundException] {
      FileSystemManager(fileSystem).getFiles("emptyPath")
    }
    assert(thrown.getMessage().contains("Provided file path 'emptyPath' does not exist."))
  }

}
