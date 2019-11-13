package com.exasol.cloudetl.bucket

import java.net.URI

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
abstract class Bucket extends LazyLogging {

  /** The path string of the bucket. */
  val bucketPath: String

  /** The user provided key value pair properties. */
  val properties: StorageProperties

  /**
   * Returns the sequence of key-value properties required for this
   * specific storage class.
   */
  def getRequiredProperties(): Seq[String]

  /**
   * Creates a Hadoop [[org.apache.hadoop.conf.Configuration]] for this
   * specific bucket type.
   */
  def getConfiguration(): Configuration

  /**
   * Validates that user provided key-value properties are available for
   * this bucket implementation.
   */
  def validate(): Unit

  protected[this] final def validateRequiredProperties(): Unit =
    getRequiredProperties().foreach { key =>
      if (!properties.containsKey(key)) {
        throw new IllegalArgumentException(
          s"Please provide a value for the $key property!"
        )
      }
    }

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

}
