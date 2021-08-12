package com.exasol.cloudetl.bucket

import com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem

class BucketTest extends AbstractBucketTest {

  test("apply throws if the scheme is not supported") {
    properties = Map(PATH -> "xyz:/bucket/files*", FORMAT -> "ORC")
    val thrown = intercept[IllegalArgumentException] {
      getBucket(properties)
    }
    assert(thrown.getMessage().startsWith("F-CSE-4"))
    assert(thrown.getMessage().contains("Provided path scheme 'xyz' is not supported."))
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

}
