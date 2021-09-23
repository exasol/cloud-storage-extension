package com.exasol.cloudetl.storage

import com.exasol.ExaMetadata
import com.exasol.common.{AbstractProperties, CommonProperties}
import com.exasol.errorreporting.ExaError

import org.apache.hadoop.fs.Path

/**
 * A specific implementation of [[com.exasol.common.AbstractProperties]] that handles user provided key-value parameters
 * for storage import and export user-defined-functions (UDFs).
 */
class StorageProperties(private val properties: Map[String, String], private val exaMetadata: Option[ExaMetadata])
    extends AbstractProperties(properties) {

  import StorageProperties._

  private[this] val DEFAULT_CHUNK_SIZE = 64L * 1024 * 1024
  private[this] val AZURE_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.AzureLogStore"
  private[this] val S3_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore"
  private[this] val HDFS_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.HDFSLogStore"

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
    case "abfs"  => AZURE_DELTA_LOG_STORE_CLASS
    case "abfss" => AZURE_DELTA_LOG_STORE_CLASS
    case "adl"   => AZURE_DELTA_LOG_STORE_CLASS
    case "gs" =>
      throw new UnsupportedOperationException(
        ExaError
          .messageBuilder("E-CSE-16")
          .message("Delta format LogStore API is not supported in Google Cloud Storage yet.")
          .toString()
      )
    case "s3a"   => S3_DELTA_LOG_STORE_CLASS
    case "wasb"  => AZURE_DELTA_LOG_STORE_CLASS
    case "wasbs" => AZURE_DELTA_LOG_STORE_CLASS
    case _       => HDFS_DELTA_LOG_STORE_CLASS
  }

  /** Returns the [[FileFormat]] file format. */
  final def getFileFormat(): FileFormat =
    FileFormat(getString(DATA_FORMAT))

  /** Returns a chunk size for splitting a Parquet file. */
  final def getChunkSize(): Long = get(CHUNK_SIZE).fold(DEFAULT_CHUNK_SIZE)(_.toLong)

  /**
   * Returns the number of partitions provided as user property.
   *
   * If it is not set, returns default value {@code nproc}.
   */
  final def getParallelism(defaultValue: => String): String =
    get(PARALLELISM).fold(defaultValue)(identity)

  /**
   * Checks if the overwite parameter is set to true.
   *
   * If the overwrite parameter is not set, then returns default {@code
   * false} value.
   */
  final def isOverwrite(): Boolean =
    isEnabled(OVERWRITE)

  /**
   * Checks if the Parquet export schema with lower case fields enabled.
   *
   * By default it is set to {@code true}.
   */
  final def isParquetLowercaseSchema(): Boolean =
    get(PARQUET_LOWERCASE_SCHEMA).fold(true)(_.toBoolean)

  /**
   * Returns the proxy host to use for the bucket.
   */
  final def getProxyHost(): Option[String] = get(PROXY_HOST)

  /**
   * Returns the proxy port to use for the bucket.
   */
  final def getProxyPort(): Option[String] = get(PROXY_PORT)

  /**
   * Returns the proxy username.
   */
  final def getProxyUsername(): Option[String] = get(PROXY_USERNAME)

  /**
   * Returns the proxy password.
   */
  final def getProxyPassword(): Option[String] = get(PROXY_PASSWORD)

  /**
   * Returns a new [[StorageProperties]] that merges the key-value pairs parsed from user provided Exasol named
   * connection object.
   */
  final def merge(accountName: String): StorageProperties = {
    val connectionParsedMap = parseConnectionInfo(accountName, exaMetadata)
    val newProperties = properties ++ connectionParsedMap
    new StorageProperties(newProperties, exaMetadata)
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

  /** An optional property key name for the overwite. */
  private[storage] final val OVERWRITE: String = "OVERWRITE"

  /** An optional property key name for size of chunk to split a Parquet file. */
  private[storage] val CHUNK_SIZE = "CHUNK_SIZE"

  /** An optional property for setting Parquet export schema with lowercase fields. */
  private[storage] final val PARQUET_LOWERCASE_SCHEMA: String = "PARQUET_LOWERCASE_SCHEMA"

  /** An optional proxy host that needs to be used when accessing the bucket. */
  private[storage] final val PROXY_HOST: String = "PROXY_HOST"

  /** An optional proxy port that needs to be used when accessing the bucket. */
  private[storage] final val PROXY_PORT: String = "PROXY_PORT"

  /** An optional username for authentication against the proxy. */
  private[storage] final val PROXY_USERNAME: String = "PROXY_USERNAME"

  /** An optional password for authentication the proxy. */
  private[storage] final val PROXY_PASSWORD: String = "PROXY_PASSWORD"

  /**
   * Returns [[StorageProperties]] from key values map and [[ExaMetadata]] metadata object.
   */
  def apply(params: Map[String, String], metadata: ExaMetadata): StorageProperties =
    new StorageProperties(params, Option(metadata))

  /**
   * Returns [[StorageProperties]] from only key-value pairs map.
   */
  def apply(params: Map[String, String]): StorageProperties =
    new StorageProperties(params, None)

  /**
   * Returns [[StorageProperties]] from [[java.util.Map]] key-value
   * pairs map.
   */
  def apply(params: java.util.Map[String, String]): StorageProperties = {
    val keyValueMap = scala.collection.mutable.Map.empty[String, String]
    params.forEach { (key, value) =>
      keyValueMap.update(key, value)
    }
    new StorageProperties(keyValueMap.toMap, None)
  }

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
