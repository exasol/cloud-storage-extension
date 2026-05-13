package com.exasol.cloudetl.sink;

import java.util.List;
import java.util.UUID;

import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.cloudetl.helper.ParquetSchemaConverter;
import com.exasol.cloudetl.parquet.ParquetRowWriter;
import com.exasol.cloudetl.parquet.ParquetWriteOptions;
import com.exasol.common.data.Row;

/** Sink that balances records across batch-sized Parquet files. */
public final class BatchSizedSink extends Sink<Row> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchSizedSink.class);
    private static final int DEFAULT_BATCH_SIZE = 100_000;

    private final long nodeId;
    private final String vmId;
    private final scala.collection.immutable.Seq<ExaColumnInfo> columns;
    private final Bucket bucket;
    private final int requestedBatchSize;
    private final boolean parquetLowercaseSchema;
    private final long batchSize;
    private long leftOvers;
    private Writer<Row> writer;
    private long recordsCount;
    private long totalRecords;

    /** Create a sink. */
    public BatchSizedSink(final long nodeId, final String vmId, final long numOfRecords,
            final scala.collection.immutable.Seq<ExaColumnInfo> columns, final Bucket bucket) {
        this.nodeId = nodeId;
        this.vmId = vmId;
        this.columns = columns;
        this.bucket = bucket;
        this.requestedBatchSize = Integer.parseInt(bucket.properties().get("EXPORT_BATCH_SIZE").isDefined()
                ? bucket.properties().get("EXPORT_BATCH_SIZE").get()
                : Integer.toString(DEFAULT_BATCH_SIZE));
        this.parquetLowercaseSchema = bucket.properties().isParquetLowercaseSchema();
        final long numOfBuckets = (long) Math.ceil(numOfRecords / (double) this.requestedBatchSize);
        this.batchSize = (long) Math.floor(numOfRecords / (double) numOfBuckets);
        this.leftOvers = numOfRecords % numOfBuckets;
    }

    /** @return total number of written records */
    public Long getTotalRecords() {
        return this.totalRecords;
    }

    @Override
    public Bucket bucket() {
        return this.bucket;
    }

    @Override
    public Writer<Row> createWriter(final String path) {
        return new Writer<Row>() {
            private final ParquetWriter<Row> writer = createParquetWriter(path);

            @Override
            public void write(final Row value) {
                try {
                    this.writer.write(value);
                } catch (final java.io.IOException exception) {
                    throw new IllegalStateException(exception);
                }
            }

            @Override
            public void close() {
                try {
                    this.writer.close();
                } catch (final java.io.IOException exception) {
                    throw new IllegalStateException(exception);
                }
            }
        };
    }

    private ParquetWriter<Row> createParquetWriter(final String path) {
        try {
            final Path newPath = new Path(this.bucket.bucketPath(), path);
            final ParquetWriteOptions options = ParquetWriteOptions.from(this.bucket.properties());
            return ParquetRowWriter.create(newPath, this.bucket.getConfiguration(), getParquetMessageType(), options);
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private MessageType getParquetMessageType() {
        return new ParquetSchemaConverter(this.parquetLowercaseSchema).createParquetMessageType(this.columns,
                "exasol_export_schema");
    }

    @Override
    public void write(final Row value) {
        if (shouldRoll()) {
            openNewFile();
        }
        this.recordsCount++;
        this.writer.write(value);
    }

    @Override
    public void close() {
        this.totalRecords += this.recordsCount;
        this.recordsCount = 0;
        if (this.writer != null) {
            try {
                this.writer.close();
            } catch (final Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
    }

    private boolean shouldRoll() {
        if (this.writer == null) {
            return true;
        }
        if (this.leftOvers > 0 && this.recordsCount >= this.batchSize + 1) {
            this.leftOvers--;
            return true;
        } else if (this.leftOvers == 0 && this.recordsCount >= this.batchSize) {
            return true;
        }
        return false;
    }

    private void openNewFile() {
        close();
        this.writer = createWriter(getNewPath());
        LOGGER.debug("Opened new export file.");
    }

    private String getNewPath() {
        final String uuidStr = UUID.randomUUID().toString().replace("-", "");
        final ParquetWriteOptions parquetOptions = ParquetWriteOptions.from(this.bucket.properties());
        if (parquetOptions.compressionCodec == CompressionCodecName.UNCOMPRESSED) {
            return "exa_export_" + this.nodeId + "_" + this.vmId + "_" + uuidStr + ".parquet";
        }
        return "exa_export_" + this.nodeId + "_" + this.vmId + "_" + uuidStr
                + parquetOptions.compressionCodec.getExtension() + ".parquet";
    }

    /** Convenience constructor for Java lists. */
    public BatchSizedSink(final long nodeId, final String vmId, final long numOfRecords, final List<ExaColumnInfo> columns,
            final Bucket bucket) {
        this(nodeId, vmId, numOfRecords, ScalaConverters.seqFromJava(columns), bucket);
    }
}
