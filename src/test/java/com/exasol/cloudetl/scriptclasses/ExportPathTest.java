package com.exasol.cloudetl.scriptclasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class ExportPathTest extends PathTest {
    @Test
    void generateSqlForExportSpecThrowsWithMessageToTheNewScriptclassName() {
        when(this.metadata.getScriptSchema()).thenReturn(this.schema);
        when(this.exportSpec.getParameters()).thenReturn(this.properties);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExportPath.generateSqlForExportSpec(this.metadata, this.exportSpec));
        assertTrue(thrown.getMessage().contains("Please use the TableExportQueryGenerator"));
    }
}
