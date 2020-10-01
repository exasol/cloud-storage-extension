package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.s3a.S3AFileSystem

class S3BucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "s3a://my-bucket/",
    FORMAT -> "AVRO",
    "S3_ENDPOINT" -> "eu-central-1"
  )

  private[this] val accessKey = "access"
  private[this] val secretKey = "secret"
  private[this] val sessionToken = "token"

  private[this] val accessProperties = defaultProperties ++ Map(
    "S3_ACCESS_KEY" -> accessKey,
    "S3_SECRET_KEY" -> secretKey,
    "S3_SESSION_TOKEN" -> sessionToken
  )

  private[this] val configMappings = Map(
    "fs.s3a.access.key" -> accessKey,
    "fs.s3a.secret.key" -> secretKey,
    "fs.s3a.session.token" -> sessionToken
  )

  private[this] def assertS3Bucket(bucket: Bucket, extraMappings: Map[String, String]): Unit = {
    assert(bucket.isInstanceOf[S3Bucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.s3a.impl" -> classOf[S3AFileSystem].getName,
      "fs.s3a.endpoint" -> "eu-central-1"
    )
    (defaultMappings ++ extraMappings).foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

  test("apply throws when no connection name is provided") {
    properties = defaultProperties
    assertNoConnectionName { getBucket(properties).validate() }
  }

  test("apply throws with access, secret or session token parameters") {
    properties = accessProperties
    assertForbiddenProperty { getBucket(properties).validate() }
  }

  test("apply returns S3Bucket with secret from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertS3Bucket(bucket, configMappings - "fs.s3a.session.token")
  }

  test("apply returns S3Bucket with secret and session token from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertS3Bucket(bucket, configMappings)
  }

  // Access key is encoded in password value of connection object.
  test("apply returns S3Bucket with access and secret from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertS3Bucket(bucket, configMappings - "fs.s3a.session.token")
  }

  test("apply returns S3Bucket with access, secret and session token from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata =
      mockConnectionInfo("", "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertS3Bucket(bucket, configMappings)
  }

}
