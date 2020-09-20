package com.exasol.cloudetl.source

class SourceTest extends AbstractSourceTest {

  override val format: String = "avro"

  test("apply throws if file format is not supported") {
    val path = new org.apache.hadoop.fs.Path(s"$resourceDir/sales10.avro")
    val thrown = intercept[IllegalArgumentException] {
      getSource(path, "file")
    }
    assert(thrown.getMessage === "Unsupported storage format: 'FILE'")
  }

}
