package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaMetadata

class ImportFilesTest extends StorageTest {

  test("run throws with message to the new scriptclass name") {
    val properties = Map("BUCKET_PATH" -> "empty", "DATA_FORMAT" -> "avro")
    val thrown = intercept[ImportScriptClassException] {
      ImportFiles.run(mock[ExaMetadata], mockExasolIterator(properties))
    }
    assert(thrown.getMessage().contains("Please use the FilesDataImporter"))
  }

}
