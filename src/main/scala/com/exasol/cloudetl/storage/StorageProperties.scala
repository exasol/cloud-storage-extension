package com.exasol.cloudetl.storage

import com.exasol.ExaConnectionInformation
import com.exasol.ExaMetadata
import com.exasol.cloudetl.common.AbstractProperties
import com.exasol.cloudetl.common.CommonProperties

import org.apache.hadoop.fs.Path

/**
 * A specific implementation of
 * [[com.exasol.cloudetl.common.AbstractProperties]] that handles user
 * provided key-value parameters for storage import and export
 * user-defined-functions (udfs).
 */
class StorageProperties(
  private val properties: Map[String, String],
  private val exaMetadata: Option[ExaMetadata]
) extends AbstractProperties(properties) {

  import StorageProperties._

  /**
   * Returns the storage path.
   *
   * It is a main path, for example, to a bucket where data files are
   * stored.
   */
  final def getStoragePath(): String =
    getString(BUCKET_PATH)

  /**
   * Returns the storage path scheme.
   *
   * For example, given the S3 bucket, `s3a://my-bucket/data/` path to
   * load data, returns the scheme `s3a` value.
   */
  final def getStoragePathScheme(): String =
    new Path(getStoragePath()).toUri.getScheme

  /**
   * Returns the Delta format LogStore storage class name.
   *
   * Please check out the
   * [[https://docs.delta.io/latest/delta-storage.html Delta Storage]]
   * documentation for available storage classes and storage class
   * requirements.
   */
  final def getDeltaFormatLogStoreClassName(): String = getStoragePathScheme() match {
    case "abfs" | "abfss" =>
      throw new UnsupportedOperationException(
        "Delta format LogStore API is not supported in Azure Data Lake Gen2 storage yet."
      )
    case "adl" => "org.apache.spark.sql.delta.storage.AzureLogStore"
    case "gs" =>
      throw new UnsupportedOperationException(
        "Delta format LogStore API is not supported in Google Cloud Storage yet."
      )
    case "s3a"            => "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore"
    case "wasb" | "wasbs" => "org.apache.spark.sql.delta.storage.AzureLogStore"
    case _                => "org.apache.spark.sql.delta.storage.HDFSLogStore"
  }

  /** Returns the [[FileFormat]] file format. */
  final def getFileFormat(): FileFormat =
    FileFormat(getString(DATA_FORMAT))

  /**
   * Returns the number of partitions provided as user property.
   *
   * If it is not set, returns default value {@code nproc}.
   */
  final def getParallelism(defaultValue: => String): String =
    get(PARALLELISM).fold(defaultValue)(identity)

  /**
   * Returns an Exasol [[ExaConnectionInformation]] named connection
   * information.
   */
  final def getConnectionInformation(): ExaConnectionInformation =
    exaMetadata.fold {
      throw new IllegalArgumentException("Exasol metadata is None!")
    }(_.getConnection(getString(CONNECTION_NAME)))

  /** Checks if the Exasol named connection property is provided. */
  final def hasNamedConnection(): Boolean =
    containsKey(CONNECTION_NAME)

  /**
   * Returns a new [[StorageProperties]] that merges the key-value pairs
   * parsed from user provided Exasol named connection object.
   */
  final def merge(accountName: String): StorageProperties = {
    val connectionParsedMap = parseConnectionInfo(accountName)
    val newProperties = properties ++ connectionParsedMap
    new StorageProperties(newProperties, exaMetadata)
  }

  /**
   * Parses the connection object password into key-value map pairs.
   *
   * If the connection object contains the username, it is mapped to the
   * {@code keyForUsername} parameter. However, this value is
   * overwritted if the provided key is available in password string of
   * connection object.
   */
  private[this] def parseConnectionInfo(keyForUsername: String): Map[String, String] = {
    val connection = getConnectionInformation()
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
        str.substring(0, idx) -> str.substring(idx + 1)
      }
      .toMap

    if (username.isEmpty()) {
      map
    } else {
      Map(keyForUsername -> username) ++ map
    }
  }

  /**
   * Returns a string value of key-value property pairs.
   *
   * The returned string is sorted by keys ordering.
   */
  final def mkString(): String =
    mkString(KEY_VALUE_SEPARATOR, PROPERTY_SEPARATOR)

}

/**
 * A companion object for [[StorageProperties]] class.
 */
object StorageProperties extends CommonProperties {

  /** A required property key name for a bucket path. */
  private[storage] final val BUCKET_PATH: String = "BUCKET_PATH"

  /** A required property key name for a data format. */
  private[storage] final val DATA_FORMAT: String = "DATA_FORMAT"

  /** An optional property key name for the parallelism. */
  private[storage] final val PARALLELISM: String = "PARALLELISM"

  /** An optional property key name for the named connection object. */
  private[storage] final val CONNECTION_NAME: String = "CONNECTION_NAME"

  /**
   * Returns [[StorageProperties]] from key values map and
   * [[ExaMetadata]] metadata object.
   */
  def apply(params: Map[String, String], metadata: ExaMetadata): StorageProperties =
    new StorageProperties(params, Option(metadata))

  /** Returns [[StorageProperties]] from only key-value pairs map. */
  def apply(params: Map[String, String]): StorageProperties =
    new StorageProperties(params, None)

  /**
   * Returns [[StorageProperties]] from properly separated string and
   * [[ExaMetadata]] metadata object.
   */
  def apply(string: String, metadata: ExaMetadata): StorageProperties =
    apply(mapFromString(string), metadata)

  /** Returns [[StorageProperties]] from properly separated string. */
  def apply(string: String): StorageProperties =
    apply(mapFromString(string))

}
