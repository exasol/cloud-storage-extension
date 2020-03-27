package com.exasol.cloudetl.common

import scala.collection.SortedMap

import com.exasol.{ExaConnectionInformation, ExaMetadata}

/**
 * An abstract class that holds the user provided key-value parameters
 * when using the user-defined-functions (UDFs).
 *
 * This only represents the raw string key-value pairs. Specific
 * implementations should extends this class to support required UDF
 * key-value parameters.
 */
abstract class AbstractProperties(private val properties: Map[String, String]) {

  /** An optional property key name for the named connection object. */
  final val CONNECTION_NAME_PROPERTY: String = "CONNECTION_NAME"

  /**
   * Checks whether the key-value properties map is empty.
   */
  final def isEmpty(): Boolean =
    properties.isEmpty

  /**
   * Checks whether the properties contain binding to the provided key.
   */
  final def containsKey(key: String): Boolean =
    properties.contains(key)

  /**
   * Checks whether given key value is set to {@code true}.
   */
  final def isEnabled(key: String): Boolean =
    containsKey(key) && "true".equalsIgnoreCase(properties.get(key).getOrElse(""))

  /**
   * Checks whether the value is {@code null} for given key.
   *
   * Returns {@code true} if the key does not exist in the properties map.
   */
  final def isNull(key: String): Boolean =
    properties.get(key).fold(true) { value =>
      value eq null
    }

  /**
   * Returns the optional value for the given key.
   *
   * @return A None if key does not exists; otherwise Some(value).
   */
  final def get(key: String): Option[String] =
    properties.get(key)

  /** Checks if the Exasol named connection property is provided. */
  final def hasNamedConnection(): Boolean =
    containsKey(CONNECTION_NAME_PROPERTY)

  /**
   * Parses the connection object password into key-value map pairs.
   *
   * If the connection object contains the username, it is mapped to the
   * {@code keyForUsername} parameter. However, this value is
   * overwritten if the provided key is available in password string of
   * connection object.
   */
  final def parseConnectionInfo(
    keyForUsername: String,
    exaMetadata: Option[ExaMetadata]
  ): Map[String, String] = {
    val connection = getConnectionInformation(exaMetadata)
    val username = connection.getUser()
    val password = connection.getPassword();
    val map = password
      .split(";")
      .map { str =>
        val idx = str.indexOf('=')
        if (idx < 0) {
          throw new IllegalArgumentException(
            "Connection object password does not contain key=value pairs!"
          )
        }
        str.substring(0, idx).strip().replace("\n", "") ->
          str.substring(idx + 1).strip().replace("\n", "")
      }
      .toMap

    if (username.isEmpty()) {
      map
    } else {
      Map(keyForUsername -> username) ++ map
    }
  }

  /**
   * Returns an Exasol [[ExaConnectionInformation]] named connection
   * information.
   */
  private[common] final def getConnectionInformation(
    exaMetadata: Option[ExaMetadata]
  ): ExaConnectionInformation =
    exaMetadata.fold {
      throw new IllegalArgumentException("Exasol metadata is None!")
    }(_.getConnection(getString(CONNECTION_NAME_PROPERTY)))

  /**
   * Returns the value of the key as a String.
   *
   * throws java.lang.IllegalArgumentException If key does not exist.
   */
  @throws[IllegalArgumentException]("If key does not exist.")
  final def getString(key: String): String =
    get(key).fold {
      throw new IllegalArgumentException(s"Please provide a value for the $key property!")
    }(identity)

  /**
   * Returns the count of the key-value properties.
   */
  final def size(): Int =
    properties.size

  /**
   * Returns a string listing of all key-value property pairs.
   *
   * The resulting string contains key-value pairs in a sorted order by
   * keys.
   *
   * @param keyValueSeparator The separator between each key-value pairs
   * @param propertySeparator The separator between each key-value pair strings
   * @return The string value of properties with provided separators
   */
  final def mkString(keyValueSeparator: String, propertySeparator: String): String =
    (SortedMap.empty[String, String] ++ properties)
      .map { case (k, v) => s"$k$keyValueSeparator$v" }
      .mkString(propertySeparator)

  @SuppressWarnings(Array("org.wartremover.warts.Return"))
  // scalastyle:off
  final override def equals(other: Any): Boolean = {
    if (!other.isInstanceOf[AbstractProperties]) {
      return false
    }
    val that = other.asInstanceOf[AbstractProperties]
    if (size() != that.size()) {
      return false
    }
    if (that.properties.equals(properties)) {
      true
    } else {
      false
    }
  }
  // scalastyle:on

  final override def hashCode(): Int =
    properties.hashCode()

}
