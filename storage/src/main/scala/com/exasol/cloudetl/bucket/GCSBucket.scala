package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Google Cloud Storage (GCS) */
final case class GCSBucket(path: String, params: StorageProperties) extends Bucket {

  private[this] val GCS_PROJECT_ID: String = "GCS_PROJECT_ID"
  private[this] val GCS_KEYFILE_PATH: String = "GCS_KEYFILE_PATH"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** @inheritdoc */
  override def validate(): Unit =
    validateRequiredProperties()

  /**
   * Returns the list of required property keys for Google Cloud
   * Storage.
   */
  override def getRequiredProperties(): Seq[String] =
    Seq(GCS_PROJECT_ID, GCS_KEYFILE_PATH)

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
    conf.set("fs.gs.project.id", properties.getString(GCS_PROJECT_ID))
    conf.set(
      "fs.gs.auth.service.account.json.keyfile",
      properties.getString(GCS_KEYFILE_PATH)
    )

    conf
  }

}
