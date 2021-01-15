package com.exasol.cloudetl.source

import com.exasol.cloudetl.storage.FileFormat

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.funsuite.AnyFunSuite

class OrcSourceTest extends AnyFunSuite {

  test("stream throws if it cannot create ORC reader") {
    val nonPath = new Path("/tmp/notFile.orc")
    val thrown = intercept[java.io.FileNotFoundException] {
      val conf = new Configuration()
      Source(FileFormat("orc"), nonPath, conf, FileSystem.get(conf))
    }
    assert(thrown.getMessage === s"File $nonPath does not exist")
  }

}
