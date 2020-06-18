package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/**
 * A specific [[Bucket]] implementation for the local 'file:' scheme.
 */
final case class LocalBucket(path: String, params: StorageProperties) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** @inheritdoc */
  override def getRequiredProperties(): Seq[String] = Seq.empty[String]

  /** @inheritdoc */
  override def validate(): Unit =
    validateRequiredProperties()

  /** @inheritdoc */
  override def getConfiguration(): Configuration = {
    validate()
    new Configuration()
  }
}
