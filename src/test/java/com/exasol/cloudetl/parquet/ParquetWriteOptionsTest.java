package com.exasol.cloudetl.parquet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.stream.Stream;

import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @ParameterizedTest
    @MethodSource("compressionCodecs")
    void fromReturnsUserProvidedCompressionCodec(final String given, final CompressionCodecName expected) {
        assertEquals(expected,
                ParquetWriteOptions.from(new StorageProperties(Map.of("PARQUET_COMPRESSION_CODEC", given))).compressionCodec);
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

    private static Stream<Arguments> compressionCodecs() {
        return Stream.of(//
                Arguments.of("snappy", CompressionCodecName.SNAPPY), //
                Arguments.of("gzip", CompressionCodecName.GZIP), //
                Arguments.of("lzo", CompressionCodecName.LZO), //
                Arguments.of("other", CompressionCodecName.UNCOMPRESSED));
    }
}
