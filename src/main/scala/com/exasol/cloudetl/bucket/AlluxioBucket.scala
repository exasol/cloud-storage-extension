package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Alluxio */
final case class AlluxioBucket(path: String, params: StorageProperties) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** Returns the list of required property keys for Alluxio storage. */
  override def getRequiredProperties(): Seq[String] = Seq.empty[String]

  /** @inheritdoc */
  override def validate(): Unit =
    validateRequiredProperties()

  /** @inheritdoc */
  override def getConfiguration(): Configuration = {
    validate()
    val conf = new Configuration()
    conf.set("fs.alluxio.impl", classOf[alluxio.hadoop.FileSystem].getName())
    conf.set(
      "fs.AbstractFileSystem.alluxio.impl",
      classOf[alluxio.hadoop.AlluxioFileSystem].getName()
    )
    conf
  }

}
