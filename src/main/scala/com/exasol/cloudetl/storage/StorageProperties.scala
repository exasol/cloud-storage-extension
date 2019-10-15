package com.exasol.cloudetl.storage

import java.net.URI

import com.exasol.cloudetl.common.AbstractProperties
import com.exasol.cloudetl.common.CommonProperties

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
@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
object StorageProperties extends CommonProperties {

  /** A required property key name for a bucket path. */
  private[storage] final val BUCKET_PATH: String = "BUCKET_PATH"

  /** A required property key name for a data format. */
  private[storage] final val DATA_FORMAT: String = "DATA_FORMAT"

  /** An optional property key name for the parallelism. */
  private[storage] final val PARALLELISM: String = "PARALLELISM"

  /** Returns [[StorageProperties]] from key-value pairs map. */
  def apply(params: Map[String, String]): StorageProperties =
    new StorageProperties(params)

  /** Returns [[StorageProperties]] from properly separated string. */
  def apply(string: String): StorageProperties =
    apply(mapFromString(string))

}
