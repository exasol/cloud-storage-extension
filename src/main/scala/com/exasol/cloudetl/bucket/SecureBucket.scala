package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageConnectionInformation

import com.typesafe.scalalogging.LazyLogging

/**
 * A trait that provides methods to access [[Bucket]]s using secure
 * access credentials.
 */
trait SecureBucket extends LazyLogging { self: Bucket =>

  /**
   * A placeholder variable for different access account methods in the
   * specific bucket implementations.
   *
   * Having these variables abstracted makes the bucket implementations
   * cleaner.
   *
   * For example, when accessing S3 bucket, accountName is set as {@code
   * AWS_ACCESS_KEY}.
   */
  val accountName: String

  /**
   * A placeholder variable for different access secret methods in the
   * specific bucket implementations.
   *
   * For example, when accessing S3 bucket, accountSecret is set as
   * {@code AWS_SECRET_KEY}.
   */
  val accountSecret: String

  /**
   * Validates that the named connection object or access credentials
   * are available.
   */
  protected[this] final def validateConnectionProperties(): Unit = {
    if (hasSecureProperties()) {
      logger.info(
        s"Using secure credentials $accountName and $accountSecret properties is deprecated. " +
          "Please use an Exasol named connection object via CONNECTION_NAME property."
      )
    }
    val connectionExceptionMessage =
      s"Please provide either only CONNECTION_NAME property or $accountName " +
        s"and $accountSecret property pairs, but not the both!"
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
    properties.containsKey(accountName) && properties.containsKey(accountSecret)

  /**
   * Returns the [[com.exasol.ExaConnectionInformation]] Exasol named
   * connection information object for this bucket.
   */
  final def getStorageConnectionInformation(): StorageConnectionInformation =
    if (!properties.hasNamedConnection() && hasSecureProperties()) {
      StorageConnectionInformation(
        properties.getString(accountName),
        properties.getString(accountSecret)
      )
    } else if (properties.hasNamedConnection()) {
      val exaConnectionInfo = properties.getConnectionInformation()
      StorageConnectionInformation(exaConnectionInfo.getUser(), exaConnectionInfo.getPassword())
    } else {
      throw new IllegalArgumentException("Please provide a CONNECTION_NAME property!")
    }

}
