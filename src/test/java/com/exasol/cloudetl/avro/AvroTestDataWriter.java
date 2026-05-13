package com.exasol.cloudetl.avro;

import java.io.*;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.*;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.fs.Path;

public class AvroTestDataWriter {
    private static final int AVRO_SYNC_INTERVAL_SIZE = 64 * 1024 * 1024;

    protected final DataFileWriter<GenericRecord> getAvroWriter(final Path path, final Schema schema) throws IOException {
        final DataFileWriter<GenericRecord> writer = new DataFileWriter<>(new SpecificDatumWriter<>());
        writer.setFlushOnEveryBlock(false);
        writer.setSyncInterval(AVRO_SYNC_INTERVAL_SIZE);
        writer.create(schema, new File(path.toUri()));
        return writer;
    }

    protected final <T> void writeDataValues(final List<T> values, final Path path, final Schema schema) throws IOException {
        try (DataFileWriter<GenericRecord> writer = getAvroWriter(path, schema)) {
            for (final T value : values) {
                final GenericRecord record = new GenericData.Record(schema);
                record.put("column", value);
                writer.append(record);
            }
        }
    }
}
