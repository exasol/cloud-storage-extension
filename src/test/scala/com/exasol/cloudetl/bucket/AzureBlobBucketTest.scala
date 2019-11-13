package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.azure.NativeAzureFileSystem
import org.apache.hadoop.fs.azure.Wasb
import org.apache.hadoop.fs.azure.Wasbs

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class AzureBlobBucketTest extends AbstractBucketTest {

  private[this] def assertAzureBlobBucket(bucket: Bucket): Unit = {
    assert(bucket.isInstanceOf[AzureBlobBucket])
    val conf = bucket.getConfiguration()
    val mappings = Map(
      "fs.azure" -> classOf[NativeAzureFileSystem].getName,
      "fs.wasb.impl" -> classOf[NativeAzureFileSystem].getName,
      "fs.wasbs.impl" -> classOf[NativeAzureFileSystem].getName,
      "fs.AbstractFileSystem.wasb.impl" -> classOf[Wasb].getName,
      "fs.AbstractFileSystem.wasbs.impl" -> classOf[Wasbs].getName
    )
    mappings.foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

  private[this] val defaultProperties = Map(
    PATH -> "wasbs://container@account1/parquet-bucket/",
    FORMAT -> "ORC"
  )

  test("getConfiguration throws if account name is not provided") {
    properties = defaultProperties
    val thrown = intercept[IllegalArgumentException] {
      assertAzureBlobBucket(getBucket(properties))
    }
    assert(thrown.getMessage === "Please provide a value for the AZURE_ACCOUNT_NAME property!")
  }

  test("getConfiguration throws if neither secret key nor sas token account is provided") {
    properties = defaultProperties ++ Map("AZURE_ACCOUNT_NAME" -> "account1")
    val thrown = intercept[IllegalArgumentException] {
      assertAzureBlobBucket(getBucket(properties))
    }
    assert(
      thrown.getMessage === "Please provide a value for either " +
        "AZURE_SECRET_KEY or AZURE_SAS_TOKEN!"
    )
  }

  test("apply returns AzureBlobBucket with secret key") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SECRET_KEY" -> "secret"
    )
    val bucket = getBucket(properties)
    assertAzureBlobBucket(bucket)
    assert(
      bucket
        .getConfiguration()
        .get("fs.azure.account.key.account1.blob.core.windows.net") === "secret"
    )
  }

  test("apply throws if container name is not provided when using with sas token") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SAS_TOKEN" -> "token"
    )
    val thrown = intercept[IllegalArgumentException] {
      assertAzureBlobBucket(getBucket(properties))
    }
    assert(thrown.getMessage === "Please provide a value for the AZURE_CONTAINER_NAME property!")
  }

  test("apply returns AzureBlobBucket with sas token") {
    properties = defaultProperties ++ Map(
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SAS_TOKEN" -> "token",
      "AZURE_CONTAINER_NAME" -> "container1"
    )
    val bucket = getBucket(properties)
    assertAzureBlobBucket(bucket)
    assert(
      bucket
        .getConfiguration()
        .get("fs.azure.sas.container1.account1.blob.core.windows.net") === "token"
    )
  }

}
