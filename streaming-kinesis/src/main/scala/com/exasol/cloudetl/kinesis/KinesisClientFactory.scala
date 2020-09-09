package com.exasol.cloudetl.kinesis

import com.exasol.ExaMetadata

import com.amazonaws.auth.{
  AWSCredentials,
  AWSStaticCredentialsProvider,
  BasicAWSCredentials,
  BasicSessionCredentials
}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.kinesis.{AmazonKinesis, AmazonKinesisClientBuilder}

/**
 * This object provides a factory method to create an instance of
 * [[com.amazonaws.services.kinesis.AmazonKinesis]].
 */
object KinesisClientFactory {

  /**
   * Creates an instance of [[com.amazonaws.services.kinesis.AmazonKinesis]].
   *
   * @param kinesisUserProperties An instance of [[KinesisUserProperties]] class
   * with user properties.
   */
  def createKinesisClient(
    kinesisUserProperties: KinesisUserProperties,
    exaMetadata: ExaMetadata
  ): AmazonKinesis = {
    val localKinesisUserProperties = if (kinesisUserProperties.hasNamedConnection()) {
      kinesisUserProperties.mergeWithConnectionObject(exaMetadata)
    } else {
      kinesisUserProperties
    }
    val region = localKinesisUserProperties.getRegion()
    val awsCredentials = createAwsCredentials(localKinesisUserProperties)
    val kinesisClientBuilder = AmazonKinesisClientBuilder.standard
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
    if (localKinesisUserProperties.containsAwsServiceEndpoint()) {
      kinesisClientBuilder.setEndpointConfiguration(
        new EndpointConfiguration(
          localKinesisUserProperties.getAwsServiceEndpoint(),
          region
        )
      )
    } else {
      kinesisClientBuilder.setRegion(region)
    }
    kinesisClientBuilder.build
  }

  private[this] def createAwsCredentials(
    kinesisUserProperties: KinesisUserProperties
  ): AWSCredentials = {
    val awsAccessKeyId = kinesisUserProperties.getAwsAccessKey()
    val awsSecretAccessKey = kinesisUserProperties.getAwsSecretKey()
    if (kinesisUserProperties.containsAwsSessionToken()) {
      val awsSessionToken = kinesisUserProperties.getAwsSessionToken()
      new BasicSessionCredentials(awsAccessKeyId, awsSecretAccessKey, awsSessionToken)
    } else {
      new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)
    }
  }
}
