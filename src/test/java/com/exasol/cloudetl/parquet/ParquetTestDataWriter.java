package com.exasol.cloudetl.parquet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupWriter;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.MessageType;

public class ParquetTestDataWriter {
    private static final long PARQUET_BLOCK_SIZE = 64L * 1024L * 1024L;

    public final ParquetWriter<Group> getParquetWriter(final Path path, final MessageType schema,
            final boolean dictionaryEncoding) throws IOException {
        return new BaseGroupWriterBuilder(path, schema).withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
                .withRowGroupSize(PARQUET_BLOCK_SIZE).withDictionaryEncoding(dictionaryEncoding).build();
    }

    public final <T> void writeDataValues(final List<T> data, final Path path, final MessageType schema)
            throws IOException {
        try (ParquetWriter<Group> writer = getParquetWriter(path, schema, true)) {
            for (final T value : data) {
                final SimpleGroup record = new SimpleGroup(schema);
                if (value != null) {
                    appendValue(value, record);
                }
                writer.write(record);
            }
        }
    }

    private void appendValue(final Object value, final SimpleGroup record) {
        if (value instanceof Boolean) {
            record.append("column", ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            record.append("column", ((Integer) value).intValue());
        } else if (value instanceof Long) {
            record.append("column", ((Long) value).longValue());
        } else if (value instanceof Float) {
            record.append("column", ((Float) value).floatValue());
        } else if (value instanceof Double) {
            record.append("column", ((Double) value).doubleValue());
        } else if (value instanceof String) {
            record.append("column", (String) value);
        } else if (value instanceof Binary) {
            record.append("column", (Binary) value);
        } else {
            throw new IllegalArgumentException("Unknown Parquet value!");
        }
    }

    private static final class BaseGroupWriteSupport extends WriteSupport<Group> {
        private final MessageType schema;
        private GroupWriter writer;

        private BaseGroupWriteSupport(final MessageType schema) {
            this.schema = schema;
        }

        @Override
        public void prepareForWrite(final RecordConsumer recordConsumer) {
            this.writer = new GroupWriter(recordConsumer, this.schema);
        }

        @Override
        public WriteContext init(final Configuration configuration) {
            return new WriteContext(this.schema, new HashMap<>());
        }

        @Override
        public void write(final Group record) {
            this.writer.write(record);
        }
    }

    private static final class BaseGroupWriterBuilder extends ParquetWriter.Builder<Group, BaseGroupWriterBuilder> {
        private final MessageType schema;

        private BaseGroupWriterBuilder(final Path path, final MessageType schema) {
            super(path);
            this.schema = schema;
        }

        @Override
        protected WriteSupport<Group> getWriteSupport(final Configuration conf) {
            return new BaseGroupWriteSupport(this.schema);
        }

        @Override
        protected BaseGroupWriterBuilder self() {
            return this;
        }
    }
}
