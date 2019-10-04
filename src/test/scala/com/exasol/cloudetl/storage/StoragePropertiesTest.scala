package com.exasol.cloudetl.storage

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

@SuppressWarnings(Array("org.wartremover.warts.Var"))
class StoragePropertiesTest extends FunSuite with BeforeAndAfterEach {

  private[this] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  test("getStoragePath returns storage path property value") {
    val path = "a/bucket/path"
    properties = Map(StorageProperties.BUCKET_PATH -> path)
    assert(BaseProperties(properties).getStoragePath() === path)
  }

  test("getStoragePath throws if storage path property is not set") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getStoragePath()
    }
    assert(
      thrown.getMessage === s"Please provide a value for the "
        + s"${StorageProperties.BUCKET_PATH} property!"
    )
  }

  test("getStoragePathScheme returns path scheme value") {
    val schemes = Seq("s3a", "s3", "wasbs", "adls", "file")
    schemes.foreach { scheme =>
      val path = s"$scheme://a/path"
      properties = Map(StorageProperties.BUCKET_PATH -> path)
      assert(BaseProperties(properties).getStoragePathScheme() === scheme)
    }
  }

  test("getFileFormat returns supported file format value") {
    properties = Map(
      StorageProperties.BUCKET_PATH -> "path",
      StorageProperties.DATA_FORMAT -> "orc"
    )
    assert(BaseProperties(properties).getFileFormat() === ORC)
  }

  test("getFileFormat throws if file format is not supported") {
    val fileFormat = "a-non-supported-file-format"
    properties = Map(
      StorageProperties.BUCKET_PATH -> "path",
      StorageProperties.DATA_FORMAT -> fileFormat
    )
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getFileFormat()
    }
    assert(thrown.getMessage === s"Unsupported file format $fileFormat!")
  }

  private[this] case class BaseProperties(val params: Map[String, String])
      extends StorageProperties(params)

}
