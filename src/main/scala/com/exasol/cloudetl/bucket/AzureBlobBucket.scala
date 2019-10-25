package com.exasol.cloudetl.bucket

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Azure Blob Storage */
final case class AzureBlobBucket(path: String, params: Map[String, String]) extends Bucket {

  private[this] val AZURE_CONTAINER_NAME: String = "AZURE_CONTAINER_NAME"
  private[this] val AZURE_SAS_TOKEN: String = "AZURE_SAS_TOKEN"
  private[this] val AZURE_SECRET_KEY: String = "AZURE_SECRET_KEY"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: Map[String, String] = params

  /** @inheritdoc */
  override def validate(): Unit = {
    Bucket.validate(properties, Bucket.AZURE_BLOB_PARAMETERS)
    if (!params.contains(AZURE_SECRET_KEY) && !params.contains(AZURE_SAS_TOKEN)) {
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

    val accountName = Bucket.requiredParam(params, "AZURE_ACCOUNT_NAME")
    if (params.contains(AZURE_SAS_TOKEN)) {
      val sasToken = Bucket.requiredParam(params, AZURE_SAS_TOKEN)
      val containerName = Bucket.requiredParam(params, AZURE_CONTAINER_NAME)
      conf.set(s"fs.azure.sas.$containerName.$accountName.blob.core.windows.net", sasToken)
    } else {
      val secretKey = Bucket.requiredParam(params, AZURE_SECRET_KEY)
      conf.set(s"fs.azure.account.key.$accountName.blob.core.windows.net", secretKey)
    }

    conf
  }

}
