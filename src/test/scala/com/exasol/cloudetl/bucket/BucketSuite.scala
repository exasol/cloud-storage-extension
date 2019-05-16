package com.exasol.cloudetl.bucket

import com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem
import org.apache.hadoop.fs.azure.NativeAzureFileSystem
import org.apache.hadoop.fs.azure.Wasb
import org.apache.hadoop.fs.azure.Wasbs
import org.apache.hadoop.fs.s3a.S3AFileSystem
import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class BucketSuite extends FunSuite with Matchers {

  test("throws an exception if the scheme is not supported") {
    val thrown = intercept[IllegalArgumentException] {
      Bucket(Map(Bucket.BUCKET_PATH -> "xyz:/bucket/files*"))
    }
    assert(thrown.getMessage === "Unsupported path scheme xyz")
  }

  test("creates an LocalBucket with local path parameter") {
    val bucket = Bucket(Map(Bucket.BUCKET_PATH -> "file://local/path/bucket/"))
    assert(bucket.isInstanceOf[LocalBucket])
  }

  test("creates an S3Bucket with given parameters") {
    val s3params = Map(
      Bucket.BUCKET_PATH -> "s3a://my-bucket/",
      "DATA_FORMAT" -> "PARQUET",
      "S3_ENDPOINT" -> "eu-central-1",
      "S3_ACCESS_KEY" -> "abc",
      "S3_SECRET_KEY" -> "xyz"
    )

    val bucket = Bucket(s3params)
    val conf = bucket.getConfiguration()

    assert(bucket.isInstanceOf[S3Bucket])
    assert(conf.get("fs.s3a.impl") === classOf[S3AFileSystem].getName)
    assert(conf.get("fs.s3a.endpoint") === "eu-central-1")
    assert(conf.get("fs.s3a.access.key") === "abc")
    assert(conf.get("fs.s3a.secret.key") === "xyz")
  }

  test("creates a GCSBucket with given parameters") {
    val gcsParams = Map(
      Bucket.BUCKET_PATH -> "gs://my-bucket/",
      "DATA_FORMAT" -> "AVRO",
      "GCS_PROJECT_ID" -> "projX",
      "GCS_KEYFILE_PATH" -> "/bucketfs/bucket1/projX.json"
    )

    val bucket = Bucket(gcsParams)
    val conf = bucket.getConfiguration()

    assert(bucket.isInstanceOf[GCSBucket])
    assert(conf.get("fs.gs.impl") === classOf[GoogleHadoopFileSystem].getName)
    assert(conf.get("fs.gs.project.id") === "projX")
    assert(conf.get("fs.gs.auth.service.account.json.keyfile") === "/bucketfs/bucket1/projX.json")
  }

  test("creates an AzureBlobBucket with given parameters") {
    val azureBlobParams = Map(
      Bucket.BUCKET_PATH -> "wasbs://container@account1/parquet-bucket/",
      "AZURE_ACCOUNT_NAME" -> "account1",
      "AZURE_SECRET_KEY" -> "secret"
    )

    val bucket = Bucket(azureBlobParams)
    val conf = bucket.getConfiguration()

    assert(bucket.isInstanceOf[AzureBlobBucket])
    assert(conf.get("fs.azure.account.key.account1.blob.core.windows.net") === "secret")
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

  test("creates an AzureAdlsBucket with provided parameters") {
    val params = Map(
      Bucket.BUCKET_PATH -> "adl://my_container.azuredatalakestore.net/orc/*",
      "AZURE_CLIENT_ID" -> "clientX",
      "AZURE_CLIENT_SECRET" -> "client_secret",
      "AZURE_DIRECTORY_ID" -> "directory_id_secret"
    )

    val bucket = Bucket(params)
    assert(bucket.isInstanceOf[AzureAdlsBucket])

    val conf = bucket.getConfiguration()
    val expectedSettings = Map(
      "fs.adl.impl" -> classOf[org.apache.hadoop.fs.adl.AdlFileSystem].getName,
      "fs.AbstractFileSystem.adl.impl" -> classOf[org.apache.hadoop.fs.adl.Adl].getName,
      "dfs.adls.oauth2.access.token.provider.type" -> "ClientCredential",
      "dfs.adls.oauth2.client.id" -> "clientX",
      "dfs.adls.oauth2.credential" -> "client_secret",
      "dfs.adls.oauth2.refresh.url" ->
        "https://login.microsoftonline.com/directory_id_secret/oauth2/token"
    )
    expectedSettings.foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

}
