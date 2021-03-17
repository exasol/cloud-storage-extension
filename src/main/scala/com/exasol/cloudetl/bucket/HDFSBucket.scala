package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the HDFS filesystem */
final case class HDFSBucket(path: String, params: StorageProperties) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** Returns the list of required property keys for HDFS filesystem. */
  override def getRequiredProperties(): Seq[String] = Seq.empty[String]

  /** @inheritdoc */
  override def validate(): Unit =
    validateRequiredProperties()

  /** @inheritdoc */
  override def getConfiguration(): Configuration = {
    validate()
    val conf = new Configuration()
    conf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName())
    conf
  }

}
