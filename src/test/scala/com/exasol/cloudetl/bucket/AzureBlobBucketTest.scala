package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.azure.NativeAzureFileSystem
import org.apache.hadoop.fs.azure.Wasb
import org.apache.hadoop.fs.azure.Wasbs

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class AzureBlobBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "wasbs://container1@account1.blob.core.windows.net/orc-data/",
    FORMAT -> "ORC"
  )

  private[this] def assertAzureBlobBucket(
    bucket: Bucket,
    extraMappings: Map[String, String]
  ): Unit = {
    assert(bucket.isInstanceOf[AzureBlobBucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.azure" -> classOf[NativeAzureFileSystem].getName,
      "fs.wasb.impl" -> classOf[NativeAzureFileSystem].getName,
      "fs.wasbs.impl" -> classOf[NativeAzureFileSystem].getName,
      "fs.AbstractFileSystem.wasb.impl" -> classOf[Wasb].getName,
      "fs.AbstractFileSystem.wasbs.impl" -> classOf[Wasbs].getName
    )
    (defaultMappings ++ extraMappings).foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

  test("apply throws if Azure Blob path is not valid") {
    val path = "wasb://container@account1.blob.windows.net/data/"
    properties = defaultProperties ++ Map(PATH -> path, "AZURE_SECRET_KEY" -> "secret")
    val thrown = intercept[IllegalArgumentException] {
      assertAzureBlobBucket(getBucket(properties), Map.empty[String, String])
    }
    assert(thrown.getMessage === s"Invalid Azure blob wasb(s) path: $path!")
  }

  test("apply throws if no connection name or credential (secret key or sas token) is provided") {
    properties = defaultProperties
    val thrown = intercept[IllegalArgumentException] {
      assertAzureBlobBucket(getBucket(properties), Map.empty[String, String])
    }
    val expected = "Please provide either CONNECTION_NAME property or secure access " +
      "credentials parameters, but not the both!"
    assert(thrown.getMessage === expected)
  }

  test("apply returns AzureBlobBucket with account name and secret key") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SECRET_KEY" -> "secret"
    )
    val bucket = getBucket(properties)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret")
    )
  }

  test("apply returns AzureBlobBucket with secret key") {
    properties = defaultProperties ++ Map("AZURE_SECRET_KEY" -> "secret")
    val bucket = getBucket(properties)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret")
    )
  }

  test("apply returns AzureBlobBucket with account, container name and sas token") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SAS_TOKEN" -> "token",
      "AZURE_CONTAINER_NAME" -> "container1"
    )
    val bucket = getBucket(properties)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token")
    )
  }

  test("apply returns AzureBlobBucket with sas token") {
    properties = defaultProperties ++ Map("AZURE_SAS_TOKEN" -> "token")
    val bucket = getBucket(properties)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token")
    )
  }

  test("apply returns secret from password of connection object with account name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret")
    )
  }

  test("apply returns secret from password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret")
    )
  }

  test("apply returns sas token from password of connection with account, container name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_CONTAINER_NAME" -> "container1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token")
    )
  }

  test("apply returns sas token from password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token")
    )
  }

  test("apply returns sas from connection object if both sas and secret are provided") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_CONTAINER_NAME" -> "container1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret;AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(
      bucket,
      Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token")
    )
  }

}
