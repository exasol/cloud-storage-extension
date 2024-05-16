package com.exasol.cloudetl.bucket

import java.nio.file.Files
import java.nio.file.Path

import org.scalatest.prop.TableDrivenPropertyChecks._

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

  test("validation fails when both keyfile and connection are specified") {
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
      thrown.getMessage() === "E-CSE-30: Both properties 'GCS_KEYFILE_PATH' and 'CONNECTION_NAME' are specified. "
        + "Please specify only one of them."
    )
  }

  test("validation fails when both keyfile and connection are missing") {
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
      thrown.getMessage() === "E-CSE-31: Neither of properties 'GCS_KEYFILE_PATH' or 'CONNECTION_NAME' is specified. "
        + "Please specify exactly one of them."
    )
  }

  val invalidConnectionContent = Table(
    (
      "",
      "E-IEUCS-4: Properties input string does not contain key-value assignment '='. "
        + "Please make sure that key-value pairs encoded correctly."
    ),
    (
      "wrong-format",
      "E-IEUCS-4: Properties input string does not contain key-value assignment '='. "
        + "Please make sure that key-value pairs encoded correctly."
    ),
    (
      "wrong_key=value",
      "E-CSE-32: The connection 'connection_info' does not contain 'GCS_KEYFILE_CONTENT' property. "
        + "Please check the connection properties."
    ),
    (
      "GCS_KEYFILE_CONTENT=invalid_json",
      "E-CSE-33: The connection 'connection_info' does not contain valid JSON in property 'GCS_KEYFILE_CONTENT'. "
        + "Please check the connection properties."
    )
  )
  forAll(invalidConnectionContent) { (connectionContent: String, expectedErrorMessage: String) =>
    test(s"validation fails for invalid connection value '$connectionContent'") {
      val bucket = getBucket(
        Map(
          PATH -> "gs://my-bucket/",
          FORMAT -> "AVRO",
          "GCS_PROJECT_ID" -> "myProject",
          "CONNECTION_NAME" -> "connection_info"
        ),
        mockConnectionInfo("", connectionContent)
      )
      val thrown = intercept[IllegalArgumentException] {
        bucket.validate()
      }
      assert(
        thrown.getMessage() === expectedErrorMessage
      )
    }
  }

  test("keyfile path configured when path property specified") {
    val bucket = getBucket(defaultProperties)
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    assert(conf.get("fs.gs.auth.service.account.json.keyfile") === "/keyfile.json")
  }

  test("keyfile path configured when connection specified") {
    val bucket = getBucket(
      Map(
        PATH -> "gs://my-bucket/",
        FORMAT -> "AVRO",
        "GCS_PROJECT_ID" -> "myProject",
        "CONNECTION_NAME" -> "connection_info"
      ),
      mockConnectionInfo("", "GCS_KEYFILE_CONTENT={\"key\":\"value\"}")
    )
    bucket.validate()
    assert(bucket.isInstanceOf[GCSBucket])
    val conf = bucket.getConfiguration()
    val tempFilePath = conf.get("fs.gs.auth.service.account.json.keyfile")
    assert(!tempFilePath.isBlank())
    assert(Files.readString(Path.of(tempFilePath)) === "{\"key\":\"value\"}")
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
