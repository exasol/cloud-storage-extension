package com.exasol.cloudetl.source;

import java.io.UncheckedIOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.orc.converter.StructConverter;
import com.exasol.common.data.Row;
import com.exasol.common.json.JsonMapper;
import com.exasol.errorreporting.ExaError;

/** ORC source for Hadoop-compatible filesystems. */
public final class OrcSource extends Source {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrcSource.class);

    private final Path path;
    private final Configuration conf;
    private final FileSystem fileSystem;
    private final Reader reader;
    private org.apache.orc.RecordReader recordReader;

    /** Create a source. */
    public OrcSource(final Path path, final Configuration conf, final FileSystem fileSystem) {
        this.path = path;
        this.conf = conf;
        this.fileSystem = fileSystem;
        this.reader = createReader();
        try {
            this.recordReader = this.reader.rows(new Reader.Options());
        } catch (final java.io.IOException exception) {
            throw new SourceValidationException(exception.getMessage(), exception);
        }
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
        return scala.jdk.javaapi.CollectionConverters.asScala(new java.util.Iterator<Row>() {
            private final VectorizedRowBatch batch = reader.getSchema().createRowBatch();
            private BatchIterator batchIterator = new BatchIterator(this.batch);

            @Override
            public boolean hasNext() {
                try {
                    while (!this.batchIterator.hasNext()) {
                        this.batch.reset();
                        recordReader.nextBatch(this.batch);
                        this.batchIterator = new BatchIterator(this.batch);
                        if (this.batch.endOfFile || this.batch.size <= 0) {
                            return false;
                        }
                    }
                    return true;
                } catch (final java.io.IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            }

            @Override
            public Row next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.batchIterator.next();
            }
        });
    }

    private Reader createReader() {
        final OrcFile.ReaderOptions options = OrcFile.readerOptions(this.conf).filesystem(this.fileSystem)
                .useUTCTimestamp(true);
        try {
            return OrcFile.createReader(this.path, options);
        } catch (final RuntimeException | java.io.IOException exception) {
            LOGGER.error("Could not create orc reader for the path: {}", this.path, exception);
            throw new SourceValidationException(ExaError.messageBuilder("E-CSE-25")
                    .message("Could not create Orc reader for path {{PATH}}.").parameter("PATH", this.path.toString())
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

    private final class BatchIterator implements java.util.Iterator<Row> {
        private final VectorizedRowBatch batch;
        private final List<org.apache.orc.TypeDescription> fields = reader.getSchema().getChildren();
        private final List<String> fieldNames = reader.getSchema().getFieldNames();
        private final StructColumnVector vector;
        private final StructConverter converter = new StructConverter(reader.getSchema());
        private int offset;

        BatchIterator(final VectorizedRowBatch batch) {
            this.batch = batch;
            this.vector = new StructColumnVector(batch.numCols, batch.cols);
        }

        @Override
        public boolean hasNext() {
            return this.offset < this.batch.size;
        }

        @Override
        public Row next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final Map<String, Object> valuesMap = this.converter.readAt(this.vector, this.offset);
            final List<Object> values = new ArrayList<>(this.fields.size());
            for (int index = 0; index < this.fields.size(); index++) {
                final String columnName = this.fieldNames.get(index);
                final Object columnValue = valuesMap.get(columnName);
                if (this.fields.get(index).getCategory().isPrimitive()) {
                    values.add(columnValue);
                } else {
                    values.add(JsonMapper.toJson(columnValue));
                }
            }
            this.offset++;
            return com.exasol.common.data.Row.apply(ScalaConverters.seqFromJava(values));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrcSource)) {
            return false;
        }
        final OrcSource other = (OrcSource) obj;
        return Objects.equals(this.path, other.path) && Objects.equals(this.conf, other.conf)
                && Objects.equals(this.fileSystem, other.fileSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.conf, this.fileSystem);
    }
}
