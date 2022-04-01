package com.exasol.cloudetl.transform

import com.exasol.cloudetl.constants.Constants.TRUNCATE_STRING
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.errorreporting.ExaError

/**
 * Base default transformation class.
 */
final class DefaultTransformation(val properties: StorageProperties) extends Transformation {
  private[this] val MAX_VARCHAR_SIZE = 2000000
  private[this] val hasTruncateString = properties.isEnabled(TRUNCATE_STRING)

  override def transform(values: Array[Object]): Array[Object] = {
    var index = 0
    while (index < values.length) {
      val value = values(index)
      if (value.isInstanceOf[String]) {
        values(index) = transformString(value.asInstanceOf[String])
      }
      index += 1
    }
    values
  }

  private[this] def transformString(value: String): String = {
    if (value.length() <= MAX_VARCHAR_SIZE) {
      return value // scalastyle:ignore
    }
    if (!hasTruncateString) {
      throw new IllegalStateException(
        ExaError
          .messageBuilder("E-CSE-29")
          .message("Length of a string value exceeds Exasol maximum allowed '2000000' VARCHAR length.")
          .mitigation("Please make sure that the string is shorter or equal to maximum allowed length")
          .mitigation("Please set 'TRUNCATE_STRING' parameter to 'true' to enable truncating longer strings.")
          .toString()
      )
    }
    value.substring(0, MAX_VARCHAR_SIZE)
  }

}
