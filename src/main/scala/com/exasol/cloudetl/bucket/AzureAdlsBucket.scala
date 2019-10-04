package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Data Lake Storage */
final case class AzureAdlsBucket(path: String, params: StorageProperties) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  /** @inheritdoc */
  override def getRequiredProperties(): Seq[String] =
    Bucket.AZURE_ADLS_PARAMETERS

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    val clientId = properties.getAs[String]("AZURE_CLIENT_ID")
    val clientSecret = properties.getAs[String]("AZURE_CLIENT_SECRET")
    val directoryId = properties.getAs[String]("AZURE_DIRECTORY_ID")
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
