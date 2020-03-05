package com.exasol.cloudetl.kinesis

import com.exasol.cloudetl.kinesis.KinesisUserProperties._

import org.scalatest.funsuite.AnyFunSuite

class KinesisUserPropertiesTest extends AnyFunSuite {
  private[this] val kinesisProperties = Map(
    "TABLE_NAME" -> "TEST_TABLE",
    "AWS_ACCESS_KEY" -> "MY_ACCESS_KEY",
    "AWS_SECRET_KEY" -> "MY_SECRET_KEY",
    "AWS_SESSION_TOKEN" -> "MY_SESSION_TOKEN",
    "STREAM_NAME" -> "Test_stream",
    "REGION" -> "eu-west-1"
  )
  test("getAwsAccessKeyProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getAwsAccessKey === "MY_ACCESS_KEY")
  }

  test("getAwsSecretKeyProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getAwsSecretKey === "MY_SECRET_KEY")
  }

  test("getAwsSessionTokenProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getAwsSessionToken === "MY_SESSION_TOKEN")
  }

  test("getStreamNameProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getStreamName === "Test_stream")
  }

  test("getRegionProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getRegion === "eu-west-1")
  }

  test("getTableNameProperty returns value from Map") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(kinesisUserProperties.getTableName === "TEST_TABLE")
  }

  test("getAwsAccessKeyProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getAwsAccessKey
    }
    assert(
      thrown.getMessage === s"Please provide a value for the $AWS_ACCESS_KEY_PROPERTY property!"
    )
  }

  test("getAwsSecretKeyProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getAwsSecretKey
    }
    assert(
      thrown.getMessage === s"Please provide a value for the $AWS_SECRET_KEY_PROPERTY property!"
    )
  }

  test("getAwsSessionTokenProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getAwsSessionToken
    }
    assert(
      thrown.getMessage === s"Please provide a value for the $AWS_SESSION_TOKEN_PROPERTY property!"
    )
  }

  test("getStreamNameProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getStreamName
    }
    assert(
      thrown.getMessage === s"Please provide a value for the $STREAM_NAME_PROPERTY property!"
    )
  }

  test("getRegionProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getRegion
    }
    assert(thrown.getMessage === s"Please provide a value for the $REGION_PROPERTY property!")
  }

  test("getTableNameProperty throws an exception as the property is missing") {
    val kinesisUserProperties = new KinesisUserProperties(Map.empty[String, String])
    val thrown = intercept[IllegalArgumentException] {
      kinesisUserProperties.getTableName
    }
    assert(thrown.getMessage === s"Please provide a value for the $TABLE_NAME_PROPERTY property!")
  }

  test("makeString returns a properties string") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(
      kinesisUserProperties.mkString() ===
        """AWS_ACCESS_KEY -> MY_ACCESS_KEY;
          |AWS_SECRET_KEY -> MY_SECRET_KEY;
          |AWS_SESSION_TOKEN -> MY_SESSION_TOKEN;
          |REGION -> eu-west-1;
          |STREAM_NAME -> Test_stream;
          |TABLE_NAME -> TEST_TABLE""".stripMargin.replace("\n", "")
    )
  }

  test("makeString returns a properties string for a custom map") {
    val propertiesMap = Map(
      "TABLE_NAME" -> "TEST_TABLE",
      "REGION" -> "eu-west-1"
    )
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    assert(
      kinesisUserProperties
        .mkString(propertiesMap) === "REGION -> eu-west-1;TABLE_NAME -> TEST_TABLE"
    )
  }

  test("createNewPropertiesMap creates a property map with selected properties") {
    val kinesisUserProperties = new KinesisUserProperties(kinesisProperties)
    val newProperties =
      kinesisUserProperties.createSelectedPropertiesMap(TABLE_NAME_PROPERTY, STREAM_NAME_PROPERTY)
    assert(newProperties === Map("TABLE_NAME" -> "TEST_TABLE", "STREAM_NAME" -> "Test_stream"))
  }
}
