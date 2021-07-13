package com.exasol.cloudetl.parquet

import com.exasol.cloudetl.storage.StorageProperties

import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class ParquetWriteOptionsTest extends AnyFunSuite with BeforeAndAfterEach {

  private[this] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  test("apply returns default values") {
    val options = ParquetWriteOptions(StorageProperties(properties))
    assert(options.compressionCodec === CompressionCodecName.UNCOMPRESSED)
    assert(options.blockSize === ParquetWriter.DEFAULT_BLOCK_SIZE)
    assert(options.pageSize === ParquetWriter.DEFAULT_PAGE_SIZE)
    assert(options.enableDictionaryEncoding === true)
    assert(options.enableValidation === true)
  }

  test("apply returns user provided compression codec") {
    val testData = Map(
      "snappy" -> CompressionCodecName.SNAPPY,
      "gzip" -> CompressionCodecName.GZIP,
      "lzo" -> CompressionCodecName.LZO,
      "other" -> CompressionCodecName.UNCOMPRESSED
    )
    testData.foreach { case (given, expected) =>
      properties = Map("PARQUET_COMPRESSION_CODEC" -> given)
      assert(ParquetWriteOptions(StorageProperties(properties)).compressionCodec === expected)
    }
  }

  test("apply returns user provided block size") {
    properties = Map("PARQUET_BLOCK_SIZE" -> "64")
    assert(ParquetWriteOptions(StorageProperties(properties)).blockSize === 64)
  }

  test("apply throws if block size value cannot be converted to integer") {
    properties = Map("PARQUET_BLOCK_SIZE" -> "6l4")
    val thrown = intercept[NumberFormatException] {
      ParquetWriteOptions(StorageProperties(properties))
    }
    assert(thrown.getMessage === s"""For input string: "6l4"""")
  }

  test("apply returns user provided page size") {
    properties = Map("PARQUET_PAGE_SIZE" -> "128")
    assert(ParquetWriteOptions(StorageProperties(properties)).pageSize === 128)
  }

  test("apply throws if page size value cannot be converted to integer") {
    properties = Map("PARQUET_PAGE_SIZE" -> "12e")
    val thrown = intercept[NumberFormatException] {
      ParquetWriteOptions(StorageProperties(properties))
    }
    assert(thrown.getMessage === s"""For input string: "12e"""")
  }

  test("apply returns user provided dictionary encoding enabled") {
    properties = Map("PARQUET_DICTIONARY_ENCODING" -> "false")
    assert(ParquetWriteOptions(StorageProperties(properties)).enableDictionaryEncoding === false)
  }

  test("apply throws if dictionary encoding enable cannot be converted to boolean") {
    properties = Map("PARQUET_DICTIONARY_ENCODING" -> "nay")
    val thrown = intercept[IllegalArgumentException] {
      ParquetWriteOptions(StorageProperties(properties))
    }
    assert(thrown.getMessage === s"""For input string: "nay"""")
  }

  test("apply returns user provided validation enabled") {
    properties = Map("PARQUET_VALIDATION" -> "false")
    assert(ParquetWriteOptions(StorageProperties(properties)).enableValidation === false)
  }

  test("apply throws if validation enable cannot be converted to boolean") {
    properties = Map("PARQUET_VALIDATION" -> "yay")
    val thrown = intercept[IllegalArgumentException] {
      ParquetWriteOptions(StorageProperties(properties))
    }
    assert(thrown.getMessage === s"""For input string: "yay"""")
  }

}
