package com.exasol.cloudetl.bucket

import org.apache.hadoop.conf.Configuration

/**
 * A specific [[Bucket]] implementation for the local 'file:' scheme.
 */
final case class LocalBucket(path: String, params: Map[String, String]) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: Map[String, String] = params

  /** @inheritdoc */
  override def validate(): Unit = ()

  /** @inheritdoc */
  override def getConfiguration(): Configuration = {
    validate()
    new Configuration()
  }
}
