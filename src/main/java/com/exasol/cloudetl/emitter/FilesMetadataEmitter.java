package com.exasol.cloudetl.emitter;

import java.util.ArrayList;
import java.util.List;

import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaIterator;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.storage.FileFormat;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.parquetio.splitter.ParquetFileSplitter;

/** Emits file metadata into Exasol. */
public final class FilesMetadataEmitter implements Emitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesMetadataEmitter.class);

    private final StorageProperties properties;
    private final int parallelism;
    private final Bucket bucket;
    private final scala.collection.immutable.Seq<org.apache.hadoop.fs.Path> paths;
    private final FileFormat fileFormat;

    /** Create an emitter. */
    public FilesMetadataEmitter(final StorageProperties properties, final int parallelism) {
        this.properties = properties;
        this.parallelism = parallelism;
        this.bucket = Bucket.create(properties);
        this.paths = this.bucket.getPaths();
        this.fileFormat = properties.getFileFormat();
    }

    @Override
    public void emit(final ExaIterator context) {
        LOGGER.info("Found total of '{}' files in path '{}'.", this.paths.size(), this.bucket.bucketPath());
        if (isParquetFormat(this.fileFormat)) {
            emitParquetFilesMetadata(context);
        } else {
            emitRegularFilesMetadata(context);
        }
    }

    private boolean isParquetFormat(final FileFormat fileFormat) {
        return fileFormat == FileFormat.PARQUET || fileFormat == FileFormat.DELTA;
    }

    private void emitRegularFilesMetadata(final ExaIterator context) {
        long index = 0L;
        for (final org.apache.hadoop.fs.Path filename : ScalaConverters.asJavaList(this.paths)) {
            emitRow(context, filename.toString(), String.valueOf(index % this.parallelism), Long.valueOf(0), Long.valueOf(0));
            index++;
        }
    }

    private void emitParquetFilesMetadata(final ExaIterator context) {
        final long chunkSize = this.properties.getChunkSize();
        final List<FilenameChunkInterval> chunks = new ArrayList<>();
        try {
            for (final org.apache.hadoop.fs.Path filename : ScalaConverters.asJavaList(this.paths)) {
                final HadoopInputFile inputFile = HadoopInputFile.fromPath(filename, this.bucket.getConfiguration());
                final java.util.List<com.exasol.parquetio.data.ChunkInterval> splits =
                        new ParquetFileSplitter(inputFile, chunkSize).getSplits();
                for (int index = 0; index < splits.size(); index++) {
                    chunks.add(new FilenameChunkInterval(filename.toString(), splits.get(index).getStartPosition(),
                            splits.get(index).getEndPosition()));
                }
            }
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
        final Partitioner partitioner = new Partitioner(chunks.size(), this.parallelism);
        long index = 0L;
        long count = 0L;
        for (final FilenameChunkInterval interval : chunks) {
            LOGGER.info("Emitting filename metadata {} -> [{} ... {}) on index {}.", interval.filename, interval.start,
                    interval.end, index);
            emitRow(context, interval.filename, String.valueOf(index), interval.start, interval.end);
            count++;
            if (partitioner.isNewPartition(count)) {
                index++;
                count = 0;
            }
        }
    }

    private static final class FilenameChunkInterval {
        private final String filename;
        private final long start;
        private final long end;

        private FilenameChunkInterval(final String filename, final long start, final long end) {
            this.filename = filename;
            this.start = start;
            this.end = end;
        }
    }

    private static final class Partitioner {
        private final long partitionSize;
        private long leftOvers;

        private Partitioner(final int total, final int numberOfPartitions) {
            this.partitionSize = (long) Math.floor(total / (double) numberOfPartitions);
            this.leftOvers = total % numberOfPartitions;
        }

        private boolean isNewPartition(final long currentCount) {
            if (this.leftOvers > 0 && currentCount >= this.partitionSize + 1) {
                this.leftOvers--;
                return true;
            } else if (this.leftOvers == 0 && currentCount >= this.partitionSize) {
                return true;
            }
            return false;
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
