package com.exasol.cloudetl.storage

import java.net.URI

import com.exasol.cloudetl.common.AbstractProperties

/**
 * A specific implementation of
 * [[com.exasol.cloudetl.common.AbstractProperties]] that handles user
 * provided key-value parameters for storage import and export
 * user-defined-functions (udfs).
 */
class StorageProperties(private val properties: Map[String, String])
    extends AbstractProperties(properties) {

  import StorageProperties._

  /**
   * Returns the storage path.
   *
   * It is a main path, for example, to a bucket where data files are
   * stored.
   */
  final def getStoragePath(): String =
    getAs[String](BUCKET_PATH)

  /**
   * Returns the storage path scheme.
   *
   * For example, given the S3 bucket, `s3a://my-bucket/data/` path to
   * load data, returns the scheme `s3a` value.
   */
  final def getStoragePathScheme(): String =
    new URI(getStoragePath()).getScheme

  /** Returns the [[FileFormat]] file format. */
  final def getFileFormat(): FileFormat =
    FileFormat(getAs[String](DATA_FORMAT))

  /**
   * Returns the number of partitions provided as user property.
   *
   * If it is not set, returns default value {@code nproc}.
   */
  final def getParallelism(defaultValue: => String): String =
    get(PARALLELISM).fold(defaultValue)(identity)

  final def getAs[T](key: String): T =
    get(key).fold {
      throw new IllegalArgumentException(s"Please provide a value for the $key property!")
    }(_.asInstanceOf[T])

  /**
   * Returns a string value of key-value property pairs.
   *
   * The resulting string is sorted by keys ordering.
   */
  @SuppressWarnings(Array("org.wartremover.warts.Overloading"))
  final def mkString(): String =
    mkString(KEY_VALUE_SEPARATOR, PROPERTY_SEPARATOR)

}

/**
 * A companion object for [[StorageProperties]] class.
 */
object StorageProperties {

  /**
   * A line separator string used for creating key-value property
   * strings.
   */
  private[storage] final val PROPERTY_SEPARATOR: String = ";"

  /**
   * A default separator string used for concatenate key-value pairs.
   */
  private[storage] final val KEY_VALUE_SEPARATOR: String = " -> "

  /** A required property key name for a bucket path. */
  private[storage] final val BUCKET_PATH: String = "BUCKET_PATH"

  /** A required property key name for a data format. */
  private[storage] final val DATA_FORMAT: String = "DATA_FORMAT"

  /** An optional property key name for the parallelism. */
  private[storage] final val PARALLELISM: String = "PARALLELISM"

  def apply(params: Map[String, String]): StorageProperties =
    new StorageProperties(params)

  /**
   * Creates [[StorageProperties]] from properly separated string.
   */
  def fromString(string: String): StorageProperties = {
    if (!string.contains(PROPERTY_SEPARATOR)) {
      throw new IllegalArgumentException(
        s"The input string is not separated by '$PROPERTY_SEPARATOR'!"
      )
    }
    val properties = string
      .split(PROPERTY_SEPARATOR)
      .map { word =>
        val pairs = word.split(KEY_VALUE_SEPARATOR)
        pairs(0) -> pairs(1)
      }
      .toMap

    new StorageProperties(properties)
  }

}
