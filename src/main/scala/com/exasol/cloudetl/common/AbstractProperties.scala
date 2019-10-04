package com.exasol.cloudetl.common

/**
 * An abstract class that holds the user provided key-value parameters
 * when using the user-defined-functions (UDFs).
 *
 * This only represents the raw string key-value pairs. Specific
 * implementations should extends this class to support required UDF
 * key-value parameters.
 */
abstract class AbstractProperties(private val properties: Map[String, String]) {

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
      value eq null // scalastyle:ignore null
    }

  /**
   * Returns the optional value for the given key.
   *
   * @return A None if key does not exists; otherwise Some(value).
   */
  final def get(key: String): Option[String] =
    properties.get(key)

  /**
   * Returns the count of the key-value properties.
   */
  final def size(): Int =
    properties.size

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.AsInstanceOf",
      "org.wartremover.warts.IsInstanceOf",
      "org.wartremover.warts.Return"
    )
  )
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
