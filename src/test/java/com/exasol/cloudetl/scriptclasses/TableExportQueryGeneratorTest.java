package com.exasol.cloudetl.scriptclasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.storage.StorageProperties;

class TableExportQueryGeneratorTest extends PathTest {
    @BeforeEach
    void beforeEach() {
        when(this.metadata.getNodeCount()).thenReturn(1L);
        when(this.metadata.getMemoryLimit()).thenReturn(new java.math.BigInteger("2000000000"));
    }

    @Test
    void generateSqlForExportSpecReturnsSqlStatement() {
        final Map<String, String> params = new HashMap<>(this.properties);
        params.put("PARALLELISM", "iproc()");
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(params);
        when(this.exportSpec.getSourceColumnNames()).thenReturn(List.of("tbl.col_int", "c_bool", "c_char"));

        final StorageProperties storageProperties = new StorageProperties(params);
        final String bucketPath = storageProperties.getStoragePath();
        final String stringPairs = storageProperties.mkString();
        final String expectedSqlStatement = String.format("SELECT\n" +
"  %s.EXPORT_TABLE(\n" +
"    '%s', '%s', 'col_int.c_bool.c_char', col_int, c_bool, c_char\n" +
")\n" +
"FROM\n" +
"  DUAL\n" +
"GROUP BY\n" +
"  iproc();\n", this.schema, bucketPath, stringPairs);

        assertEquals(expectedSqlStatement, TableExportQueryGenerator.generateSqlForExportSpec(this.metadata, this.exportSpec));
        verify(this.metadata, atLeastOnce()).getScriptSchema();
        verify(this.exportSpec, times(1)).getParameters();
        verify(this.exportSpec, times(1)).getSourceColumnNames();
    }

    @Test
    void generateSqlForExportSpecThrowsIfRequiredPropertyIsNotSet() {
        final Map<String, String> newProperties = new HashMap<>(this.properties);
        newProperties.remove("S3_ENDPOINT");
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(newProperties);

        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> TableExportQueryGenerator.generateSqlForExportSpec(this.metadata, this.exportSpec));
        assertTrue(thrown.getMessage().startsWith("E-CSE-2"));
        assertTrue(thrown.getMessage().contains("'S3_ENDPOINT' property value is missing."));
        verify(this.exportSpec, times(1)).getParameters();
        verify(this.exportSpec, never()).getSourceColumnNames();
    }

    @Test
    void generateSqlForExportSpecThrowsIfColumnsCannotBeParsed() {
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(this.properties);
        when(this.exportSpec.getSourceColumnNames()).thenReturn(List.of("tbl.c_int.integer"));

        final TableExporterException thrown = assertThrows(TableExporterException.class,
                () -> TableExportQueryGenerator.generateSqlForExportSpec(this.metadata, this.exportSpec));
        assertTrue(thrown.getMessage().startsWith("E-CSE-34"));
        assertTrue(thrown.getMessage().contains("from given column syntax 'tbl.c_int.integer'"));
        verify(this.metadata, atLeastOnce()).getScriptSchema();
        verify(this.exportSpec, times(1)).getParameters();
        verify(this.exportSpec, times(1)).getSourceColumnNames();
    }

    @Test
    void generateSqlForExportSpecKeepsThePathFilesIfOverwriteParameterIsNotSet() throws IOException {
        final java.nio.file.Path bucketPath = Files.createTempDirectory("bucketPath");
        final List<java.nio.file.Path> files = createDummyFiles(bucketPath);
        final Map<String, String> newProperties = new HashMap<>(this.properties);
        newProperties.put("BUCKET_PATH", "file://" + bucketPath.toUri().getRawPath());
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(newProperties);
        TableExportQueryGenerator.generateSqlForExportSpec(this.metadata, this.exportSpec);
        assertTrue(Files.exists(bucketPath));
        try (var list = Files.list(bucketPath)) {
            assertTrue(list.findAny().isPresent());
        }
        try (var list = Files.list(bucketPath)) {
            assertEquals(3, list.count());
        }

        for (final java.nio.file.Path file : files) {
            Files.deleteIfExists(file);
        }
        Files.delete(bucketPath);
    }

    @Test
    void generateSqlForExportSpecDeletesThePathFilesIfOverwriteParameterIsSet() throws IOException {
        final java.nio.file.Path bucketPath = Files.createTempDirectory("bucketPath");
        createDummyFiles(bucketPath);
        final Map<String, String> newProperties = new HashMap<>(this.properties);
        newProperties.put("BUCKET_PATH", "file://" + bucketPath.toUri().getRawPath());
        newProperties.put("OVERWRITE", "true");
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(newProperties);
        TableExportQueryGenerator.generateSqlForExportSpec(this.metadata, this.exportSpec);
        assertFalse(Files.exists(bucketPath));
    }

    private List<java.nio.file.Path> createDummyFiles(final java.nio.file.Path path) throws IOException {
        final List<java.nio.file.Path> files = List.of("a.parquet", "b.parquet", "c.parquet").stream().map(path::resolve)
                .collect(Collectors.toList());
        for (final java.nio.file.Path file : files) {
            Files.createFile(file);
        }
        return files;
    }
}
