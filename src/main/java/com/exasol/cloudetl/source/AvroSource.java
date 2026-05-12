package com.exasol.cloudetl.source;

import java.util.Objects;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.common.avro.AvroRowIterator;
import com.exasol.common.data.Row;
import com.exasol.errorreporting.ExaError;

/** Avro source for Hadoop-compatible filesystems. */
public final class AvroSource extends Source {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSource.class);

    private final Path path;
    private final Configuration conf;
    private final FileSystem fileSystem;
    private DataFileReader<GenericRecord> recordReader;

    /** Create a source. */
    public AvroSource(final Path path, final Configuration conf, final FileSystem fileSystem) {
        this.path = path;
        this.conf = conf;
        this.fileSystem = fileSystem;
        this.recordReader = createReader();
    }

    @Override
    public Path path() {
        return this.path;
    }

    @Override
    public Configuration conf() {
        return this.conf;
    }

    @Override
    public FileSystem fileSystem() {
        return this.fileSystem;
    }

    @Override
    public scala.collection.Iterator<Row> stream() {
        return AvroRowIterator.apply(this.recordReader);
    }

    private DataFileReader<GenericRecord> createReader() {
        try {
            final SeekableInput input = new FsInput(this.path, this.fileSystem);
            return new DataFileReader<>(input, new GenericDatumReader<>());
        } catch (final RuntimeException | java.io.IOException exception) {
            LOGGER.error("Could not create avro reader for path: {}", this.path, exception);
            throw new SourceValidationException(ExaError.messageBuilder("E-CSE-26")
                    .message("Could not create Avro reader for path {{PATH}}.").parameter("PATH", this.path.toString())
                    .toString(), exception);
        }
    }

    @Override
    public void close() throws java.io.IOException {
        if (this.recordReader != null) {
            try {
                this.recordReader.close();
            } finally {
                this.recordReader = null;
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AvroSource)) {
            return false;
        }
        final AvroSource other = (AvroSource) obj;
        return Objects.equals(this.path, other.path) && Objects.equals(this.conf, other.conf)
                && Objects.equals(this.fileSystem, other.fileSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.conf, this.fileSystem);
    }
}
