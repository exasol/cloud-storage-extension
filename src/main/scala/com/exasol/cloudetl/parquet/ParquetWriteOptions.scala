package com.exasol.cloudetl.parquet

import java.util.Locale.ENGLISH

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName

final case class ParquetWriteOptions(
  blockSize: Int,
  pageSize: Int,
  compressionCodec: CompressionCodecName,
  enableDictionaryEncoding: Boolean,
  enableValidation: Boolean
)

object ParquetWriteOptions {

  def apply(params: StorageProperties): ParquetWriteOptions = {
    val compressionCodec =
      params.get("PARQUET_COMPRESSION_CODEC").getOrElse("").toUpperCase(ENGLISH) match {
        case "SNAPPY" => CompressionCodecName.SNAPPY
        case "GZIP"   => CompressionCodecName.GZIP
        case "LZO"    => CompressionCodecName.LZO
        case _        => CompressionCodecName.UNCOMPRESSED
      }
    val blockSize = params.get("PARQUET_BLOCK_SIZE").fold(ParquetWriter.DEFAULT_BLOCK_SIZE)(_.toInt)
    val pageSize = params.get("PARQUET_PAGE_SIZE").fold(ParquetWriter.DEFAULT_PAGE_SIZE)(_.toInt)
    val dictionary = params.get("PARQUET_DICTIONARY_ENCODING").fold(true)(_.toBoolean)
    val validation = params.get("PARQUET_VALIDATION").fold(true)(_.toBoolean)

    ParquetWriteOptions(blockSize, pageSize, compressionCodec, dictionary, validation)
  }

}
