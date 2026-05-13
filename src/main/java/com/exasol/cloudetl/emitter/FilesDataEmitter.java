package com.exasol.cloudetl.emitter;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaIterator;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.parquet.ParquetValueConverter;
import com.exasol.cloudetl.source.Source;
import com.exasol.cloudetl.storage.FileFormat;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.cloudetl.transform.DefaultTransformation;
import com.exasol.common.data.Row;
import com.exasol.errorreporting.ExaError;
import com.exasol.parquetio.data.ChunkInterval;
import com.exasol.parquetio.reader.RowParquetChunkReader;
import com.exasol.parquetio.reader.RowParquetReader;

/** Emits file data into Exasol. */
public final class FilesDataEmitter implements Emitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesDataEmitter.class);

    private final StorageProperties properties;
    private final Map<String, List<ChunkInterval>> files;
    private final Bucket bucket;
    private final FileFormat fileFormat;
    private final DefaultTransformation defaultTransformation;

    /** Create an emitter. */
    public FilesDataEmitter(final StorageProperties properties,
            final scala.collection.immutable.Map<String, List<ChunkInterval>> files) {
        this.properties = properties;
        this.files = ScalaConverters.javaMapCopy(files);
        this.bucket = Bucket.create(properties);
        this.fileFormat = properties.getFileFormat();
        this.defaultTransformation = new DefaultTransformation(properties);
    }

    @Override
    public void emit(final ExaIterator context) {
        setDefaultTimezoneToUtcIfEnabled();
        emitFileFormatData(context);
    }

    private void setDefaultTimezoneToUtcIfEnabled() {
        if (this.properties.isEnabled("TIMEZONE_UTC")) {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }
    }

    private void emitFileFormatData(final ExaIterator context) {
        if (this.fileFormat == FileFormat.PARQUET || this.fileFormat == FileFormat.DELTA) {
            emitParquetData(context);
        } else {
            emitRegularData(context);
        }
    }

    private void emitRegularData(final ExaIterator context) {
        int totalRowCount = 0;
        for (final String filename : this.files.keySet()) {
            try (Source source = Source.create(this.fileFormat, new Path(filename), this.bucket.getConfiguration(),
                    this.bucket.fileSystem())) {
                int rowCount = 0;
                final scala.collection.Iterator<Row> stream = source.stream();
                while (stream.hasNext()) {
                    final Object[] values = this.defaultTransformation.transform(transformRegularRowValues(stream.next()));
                    emitRow(context, values);
                    rowCount++;
                }
                totalRowCount += rowCount;
                LOGGER.info("Imported file {} with {} rows", filename, rowCount);
            } catch (final Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
        LOGGER.info("Imported {} files with {} rows in total", this.files.size(), totalRowCount);
    }

    private Object[] transformRegularRowValues(final Row row) {
        return ScalaConverters.asJavaList(row.getValues()).toArray(new Object[0]);
    }

    private void emitParquetData(final ExaIterator context) {
        int totalRowCount = 0;
        int totalIntervalCount = 0;
        for (final Map.Entry<String, List<ChunkInterval>> entry : this.files.entrySet()) {
            final String filename = entry.getKey();
            final List<ChunkInterval> intervals = entry.getValue();
            final InputFile inputFile = getInputFile(filename);
            try {
                final ParquetValueConverter converter = new ParquetValueConverter(RowParquetReader.getSchema(inputFile));
                final RowParquetChunkReader source = new RowParquetChunkReader(inputFile, intervals);
                final int[] rowCount = { 0 };
                source.read(row -> {
                    final Object[] values = defaultTransformation.transform(converter.convert(row));
                    emitRow(context, values);
                    rowCount[0]++;
                });
                totalRowCount += rowCount[0];
                totalIntervalCount += intervals.size();
                LOGGER.info("Imported file {} with {} rows and {} intervals", inputFile, rowCount[0], intervals.size());
            } catch (final IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
        LOGGER.info("Imported {} files with {} intervals and {} rows in total", this.files.size(), totalIntervalCount,
                totalRowCount);
    }

    private InputFile getInputFile(final String filename) {
        try {
            return HadoopInputFile.fromPath(new Path(filename), this.bucket.getConfiguration());
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-CSE-27")
                    .message("Failed to create an input file from {{FILE}}.", filename)
                    .mitigation("Please make sure that the file is not corrupted.").toString(), exception);
        }
    }

    private static void emitRow(final ExaIterator context, final Object... values) {
        try {
            context.emit(values);
        } catch (final Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
