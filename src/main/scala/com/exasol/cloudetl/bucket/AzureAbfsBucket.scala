package com.exasol.cloudetl.bucket

import scala.util.matching.Regex

import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.errorreporting.ExaError

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Data Lake Gen2 Storage */
final case class AzureAbfsBucket(path: String, params: StorageProperties) extends Bucket with SecureBucket {

  private[this] val AZURE_ACCOUNT_NAME: String = "AZURE_ACCOUNT_NAME"
  private[this] val AZURE_SECRET_KEY: String = "AZURE_SECRET_KEY"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /**
   * Returns the list of required property keys for Azure Data Lake Gen2
   * Storage.
   */
  override def getRequiredProperties(): Seq[String] =
    Seq.empty[String]

  /** @inheritdoc */
  override def getSecureProperties(): Seq[String] =
    Seq(AZURE_SECRET_KEY)

  /** @inheritdoc */
  override def validate(): Unit = {
    validateRequiredProperties()
    validateConnectionProperties()
  }

  /** @inheritdoc */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.abfs.impl", classOf[org.apache.hadoop.fs.azurebfs.AzureBlobFileSystem].getName())
    conf.set("fs.AbstractFileSystem.abfs.impl", classOf[org.apache.hadoop.fs.azurebfs.Abfs].getName())
    conf.set("fs.abfss.impl", classOf[org.apache.hadoop.fs.azurebfs.SecureAzureBlobFileSystem].getName())
    conf.set("fs.AbstractFileSystem.abfss.impl", classOf[org.apache.hadoop.fs.azurebfs.Abfss].getName())

    val mergedProperties = if (properties.hasNamedConnection()) {
      properties.merge(AZURE_ACCOUNT_NAME)
    } else {
      properties
    }

    val accountAndContainer = regexParsePath(path)
    val accountName = mergedProperties
      .get(AZURE_ACCOUNT_NAME)
      .getOrElse(accountAndContainer.accountName)
    val secretKey = mergedProperties.getString(AZURE_SECRET_KEY)

    conf.set(s"fs.azure.account.key.$accountName.dfs.core.windows.net", secretKey)

    conf
  }

  // Intentionally copy-paste, duplicate count: 2. Please, refactor when
  // it reaches 3+.
  // Fabric / OneLake: .dfs.fabric.microsoft.com
  private[this] final val AZURE_ABFS_PATH_REGEX: Regex =
    """abfss?://(.*)@([^.]+).dfs.core.windows.net/(.*)$""".r
  private[this] final val AZURE_ABFS_ONELAKE_PATH_REGEX: Regex =
    """abfss?://(.*)@([^.]+)\.dfs\.fabric\.microsoft\.com/(.*)$""".r

  private[this] def regexParsePath(path: String): AccountAndContainer = path match {
    case AZURE_ABFS_PATH_REGEX(containerName, accountName, _) =>
      AccountAndContainer(accountName, containerName)
    case AZURE_ABFS_ONELAKE_PATH_REGEX(containerName, accountName, _) =>
      AccountAndContainer(accountName, containerName)
    case _ =>
      throw new BucketValidationException(
        ExaError
          .messageBuilder("E-CSE-20")
          .message("Azure datalake storage path {{PATH}} scheme is not valid.", path)
          .mitigation("It should be either 'abfs' or 'abfss'.")
          .toString()
      )
  }

  private[this] case class AccountAndContainer(accountName: String, containerName: String)

}
