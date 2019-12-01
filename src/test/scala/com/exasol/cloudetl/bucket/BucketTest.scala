package com.exasol.cloudetl.bucket

import com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem

@SuppressWarnings(Array("org.wartremover.warts.IsInstanceOf"))
class BucketTest extends AbstractBucketTest {

  test("apply throws if the scheme is not supported") {
    properties = Map(PATH -> "xyz:/bucket/files*", FORMAT -> "ORC")
    val thrown = intercept[IllegalArgumentException] {
      getBucket(properties)
    }
    assert(thrown.getMessage === "Unsupported path scheme xyz!")
  }

  test("apply returns LocalBucket") {
    properties = Map(PATH -> "file://local/path/bucket/", FORMAT -> "ORC")
    val bucket = getBucket(properties)
    assert(bucket.isInstanceOf[LocalBucket])
  }

  test("apply returns GCSBucket") {
    properties = Map(
      PATH -> "gs://my-bucket/",
      FORMAT -> "AVRO",
      "GCS_PROJECT_ID" -> "projX",
      "GCS_KEYFILE_PATH" -> "/bucketfs/bucket1/projX.json"
    )
    val bucket = getBucket(properties)
    val conf = bucket.getConfiguration()

    assert(bucket.isInstanceOf[GCSBucket])
    assert(conf.get("fs.gs.impl") === classOf[GoogleHadoopFileSystem].getName)
    assert(conf.get("fs.gs.project.id") === "projX")
    assert(conf.get("fs.gs.auth.service.account.json.keyfile") === "/bucketfs/bucket1/projX.json")
  }

  test("apply returns AzureAdlsBucket") {
    properties = Map(
      PATH -> "adl://my_container.azuredatalakestore.net/orc/*",
      FORMAT -> "CSV",
      "AZURE_CLIENT_ID" -> "clientX",
      "AZURE_CLIENT_SECRET" -> "client_secret",
      "AZURE_DIRECTORY_ID" -> "directory_id_secret"
    )
    val bucket = getBucket(properties)
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
