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

  @SuppressWarnings(Array("org.wartremover.contrib.warts.UnsafeInheritance"))
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
}
