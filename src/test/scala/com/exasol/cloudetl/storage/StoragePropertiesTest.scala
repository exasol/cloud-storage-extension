package com.exasol.cloudetl.storage

import com.exasol.{ExaConnectionInformation, ExaMetadata}

import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class StoragePropertiesTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

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

  test("getStoragePathScheme returns path scheme with regex pattern") {
    val path = "s3a://bucket/{year=2019/month=1,year=2019/month=2}/*"
    properties = Map(StorageProperties.BUCKET_PATH -> path)
    assert(BaseProperties(properties).getStoragePathScheme() === "s3a")
  }

  test("getDeltaFormatLogStoreClassName returns storage class name for scheme") {
    val data = Map(
      "s3a" -> "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore",
      "abfs" -> "org.apache.spark.sql.delta.storage.AzureLogStore",
      "abfss" -> "org.apache.spark.sql.delta.storage.AzureLogStore",
      "adl" -> "org.apache.spark.sql.delta.storage.AzureLogStore",
      "wasbs" -> "org.apache.spark.sql.delta.storage.AzureLogStore",
      "wasb" -> "org.apache.spark.sql.delta.storage.AzureLogStore",
      "file" -> "org.apache.spark.sql.delta.storage.HDFSLogStore"
    )
    data.foreach {
      case (scheme, expected) =>
        val path = s"$scheme://a/path"
        properties = Map(StorageProperties.BUCKET_PATH -> path)
        assert(BaseProperties(properties).getDeltaFormatLogStoreClassName() === expected)
    }
  }

  test("getDeltaFormatLogStoreClassName throws for unsupported storage systems") {
    Seq("gs").foreach { scheme =>
      val path = s"$scheme://a/path"
      properties = Map(StorageProperties.BUCKET_PATH -> path)
      val thrown = intercept[UnsupportedOperationException] {
        BaseProperties(properties).getDeltaFormatLogStoreClassName()
      }
      assert(thrown.getMessage.startsWith("Delta format LogStore API is not supported"))
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

  test("isOverwrite returns true is set") {
    properties = Map(StorageProperties.OVERWRITE -> "true")
    assert(BaseProperties(properties).isOverwrite() === true)
  }

  test("isOverwrite returns default false value if it is not set") {
    assert(BaseProperties(properties).isOverwrite() === false)
  }

  final def newConnectionInformation(
    username: String,
    password: String
  ): ExaConnectionInformation =
    new ExaConnectionInformation() {
      override def getType(): ExaConnectionInformation.ConnectionType =
        ExaConnectionInformation.ConnectionType.PASSWORD
      override def getAddress(): String = ""
      override def getUser(): String = username
      override def getPassword(): String = password
    }

  test("merge returns StorageProperties with new properties") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo = newConnectionInformation("", "KEY1=secret1==;KEY2=sec=ret2;KEY3=secret")
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    val storageProperties = StorageProperties(properties, metadata).merge("")
    assert(storageProperties.getString("KEY1") === "secret1==")
    assert(storageProperties.getString("KEY2") === "sec=ret2")
    assert(storageProperties.getString("KEY3") === "secret")
  }

  test("merge returns with keyForUsername mapped to connection username") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo = newConnectionInformation("usernameValue", "KEY1=secret1")
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    val storageProperties = StorageProperties(properties, metadata).merge("usernameKey")
    assert(storageProperties.getString("usernameKey") === "usernameValue")
    assert(storageProperties.getString("KEY1") === "secret1")
  }

  test("merge returns with keyForUsername -> connection username overwritted") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo =
      newConnectionInformation("usernameValue", "KEY1=secret1;usernameKey=newUsername")
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    val storageProperties = StorageProperties(properties, metadata).merge("usernameKey")
    assert(storageProperties.getString("usernameKey") === "newUsername")
    assert(storageProperties.getString("KEY1") === "secret1")
  }

  test("merge throws if it cannot find key=value pairs in connection passoword") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo = newConnectionInformation("", "secret1;key=value")
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    val thrown = intercept[IllegalArgumentException] {
      StorageProperties(properties, metadata).merge("")
    }
    assert(thrown.getMessage === "Connection object password does not contain key=value pairs!")
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
