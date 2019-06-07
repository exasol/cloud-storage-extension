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
class AvroSourceSuite extends FunSuite with BeforeAndAfterAll with Matchers {

  private var conf: Configuration = _
  private var fileSystem: FileSystem = _
  private var avroResourceFolder: Path = _

  override final def beforeAll(): Unit = {
    conf = new Configuration()
    fileSystem = FileSystem.get(conf)
    avroResourceFolder = Paths.get(getClass.getResource("/data/import/avro").toURI).toAbsolutePath
    ()
  }

  test("reads the sales avro format files") {
    val filePath = Paths.get(s"$avroResourceFolder/sales1*.avro")
    val globbedFilePath = FileSystemUtil.globWithLocal(filePath, fileSystem)
    val result = globbedFilePath.map { file =>
      val source = AvroSource(file, conf, fileSystem)
      val cnt = source.stream().size
      source.close()
      cnt
    }.sum

    assert(result === 1998)
  }

}
