package com.exasol.cloudetl.parquet;

import static org.junit.jupiter.api.Assertions.*;

import java.math.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.schema.MessageTypeParser;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.helper.UUIDConverter;
import com.exasol.common.data.Row;

class ParquetRowReaderPrimitiveTypesTest extends BaseParquetReaderTest {
    @Test
    void readsInt64TimestampMillisAsTimestampValue() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required int64 col_long;\n" +
"  required int64 col_timestamp (TIMESTAMP_MILLIS);\n" +
"}\n" +
"\n"));
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.append("col_long", 153L);
            record.append("col_timestamp", timestamp.getTime());
            writer.write(record);
        });
        assertEquals(row(153L, timestamp), getRecords().get(0));
    }

    @Test
    void readsInt64TimestampMicrosAsTimestampValue() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required int64 col_timestamp (TIMESTAMP_MICROS);\n" +
"}\n" +
"\n"));
        final Timestamp timestamp = Timestamp.valueOf("2022-01-12 08:28:53.123456");
        final long micros = timestamp.getTime() * 1000L + timestamp.getNanos() / 1000L % 1000L;
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.append("col_timestamp", micros);
            writer.write(record);
        });
        assertEquals(row(timestamp), getRecords().get(0));
    }

    @Test
    void readsFixedLenByteArrayAsStringValue() throws Exception {
        final int size = 5;
        final var schema = MessageTypeParser.parseMessageType(String.format("message test {\n" +
"  required fixed_len_byte_array(%d) col_byte_array;\n" +
"}\n" +
"\n", size));
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.append("col_byte_array", "hello");
            writer.write(record);
        });
        assertEquals(row("hello"), getRecords().get(0));
    }

    @Test
    void readsBinaryAsStringValue() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary col_binary;\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.append("col_binary", "test");
            writer.write(record);
        });
        assertEquals(row("test"), getRecords().get(0));
    }

    @Test
    void readsBinaryUtf8AsStringValueUsingDictionaryEncoding() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary col_binary (UTF8);\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            for (final String value : List.of("test1", "test2", "test1", "test2")) {
                final SimpleGroup record = new SimpleGroup(schema);
                record.append("col_binary", value);
                writer.write(record);
            }
        });
        final List<Row> records = getRecords();
        assertEquals(4, records.size());
        assertEquals(row("test1"), records.get(0));
    }

    @Test
    void readsInt32DecimalAsBigDecimalValueUsingDictionaryEncoding() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required int32 col_int (DECIMAL(9,2));\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            for (final int value : List.of(123456789, 0, 0, 123456789)) {
                final SimpleGroup record = new SimpleGroup(schema);
                record.append("col_int", value);
                writer.write(record);
            }
        });
        final List<Row> records = getRecords();
        assertEquals(4, records.size());
        assertEquals(row(BigDecimal.valueOf(123456789, 2)), records.get(0));
        assertEquals(row(BigDecimal.valueOf(0, 2)), records.get(1));
    }

    @Test
    void readsInt64DecimalAsBigDecimalValueUsingDictionaryEncoding() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required int64 column (DECIMAL(18,2));\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            for (final long value : List.of(1234567890123456L, 1L, 1L, 1234567890123456L)) {
                final SimpleGroup record = new SimpleGroup(schema);
                record.append("column", value);
                writer.write(record);
            }
        });
        final List<Row> records = getRecords();
        assertEquals(4, records.size());
        assertEquals(row(BigDecimal.valueOf(1234567890123456L, 2)), records.get(0));
        assertEquals(row(BigDecimal.valueOf(1L, 2)), records.get(1));
    }

    @Test
    void readsBinaryDecimalAsBigDecimalValueUsingDictionaryEncoding() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary column (DECIMAL(30,2));\n" +
"}\n" +
"\n"));
        final String decimalValue = "123456789012345678901234567890";
        withResource(getParquetWriter(schema, true), writer -> {
            for (final String value : List.of(decimalValue, decimalValue)) {
                final SimpleGroup record = new SimpleGroup(schema);
                record.append("column", value);
                writer.write(record);
            }
        });
        final BigDecimal expected = new BigDecimal(new BigInteger(decimalValue.getBytes(StandardCharsets.UTF_8)), 2,
                new MathContext(30));
        final List<Row> records = getRecords();
        assertEquals(2, records.size());
        assertEquals(row(expected), records.get(0));
    }

    @Test
    void readsFixedLenByteArrayDecimalAsBigDecimalValueUsingDictionaryEncoding() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required fixed_len_byte_array(9) column (DECIMAL(20,2));\n" +
"}\n" +
"\n"));
        final String decimalValue = "12345678901234567890";
        final Binary decimalBinary = Binary.fromConstantByteArray(new BigDecimal(decimalValue).unscaledValue().toByteArray());
        final Binary zeros = Binary.fromConstantByteArray(new byte[9], 0, 9);
        withResource(getParquetWriter(schema, true), writer -> {
            for (final Binary value : List.of(decimalBinary, zeros)) {
                final SimpleGroup record = new SimpleGroup(schema);
                record.append("column", value);
                writer.write(record);
            }
        });
        final BigDecimal expected = new BigDecimal(new BigInteger(decimalValue), 2, new MathContext(20));
        final List<Row> records = getRecords();
        assertEquals(2, records.size());
        assertEquals(row(expected), records.get(0));
        assertEquals(row(BigDecimal.valueOf(0, 2)), records.get(1));
    }

    @Test
    void readsFixedLenByteArrayUuidAsStringValue() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required fixed_len_byte_array(16) column (UUID);\n" +
"}\n" +
"\n"));
        final UUID uuid = UUID.randomUUID();
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.append("column", Binary.fromConstantByteArray(UUIDConverter.toByteArray(uuid)));
            writer.write(record);
        });
        assertEquals(row(uuid.toString()), getRecords().get(0));
    }
}
