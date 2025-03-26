package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.azure.NativeAzureFileSystem
import org.apache.hadoop.fs.azure.Wasb
import org.apache.hadoop.fs.azure.Wasbs

class AzureBlobBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "wasbs://container1@account1.blob.core.windows.net/orc-data/",
    FORMAT -> "ORC"
  )

  private[this] def assertAzureBlobBucket(bucket: Bucket, extraMappings: Map[String, String]): Unit = {
    assert(bucket.isInstanceOf[AzureBlobBucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.azure" -> classOf[NativeAzureFileSystem].getName(),
      "fs.wasb.impl" -> classOf[NativeAzureFileSystem].getName(),
      "fs.wasbs.impl" -> classOf[NativeAzureFileSystem].getName(),
      "fs.AbstractFileSystem.wasb.impl" -> classOf[Wasb].getName(),
      "fs.AbstractFileSystem.wasbs.impl" -> classOf[Wasbs].getName()
    )
    (defaultMappings ++ extraMappings).foreach { case (given, expected) =>
      assert(conf.get(given) === expected)
    }
  }

  test("apply throws if Azure Blob path is not valid") {
    val path = "wasbs://container@wrongdomain/data/"
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
    val thrown = intercept[BucketValidationException] {
      getBucket(properties, exaMetadata).getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-19"))
    assert(thrown.getMessage().contains(s"path '$path' scheme is not valid."))
  }

  test("apply throws if no connection name is provided") {
    properties = defaultProperties
    assertNoConnectionName(getBucket(properties).validate())
  }

  test("apply throws if secret key is provided as parameter") {
    properties = defaultProperties ++ Map("AZURE_SECRET_KEY" -> "secret")
    assertForbiddenProperty(getBucket(properties).validate())
  }

  test("apply throws if sas token is provided as parameter") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SAS_TOKEN" -> "token",
      "AZURE_CONTAINER_NAME" -> "container1"
    )
    assertForbiddenProperty(getBucket(properties).validate())
  }

  test("apply returns secret from password of connection object with account name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret"))
  }

  test("apply returns secret from password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret"))
  }

  test("check private DNS urls are handled properly") {
    properties = Map(
      PATH -> "wasbs://container1@account1.custom.domain/orc-data/",
      FORMAT -> "ORC",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.account.key.account1.blob.core.windows.net" -> "secret"))
  }

  test("apply returns sas token from password of connection with account, container name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_CONTAINER_NAME" -> "container1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token"))
  }

  test("apply returns sas token from password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token"))
  }

  test("apply returns sas from connection object if both sas and secret are provided") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_CONTAINER_NAME" -> "container1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret;AZURE_SAS_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureBlobBucket(bucket, Map("fs.azure.sas.container1.account1.blob.core.windows.net" -> "token"))
  }

}
