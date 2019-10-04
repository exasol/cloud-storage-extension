package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Google Cloud Storage (GCS) */
final case class GCSBucket(path: String, params: StorageProperties) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** @inheritdoc */
  override def getRequiredProperties(): Seq[String] =
    Bucket.GCS_PARAMETERS

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.gs.impl", classOf[com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem].getName)
    conf.setBoolean("fs.gs.auth.service.account.enable", true)
    conf.set("fs.gs.project.id", properties.getAs[String]("GCS_PROJECT_ID"))
    conf.set(
      "fs.gs.auth.service.account.json.keyfile",
      properties.getAs[String]("GCS_KEYFILE_PATH")
    )

    conf
  }

}
