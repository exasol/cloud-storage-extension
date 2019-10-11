package com.exasol.cloudetl.bucket

import java.net.URI

import scala.collection.SortedMap

import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.cloudetl.util.FileSystemUtil

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

/**
 * Abstract representation of a bucket.
 *
 * We adopted the name 'bucket' to mean a cloud storage path. For
 * example, a specific implementation of this class can be an AWS S3
 * bucket, Azure Blob store, Azure Data Lake store, or a Google Cloud
 * storage.
 *
 * All specific implementation of a bucket should extend this class.
 */
abstract class Bucket {

  /** The path string of the bucket. */
  val bucketPath: String

  /** The user provided key value pair properties. */
  val properties: StorageProperties

  /**
   * Returns the sequence of key-value properties required for this
   * specific storage class.
   */
  def getRequiredProperties(): Seq[String]

  /** Validates that all required parameter key values are available. */
  final def validate(): Unit =
    validateRequiredProperties()

  private[this] def validateRequiredProperties(): Unit =
    getRequiredProperties().foreach { key =>
      if (!properties.containsKey(key)) {
        throw new IllegalArgumentException(
          s"Please provide a value for the $key property!"
        )
      }
    }

  /**
   * Creates a Hadoop [[org.apache.hadoop.conf.Configuration]] for this
   * specific bucket type.
   */
  def getConfiguration(): Configuration

  /**
   * The Hadoop [[org.apache.hadoop.fs.FileSystem]] for this specific
   * bucket path.
   */
  final lazy val fileSystem: FileSystem =
    FileSystem.get(new URI(bucketPath), getConfiguration())

  /**
   * Get the all the paths in this bucket path.
   *
   * This method also globifies the bucket path if it contains regex.
   */
  final def getPaths(): Seq[Path] =
    FileSystemUtil.globWithPattern(bucketPath, fileSystem)
}

/**
 * A companion object to the [[Bucket]] class.
 *
 * Provides a factory method to create bucket and several utility
 * functions.
 */
@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
object Bucket extends LazyLogging {

  /**
   * Creates specific [[Bucket]] class using the path scheme from
   * [[com.exasol.cloudetl.storage.StorageProperties]] properties.
   *
   * @param storageProperties The user provided storage key-value
   *        properties
   * @return A [[Bucket]] class for the given path
   */
  def apply(storageProperties: StorageProperties): Bucket = {
    val path = storageProperties.getStoragePath()
    val scheme = storageProperties.getStoragePathScheme()

    scheme match {
      case "s3a"            => S3Bucket(path, storageProperties)
      case "gs"             => GCSBucket(path, storageProperties)
      case "wasb" | "wasbs" => AzureBlobBucket(path, storageProperties)
      case "adl"            => AzureAdlsBucket(path, storageProperties)
      case "file"           => LocalBucket(path, storageProperties)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported path scheme $scheme!")
    }
  }

  /**
   * Creates specific [[Bucket]] class using the path scheme from
   * key-value properties.
   *
   * @param params The key value parameters
   * @return A [[Bucket]] class for the given path
   */
  def apply(params: Map[String, String]): Bucket =
    apply(StorageProperties(params))

  /**
   * Checks whether the optional parameter is available. If it is not
   * available returns the default value.
   *
   * @param params The parameters key value map
   * @param key The optional parameter key
   * @param defaultValue The default value to return if key not
   *        available
   * @return The the value for the optional key if it exists; otherwise
   *         return the default value
   */
  def optionalParameter(params: Map[String, String], key: String, defaultValue: String): String =
    params.get(key).fold(defaultValue)(identity)

  /**
   * Checks whether the optional parameter is available. If it is not
   * available returns the default value.
   *
   * @param params The parameters key value map
   * @param key The optional parameter key
   * @param defaultValue The default value to return if key not
   *        available
   * @return The the value for the optional key if it exists; otherwise
   *         return the default value
   */
  def optionalIntParameter(params: Map[String, String], key: String, defaultValue: Int): Int =
    params.get(key).map(_.toInt).fold(defaultValue)(identity)

  /**
   * Converts key value pair strings into a single string with
   * separators in between.
   *
   * In the resulting string, key value pairs will be sorted by the
   * keys.
   *
   * @param params The key value parameters map
   * @return A single string with separators
   */
  def keyValueMapToString(params: Map[String, String]): String =
    (SortedMap.empty[String, String] ++ params)
      .map { case (k, v) => s"$k$KEY_VALUE_SEPARATOR$v" }
      .mkString(PARAMETER_SEPARATOR)

  /**
   * This is opposite of [[Bucket#keyValueMapToString]], given a string
   * with separators returns a key value pairs map.
   *
   * @param params The key value parameters map
   * @return A single string with separators
   */
  def keyValueStringToMap(keyValueString: String): Map[String, String] =
    keyValueString
      .split(PARAMETER_SEPARATOR)
      .map { word =>
        val kv = word.split(KEY_VALUE_SEPARATOR)
        kv(0) -> kv(1)
      }
      .toMap

  /**
   * Checks if the sequence of keys are available in the key value
   * parameter map.
   */
  private[bucket] def validate(params: Map[String, String], keys: Seq[String]): Unit =
    keys.foreach { key =>
      requiredParam(params, key)
    }

  /**
   * Checks if the provided key is available in the key value parameter
   * map. If it does not exist, throws an
   * [[java.lang.IllegalArgumentException]] exception.
   */
  def requiredParam(params: Map[String, String], key: String): String = {
    val opt = params.get(key)
    opt.fold {
      throw new IllegalArgumentException(s"The required parameter $key is not defined!")
    }(identity)
  }

  private[this] final val PARAMETER_SEPARATOR: String = ";"
  private[this] final val KEY_VALUE_SEPARATOR: String = ":=:"
}
