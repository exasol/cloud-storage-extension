package com.exasol.s3etl.util

import java.nio.file.Files

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FsUtilSuite extends FunSuite with Matchers {

  test("`glob` returns paths from a pattern") {
    val fs = FileSystem.get(new Configuration())

    val directory = Files.createTempDirectory("tempdir")
    val files = Seq("a", "b", "c", "a.parquet", "b.parquet").map(directory.resolve(_))
    val _ = files.foreach { case f => Files.createFile(f) }

    val dirPattern = s"${directory.toUri.getRawPath}/*"
    val paths = files.map(f => new Path(s"file:${f.toUri.getRawPath}"))
    assert(FsUtil.globWithPattern(dirPattern, fs).toSet === paths.toSet)

    val extensions = FsUtil.globWithPattern(s"$dirPattern.parquet", fs)
    assert(extensions.map(_.toUri.getRawPath).forall(_.contains("parquet")))

    files.foreach(Files.deleteIfExists)
    Files.deleteIfExists(directory)
  }

}
