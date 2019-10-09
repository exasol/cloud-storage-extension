package com.exasol.cloudetl.source

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.cloudetl.storage.FileFormat
import com.exasol.cloudetl.util.FileSystemUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

class SourceTest extends FunSuite with BeforeAndAfterEach {

  private[this] var conf: Configuration = _
  private[this] var fileSystem: FileSystem = _

  private[source] val format: String = "dummy"
  private[source] var resourceDir: Path = _

  override final def beforeEach(): Unit = {
    conf = new Configuration()
    fileSystem = FileSystem.get(conf)
    resourceDir = Paths.get(getClass.getResource(s"/data/import/$format").toURI).toAbsolutePath
    ()
  }

  final def getConf(): Configuration = conf

  final def getFileSystem(): FileSystem = fileSystem

  final def getRecordsCount(filePath: Path): Int = {
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    globbedFilePath.map { file =>
      val source = Source(FileFormat(format), file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum
  }

}
