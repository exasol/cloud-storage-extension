package com.exasol.cloudetl

import com.exasol.ExaMetadata
import com.exasol.common.PropertiesParser

package object storage {
  val FileFormat: FileFormatFactory.type = FileFormatFactory

  val StorageProperties: StoragePropertiesFactory.type = StoragePropertiesFactory
}

object FileFormatFactory {
  val AVRO: _root_.com.exasol.cloudetl.storage.FileFormat = _root_.com.exasol.cloudetl.storage.FileFormat.AVRO
  val DELTA: _root_.com.exasol.cloudetl.storage.FileFormat = _root_.com.exasol.cloudetl.storage.FileFormat.DELTA
  val FILE: _root_.com.exasol.cloudetl.storage.FileFormat = _root_.com.exasol.cloudetl.storage.FileFormat.FILE
  val ORC: _root_.com.exasol.cloudetl.storage.FileFormat = _root_.com.exasol.cloudetl.storage.FileFormat.ORC
  val PARQUET: _root_.com.exasol.cloudetl.storage.FileFormat = _root_.com.exasol.cloudetl.storage.FileFormat.PARQUET

  def apply(fileFormat: String): _root_.com.exasol.cloudetl.storage.FileFormat =
    fileFormat.toUpperCase(java.util.Locale.ENGLISH) match {
      case "AVRO"    => AVRO
      case "DELTA"   => DELTA
      case "FILE"    => FILE
      case "ORC"     => ORC
      case "PARQUET" => PARQUET
      case _ =>
        throw new IllegalArgumentException(
          com.exasol.errorreporting.ExaError
            .messageBuilder("E-CSE-17")
            .message("Provided file format {{FORMAT}} is not supported.", fileFormat)
            .mitigation("Please use one of supported formats.")
            .mitigation(
              "You can check user guide at {{LINK}} for supported list of formats.",
              com.exasol.cloudetl.constants.Constants.USER_GUIDE_LINK
            )
            .toString()
        )
    }
}

object StoragePropertiesFactory {
  val STORAGE_PROPERTY_SEPARATOR: String =
    _root_.com.exasol.cloudetl.storage.StorageProperties.STORAGE_PROPERTY_SEPARATOR
  val STORAGE_KEY_VALUE_SEPARATOR: String =
    _root_.com.exasol.cloudetl.storage.StorageProperties.STORAGE_KEY_VALUE_SEPARATOR
  val BUCKET_PATH: String = _root_.com.exasol.cloudetl.storage.StorageProperties.BUCKET_PATH
  val DATA_FORMAT: String = _root_.com.exasol.cloudetl.storage.StorageProperties.DATA_FORMAT
  val PARALLELISM: String = _root_.com.exasol.cloudetl.storage.StorageProperties.PARALLELISM
  val UDF_MEMORY: String = _root_.com.exasol.cloudetl.storage.StorageProperties.UDF_MEMORY
  val OVERWRITE: String = _root_.com.exasol.cloudetl.storage.StorageProperties.OVERWRITE
  val CHUNK_SIZE: String = _root_.com.exasol.cloudetl.storage.StorageProperties.CHUNK_SIZE
  val PARQUET_LOWERCASE_SCHEMA: String =
    _root_.com.exasol.cloudetl.storage.StorageProperties.PARQUET_LOWERCASE_SCHEMA
  val PROXY_HOST: String = _root_.com.exasol.cloudetl.storage.StorageProperties.PROXY_HOST
  val PROXY_PORT: String = _root_.com.exasol.cloudetl.storage.StorageProperties.PROXY_PORT
  val PROXY_USERNAME: String = _root_.com.exasol.cloudetl.storage.StorageProperties.PROXY_USERNAME
  val PROXY_PASSWORD: String = _root_.com.exasol.cloudetl.storage.StorageProperties.PROXY_PASSWORD

  private val parser = PropertiesParser(STORAGE_KEY_VALUE_SEPARATOR, STORAGE_PROPERTY_SEPARATOR)

  def apply(params: Map[String, String]): _root_.com.exasol.cloudetl.storage.StorageProperties =
    new _root_.com.exasol.cloudetl.storage.StorageProperties(params, None)

  def apply(
    params: Map[String, String],
    metadata: ExaMetadata
  ): _root_.com.exasol.cloudetl.storage.StorageProperties =
    new _root_.com.exasol.cloudetl.storage.StorageProperties(params, Option(metadata))

  def apply(string: String): _root_.com.exasol.cloudetl.storage.StorageProperties =
    new _root_.com.exasol.cloudetl.storage.StorageProperties(parser.mapFromString(string), None)

  def apply(string: String, metadata: ExaMetadata): _root_.com.exasol.cloudetl.storage.StorageProperties =
    new _root_.com.exasol.cloudetl.storage.StorageProperties(parser.mapFromString(string), Option(metadata))
}
