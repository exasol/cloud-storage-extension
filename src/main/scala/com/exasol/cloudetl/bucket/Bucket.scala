package com.exasol.cloudetl.bucket

import java.net.URI

import scala.collection.SortedMap

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
  val properties: Map[String, String]

  /** Validates that all required parameter key values are available. */
  def validate(): Unit

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
object Bucket extends LazyLogging {

  /** A required key string for a bucket path. */
  final val BUCKET_PATH: String = "BUCKET_PATH"

  /** The list of required parameter keys for AWS S3 bucket. */
  final val S3_PARAMETERS: Seq[String] =
    Seq("S3_ENDPOINT", "S3_ACCESS_KEY", "S3_SECRET_KEY")

  /**
   * The list of required parameter keys for Google Cloud Storage
   * bucket.
   */
  final val GCS_PARAMETERS: Seq[String] =
    Seq("GCS_PROJECT_ID", "GCS_KEYFILE_PATH")

  /**
   * The list of required parameter keys for Azure Blob Storage bucket.
   */
  final val AZURE_BLOB_PARAMETERS: Seq[String] =
    Seq("AZURE_ACCOUNT_NAME", "AZURE_SECRET_KEY")

  /**
   * The list of required keys for Azure Data Lake Storage bucket.
   */
  final val AZURE_ADLS_PARAMETERS: Seq[String] =
    Seq("AZURE_CLIENT_ID", "AZURE_CLIENT_SECRET", "AZURE_DIRECTORY_ID")

  /**
   * An apply method that creates different [[Bucket]] classes depending
   * on the path scheme.
   *
   * @param params The key value parameters
   * @return A [[Bucket]] class for the given path
   */
  def apply(params: Map[String, String]): Bucket = {
    val path = requiredParam(params, BUCKET_PATH)
    val scheme = getScheme(path)

    scheme match {
      case "s3a"            => S3Bucket(path, params)
      case "gs"             => GCSBucket(path, params)
      case "wasb" | "wasbs" => AzureBlobBucket(path, params)
      case "adl"            => AzureAdlsBucket(path, params)
      case "file"           => LocalBucket(path, params)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported path scheme $scheme")
    }
  }

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
   * map. If it does not exist, throws an [[IllegalArgumentException]]
   * exception.
   */
  private[bucket] def requiredParam(params: Map[String, String], key: String): String = {
    val opt = params.get(key)
    opt.fold {
      throw new IllegalArgumentException(s"The required parameter $key is not defined!")
    }(identity)
  }

  private[this] def getScheme(path: String): String =
    new URI(path).getScheme

  private[this] final val PARAMETER_SEPARATOR: String = ";"
  private[this] final val KEY_VALUE_SEPARATOR: String = ":=:"
}
