package com.exasol.cloudetl

import com.exasol.ExaMetadata
import com.exasol.common.PropertiesParser

package object storage {
  lazy val FileFormat: FileFormatFactory.type = FileFormatFactory

  lazy val StorageProperties: StoragePropertiesFactory.type = StoragePropertiesFactory
}

object FileFormatFactory {
  lazy val AVRO: _root_.com.exasol.cloudetl.storage.FileFormat =
    java.lang.Enum.valueOf(classOf[_root_.com.exasol.cloudetl.storage.FileFormat], "AVRO")
  lazy val DELTA: _root_.com.exasol.cloudetl.storage.FileFormat =
    java.lang.Enum.valueOf(classOf[_root_.com.exasol.cloudetl.storage.FileFormat], "DELTA")
  lazy val FILE: _root_.com.exasol.cloudetl.storage.FileFormat =
    java.lang.Enum.valueOf(classOf[_root_.com.exasol.cloudetl.storage.FileFormat], "FILE")
  lazy val ORC: _root_.com.exasol.cloudetl.storage.FileFormat =
    java.lang.Enum.valueOf(classOf[_root_.com.exasol.cloudetl.storage.FileFormat], "ORC")
  lazy val PARQUET: _root_.com.exasol.cloudetl.storage.FileFormat =
    java.lang.Enum.valueOf(classOf[_root_.com.exasol.cloudetl.storage.FileFormat], "PARQUET")

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
  val STORAGE_PROPERTY_SEPARATOR: String = " -> "
  val STORAGE_KEY_VALUE_SEPARATOR: String = ";"
  val BUCKET_PATH: String = "BUCKET_PATH"
  val DATA_FORMAT: String = "DATA_FORMAT"
  val PARALLELISM: String = "PARALLELISM"
  val UDF_MEMORY: String = "UDF_MEMORY"
  val OVERWRITE: String = "OVERWRITE"
  val CHUNK_SIZE: String = "CHUNK_SIZE"
  val PARQUET_LOWERCASE_SCHEMA: String = "PARQUET_LOWERCASE_SCHEMA"
  val PROXY_HOST: String = "PROXY_HOST"
  val PROXY_PORT: String = "PROXY_PORT"
  val PROXY_USERNAME: String = "PROXY_USERNAME"
  val PROXY_PASSWORD: String = "PROXY_PASSWORD"

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
