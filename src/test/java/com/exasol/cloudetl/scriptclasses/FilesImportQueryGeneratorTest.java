package com.exasol.cloudetl.scriptclasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.storage.StorageProperties;

class FilesImportQueryGeneratorTest extends PathTest {
    @Test
    void generateSqlForImportSpecReturnsSqlStatement() {
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.importSpec.getParameters()).thenReturn(this.properties);
        final StorageProperties storageProperties = new StorageProperties(this.properties);
        final String bucketPath = storageProperties.getStoragePath();
        final String storagePropertyPairs = storageProperties.mkString();
        final String expectedSqlStatement = String.format("SELECT\n" +
"  %s.IMPORT_FILES(\n" +
"    '%s', '%s', filename, start_index, end_index\n" +
")\n" +
"FROM (\n" +
"  SELECT %s.IMPORT_METADATA(\n" +
"    '%s', '%s', 65536\n" +
"  )\n" +
")\n" +
"GROUP BY\n" +
"  partition_index;\n", this.schema, bucketPath, storagePropertyPairs, this.schema, bucketPath,
                storagePropertyPairs);

        assertEquals(expectedSqlStatement, FilesImportQueryGenerator.generateSqlForImportSpec(this.metadata, this.importSpec));
        verify(this.metadata, atLeastOnce()).getScriptSchema();
        verify(this.importSpec, times(1)).getParameters();
    }

    @Test
    void generateSqlForImportSpecThrowsIfRequiredPropertyIsNotSet() {
        final Map<String, String> newProperties = new HashMap<>(this.properties);
        newProperties.remove("S3_ENDPOINT");
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.importSpec.getParameters()).thenReturn(newProperties);

        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> FilesImportQueryGenerator.generateSqlForImportSpec(this.metadata, this.importSpec));
        assertTrue(thrown.getMessage().startsWith("E-CSE-2"));
        assertTrue(thrown.getMessage().contains("'S3_ENDPOINT' property value is missing."));
        verify(this.importSpec, times(1)).getParameters();
    }
}
