package com.exasol.cloudetl.bucket

import scala.jdk.CollectionConverters.MapHasAsScala

class GCSBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "gs://my-bucket/",
    FORMAT -> "AVRO",
    "GCS_KEYFILE_PATH" -> "/keyfile.json",
    "GCS_PROJECT_ID" -> "myProject"
  )

  test("constructs bucket properly") {
    val bucket = getBucket(defaultProperties)
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
  }

  test("constructing fails when both keyfile and connection are specified") {
    val bucket = getBucket(
      Map(
        PATH -> "gs://my-bucket/",
        FORMAT -> "AVRO",
        "GCS_PROJECT_ID" -> "myProject",
        "GCS_KEYFILE_PATH" -> "/keyfile.json",
        "CONNECTION_NAME" -> "GCS_CONNECTION"
      )
    )
    val thrown = intercept[IllegalArgumentException] {
      bucket.validate()
    }
    assert(
      thrown.getMessage() === "E-CSE-30: Both properties 'GCS_KEYFILE_PATH' and 'CONNECTION_NAME' are specified. Please specify only one of them."
    )
  }

  test("constructing fails when both keyfile and connection are missing") {
    val bucket = getBucket(
      Map(
        PATH -> "gs://my-bucket/",
        FORMAT -> "AVRO",
        "GCS_PROJECT_ID" -> "myProject"
      )
    )
    val thrown = intercept[IllegalArgumentException] {
      bucket.validate()
    }
    assert(
      thrown.getMessage() === "E-CSE-31: Neither of properties 'GCS_KEYFILE_PATH' or 'CONNECTION_NAME' is specified. Please specify exactly one of them."
    )
  }

  test("proxy settings should be added when present") {
    val bucket = getBucket(
      defaultProperties ++ Map(
        "PROXY_HOST" -> "myproxy.net",
        "PROXY_PORT" -> "3198",
        "PROXY_USERNAME" -> "user",
        "PROXY_PASSWORD" -> "password"
      )
    )
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    assert(conf.get("fs.gs.proxy.address") === "myproxy.net:3198")
    assert(conf.get("fs.gs.proxy.username") === "user")
    assert(conf.get("fs.gs.proxy.password") === "password")
  }

  test("proxy settings should not be added without host") {
    val bucket = getBucket(
      defaultProperties ++ Map(
        "PROXY_PORT" -> "3198",
        "PROXY_USERNAME" -> "user",
        "PROXY_PASSWORD" -> "password"
      )
    )
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    assert(conf.getValByRegex("fs\\.s3a\\.proxy.+").asScala === Map.empty[String, String])
  }
}
