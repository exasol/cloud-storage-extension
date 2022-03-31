package com.exasol.cloudetl.bucket

import com.exasol.ExaConnectionInformation
import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class AbstractBucketTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

  private[cloudetl] val PATH: String = "BUCKET_PATH"
  private[cloudetl] val FORMAT: String = "DATA_FORMAT"
  private[cloudetl] var properties: Map[String, String] = _

  override def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  protected[this] final def getBucket(params: Map[String, String]): Bucket =
    Bucket(StorageProperties(params))

  protected[this] final def getBucket(
    params: Map[String, String],
    exaMetadata: ExaMetadata
  ): Bucket =
    Bucket(StorageProperties(params, exaMetadata))

  protected[this] final def mockConnectionInfo(
    username: String,
    password: String
  ): ExaMetadata = {
    val metadata = mock[ExaMetadata]
    val connectionInfo: ExaConnectionInformation = new ExaConnectionInformation() {
      override def getType(): ExaConnectionInformation.ConnectionType =
        ExaConnectionInformation.ConnectionType.PASSWORD
      override def getAddress(): String = ""
      override def getUser(): String = username
      override def getPassword(): String = password
    }
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    metadata
  }

  final def assertNoConnectionName(fn: => Unit): Unit = {
    val thrown = intercept[BucketValidationException] {
      fn
    }
    val message = thrown.getMessage()
    assert(message.contains("No CONNECTION_NAME property is defined"))
    assert(message.contains("Please use connection object to provide access credentials"))
    ()
  }

  final def assertForbiddenProperty(fn: => Unit): Unit = {
    val thrown = intercept[BucketValidationException] {
      fn
    }
    val message = thrown.getMessage()
    assert(message.contains("Using credentials as parameters is forbidded"))
    assert(message.contains("Please use an Exasol named connection object"))
    ()
  }

}
