package com.exasol.cloudetl.common

/**
 * A trait with common variables and methods that can be used in
 * companion objects of specific properties classes.
 */
trait CommonProperties {

  /**
   * A line separator string used for creating key-value property
   * strings.
   */
  final val PROPERTY_SEPARATOR: String = ";"

  /**
   * A default separator string used for concatenate key-value pairs.
   */
  final val KEY_VALUE_SEPARATOR: String = " -> "

  /**
   * Parses propertly separated input string into key value pair map
   * data structure.
   */
  final def mapFromString(string: String): Map[String, String] = {
    if (!string.contains(PROPERTY_SEPARATOR)) {
      throw new IllegalArgumentException(
        s"The input string is not separated by '$PROPERTY_SEPARATOR'!"
      )
    }
    string
      .split(PROPERTY_SEPARATOR)
      .map { word =>
        val pairs = word.split(KEY_VALUE_SEPARATOR)
        pairs(0) -> pairs(1)
      }
      .toMap
  }

}
