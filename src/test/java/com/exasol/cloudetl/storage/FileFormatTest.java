package com.exasol.cloudetl.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class FileFormatTest {
    @Test
    void applyReturnsSupportedFileFormats() {
        final Map<String, FileFormat> testData = Map.of("AVro", FileFormat.AVRO, "delta", FileFormat.DELTA, "ORC",
                FileFormat.ORC, "fiLE", FileFormat.FILE, "parquet", FileFormat.PARQUET);
        testData.forEach((given, expected) -> assertEquals(expected, FileFormat.apply(given)));
    }

    @Test
    void applyThrowsIfFileFormatIsNotSupported() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> FileFormat.apply("CsV"));
        assertTrue(thrown.getMessage().startsWith("E-CSE-17"));
        assertTrue(thrown.getMessage().contains("file format 'CsV' is not supported."));
    }
}
