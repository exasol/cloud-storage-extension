package com.exasol.cloudetl.parquet;

import static com.exasol.cloudetl.helper.DateTimeConverter.*;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.matcher.TypeMatchMode.NO_JAVA_TYPE_CHECK;
import static com.exasol.matcher.TypeMatchMode.STRICT;

import java.io.IOException;
import java.math.*;
import java.nio.*;
import java.sql.*;
import java.time.*;
import java.util.*;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.schema.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.BaseDataImporter;
import com.exasol.cloudetl.helper.UUIDConverter;
import com.exasol.matcher.CellMatcherFactory;

class ParquetDataImporterIT extends BaseDataImporter {
    private static final ParquetTestDataWriter WRITER = new ParquetTestDataWriter();

    @Override
    protected String schemaName() {
        return "PARQUET_SCHEMA";
    }

    @Override
    protected String bucketName() {
        return "parquet-bucket";
    }

    @Override
    protected String dataFormat() {
        return "parquet";
    }

    @Test
    void importsPrimitiveTypes() throws IOException, SQLException {
        checker("optional boolean column;", "BOOLEAN", "boolean_table").withInputValues(values(true, false, (Object) null))
                .assertResultSet(table().row(Boolean.TRUE).row(Boolean.FALSE).row((Object) null).matches());
        checker("optional int32 column;", "DECIMAL(10,0)", "int32")
                .withInputValues(values(this.intMin, 666, (Object) null, this.intMax)).assertResultSet(table()
                        .row(Integer.valueOf(this.intMin)).row(Integer.valueOf(666)).row((Object) null).row(Integer.valueOf(this.intMax))
                        .matches(NO_JAVA_TYPE_CHECK));
        checker("required int32 column (DATE);", "DATE", "int32_date").withInputValues(values(0, 1, 54, 567, 1234))
                .assertResultSet(table().row(daysToDate(0)).row(daysToDate(1)).row(daysToDate(54)).row(daysToDate(567))
                        .row(daysToDate(1234)).matches());
        checker("required int32 column (DECIMAL(8,3));", "DECIMAL(18,3)", "int32_decimal")
                .withInputValues(values(1, 34, 567, 1234)).assertResultSet(table().row(0.001).row(0.034).row(0.567)
                        .row(1.234).matches(NO_JAVA_TYPE_CHECK));
        checker("optional int64 column;", "DECIMAL(19,0)", "int64")
                .withInputValues(values(this.longMin, 999L, (Object) null, this.longMax)).assertResultSet(table()
                        .row(Long.valueOf(this.longMin)).row(Long.valueOf(999)).row((Object) null).row(Long.valueOf(this.longMax))
                        .matches(NO_JAVA_TYPE_CHECK));
        checker("optional int64 column (DECIMAL(12,2));", "DECIMAL(27,2)", "int64_decimal")
                .withInputValues(values(271717171717L, 314141414141L, (Object) null)).assertResultSet(table()
                        .row(Double.valueOf(2717171717.17)).row(Double.valueOf(3141414141.41)).row((Object) null)
                        .matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsTimestampsAndFloatingPoint() throws IOException, SQLException {
        final long millis1 = Instant.EPOCH.toEpochMilli();
        final long millis2 = System.currentTimeMillis();
        final Timestamp expectedTimestamp1 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        final Timestamp expectedTimestamp2 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        checker("optional int64 column (TIMESTAMP_MILLIS);", "TIMESTAMP", "int64_timestamp_millis")
                .withInputValues(values(millis1, millis2, (Object) null))
                .assertResultSet(table().row(expectedTimestamp1).row(expectedTimestamp2).row((Object) null).matches());

        final Timestamp timestamp = Timestamp.valueOf("2022-01-12 10:28:53.123456");
        final long micros = timestamp.getTime() * 1000L + (timestamp.getNanos() / 1000L) % 1000L;
        final Timestamp expectedMicros = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("Europe/Berlin"))
                        .toLocalDateTime());
        checker("optional int64 column (TIMESTAMP_MICROS);", "TIMESTAMP", "int64_timestamp_micros")
                .withInputValues(values(micros, (Object) null)).assertResultSet(table().row(expectedMicros).row((Object) null).matches());

        final BigDecimal eps = BigDecimal.valueOf(0.0001);
        checker("optional float column;", "FLOAT", "float_table").withInputValues(values(2.71f, 3.14f, (Object) null))
                .assertResultSet(table().row(CellMatcherFactory.cellMatcher(2.71, STRICT, eps))
                        .row(CellMatcherFactory.cellMatcher(3.14, STRICT, eps)).row((Object) null).matches());
        checker("optional double column;", "DOUBLE", "double_table").withInputValues(values(20.21, 1.13, (Object) null))
                .assertResultSet(table().row(20.21).row(1.13).row((Object) null).matches());
    }

    @Test
    void importsBinaryTypes() throws IOException, SQLException {
        checker("optional binary column;", "VARCHAR(20)", "binary_table").withInputValues(values("hello", "world", (Object) null))
                .assertResultSet(table().row("hello").row("world").row((Object) null).matches());
        checker("required binary column (UTF8);", "VARCHAR(20)", "binary_utf8_table")
                .withInputValues(values("ÄäÖöÜüß / ☺", "world", ""))
                .assertResultSet(table().row("ÄäÖöÜüß / ☺").row("world").row((Object) null).matches());
        final Binary decimal1 = Binary.fromConstantByteArray(new BigDecimal("12345").unscaledValue().toByteArray());
        final Binary decimal2 = Binary.fromConstantByteArray(new BigDecimal("123456").unscaledValue().toByteArray());
        checker("required binary column (DECIMAL(8,3));", "DECIMAL(18,3)", "binary_decimal")
                .withInputValues(values(decimal1, decimal2)).assertResultSet(table().row(12.345).row(123.456)
                        .matches(NO_JAVA_TYPE_CHECK));
        checker("required fixed_len_byte_array(5) column;", "VARCHAR(5)", "fixed")
                .withInputValues(values("abcde", "world", "     "))
                .assertResultSet(table().row("abcde").row("world").row("     ").matches());
        final String decimalValueString = "12345678901234567890";
        final Binary fixedDecimal = Binary.fromConstantByteArray(new BigDecimal(decimalValueString).unscaledValue().toByteArray());
        final Binary fixedZeros = Binary.fromConstantByteArray(new byte[9], 0, 9);
        checker("required fixed_len_byte_array(9) column (DECIMAL(20,5));", "DECIMAL(20,5)", "fixed_decimal")
                .withInputValues(values(fixedDecimal, fixedZeros)).assertResultSet(table()
                        .row(new BigDecimal(new BigInteger(decimalValueString), 5, new MathContext(20))).row(0.00)
                        .matches(NO_JAVA_TYPE_CHECK));
        final java.util.UUID uuid = java.util.UUID.randomUUID();
        final Binary uuidBinary = Binary.fromConstantByteArray(UUIDConverter.toByteArray(uuid));
        final Binary uuidZeros = Binary.fromConstantByteArray(new byte[16], 0, 16);
        checker("required fixed_len_byte_array(16) column (UUID);", "VARCHAR(36)", "fixed_uuid")
                .withInputValues(values(uuidBinary, uuidZeros))
                .assertResultSet(table().row(uuid.toString()).row("00000000-0000-0000-0000-000000000000").matches());
    }

    @Test
    void importsInt96TimestampNanos() throws IOException, SQLException {
        final long millis = System.currentTimeMillis();
        final Timestamp timestamp = new Timestamp(millis);
        final ByteBuffer buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
        final scala.Tuple2<Integer, Long> dayAndNanos = getJulianDayAndNanos(getMicrosFromTimestamp(timestamp));
        buffer.putLong(dayAndNanos._2().longValue()).putInt(dayAndNanos._1().intValue());
        final Timestamp expectedTimestamp = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        checker("required int96 column;", "TIMESTAMP", "int96_timestamp")
                .withInputValues(values(Binary.fromConstantByteArray(buffer.array())))
                .assertResultSet(table().row(expectedTimestamp).matches());
    }

    @Test
    void importsListAndMapTypes() throws IOException, SQLException {
        checker(listType("names", "required binary name (UTF8);"), "VARCHAR(20)", "list_strings")
                .withWriter((writer, schema) -> {
                    final SimpleGroup record = new SimpleGroup(schema);
                    final Group names = record.addGroup(0);
                    names.addGroup(0).append("name", "John");
                    names.addGroup(0).append("name", "Jana");
                    writer.write(record);
                }).assertResultSet(table().row("[\"John\",\"Jana\"]").matches());
        checker(listType("prices", "required double price;"), "VARCHAR(20)", "list_doubles").withWriter((writer, schema) -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final Group prices = record.addGroup(0);
            prices.addGroup(0).append("price", 0.14);
            prices.addGroup(0).append("price", 1.234);
            writer.write(record);
        }).assertResultSet(table().row("[0.14,1.234]").matches());
        checker("optional group map (MAP) { repeated group key_value { required binary key (UTF8); required int64 value; }}",
                "VARCHAR(20)", "map_table").withWriter((writer, schema) -> {
                    final SimpleGroup record = new SimpleGroup(schema);
                    final Group map = record.addGroup(0);
                    map.addGroup("key_value").append("key", "key1").append("value", 3L);
                    map.addGroup("key_value").append("key", "key2").append("value", 7L);
                    writer.write(record);
                }).assertResultSet(table().row("{\"key1\":3,\"key2\":7}").matches());
        checker("repeated binary name (UTF8);", "VARCHAR(20)", "repeated_field").withWriter((writer, schema) -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.add(0, "John");
            record.add(0, "Jane");
            writer.write(record);
        }).assertResultSet(table().row("[\"John\",\"Jane\"]").matches());
    }

    @Test
    void importsRepeatedGroupsAndMultipleColumns() throws IOException, SQLException {
        checker("repeated group person { required binary name (UTF8); }", "VARCHAR(20)", "repeated_group_single_field")
                .withWriter((writer, schema) -> {
                    final SimpleGroup record = new SimpleGroup(schema);
                    record.addGroup(0).append("name", "John");
                    record.addGroup(0).append("name", "Jane");
                    writer.write(record);
                }).assertResultSet(table().row("[\"John\",\"Jane\"]").matches());
        checker("repeated group person { required binary name (UTF8); optional int32 age; }", "VARCHAR(60)",
                "repeated_group_many_fields").withWriter((writer, schema) -> {
                    final SimpleGroup record = new SimpleGroup(schema);
                    record.addGroup(0).append("name", "John").append("age", 24);
                    record.addGroup(0).append("name", "Jane").append("age", 22);
                    writer.write(record);
                }).assertResultSet(table().row("[{\"name\":\"John\",\"age\":24},{\"name\":\"Jane\",\"age\":22}]").matches());
        multiChecker("required binary name (UTF8); required int32 age;", mapOf("NAME", "VARCHAR(60)", "AGE", "INTEGER"),
                "multi_col_single_file").addParquetFile((writer, schema) -> {
                    writer.write(new SimpleGroup(schema).append("name", "John").append("age", 24));
                    writer.write(new SimpleGroup(schema).append("name", "Jane").append("age", 22));
                }).assertResultSet(table("VARCHAR", "BIGINT").row("John", 24L).row("Jane", 22L).matches());
        multiChecker("required binary name (UTF8); required int32 age;", mapOf("NAME", "VARCHAR(60)", "AGE", "INTEGER"),
                "multi_col_multi_file")
                .addParquetFile((writer, schema) -> writer.write(new SimpleGroup(schema).append("name", "John").append("age", 24)))
                .addParquetFile((writer, schema) -> writer.write(new SimpleGroup(schema).append("name", "Jane").append("age", 22)))
                .assertResultSet(table("VARCHAR", "BIGINT").row("John", 24L).row("Jane", 22L).matches());
    }

    @Test
    void importFailures() throws IOException {
        multiChecker("required binary name (UTF8); required int32 age;", mapOf("NAME", "VARCHAR(60)", "AGE", "INTEGER"),
                "missing_field").addParquetFile((writer, schema) -> writer.write(new SimpleGroup(schema).append("name", "John")))
                .assertFails(Matchers.containsString("ParquetDecodingException: Can't read value in column [age] required int32 age"));
        multiChecker("required binary name (UTF8);", mapOf("NAME", "VARCHAR(60)", "AGE", "INTEGER"), "missing_column")
                .addParquetFileWithSchema(MessageTypeParser.parseMessageType(
                        "message test { required binary name (UTF8); required int32 age; }"),
                        (writer, schema) -> writer.write(new SimpleGroup(schema).append("name", "John").append("age", 24)))
                .addParquetFileWithSchema(MessageTypeParser.parseMessageType("message test { required binary name (UTF8); }"),
                        (writer, schema) -> writer.write(new SimpleGroup(schema).append("name", "Jane")))
                .assertFails(Matchers.containsString("ExaIterationException: E-UDF-CL-SL-JAVA-1107: emit() takes exactly 2 arguments (1 given)"));
    }

    private String listType(final String fieldName, final String repeatedField) {
        return "optional group " + fieldName + " (LIST) { repeated group list { " + repeatedField + " }}";
    }

    private ParquetChecker checker(final String parquetColumn, final String exaColumn, final String tableName) {
        return new ParquetChecker(parquetColumn, exaColumn, tableName);
    }

    private MultiParquetChecker multiChecker(final String parquetColumn, final Map<String, String> exaColumns,
            final String tableName) {
        return new MultiParquetChecker(parquetColumn, exaColumns, tableName);
    }

    private static List<Object> values(final Object... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    private static Map<String, String> mapOf(final String... values) {
        final Map<String, String> result = new LinkedHashMap<>();
        for (int index = 0; index < values.length; index += 2) {
            result.put(values[index], values[index + 1]);
        }
        return result;
    }

    private final class ParquetChecker extends AbstractChecker {
        private final MessageType parquetSchema;

        private ParquetChecker(final String parquetColumn, final String exaColumn, final String tableName) {
            super(exaColumn, tableName);
            this.parquetSchema = MessageTypeParser.parseMessageType("message test { " + parquetColumn + " }");
        }

        private ParquetChecker withWriter(final WriterConsumer block) throws IOException {
            final org.apache.hadoop.fs.Path path = addFile();
            try (ParquetWriter<Group> writer = WRITER.getParquetWriter(path, this.parquetSchema, true)) {
                block.accept(writer, this.parquetSchema);
            }
            return this;
        }

        private ParquetChecker withInputValues(final List<?> values) throws IOException {
            WRITER.writeDataValues(values, addFile(), this.parquetSchema);
            return this;
        }
    }

    private final class MultiParquetChecker extends AbstractMultiColChecker {
        private final MessageType parquetSchema;

        private MultiParquetChecker(final String parquetColumn, final Map<String, String> exaColumns, final String tableName) {
            super(exaColumns, tableName);
            this.parquetSchema = MessageTypeParser.parseMessageType("message test { " + parquetColumn + " }");
        }

        private MultiParquetChecker addParquetFile(final WriterConsumer block) throws IOException {
            return addParquetFileWithSchema(this.parquetSchema, block);
        }

        private MultiParquetChecker addParquetFileWithSchema(final MessageType customParquetSchema,
                final WriterConsumer block) throws IOException {
            final org.apache.hadoop.fs.Path path = addFile();
            try (ParquetWriter<Group> writer = WRITER.getParquetWriter(path, customParquetSchema, true)) {
                block.accept(writer, customParquetSchema);
            }
            return this;
        }
    }

    @FunctionalInterface
    private interface WriterConsumer {
        void accept(ParquetWriter<Group> writer, MessageType schema) throws IOException;
    }
}
