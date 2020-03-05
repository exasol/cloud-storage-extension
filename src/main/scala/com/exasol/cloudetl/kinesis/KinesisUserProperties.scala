package com.exasol.cloudetl.kinesis

import scala.collection.SortedMap

import com.exasol.cloudetl.common.{AbstractProperties, CommonProperties}

/**
 * A companion object for [[KinesisUserProperties]] class.
 */
object KinesisUserProperties extends CommonProperties {
  val AWS_ACCESS_KEY_PROPERTY: String = "AWS_ACCESS_KEY"
  val AWS_SECRET_KEY_PROPERTY: String = "AWS_SECRET_KEY"
  val AWS_SESSION_TOKEN_PROPERTY: String = "AWS_SESSION_TOKEN"
  val STREAM_NAME_PROPERTY: String = "STREAM_NAME"
  val REGION_PROPERTY: String = "REGION"
  val TABLE_NAME_PROPERTY: String = "TABLE_NAME"

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

  final def getAwsAccessKey: String =
    getString(AWS_ACCESS_KEY_PROPERTY)

  final def getAwsSecretKey: String =
    getString(AWS_SECRET_KEY_PROPERTY)

  final def getAwsSessionToken: String =
    getString(AWS_SESSION_TOKEN_PROPERTY)

  final def getStreamName: String =
    getString(STREAM_NAME_PROPERTY)

  final def getRegion: String =
    getString(REGION_PROPERTY)

  final def getTableName: String =
    getString(TABLE_NAME_PROPERTY)

  /**
   * Converts a properties map to a string.
   *
   * @param propertiesMap A map with properties.
   */
  final def mkString(propertiesMap: Map[String, String]): String =
    (SortedMap.empty[String, String] ++ propertiesMap)
      .map { case (k, v) => s"$k$KEY_VALUE_SEPARATOR$v" }
      .mkString(PROPERTY_SEPARATOR)

  /**
   * Converts a properties map to a string.
   *
   * Uses a map that user passed to a constructor.
   */
  final def mkString(): String =
    mkString(propertiesMap)

  /**
   * Creates a new [[scala.collection.Map]] with user-selected properties based on the existing
   * one.
   *
   * @param propertyNames Names of the properties to include.
   */
  final def createSelectedPropertiesMap(propertyNames: String*): Map[String, String] =
    propertiesMap.filter { case (key, _) => propertyNames.contains(key) }
}
