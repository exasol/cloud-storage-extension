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

  private[this] def assertConfigurationProperties(
    bucket: Bucket,
    extraMappings: Map[String, String]
  ): Unit = {
    assert(bucket.isInstanceOf[S3Bucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.s3a.impl" -> classOf[S3AFileSystem].getName,
      "fs.s3a.endpoint" -> "eu-central-1"
    )
    (defaultMappings ++ extraMappings).foreach { case (given, expected) =>
      assert(conf.get(given) === expected)
    }
  }

  test("apply throws when no connection name is provided") {
    properties = defaultProperties
    assertNoConnectionName(getBucket(properties).validate())
  }

  test("apply throws with access, secret or session token parameters") {
    properties = accessProperties
    assertForbiddenProperty(getBucket(properties).validate())
  }

  test("apply returns S3Bucket with secret from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings - "fs.s3a.session.token")
  }

  test("apply returns S3Bucket with secret and session token from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("access", "S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token")
    val bucket = getBucket(properties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings)
  }

  // Access key is encoded in password value of connection object.
  test("apply returns S3Bucket with access and secret from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    val exaMetadata = mockConnectionInfo("", "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret")
    val bucket = getBucket(properties, exaMetadata)
    assertConfigurationProperties(bucket, configMappings - "fs.s3a.session.token")
  }

  private[this] def bucketWithDefaultConnectionString(properties: Map[String, String]): Bucket = {
    val identifier = "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token"
    val exaMetadata = mockConnectionInfo("", identifier)
    getBucket(properties, exaMetadata)
  }

  test("apply returns S3Bucket with access, secret and session token from connection") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info"
    )
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), configMappings)
  }

  test("apply returns S3Bucket with change detection mode") {
    properties = defaultProperties ++ Map(
      "S3_CHANGE_DETECTION_MODE" -> "none",
      "CONNECTION_NAME" -> "connection_info"
    )
    val extraConfigs = configMappings ++ Map("fs.s3a.change.detection.mode" -> "none")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("apply returns S3Bucket with path style access") {
    properties = defaultProperties ++ Map(
      "S3_PATH_STYLE_ACCESS" -> "true",
      "CONNECTION_NAME" -> "connection_info"
    )
    val extraConfigs = configMappings ++ Map("fs.s3a.path.style.access" -> "true")
    assertConfigurationProperties(bucketWithDefaultConnectionString(properties), extraConfigs)
  }

  test("proxy settings should be added when present") {
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info",
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
    properties = defaultProperties ++ Map(
      "CONNECTION_NAME" -> "connection_info",
      "PROXY_USERNAME" -> "myUser",
      "PROXY_PASSWORD" -> "mySecretPassword",
      "PROXY_PORT" -> "2345"
    )

    import scala.collection.JavaConverters.mapAsScalaMapConverter
    val conf = bucketWithDefaultConnectionString(properties).getConfiguration()
    assert(conf.getValByRegex("fs\\.s3a\\.proxy.+").asScala == Map.empty)
  }

}
