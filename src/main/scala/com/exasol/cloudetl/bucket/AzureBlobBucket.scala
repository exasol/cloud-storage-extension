package com.exasol.cloudetl.bucket

import scala.util.matching.Regex

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
    Seq.empty[String]

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

    val accountAndContainer = regexParsePath(path)

    val accountName =
      mergedProperties.get(AZURE_ACCOUNT_NAME).getOrElse(accountAndContainer.accountName)

    if (mergedProperties.containsKey(AZURE_SAS_TOKEN)) {
      val sasToken = mergedProperties.getString(AZURE_SAS_TOKEN)
      val containerName =
        mergedProperties.get(AZURE_CONTAINER_NAME).getOrElse(accountAndContainer.containerName)
      conf.set(s"fs.azure.sas.$containerName.$accountName.blob.core.windows.net", sasToken)
    } else {
      val secretKey = mergedProperties.getString(AZURE_SECRET_KEY)
      conf.set(s"fs.azure.account.key.$accountName.blob.core.windows.net", secretKey)
    }

    conf
  }

  private[this] final val AZURE_BLOB_PATH_REGEX: Regex =
    """wasbs?://(.*)@([^.]+).blob.core.windows.net/(.*)$""".r

  private[this] def regexParsePath(path: String): AccountAndContainer = path match {
    case AZURE_BLOB_PATH_REGEX(containerName, accountName, _) =>
      AccountAndContainer(accountName, containerName)
    case _ => throw new IllegalArgumentException(s"Invalid Azure blob wasb(s) path: $path!")
  }

  private[this] case class AccountAndContainer(accountName: String, containerName: String)

}
