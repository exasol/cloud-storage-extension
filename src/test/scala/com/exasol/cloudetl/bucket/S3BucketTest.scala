package com.exasol.cloudetl.bucket

import org.apache.hadoop.fs.s3a.S3AFileSystem

import scala.jdk.CollectionConverters.MapHasAsScala

class S3BucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "s3a://my-bucket/",
    FORMAT -> "AVRO",
    "CONNECTION_NAME" -> "connection_info",
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

  private[this] def assertConfigurationProperties(bucket: Bucket, extraMappings: Map[String, String]): Unit = {
    assert(bucket.isInstanceOf[S3Bucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.s3a.impl" -> classOf[S3AFileSystem].getName(),
      "fs.s3a.endpoint" -> "eu-central-1"
    )
    (defaultMappings ++ extraMappings).foreach { case (given, expected) =>
      assert(conf.get(given) === expected)
    }
  }

  private[this] def bucketWithDefaultConnectionString(properties: Map[String, String]): Bucket = {
    val identifier = "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token"
    val exaMetadata = mockConnectionInfo("", identifier)
    getBucket(properties, exaMetadata)
  }

  test("apply throws when no connection name is provided") {
    val properties = defaultProperties - "CONNECTION_NAME"
    assertNoConnectionName(getBucket(properties).validate())
  }

  test("apply throws with access, secret or session token parameters") {
    assertForbiddenProperty(getBucket(accessProperties).validate())
  }

  test("apply returns S3Bucket with secret from connection") {
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret")
    val bucket = getBucket(defaultProperties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings - "fs.s3a.session.token")
  }

  test(testName = "apply return specific credentials provider for public access configuration") {
    val exaMetadata = mockConnectionInfo("access", "S3_ACCESS_KEY=;S3_SECRET_KEY=")
    val bucket = getBucket(defaultProperties, exaMetadata)
    assert(bucket.getConfiguration().get("fs.s3a.aws.credentials.provider") ==
      "org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider")
  }

  test("apply returns S3Bucket with secret and session token from connection") {
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token")
    val bucket = getBucket(defaultProperties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings)
  }

  // Access key is encoded in password value of connection object.
  test("apply returns S3Bucket with access and secret from connection") {
    val exaMetadata = mockConnectionInfo("", "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret")
    val bucket = getBucket(defaultProperties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings - "fs.s3a.session.token")
  }

  test("apply returns S3Bucket with access, secret and session token from connection") {
    assertConfigurationProperties(bucketWithDefaultConnectionString(defaultProperties), configMappings)
  }

  test("apply returns S3Bucket with change detection mode") {
    val properties = defaultProperties ++ Map("S3_CHANGE_DETECTION_MODE" -> "none")
    val extraConfigs = configMappings ++ Map("fs.s3a.change.detection.mode" -> "none")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("apply returns S3Bucket with path style access") {
    val properties = defaultProperties ++ Map("S3_PATH_STYLE_ACCESS" -> "true")
    val extraConfigs = configMappings ++ Map("fs.s3a.path.style.access" -> "true")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("apply returns S3Bucket with endpoint region") {
    val properties = defaultProperties ++ Map("S3_ENDPOINT_REGION" -> "eu-central-1")
    val extraConfigs = configMappings ++ Map("fs.s3a.endpoint.region" -> "eu-central-1")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("apply returns S3Bucket with ssl enabled by default") {
    val extraConfigs = configMappings ++ Map("fs.s3a.connection.ssl.enabled" -> "true")
    assertConfigurationProperties(bucketWithDefaultConnectionString(defaultProperties), extraConfigs)
  }

  test("apply returns S3Bucket with ssl enabled by user") {
    val properties = defaultProperties ++ Map("S3_SSL_ENABLED" -> "falsy")
    val extraConfigs = configMappings ++ Map("fs.s3a.connection.ssl.enabled" -> "falsy")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("proxy settings should be added when present") {
    val properties = defaultProperties ++ Map(
      "PROXY_HOST" -> "my_proxy.net",
      "PROXY_PORT" -> "2345"
    )
    val extraConfigs = configMappings ++ Map(
      "fs.s3a.proxy.host" -> "my_proxy.net",
      "fs.s3a.proxy.port" -> "2345"
    )
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("proxy settings should not be added without host") {
    val properties = defaultProperties ++ Map(
      "PROXY_USERNAME" -> "myUser",
      "PROXY_PASSWORD" -> "mySecretPassword",
      "PROXY_PORT" -> "2345"
    )
    val conf = bucketWithDefaultConnectionString(properties).getConfiguration()
    assert(conf.getValByRegex("fs\\.s3a\\.proxy.+").asScala === Map.empty[String, String])
  }

  test("throws for S3 bucket name that end with a number") {
    val properties = defaultProperties ++ Map(PATH -> "s3a://my-bucket.test.s3.007")
    val thrown = intercept[BucketValidationException] {
      val bucket = bucketWithDefaultConnectionString(properties)
      bucket.getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-28"))
    assert(thrown.getMessage().contains("end with a number"))
  }

  test("throws for S3 bucket name that contain underscore") {
    val properties = defaultProperties ++ Map(PATH -> "s3a://my_bucket.test.s3")
    val thrown = intercept[BucketValidationException] {
      val bucket = bucketWithDefaultConnectionString(properties)
      bucket.getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-28"))
    assert(thrown.getMessage().contains("contain underscores"))
  }

  test("throws for S3 bucket name that end with a hyphen") {
    val properties = defaultProperties ++ Map(PATH -> "s3a://my-bucket-")
    val thrown = intercept[BucketValidationException] {
      val bucket = bucketWithDefaultConnectionString(properties)
      bucket.getConfiguration()
    }
    assert(thrown.getMessage().startsWith("E-CSE-28"))
    assert(thrown.getMessage().contains("end with a number or a hyphen"))
  }

}
