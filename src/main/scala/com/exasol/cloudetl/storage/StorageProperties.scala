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

  /** Returns the storage main path. */
  final def getStoragePath(): String =
    getAs[String](BUCKET_PATH)

  /** Returns the maint storage path scheme. */
  final def getStoragePathScheme(): String =
    new URI(getStoragePath()).getScheme

  /** Returns the [[FileFormat]] file format. */
  final def getFileFormat(): FileFormat =
    FileFormat(getAs[String](DATA_FORMAT))

  final def getAs[T](key: String): T =
    get(key).fold {
      throw new IllegalArgumentException(s"Please provide a value for the $key property!")
    }(_.asInstanceOf[T])

}

/**
 * A companion object for [[StorageProperties]] class.
 */
object StorageProperties {

  val BUCKET_PATH: String = "BUCKET_PATH"
  val DATA_FORMAT: String = "DATA_FORMAT"

  def apply(params: Map[String, String]): StorageProperties =
    new StorageProperties(params)

}
