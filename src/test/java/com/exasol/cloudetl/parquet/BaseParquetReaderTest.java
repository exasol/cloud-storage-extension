package com.exasol.cloudetl.parquet;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.schema.MessageType;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.*;
import com.exasol.common.data.Row;

abstract class BaseParquetReaderTest extends ParquetTestDataWriter {
    private Configuration conf;
    private Path outputDirectory;
    private org.apache.hadoop.fs.Path path;

    @BeforeEach
    void beforeEach() throws IOException {
        this.conf = new Configuration();
        this.outputDirectory = TestFileManager.createTemporaryFolder("parquetRowReaderTest");
        this.path = new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(), "part-00000.parquet");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputDirectory);
    }

    protected final <T extends Closeable> void withResource(final T writer, final ResourceConsumer<T> block)
            throws IOException {
        try (T resource = writer) {
            block.accept(resource);
        }
    }

    protected final List<Row> getRecords() {
        final ParquetSourceTest source = new ParquetSourceTest(this.path, this.conf);
        try {
            return new ArrayList<>(ScalaConverters.asJavaList(source.stream().toSeq()));
        } finally {
            source.close();
        }
    }

    protected final ParquetWriter<Group> getParquetWriter(final MessageType schema, final boolean encoding)
            throws IOException {
        return getParquetWriter(this.path, schema, encoding);
    }

    protected final Row row(final Object... values) {
        return Row.apply(ScalaConverters.seqFromJava(Arrays.asList(values)));
    }

    @FunctionalInterface
    protected interface ResourceConsumer<T> {
        void accept(T value) throws IOException;
    }
}
