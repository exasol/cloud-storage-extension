package com.exasol.cloudetl.orc

import java.nio.file.Path

import com.exasol.cloudetl.TestFileManager
import com.exasol.cloudetl.source.OrcSource
import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription
import org.apache.orc.Writer
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class BaseOrcConverterTest extends AnyFunSuite with BeforeAndAfterEach with TestFileManager {

  protected var conf: Configuration = _
  protected var fileSystem: FileSystem = _
  protected var path: HPath = _
  protected var orcWriter: OrcTestDataWriter = _
  private[this] var outputDirectory: Path = _

  override final def beforeEach(): Unit = {
    conf = new Configuration
    fileSystem = FileSystem.get(conf)
    outputDirectory = createTemporaryFolder("orc-tests-")
    path = new HPath(outputDirectory.toUri.toString, "orc-file.orc")
    orcWriter = new OrcTestDataWriter(path, conf)
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    ()
  }

  protected final def withWriter(schema: TypeDescription)(block: Writer => Unit): Unit = {
    val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(schema))
    block(writer)
    writer.close()
  }

  protected final def getRecords(): Seq[Row] = {
    val src = OrcSource(path, conf, fileSystem)
    val records = src.stream().toSeq
    src.close()
    records
  }

}
