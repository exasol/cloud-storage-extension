package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the AWS S3 */
final case class S3Bucket(path: String, params: StorageProperties) extends Bucket with SecureBucket {

  private[this] val S3_ENDPOINT: String = "S3_ENDPOINT"
  private[this] val S3_ACCESS_KEY: String = "S3_ACCESS_KEY"
  private[this] val S3_SECRET_KEY: String = "S3_SECRET_KEY"
  private[this] val S3_SESSION_TOKEN: String = "S3_SESSION_TOKEN"
  private[this] val S3_PATH_STYLE_ACCESS: String = "S3_PATH_STYLE_ACCESS"
  private[this] val S3_CHANGE_DETECTION_MODE: String = "S3_CHANGE_DETECTION_MODE"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** Returns the list of required property keys for AWS S3 Storage. */
  override def getRequiredProperties(): Seq[String] = Seq(S3_ENDPOINT)

  /** @inheritdoc */
  override def getSecureProperties(): Seq[String] = Seq(S3_SECRET_KEY, S3_SESSION_TOKEN)

  /** @inheritdoc */
  override def validate(): Unit = {
    validateRequiredProperties()
    validateConnectionProperties()
  }

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
    conf.set("fs.s3a.impl", classOf[org.apache.hadoop.fs.s3a.S3AFileSystem].getName)
    conf.set("fs.s3a.endpoint", properties.getString(S3_ENDPOINT))
    if (properties.containsKey(S3_CHANGE_DETECTION_MODE)) {
      conf.set("fs.s3a.change.detection.mode", properties.getString(S3_CHANGE_DETECTION_MODE))
    }
    if (properties.containsKey(S3_PATH_STYLE_ACCESS)) {
      conf.set("fs.s3a.path.style.access", properties.getString(S3_PATH_STYLE_ACCESS))
    }

    val mergedProperties = if (properties.hasNamedConnection()) {
      properties.merge(S3_ACCESS_KEY)
    } else {
      properties
    }

    conf.set("fs.s3a.access.key", mergedProperties.getString(S3_ACCESS_KEY))
    conf.set("fs.s3a.secret.key", mergedProperties.getString(S3_SECRET_KEY))

    if (mergedProperties.containsKey(S3_SESSION_TOKEN)) {
      conf.set(
        "fs.s3a.aws.credentials.provider",
        classOf[org.apache.hadoop.fs.s3a.TemporaryAWSCredentialsProvider].getName()
      )
      conf.set("fs.s3a.session.token", mergedProperties.getString(S3_SESSION_TOKEN))
    }

    properties.getProxyHost().foreach { proxyHost =>
      conf.set("fs.s3a.proxy.host", proxyHost)
      properties.getProxyPort().foreach(conf.set("fs.s3a.proxy.port", _))
      properties.getProxyUsername().foreach(conf.set("fs.s3a.proxy.username", _))
      properties.getProxyPassword().foreach(conf.set("fs.s3a.proxy.password", _))
    }

    conf
  }

}
