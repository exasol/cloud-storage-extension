package com.exasol.cloudetl.scriptclasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class ImportPathTest extends PathTest {
    @Test
    void generateSqlForImportSpecThrowsWithMessageToTheNewScriptclassName() {
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.importSpec.getParameters()).thenReturn(this.properties);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ImportPath.generateSqlForImportSpec(this.metadata, this.importSpec));
        assertTrue(thrown.getMessage().contains("Please use the FilesImportQueryGenerator"));
    }
}
