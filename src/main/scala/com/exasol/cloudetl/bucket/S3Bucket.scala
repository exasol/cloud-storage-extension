package com.exasol.cloudetl.bucket

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the AWS S3 */
final case class S3Bucket(path: String, params: Map[String, String]) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override def validate(): Unit =
    Bucket.validate(params, Bucket.S3_PARAMETERS)

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def createConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
    conf.set("fs.s3a.impl", classOf[org.apache.hadoop.fs.s3a.S3AFileSystem].getName)
    conf.set("fs.s3a.endpoint", Bucket.requiredParam(params, "S3_ENDPOINT"))
    conf.set("fs.s3a.access.key", Bucket.requiredParam(params, "S3_ACCESS_KEY"))
    conf.set("fs.s3a.secret.key", Bucket.requiredParam(params, "S3_SECRET_KEY"))

    conf
  }

}
