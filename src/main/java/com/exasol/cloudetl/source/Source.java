package com.exasol.cloudetl.source;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.exasol.cloudetl.storage.FileFormat;
import com.exasol.common.data.Row;
import com.exasol.errorreporting.ExaError;

/** Abstract Hadoop-compatible file source. */
public abstract class Source implements AutoCloseable {
    /** @return file path */
    public abstract Path path();

    /** @return Hadoop filesystem */
    public abstract FileSystem fileSystem();

    /** @return Hadoop configuration */
    public abstract Configuration conf();

    /** @return stream of rows */
    public abstract scala.collection.Iterator<Row> stream();

    @Override
    public void close() throws java.io.IOException {
        // Default implementation for test sources.
    }

    /** Create a concrete source implementation. */
    public static Source create(final FileFormat fileFormat, final Path filePath, final Configuration conf,
            final FileSystem fileSystem) {
        switch (fileFormat) {
        case AVRO:
            return new AvroSource(filePath, conf, fileSystem);
        case ORC:
            return new OrcSource(filePath, conf, fileSystem);
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-21")
                    .message("Storage format {{FORMAT}} is not supported.").parameter("FORMAT", String.valueOf(fileFormat))
                    .mitigation("Please use one of supported storage formats.")
                    .mitigation("Please check the user guide for more information.").toString());
        }
    }
}
