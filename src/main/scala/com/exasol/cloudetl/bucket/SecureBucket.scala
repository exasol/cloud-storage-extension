package com.exasol.cloudetl.bucket

import com.typesafe.scalalogging.LazyLogging

/**
 * A trait that provides methods to access [[Bucket]]s using secure
 * access credentials.
 */
trait SecureBucket extends LazyLogging { self: Bucket =>

  /**
   * Return the list of property key names that are used as secure
   * access credentials.
   *
   * For example, {@code AWS_SECRET_KEY} when accessing an S3 bucket.
   */
  def getSecureProperties(): Seq[String]

  /**
   * Validates that the named connection object or access credentials
   * are available.
   */
  protected[this] final def validateConnectionProperties(): Unit = {
    if (hasSecureProperties()) {
      logger.info(
        "Using secure credential parameters is deprecated. " +
          "Please use an Exasol named connection object via CONNECTION_NAME property."
      )
    }
    val connectionExceptionMessage =
      "Please provide either CONNECTION_NAME property or secure access " +
        "credentials parameters, but not the both!"
    if (properties.hasNamedConnection()) {
      if (hasSecureProperties()) {
        throw new IllegalArgumentException(connectionExceptionMessage)
      }
    } else {
      if (!hasSecureProperties()) {
        throw new IllegalArgumentException(connectionExceptionMessage)
      }
    }
  }

  private[this] def hasSecureProperties(): Boolean =
    getSecureProperties.exists(properties.containsKey(_))

}
