package com.exasol.cloudetl.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FileFormatTest {
    @ParameterizedTest
    @MethodSource("supportedFileFormats")
    void applyReturnsSupportedFileFormats(final String given, final FileFormat expected) {
        assertEquals(expected, FileFormat.apply(given));
    }

    @Test
    void applyThrowsIfFileFormatIsNotSupported() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> FileFormat.apply("CsV"));
        assertTrue(thrown.getMessage().startsWith("E-CSE-17"));
        assertTrue(thrown.getMessage().contains("file format 'CsV' is not supported."));
    }

    private static Stream<Arguments> supportedFileFormats() {
        return Stream.of(//
                Arguments.of("AVro", FileFormat.AVRO), //
                Arguments.of("delta", FileFormat.DELTA), //
                Arguments.of("ORC", FileFormat.ORC), //
                Arguments.of("fiLE", FileFormat.FILE), //
                Arguments.of("parquet", FileFormat.PARQUET));
    }
}
