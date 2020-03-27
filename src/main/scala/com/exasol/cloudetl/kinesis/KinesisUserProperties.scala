package com.exasol.cloudetl.kinesis

import scala.collection.SortedMap

import com.exasol.ExaMetadata
import com.exasol.cloudetl.common.{AbstractProperties, CommonProperties}

/**
 * A companion object for [[KinesisUserProperties]] class.
 */
object KinesisUserProperties extends CommonProperties {
  val AWS_ACCESS_KEY_PROPERTY: String = "AWS_ACCESS_KEY"
  val AWS_SECRET_KEY_PROPERTY: String = "AWS_SECRET_KEY"
  val AWS_SESSION_TOKEN_PROPERTY: String = "AWS_SESSION_TOKEN"
  val AWS_SERVICE_ENDPOINT_PROPERTY: String = "AWS_SERVICE_ENDPOINT"
  val STREAM_NAME_PROPERTY: String = "STREAM_NAME"
  val REGION_PROPERTY: String = "REGION"
  val TABLE_NAME_PROPERTY: String = "TABLE_NAME"
  val MAX_RECORDS_PER_RUN_PROPERTY: String = "MAX_RECORDS_PER_RUN"

  /**
   * Creates an instance of [[KinesisUserProperties]] from user provided key value
   * properties.
   */
  def apply(params: Map[String, String]): KinesisUserProperties =
    new KinesisUserProperties(params)

  /**
   * Creates an instance of [[KinesisUserProperties]] from a user provided formatted
   * string with properties.
   */
  def apply(string: String): KinesisUserProperties =
    apply(mapFromString(string))
}

/**
 * This class parses and stores Kinesis user's properties.
 */
class KinesisUserProperties(val propertiesMap: Map[String, String])
    extends AbstractProperties(propertiesMap) {
  import KinesisUserProperties._

  final def getAwsAccessKey(): String =
    getString(AWS_ACCESS_KEY_PROPERTY)

  final def getAwsSecretKey(): String =
    getString(AWS_SECRET_KEY_PROPERTY)

  final def containsAwsSecretKey(): Boolean =
    containsKey(AWS_SECRET_KEY_PROPERTY)

  final def getAwsSessionToken(): String =
    getString(AWS_SESSION_TOKEN_PROPERTY)

  final def containsAwsSessionToken(): Boolean =
    containsKey(AWS_SESSION_TOKEN_PROPERTY)

  final def getAwsServiceEndpoint(): String =
    getString(AWS_SERVICE_ENDPOINT_PROPERTY)

  final def containsAwsServiceEndpoint(): Boolean =
    containsKey(AWS_SERVICE_ENDPOINT_PROPERTY)

  final def getStreamName(): String =
    getString(STREAM_NAME_PROPERTY)

  final def getRegion(): String =
    getString(REGION_PROPERTY)

  final def getTableName(): String =
    getString(TABLE_NAME_PROPERTY)

  final def containsMaxRecordsPerRun(): Boolean =
    containsKey(MAX_RECORDS_PER_RUN_PROPERTY)

  final def getMaxRecordsPerRun(): Int =
    getString(MAX_RECORDS_PER_RUN_PROPERTY).toInt

  /**
   * Returns a new [[KinesisUserProperties]] that merges the key-value pairs
   * parsed from user provided Exasol named connection object.
   */
  final def mergeWithConnectionObject(exaMetadata: ExaMetadata): KinesisUserProperties = {
    validateConnectionObject()
    val connectionParsedMap =
      parseConnectionInfo(AWS_ACCESS_KEY_PROPERTY, Option(exaMetadata))
    val newProperties = propertiesMap ++ connectionParsedMap
    new KinesisUserProperties(newProperties)
  }

  private[this] def validateConnectionObject(): Unit =
    if (containsAwsSecretKey() || containsAwsSessionToken()) {
      throw new KinesisConnectorException(
        "Please provide either CONNECTION_NAME property or " +
          "secure access credentials parameters, but not the both!"
      )
    }

  /**
   * Converts a properties map to a string.
   *
   * Uses a map that user passed to a constructor.
   */
  final def mkString(): String =
    (SortedMap.empty[String, String] ++ propertiesMap)
      .map { case (k, v) => s"$k$KEY_VALUE_SEPARATOR$v" }
      .mkString(PROPERTY_SEPARATOR)
}
