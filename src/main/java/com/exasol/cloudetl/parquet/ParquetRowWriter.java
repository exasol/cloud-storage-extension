package com.exasol.cloudetl.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.schema.MessageType;

import com.exasol.common.data.Row;

/** Factory for Parquet row writers. */
public final class ParquetRowWriter {
    private ParquetRowWriter() {
        // Utility class.
    }

    /** Create a Parquet writer. */
    @SuppressWarnings("deprecation")
    public static ParquetWriter<Row> create(final Path path, final Configuration conf, final MessageType messageType,
            final ParquetWriteOptions options) throws java.io.IOException {
        return new Builder(path, messageType).withRowGroupSize(options.blockSize).withPageSize(options.pageSize)
                .withCompressionCodec(options.compressionCodec).withDictionaryEncoding(options.enableDictionaryEncoding)
                .withValidation(options.enableValidation).withWriteMode(ParquetFileWriter.Mode.CREATE)
                .withWriterVersion(ParquetProperties.WriterVersion.PARQUET_1_0).withConf(conf).build();
    }

    private static final class Builder extends ParquetWriter.Builder<Row, Builder> {
        private final MessageType messageType;

        private Builder(final Path path, final MessageType messageType) {
            super(path);
            this.messageType = messageType;
        }

        @Override
        protected WriteSupport<Row> getWriteSupport(final Configuration conf) {
            return new RowWriteSupport(this.messageType);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
