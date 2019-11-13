package com.exasol.cloudetl.storage

import com.exasol.ExaConnectionInformation
import com.exasol.ExaMetadata

import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar

class StoragePropertiesTest extends FunSuite with BeforeAndAfterEach with MockitoSugar {

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
    assert(BaseProperties(properties).getFileFormat() === FileFormat.ORC)
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

  test("getParallelism returns user provided value") {
    properties = Map(StorageProperties.PARALLELISM -> "2")
    assert(BaseProperties(properties).getParallelism("default") === "2")
  }

  test("getParallelism returns default value if parallelism is not set") {
    assert(BaseProperties(properties).getParallelism("nproc()") === "nproc()")
  }

  test("getConnectionInformation throws if Exasol metadata is not provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getConnectionInformation()
    }
    assert(thrown.getMessage === "Exasol metadata is None!")
  }

  test("getConnectionInformation returns storage connection information") {
    properties = Map(StorageProperties.CONNECTION_NAME -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo: ExaConnectionInformation = new ExaConnectionInformation() {
      override def getType(): ExaConnectionInformation.ConnectionType =
        ExaConnectionInformation.ConnectionType.PASSWORD
      override def getAddress(): String = ""
      override def getUser(): String = "user"
      override def getPassword(): String = "secret"
    }

    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    assert(StorageProperties(properties, metadata).getConnectionInformation() === connectionInfo)
    verify(metadata, times(1)).getConnection("connection_info")
  }

  test("hasNamedConnection returns false by default") {
    assert(BaseProperties(properties).hasNamedConnection() === false)
  }

  test("hasNamedConnection returns true if connection name is set") {
    properties = Map(StorageProperties.CONNECTION_NAME -> "named_connection")
    assert(BaseProperties(properties).hasNamedConnection() === true)
  }

  test("mkString returns empty string by default") {
    val str = BaseProperties(properties).mkString()
    assert(str.isEmpty === true)
    assert(str === "")
  }

  test("mkString returns key-value properties string") {
    properties = Map("k1" -> "v1", "k3" -> "v3", "k2" -> "v2")
    val expected = "k1 -> v1;k2 -> v2;k3 -> v3"
    assert(BaseProperties(properties).mkString() === expected)
  }

  test("apply(map) returns correct StorageProperties") {
    properties = Map("a" -> "b")
    val baseProperty = BaseProperties(properties)
    assert(StorageProperties(properties) === baseProperty)
  }

  test("apply(map) with Exasol metadata returns correct StorageProperties") {
    properties = Map("k" -> "v")
    val metadata = mock[ExaMetadata]
    val expected = StorageProperties(properties, metadata)
    assert(StorageProperties(properties, metadata) === expected)
  }

  test("apply(string) throws if input string does not contain separator") {
    val thrown = intercept[IllegalArgumentException] {
      StorageProperties("")
    }
    assert(thrown.getMessage === s"The input string is not separated by ';'!")
  }

  test("apply(string) returns correct StorageProperties") {
    properties = Map("k3" -> "v3", "k2" -> "v2")
    val baseProperty = BaseProperties(properties)
    val mkStringResult = baseProperty.mkString()
    assert(StorageProperties(mkStringResult) === baseProperty)
  }

  test("apply(string) with Exasol metadata returns correct StorageProperties") {
    properties = Map("k1" -> "v1", "k2" -> "v2")
    val metadata = mock[ExaMetadata]
    val expected = StorageProperties(properties, metadata)
    val mkStringResult = BaseProperties(properties).mkString()
    assert(StorageProperties(mkStringResult, metadata) === expected)
  }

  private[this] case class BaseProperties(val params: Map[String, String])
      extends StorageProperties(params, None)

}
