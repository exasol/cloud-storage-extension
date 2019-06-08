package com.exasol.cloudetl.source

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.cloudetl.util.FileSystemUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.Var"))
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
    val filePath = Paths.get(s"$orcResourceFolder/sales*.orc")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    val result = globbedFilePath.map { file =>
      val source = OrcSource(file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum

    assert(result === 1998)
  }

  test("reads an employee data orc file") {
    val filePath = Paths.get(s"$orcResourceFolder/employee*.orc")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    val result = globbedFilePath.map { file =>
      val source = OrcSource(file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum

    assert(result === 438304)
  }

}
