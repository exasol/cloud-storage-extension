package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Data Lake Storage */
final case class AzureAdlsBucket(path: String, params: StorageProperties)
    extends Bucket
    with SecureBucket {

  private[this] val AZURE_CLIENT_ID: String = "AZURE_CLIENT_ID"
  private[this] val AZURE_CLIENT_SECRET: String = "AZURE_CLIENT_SECRET"
  private[this] val AZURE_DIRECTORY_ID: String = "AZURE_DIRECTORY_ID"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /**
   * Returns the list of required property keys for Azure Data Lake
   * Storage.
   */
  override def getRequiredProperties(): Seq[String] =
    Seq.empty[String]

  /** @inheritdoc */
  override def getSecureProperties(): Seq[String] =
    Seq(AZURE_CLIENT_ID, AZURE_CLIENT_SECRET, AZURE_DIRECTORY_ID)

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
    val mergedProperties = if (properties.hasNamedConnection()) {
      properties.merge(AZURE_CLIENT_ID)
    } else {
      properties
    }
    val clientId = mergedProperties.getString(AZURE_CLIENT_ID)
    val clientSecret = mergedProperties.getString(AZURE_CLIENT_SECRET)
    val directoryId = mergedProperties.getString(AZURE_DIRECTORY_ID)
    val tokenEndpoint = s"https://login.microsoftonline.com/$directoryId/oauth2/token"
    conf.set("fs.adl.impl", classOf[org.apache.hadoop.fs.adl.AdlFileSystem].getName)
    conf.set("fs.AbstractFileSystem.adl.impl", classOf[org.apache.hadoop.fs.adl.Adl].getName)
    conf.set("dfs.adls.oauth2.access.token.provider.type", "ClientCredential")
    conf.set("dfs.adls.oauth2.client.id", clientId)
    conf.set("dfs.adls.oauth2.credential", clientSecret)
    conf.set("dfs.adls.oauth2.refresh.url", tokenEndpoint)

    conf
  }

}
