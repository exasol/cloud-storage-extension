package com.exasol.cloudetl.source

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.cloudetl.util.FileSystemUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Var"))
class OrcSourceSuite extends FunSuite with BeforeAndAfterAll with Matchers {

  private var conf: Configuration = _
  private var fileSystem: FileSystem = _
  private var orcResourceFolder: Path = _

  override final def beforeAll(): Unit = {
    conf = new Configuration()
    fileSystem = FileSystem.get(conf)
    orcResourceFolder = Paths.get(getClass.getResource("/data/import/orc").toURI).toAbsolutePath
    ()
  }

  test("reads a single orc format file") {
    val filePath = Paths.get(s"$orcResourceFolder/sales1*.orc")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    val source = OrcSource(globbedFilePath, fileSystem, conf)
    assert(source.stream.map(_.size).sum === 999)
  }

}
