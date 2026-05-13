package com.exasol.cloudetl.parquet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.storage.StorageProperties;

class ParquetWriteOptionsTest {
    @Test
    void fromReturnsDefaultValues() {
        final ParquetWriteOptions options = ParquetWriteOptions.from(new StorageProperties(Map.of()));
        assertEquals(CompressionCodecName.UNCOMPRESSED, options.compressionCodec);
        assertEquals(ParquetWriter.DEFAULT_BLOCK_SIZE, options.blockSize);
        assertEquals(ParquetWriter.DEFAULT_PAGE_SIZE, options.pageSize);
        assertTrue(options.enableDictionaryEncoding);
        assertTrue(options.enableValidation);
    }

    @Test
    void fromReturnsUserProvidedCompressionCodec() {
        final Map<String, CompressionCodecName> testData = Map.of("snappy", CompressionCodecName.SNAPPY, "gzip",
                CompressionCodecName.GZIP, "lzo", CompressionCodecName.LZO, "other",
                CompressionCodecName.UNCOMPRESSED);
        testData.forEach((given, expected) -> assertEquals(expected,
                ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_COMPRESSION_CODEC", given))).compressionCodec));
    }

    @Test
    void fromReturnsUserProvidedBlockSize() {
        assertEquals(64,
                ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_BLOCK_SIZE", "64"))).blockSize);
    }

    @Test
    void fromThrowsIfBlockSizeValueCannotBeConvertedToInteger() {
        final NumberFormatException thrown = assertThrows(NumberFormatException.class,
                () -> ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_BLOCK_SIZE", "6l4"))));
        assertEquals("For input string: \"6l4\"", thrown.getMessage());
    }

    @Test
    void fromReturnsUserProvidedPageSize() {
        assertEquals(128,
                ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_PAGE_SIZE", "128"))).pageSize);
    }

    @Test
    void fromThrowsIfPageSizeValueCannotBeConvertedToInteger() {
        final NumberFormatException thrown = assertThrows(NumberFormatException.class,
                () -> ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_PAGE_SIZE", "12e"))));
        assertEquals("For input string: \"12e\"", thrown.getMessage());
    }

    @Test
    void fromReturnsUserProvidedDictionaryEncodingEnabled() {
        assertFalse(ParquetWriteOptions
                .from(new StorageProperties(Map.of("PARQUET_DICTIONARY_ENCODING", "false"))).enableDictionaryEncoding);
    }

    @Test
    void fromReturnsUserProvidedValidationEnabled() {
        assertFalse(ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_VALIDATION", "false"))).enableValidation);
    }
}
