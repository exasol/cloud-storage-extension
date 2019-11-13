package com.exasol.cloudetl.bucket

import com.exasol.ExaConnectionInformation
import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageConnectionInformation
import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar

class SecureBucketTest extends AbstractBucketTest with MockitoSugar {

  test("validate throws if no access credentials are provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseSecureBucket(properties).validate()
    }
    assert(thrown.getMessage.contains("Please provide either only CONNECTION_NAME property or"))
  }

  test("validate throws if both connection name and access properties are provided") {
    properties = Map(
      "CONNECTION_NAME" -> "named_connection",
      "accountNameProperty" -> "user",
      "accountSecretProperty" -> "secret"
    )
    val thrown = intercept[IllegalArgumentException] {
      BaseSecureBucket(properties).validate()
    }
    assert(thrown.getMessage.contains("Please provide either only CONNECTION_NAME property or"))
    assert(thrown.getMessage.contains("property pairs, but not the both!"))
  }

  test("getStorageConnectionInformation throws if no access properties are provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseSecureBucket(properties).getStorageConnectionInformation()
    }
    assert(thrown.getMessage === "Please provide a CONNECTION_NAME property!")
  }

  test("getStorageConnectionInformation returns secure properties, no named connection") {
    properties = Map("accountNameProperty" -> "account", "accountSecretProperty" -> "sekret")
    val expected = StorageConnectionInformation("account", "sekret")
    assert(BaseSecureBucket(properties).getStorageConnectionInformation() === expected)
  }

  test("getStorageConnectionInformation returns connection info from Exasol named connection") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo: ExaConnectionInformation = new ExaConnectionInformation() {
      override def getType(): ExaConnectionInformation.ConnectionType =
        ExaConnectionInformation.ConnectionType.PASSWORD
      override def getAddress(): String = ""
      override def getUser(): String = "account"
      override def getPassword(): String = "secRet"
    }
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    val expected = StorageConnectionInformation("account", "secRet")
    assert(BaseSecureBucket(properties, metadata).getStorageConnectionInformation() === expected)
    verify(metadata, times(1)).getConnection("connection_info")
  }

  @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
  private[this] case class BaseSecureBucket(
    val params: Map[String, String],
    val metadata: ExaMetadata = null
  ) extends Bucket
      with SecureBucket {
    override val properties = StorageProperties(params, metadata)

    override val bucketPath = "local_path"
    override def getRequiredProperties: Seq[String] = Seq.empty[String]
    override def getConfiguration: Configuration = new Configuration()
    override def validate(): Unit = {
      validateRequiredProperties()
      validateConnectionProperties()
    }
    override val accountName = "accountNameProperty"
    override val accountSecret = "accountSecretProperty"
  }
}
