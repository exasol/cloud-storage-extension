package com.exasol.cloudetl.storage

import java.util.Locale.ENGLISH

import com.exasol.cloudetl.constants.Constants.USER_GUIDE_LINK
import com.exasol.errorreporting.ExaError

/**
 * A companion object for [[FileFormat]] class.
 *
 * It provides factory methods to create file format classes from
 * strings.
 */
object FileFormat {

  def apply(fileFormat: String): FileFormat = fileFormat.toUpperCase(ENGLISH) match {
    case "AVRO"    => AVRO
    case "DELTA"   => DELTA
    case "FILE"    => FILE
    case "ORC"     => ORC
    case "PARQUET" => PARQUET
    case _ =>
      throw new IllegalArgumentException(
        ExaError
          .messageBuilder("E-CSE-17")
          .message("Provided file format {{FORMAT}} is not supported.", fileFormat)
          .mitigation("Please use one of supported formats.")
          .mitigation("You can check user guide at {{LINK}} for supported list of formats.", USER_GUIDE_LINK)
          .toString()
      )
  }

  case object AVRO extends FileFormat
  case object DELTA extends FileFormat
  case object FILE extends FileFormat
  case object ORC extends FileFormat
  case object PARQUET extends FileFormat

}

/**
 * An enum for supported file formats.
 */
sealed trait FileFormat extends Product with Serializable
