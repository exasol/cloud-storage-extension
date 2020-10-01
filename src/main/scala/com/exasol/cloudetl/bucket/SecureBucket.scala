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
      throw new BucketValidationException(
        "Using credentials as parameters is forbidded. Please use an Exasol " +
          "named connection object via CONNECTION_NAME property."
      )
    }
    if (!properties.hasNamedConnection()) {
      throw new BucketValidationException(
        "No CONNECTION_NAME property is defined. Please use connection object " +
          "to provide access credentials."
      )
    }
  }

  private[this] def hasSecureProperties(): Boolean =
    getSecureProperties.exists(properties.containsKey(_))

}
