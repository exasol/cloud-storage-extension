package com.exasol.cloudetl.parquet;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.schema.MessageType;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.source.Source;
import com.exasol.common.data.Row;
import com.exasol.parquetio.reader.RowParquetChunkReader;
import com.exasol.parquetio.reader.RowParquetReader;

public final class ParquetSourceTest extends Source {
    private static final Logger LOGGER = Logger.getLogger(ParquetSourceTest.class.getName());
    private final Path path;
    private final Configuration conf;
    private final MessageType schema;
    private RowParquetChunkReader recordReader;
    private final ParquetValueConverter valueConverter;

    public ParquetSourceTest(final Path path, final Configuration conf) {
        this.path = path;
        this.conf = conf;
        try {
            this.schema = RowParquetReader.getSchema(getInputFile(path));
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
        this.recordReader = createReader();
        this.valueConverter = new ParquetValueConverter(this.schema);
    }

    @Override
    public Path path() {
        return this.path;
    }

    @Override
    public FileSystem fileSystem() {
        return null;
    }

    @Override
    public Configuration conf() {
        return this.conf;
    }

    @Override
    public scala.collection.Iterator<Row> stream() {
        final List<Row> values = new ArrayList<>();
        this.recordReader.read(new Consumer<>() {
            @Override
            public void accept(final com.exasol.parquetio.data.Row row) {
                final Object[] converted = ParquetSourceTest.this.valueConverter.convert(row).clone();
                values.add(Row.apply(ScalaConverters.seqFromJava(Arrays.asList(converted))));
            }
        });
        return ScalaConverters.seqFromJava(values).iterator();
    }

    private RowParquetChunkReader createReader() {
        try {
            return new RowParquetChunkReader(getInputFile(this.path));
        } catch (final RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Could not create Parquet reader for path '" + this.path + "'.", exception);
            throw new IllegalStateException("Could not create Parquet reader for path " + this.path + ".", exception);
        }
    }

    private InputFile getInputFile(final Path path) {
        try {
            return HadoopInputFile.fromPath(path, this.conf);
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    public void close() {
        this.recordReader = null;
    }
}
