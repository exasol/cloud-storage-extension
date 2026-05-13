package com.exasol.cloudetl.source;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

class AvroSourceTest extends AbstractSourceTest {
    @Override
    protected String getFormat() {
        return "avro";
    }

    @Test
    void streamReturnsCountOfRecordsFromAvroFiles() throws java.io.IOException {
        assertEquals(1998, getRecordsCount(Paths.get(this.resourceDir + "/sales1*.avro")));
    }

    @Test
    void streamThrowsIfItCannotCreateAvroReader() {
        final Path nonPath = new Path(this.resourceDir + "/notFile.avro");
        final SourceValidationException thrown = assertThrows(SourceValidationException.class,
                () -> getSource(nonPath).stream().size());
        assertTrue(thrown.getMessage().startsWith("E-CSE-26"));
        assertTrue(thrown.getMessage().contains("Could not create Avro reader for path '" + nonPath + "'."));
    }
}
