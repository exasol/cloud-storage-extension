package com.exasol.cloudetl.kinesis

import com.amazonaws.services.kinesis.AmazonKinesis
import org.scalatest.funsuite.AnyFunSuite

class KinesisClientFactoryTest extends AnyFunSuite {
  test("createKinesisClient returns AmazonKinesis") {
    val kinesisProperties = Map(
      "AWS_ACCESS_KEY" -> "MY_ACCESS_KEY",
      "AWS_SECRET_KEY" -> "MY_SECRET_KEY",
      "AWS_SESSION_TOKEN" -> "MY_SESSION_TOKEN",
      "REGION" -> "eu-west-1"
    )
    val userProperties = KinesisUserProperties(kinesisProperties)
    val kinesisClient = KinesisClientFactory.createKinesisClient(userProperties)
    assert(kinesisClient.isInstanceOf[AmazonKinesis])
  }
}
