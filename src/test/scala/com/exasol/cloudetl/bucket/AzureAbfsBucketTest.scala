package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.azurebfs.Abfs
import org.apache.hadoop.fs.azurebfs.Abfss
import org.apache.hadoop.fs.azurebfs.AzureBlobFileSystem
import org.apache.hadoop.fs.azurebfs.SecureAzureBlobFileSystem

class AzureAbfsBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "abfs://container1@account1.dfs.core.windows.net/data/",
    FORMAT -> "PARQUET"
  )

  private[this] def assertAzureAbfsBucket(
    bucket: Bucket,
    extraMappings: Map[String, String]
  ): Unit = {
    assert(bucket.isInstanceOf[AzureAbfsBucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.abfs.impl" -> classOf[AzureBlobFileSystem].getName,
      "fs.abfss.impl" -> classOf[SecureAzureBlobFileSystem].getName,
      "fs.AbstractFileSystem.abfs.impl" -> classOf[Abfs].getName,
      "fs.AbstractFileSystem.abfss.impl" -> classOf[Abfss].getName
    )
    (defaultMappings ++ extraMappings).foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

  test("apply throws if Azure Abfs path is not valid") {
    val path = "abfss://container@account1.dfs.windows.net/data/"
    properties = defaultProperties ++ Map(PATH -> path, "AZURE_SECRET_KEY" -> "secret")
    val thrown = intercept[IllegalArgumentException] {
      assertAzureAbfsBucket(getBucket(properties), Map.empty[String, String])
    }
    assert(thrown.getMessage === s"Invalid Azure datalake abfs(s) path: $path!")
  }

  test("apply throws if no connection name or no secret key is provided") {
    properties = defaultProperties
    val thrown = intercept[IllegalArgumentException] {
      assertAzureAbfsBucket(getBucket(properties), Map.empty[String, String])
    }
    val expected = "Please provide either CONNECTION_NAME property or secure access " +
      "credentials parameters, but not the both!"
    assert(thrown.getMessage === expected)
  }

  test("apply returns AzureAbfsBucket with secret key (account name is obtained from path)") {
    properties = defaultProperties ++ Map("AZURE_SECRET_KEY" -> "secret")
    val bucket = getBucket(properties)
    assertAzureAbfsBucket(
      bucket,
      Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret")
    )
  }

  test("apply returns AzureAbfsBucket with account name and secret key") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SECRET_KEY" -> "secret"
    )
    val bucket = getBucket(properties)
    assertAzureAbfsBucket(
      bucket,
      Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret")
    )
  }

  test("apply returns secret from password of connection object (account name from path)") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAbfsBucket(
      bucket,
      Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret")
    )
  }

  test("apply returns secret from password of connection object with account name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAbfsBucket(
      bucket,
      Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret")
    )
  }

}
