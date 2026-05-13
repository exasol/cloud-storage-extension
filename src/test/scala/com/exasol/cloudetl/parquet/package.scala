package com.exasol.cloudetl

import java.util.Locale.ENGLISH

import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.apache.parquet.schema.MessageType

package object parquet {
  val ParquetValueConverter: ParquetValueConverterFactory.type = ParquetValueConverterFactory
  val ParquetWriteOptions: ParquetWriteOptionsFactory.type = ParquetWriteOptionsFactory
}

object ParquetValueConverterFactory {
  def apply(schema: MessageType): _root_.com.exasol.cloudetl.parquet.ParquetValueConverter =
    new _root_.com.exasol.cloudetl.parquet.ParquetValueConverter(schema)
}

object ParquetWriteOptionsFactory {
  def apply(
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.parquet.ParquetWriteOptions = {
    val compressionCodec = params.get("PARQUET_COMPRESSION_CODEC").getOrElse("").toUpperCase(ENGLISH) match {
      case "SNAPPY" => CompressionCodecName.SNAPPY
      case "GZIP"   => CompressionCodecName.GZIP
      case "LZO"    => CompressionCodecName.LZO
      case _        => CompressionCodecName.UNCOMPRESSED
    }
    val blockSize = params.get("PARQUET_BLOCK_SIZE").fold(ParquetWriter.DEFAULT_BLOCK_SIZE)(_.toInt)
    val pageSize = params.get("PARQUET_PAGE_SIZE").fold(ParquetWriter.DEFAULT_PAGE_SIZE)(_.toInt)
    val dictionary = params.get("PARQUET_DICTIONARY_ENCODING").fold(true)(_.toBoolean)
    val validation = params.get("PARQUET_VALIDATION").fold(true)(_.toBoolean)
    new _root_.com.exasol.cloudetl.parquet.ParquetWriteOptions(
      blockSize,
      pageSize,
      compressionCodec,
      dictionary,
      validation
    )
  }
}
