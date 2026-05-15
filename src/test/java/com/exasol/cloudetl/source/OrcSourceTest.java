package com.exasol.cloudetl.source;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.storage.FileFormat;

class OrcSourceTest {
    @Test
    void streamThrowsIfItCannotCreateOrcReader() {
        final Path nonPath = new Path("/tmp/notFile.orc");
        final SourceValidationException thrown = assertThrows(SourceValidationException.class, () -> {
            final Configuration conf = new Configuration();
            Source.create(FileFormat.apply("orc"), nonPath, conf, FileSystem.get(conf));
        });
        assertTrue(thrown.getMessage().startsWith("E-CSE-25"));
        assertTrue(thrown.getMessage().contains("Could not create Orc reader for path '" + nonPath + "'."));
    }
}
