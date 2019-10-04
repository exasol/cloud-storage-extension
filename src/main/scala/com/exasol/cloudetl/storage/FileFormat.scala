package com.exasol.cloudetl.storage

/**
 * A companion object for [[FileFormat]] class.
 *
 * It provides factory methods to create file format classes from
 * strings.
 */
object FileFormat {

  def apply(fileFormat: String): FileFormat = fileFormat.toUpperCase match {
    case "AVRO"    => AVRO
    case "FILE"    => FILE
    case "ORC"     => ORC
    case "PARQUET" => PARQUET
    case _         => throw new IllegalArgumentException(s"Unsupported file format $fileFormat!")
  }

}

/**
 * An enum for supported file formats.
 */
sealed trait FileFormat extends Product with Serializable
case object AVRO extends FileFormat
case object FILE extends FileFormat
case object ORC extends FileFormat
case object PARQUET extends FileFormat
