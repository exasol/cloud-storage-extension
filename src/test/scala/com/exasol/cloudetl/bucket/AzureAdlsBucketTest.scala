package com.exasol.cloudetl.bucket

class AzureAdlsBucketTest extends AbstractBucketTest {

  private[this] val defaultProperties = Map(
    PATH -> "adl://container1.azuredatalakestore.net/avro-data/*",
    FORMAT -> "AVRO"
  )

  private[this] val clientID = "clientID"
  private[this] val clientSecret = "clientSecret"
  private[this] val directoryID = "directoryID"

  private[this] val configMappings = Map(
    "dfs.adls.oauth2.client.id" -> clientID,
    "dfs.adls.oauth2.credential" -> clientSecret,
    "dfs.adls.oauth2.refresh.url" -> s"https://login.microsoftonline.com/$directoryID/oauth2/token"
  )

  private[this] def assertAzureAdlsBucket(
    bucket: Bucket,
    extraMappings: Map[String, String]
  ): Unit = {
    assert(bucket.isInstanceOf[AzureAdlsBucket])
    val conf = bucket.getConfiguration()
    val defaultMappings = Map(
      "fs.adl.impl" -> classOf[org.apache.hadoop.fs.adl.AdlFileSystem].getName,
      "fs.AbstractFileSystem.adl.impl" -> classOf[org.apache.hadoop.fs.adl.Adl].getName,
      "dfs.adls.oauth2.access.token.provider.type" -> "ClientCredential"
    )
    (defaultMappings ++ extraMappings).foreach {
      case (given, expected) =>
        assert(conf.get(given) === expected)
    }
  }

  test("apply throws if no connection name is provided") {
    properties = defaultProperties
    assertNoConnectionName { getBucket(properties).validate() }
  }

  test("apply throws if client id, client secret or directory id is provided as parameter") {
    properties = defaultProperties ++ Map(
      "AZURE_CLIENT_ID" -> clientID,
      "AZURE_CLIENT_SECRET" -> clientSecret,
      "AZURE_DIRECTORY_ID" -> directoryID
    )
    assertForbiddenProperty { getBucket(properties).validate() }
  }

  test("apply returns with credentails from username and password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val exaMetadata = mockConnectionInfo(
      clientID,
      s"AZURE_CLIENT_SECRET=$clientSecret;AZURE_DIRECTORY_ID=$directoryID"
    )
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAdlsBucket(bucket, configMappings)
  }

  test("apply returns with credentails from password of connection object") {
    properties = defaultProperties ++ Map("CONNECTION_NAME" -> "connection_info")
    val connectionInfoPassword = s"AZURE_CLIENT_ID=$clientID;" +
      s"AZURE_CLIENT_SECRET=$clientSecret;AZURE_DIRECTORY_ID=$directoryID"
    val exaMetadata = mockConnectionInfo("", connectionInfoPassword)
    val bucket = getBucket(properties, exaMetadata)
    assertAzureAdlsBucket(bucket, configMappings)
  }

}
