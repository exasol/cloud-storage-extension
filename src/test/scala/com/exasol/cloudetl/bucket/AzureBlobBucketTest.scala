package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.azure.NativeAzureFileSystem
import org.apache.hadoop.fs.azure.Wasb
import org.apache.hadoop.fs.azure.Wasbs
import org.scalatest.prop.TableDrivenPropertyChecks._

class AzureBlobBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "wasbs://container1@account1.blob.core.windows.net/orc-data/",
    FORMAT -> "ORC"
  )

  private[this] val secretKey = "secret"
  private[this] val sasToken = "token"

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
    val path = "wasb://container@account1.blob.windows.net/data/"
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
    val thrown = intercept[BucketValidationException] {
      getBucket(properties, exaMetadata).getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-19"))
    assert(thrown.getMessage().contains(s"path '$path' scheme is not valid."))
  }

  val invalidAzureBlobPaths = Table(
    "path",
    "wasbs://container1@account1.blob.core.windows.net",
    "wasbs://container1@.blob.core.windows.net/orc-data/",
    "wasbs://container1@account1.dfs.core.windows.net/orc-data/",
    "wasbs://container1@account1.blob.windows.net/orc-data/"
  )
  forAll(invalidAzureBlobPaths) { path =>
    test(s"apply throws if Azure Blob path '$path' does not match the expected format") {
      properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
      val exaMetadata = mockConnectionInfo("", s"AZURE_SECRET_KEY=$secretKey")
      val thrown = intercept[BucketValidationException] {
        getBucket(properties, exaMetadata).getConfiguration()
      }
      assert(thrown.getMessage().startsWith("E-CSE-19"))
      assert(thrown.getMessage().contains(s"path '$path' scheme is not valid."))
    }
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

  val azureBlobPaths = Table(
    ("description", "path", "expectedAccountName", "expectedContainerName"),
    (
      "wasbs path with trailing slash",
      "wasbs://container1@account1.blob.core.windows.net/orc-data/",
      "account1",
      "container1"
    ),
    (
      "wasb path with nested file path",
      "wasb://container-2@account2.blob.core.windows.net/parquet-data/year=2026/file.parquet",
      "account2",
      "container-2"
    )
  )
  forAll(azureBlobPaths) { (description, path, expectedAccountName, _) =>
    test(s"apply parses account name from Azure Blob $description") {
      properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
      val exaMetadata = mockConnectionInfo("", s"AZURE_SECRET_KEY=$secretKey")
      val bucket = getBucket(properties, exaMetadata)
      assertAzureBlobBucket(
        bucket,
        Map(s"fs.azure.account.key.$expectedAccountName.blob.core.windows.net" -> secretKey)
      )
    }
  }

  forAll(azureBlobPaths) { (description, path, expectedAccountName, expectedContainerName) =>
    test(s"apply parses account and container names from Azure Blob $description for sas token") {
      properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
      val exaMetadata = mockConnectionInfo("", s"AZURE_SAS_TOKEN=$sasToken")
      val bucket = getBucket(properties, exaMetadata)
      assertAzureBlobBucket(
        bucket,
        Map(s"fs.azure.sas.$expectedContainerName.$expectedAccountName.blob.core.windows.net" -> sasToken)
      )
    }
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
