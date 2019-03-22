package com.exasol.cloudetl.util

import java.nio.file.{Path => FPath}
import java.nio.file.Files

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.Var"))
class FileSystemUtilSuite extends FunSuite with BeforeAndAfterAll with Matchers {

  private var temporaryDirectory: FPath = _
  private var files: Seq[FPath] = _

  override final def beforeAll(): Unit = {
    temporaryDirectory = Files.createTempDirectory("tempdir")
    files = Seq("a", "b", "c", "a.parquet", "b.parquet").map(temporaryDirectory.resolve(_))
    files.foreach { case f => Files.createFile(f) }
    ()
  }

  override final def afterAll(): Unit = {
    files.foreach(Files.deleteIfExists)
    Files.deleteIfExists(temporaryDirectory)
    ()
  }

  test("`globWithPattern` method returns paths from a pattern") {
    val fs = FileSystem.get(new Configuration())
    val expectedPaths = files.map(f => new Path(s"file:${f.toUri.getRawPath}"))
    val pathPattern = s"${temporaryDirectory.toUri.getRawPath}/*"
    assert(FileSystemUtil.globWithPattern(pathPattern, fs).toSet === expectedPaths.toSet)
  }

  test("`globWithPattern` method returns paths from a pattern with file extensions") {
    val fs = FileSystem.get(new Configuration())
    val pathPattern = s"${temporaryDirectory.toUri.getRawPath}/*"
    val pathsWithExtensions = FileSystemUtil.globWithPattern(s"$pathPattern.parquet", fs)
    assert(pathsWithExtensions.map(_.toUri.getRawPath).forall(_.contains("parquet")))
  }

  test("`glob` method returns empty sequence if no path is available") {
    val fileSystem = FileSystem.get(new Configuration())
    assert(FileSystemUtil.glob(new Path("emptyPath"), fileSystem) === Seq.empty[Path])
  }

}
