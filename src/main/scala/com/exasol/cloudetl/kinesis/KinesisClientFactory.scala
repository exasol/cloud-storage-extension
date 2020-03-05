package com.exasol.cloudetl.kinesis

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicSessionCredentials}
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
  def createKinesisClient(kinesisUserProperties: KinesisUserProperties): AmazonKinesis = {
    val awsAccessKeyId = kinesisUserProperties.getAwsAccessKey
    val awsSecretAccessKey = kinesisUserProperties.getAwsSecretKey
    val awsSessionToken = kinesisUserProperties.getAwsSessionToken
    val region = kinesisUserProperties.getRegion
    val awsCredentials =
      new BasicSessionCredentials(awsAccessKeyId, awsSecretAccessKey, awsSessionToken)
    AmazonKinesisClientBuilder.standard
      .withRegion(region)
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .build
  }
}
