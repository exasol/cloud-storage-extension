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

  private[this] def assertAzureAbfsBucket(bucket: Bucket, extraMappings: Map[String, String]): Unit = {
    assert(bucket.isInstanceOf[AzureAbfsBucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.abfs.impl" -> classOf[AzureBlobFileSystem].getName,
      "fs.abfss.impl" -> classOf[SecureAzureBlobFileSystem].getName,
      "fs.AbstractFileSystem.abfs.impl" -> classOf[Abfs].getName,
      "fs.AbstractFileSystem.abfss.impl" -> classOf[Abfss].getName
    )
    (defaultMappings ++ extraMappings).foreach { case (given, expected) =>
      assert(conf.get(given) === expected)
    }
  }

  test("apply throws if Azure abfs path is not valid") {
    val path = "abfss://container@account1.dfs.windows.net/data/"
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    properties = defaultProperties ++ Map(PATH -> path, "CONNECTION_NAME" -> "connection_info")
    val thrown = intercept[BucketValidationException] {
      getBucket(properties, exaMetadata).getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-20"))
    assert(thrown.getMessage().contains(s"path '$path' is not valid."))
  }

  test("apply throws if no connection name is provided") {
    properties = defaultProperties
    assertNoConnectionName(getBucket(properties).validate())
  }

  test("apply throws if secret key is provided as parameter") {
    properties = defaultProperties ++ Map("AZURE_SECRET_KEY" -> "secret")
    assertForbiddenProperty(getBucket(properties).validate())
  }

  test("apply returns secret from connection object (account name from path)") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAbfsBucket(bucket, Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret"))
  }

  test("apply returns secret from connection object with account name") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "AZURE_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAbfsBucket(bucket, Map("fs.azure.account.key.account1.dfs.core.windows.net" -> "secret"))
  }

}
