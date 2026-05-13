package com.exasol.cloudetl.orc.converter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.orc.TypeDescription;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.*;
import com.exasol.cloudetl.orc.OrcTestDataWriter;
import com.exasol.cloudetl.source.OrcSource;
import com.exasol.common.data.Row;

abstract class BaseOrcConverterTest extends OrcTestDataWriter {
    protected Configuration conf;
    protected FileSystem fileSystem;
    protected org.apache.hadoop.fs.Path path;
    private Path outputDirectory;

    @BeforeEach
    void beforeEach() throws IOException {
        this.conf = new Configuration();
        this.fileSystem = FileSystem.get(this.conf);
        this.outputDirectory = TestFileManager.createTemporaryFolder("orc-tests-");
        this.path = new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(), "orc-file.orc");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputDirectory);
    }

    protected final <T> void write(final TypeDescription schema, final List<T> values) throws IOException {
        writeDataValues(values, this.path, schema);
    }

    protected final List<Row> getRecords() {
        final OrcSource source = new OrcSource(this.path, this.conf, this.fileSystem);
        try {
            return new ArrayList<>(ScalaConverters.asJavaList(source.stream().toSeq()));
        } finally {
            try {
                source.close();
            } catch (final IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
    }

    protected final Row row(final Object... values) {
        return Row.apply(ScalaConverters.seqFromJava(Arrays.asList(values)));
    }
}
