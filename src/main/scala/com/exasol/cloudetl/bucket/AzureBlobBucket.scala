package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Blob Storage */
final case class AzureBlobBucket(path: String, params: StorageProperties)
    extends Bucket
    with SecureBucket {

  private[this] val AZURE_ACCOUNT_NAME: String = "AZURE_ACCOUNT_NAME"
  private[this] val AZURE_CONTAINER_NAME: String = "AZURE_CONTAINER_NAME"
  private[this] val AZURE_SAS_TOKEN: String = "AZURE_SAS_TOKEN"
  private[this] val AZURE_SECRET_KEY: String = "AZURE_SECRET_KEY"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /**
   * Returns the list of required property keys for Azure Blob Storage.
   */
  override def getRequiredProperties(): Seq[String] =
    Seq(AZURE_ACCOUNT_NAME)

  /** @inheritdoc */
  override def getSecureProperties(): Seq[String] =
    Seq(AZURE_SECRET_KEY, AZURE_SAS_TOKEN)

  /** @inheritdoc */
  override def validate(): Unit = {
    validateRequiredProperties()
    validateConnectionProperties()
  }

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.azure", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.wasb.impl", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.wasbs.impl", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.AbstractFileSystem.wasb.impl", classOf[org.apache.hadoop.fs.azure.Wasb].getName)
    conf.set(
      "fs.AbstractFileSystem.wasbs.impl",
      classOf[org.apache.hadoop.fs.azure.Wasbs].getName
    )

    val mergedProperties = if (properties.hasNamedConnection()) {
      properties.merge(AZURE_ACCOUNT_NAME)
    } else {
      properties
    }

    val accountName = mergedProperties.getString(AZURE_ACCOUNT_NAME)
    if (mergedProperties.containsKey(AZURE_SAS_TOKEN)) {
      val sasToken = mergedProperties.getString(AZURE_SAS_TOKEN)
      val containerName = mergedProperties.getString(AZURE_CONTAINER_NAME)
      conf.set(s"fs.azure.sas.$containerName.$accountName.blob.core.windows.net", sasToken)
    } else {
      val secretKey = mergedProperties.getString(AZURE_SECRET_KEY)
      conf.set(s"fs.azure.account.key.$accountName.blob.core.windows.net", secretKey)
    }

    conf
  }

}
