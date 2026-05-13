package com.exasol.cloudetl.source;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

class SourceTest extends AbstractSourceTest {
    @Override
    protected String getFormat() {
        return "avro";
    }

    @Test
    void createThrowsIfFileFormatIsNotSupported() {
        final Path path = new Path(this.resourceDir + "/sales10.avro");
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> getSource(path, "file"));
        assertTrue(thrown.getMessage().startsWith("E-CSE-21"));
        assertTrue(thrown.getMessage().contains("Storage format 'FILE' is not supported."));
    }
}
