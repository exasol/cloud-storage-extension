package com.exasol.cloudetl.bucket

import scala.collection.JavaConverters.mapAsScalaMapConverter

class GCSBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "gs://my-bucket/",
    FORMAT -> "AVRO",
    "GCS_KEYFILE_PATH" -> "/keyfile.json",
    "GCS_PROJECT_ID" -> "myProject"
  )

  test("Must construct bucket properly") {
    val bucket = getBucket(defaultProperties)
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
  }

  test("proxy settings should be added when present") {
    val bucket = getBucket(defaultProperties ++ Map(
      "PROXY_HOST" -> "myproxy.net",
      "PROXY_PORT" -> "3198",
      "PROXY_USERNAME" -> "user",
      "PROXY_PASSWORD" -> "password"
    ))
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    assert(conf.get("fs.gs.proxy.address") === "myproxy.net:3198")
    assert(conf.get("fs.gs.proxy.username") === "user")
    assert(conf.get("fs.gs.proxy.password") === "password")
  }

  test("proxy settings should not be added without host") {
    val bucket = getBucket(defaultProperties ++ Map(
      "PROXY_PORT" -> "3198",
      "PROXY_USERNAME" -> "user",
      "PROXY_PASSWORD" -> "password"
    ))
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    assert(conf.getValByRegex("fs\\.s3a\\.proxy.+").asScala == Map.empty)
  }
}
