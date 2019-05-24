package com.exasol.cloudetl.bucket

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Data Lake Storage */
final case class AzureAdlsBucket(path: String, params: Map[String, String]) extends Bucket {

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: Map[String, String] = params

  /** @inheritdoc */
  override def validate(): Unit =
    Bucket.validate(properties, Bucket.AZURE_ADLS_PARAMETERS)

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available
   * in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    val clientId = Bucket.requiredParam(params, "AZURE_CLIENT_ID")
    val clientSecret = Bucket.requiredParam(params, "AZURE_CLIENT_SECRET")
    val directoryId = Bucket.requiredParam(params, "AZURE_DIRECTORY_ID")
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
