package com.exasol.cloudetl.kinesis

import com.exasol.ExaMetadata

import com.amazonaws.services.kinesis.AmazonKinesis
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class KinesisClientFactoryTest extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach {
  private[this] var exaMetadata: ExaMetadata = _
  private[this] var userProperties: KinesisUserProperties = _

  override final def beforeEach(): Unit = {
    exaMetadata = mock[ExaMetadata]
    userProperties = mock[KinesisUserProperties]
    when(userProperties.getAwsAccessKey()).thenReturn("MY_ACCESS_KEY")
    when(userProperties.getAwsSecretKey()).thenReturn("MY_SECRET_KEY")
    when(userProperties.getRegion()).thenReturn("eu-west-1")
    ()
  }

  test("createKinesisClient returns AmazonKinesis with region and session token") {
    when(userProperties.containsAwsSessionToken()).thenReturn(true)
    when(userProperties.getAwsSessionToken()).thenReturn("MY_SESSION_TOKEN")
    when(userProperties.containsAwsServiceEndpoint()).thenReturn(false)
    when(userProperties.hasNamedConnection()).thenReturn(false)
    val kinesisClient = KinesisClientFactory.createKinesisClient(userProperties, exaMetadata)
    assert(kinesisClient.isInstanceOf[AmazonKinesis])
    verify(userProperties, times(1)).containsAwsServiceEndpoint()
    verify(userProperties, times(1)).containsAwsSessionToken()
    verify(userProperties, times(1)).getAwsSessionToken()
    verify(userProperties, times(1)).hasNamedConnection()
    verify(userProperties, never).getAwsServiceEndpoint()
  }

  test("createKinesisClient returns AmazonKinesis with endpoint") {
    when(userProperties.containsAwsSessionToken()).thenReturn(false)
    when(userProperties.containsAwsServiceEndpoint()).thenReturn(true)
    when(userProperties.getAwsServiceEndpoint()).thenReturn("http://127.0.0.1:40215")
    when(userProperties.hasNamedConnection()).thenReturn(false)
    val kinesisClient = KinesisClientFactory.createKinesisClient(userProperties, exaMetadata)
    assert(kinesisClient.isInstanceOf[AmazonKinesis])
    verify(userProperties, times(1)).containsAwsServiceEndpoint()
    verify(userProperties, times(1)).containsAwsSessionToken()
    verify(userProperties, times(1)).getAwsServiceEndpoint()
    verify(userProperties, times(1)).hasNamedConnection()
    verify(userProperties, never).getAwsSessionToken()
  }

  test("createKinesisClient returns AmazonKinesis with connection name") {
    when(userProperties.containsAwsSessionToken()).thenReturn(true)
    when(userProperties.getAwsSessionToken()).thenReturn("MY_SESSION_TOKEN")
    when(userProperties.containsAwsServiceEndpoint()).thenReturn(false)
    when(userProperties.hasNamedConnection()).thenReturn(true)
    when(userProperties.mergeWithConnectionObject(exaMetadata)).thenReturn(userProperties)
    val kinesisClient = KinesisClientFactory.createKinesisClient(userProperties, exaMetadata)
    assert(kinesisClient.isInstanceOf[AmazonKinesis])
    verify(userProperties, times(1)).hasNamedConnection()
    verify(userProperties, times(1)).mergeWithConnectionObject(exaMetadata)
  }
}
