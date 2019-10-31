package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Blob Storage */
final case class AzureBlobBucket(path: String, params: StorageProperties) extends Bucket {

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

  /**
   * Validation method specific to the Azure Blob Storage.
   *
   * Validates required properties by calling parent {@code validate}
   * method. Furthermore, checks whether either [[AZURE_SECRET_KEY]] or
   * [[AZURE_SAS_TOKEN]] parameters are available.
   */
  private[this] def validateExtra(): Unit = {
    validate()
    if (!properties.containsKey(AZURE_SECRET_KEY) && !properties.containsKey(AZURE_SAS_TOKEN)) {
      throw new IllegalArgumentException(
        s"Please provide a value for either $AZURE_SECRET_KEY or $AZURE_SAS_TOKEN!"
      )
    }
  }

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validateExtra()

    val conf = new Configuration()
    conf.set("fs.azure", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.wasb.impl", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.wasbs.impl", classOf[org.apache.hadoop.fs.azure.NativeAzureFileSystem].getName)
    conf.set("fs.AbstractFileSystem.wasb.impl", classOf[org.apache.hadoop.fs.azure.Wasb].getName)
    conf.set(
      "fs.AbstractFileSystem.wasbs.impl",
      classOf[org.apache.hadoop.fs.azure.Wasbs].getName
    )

    val accountName = properties.getString(AZURE_ACCOUNT_NAME)
    if (properties.containsKey(AZURE_SAS_TOKEN)) {
      val sasToken = properties.getString(AZURE_SAS_TOKEN)
      val containerName = properties.getString(AZURE_CONTAINER_NAME)
      conf.set(s"fs.azure.sas.$containerName.$accountName.blob.core.windows.net", sasToken)
    } else {
      val secretKey = properties.getString(AZURE_SECRET_KEY)
      conf.set(s"fs.azure.account.key.$accountName.blob.core.windows.net", secretKey)
    }

    conf
  }

}
