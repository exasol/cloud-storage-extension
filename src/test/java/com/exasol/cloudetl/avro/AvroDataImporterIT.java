package com.exasol.cloudetl.avro;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.matcher.TypeMatchMode.NO_JAVA_TYPE_CHECK;
import static com.exasol.matcher.TypeMatchMode.STRICT;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.util.List;

import org.apache.avro.*;
import org.apache.avro.generic.GenericData;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.BaseDataImporter;
import com.exasol.matcher.CellMatcherFactory;

class AvroDataImporterIT extends BaseDataImporter {
    private static final AvroTestDataWriter WRITER = new AvroTestDataWriter();

    @Override
    protected String schemaName() {
        return "AVRO_SCHEMA";
    }

    @Override
    protected String bucketName() {
        return "avro-bucket";
    }

    @Override
    protected String dataFormat() {
        return "avro";
    }

    @Test
    void importsBoolean() throws IOException, SQLException {
        checker(basicSchema("\"boolean\""), "BOOLEAN", "bool").withInputValues(List.of(Boolean.TRUE, Boolean.FALSE))
                .assertResultSet(table().row(Boolean.TRUE).row(Boolean.FALSE).matches());
    }

    @Test
    void importsInt() throws IOException, SQLException {
        checker(basicSchema("\"int\""), "DECIMAL(10,0)", "int")
                .withInputValues(List.of(Integer.valueOf(this.intMin), Integer.valueOf(13), Integer.valueOf(this.intMax)))
                .assertResultSet(table().row(Integer.valueOf(this.intMin)).row(Integer.valueOf(13))
                        .row(Integer.valueOf(this.intMax)).matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsIntDate() throws IOException, SQLException {
        checker(basicSchema("{\"type\":\"int\",\"logicalType\":\"date\"}"), "DATE", "int_date")
                .withInputValues(List.of(0, 1)).assertResultSet(table().row(Date.valueOf("1970-01-01"))
                        .row(Date.valueOf("1970-01-02")).matches());
    }

    @Test
    void importsLong() throws IOException, SQLException {
        checker(basicSchema("\"long\""), "DECIMAL(19,0)", "long")
                .withInputValues(List.of(Long.valueOf(this.longMin), Long.valueOf(77), Long.valueOf(this.longMax)))
                .assertResultSet(table().row(Long.valueOf(this.longMin)).row(Long.valueOf(77))
                        .row(Long.valueOf(this.longMax)).matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsLongTimestampMillis() throws IOException, SQLException {
        final long millis = System.currentTimeMillis();
        final Timestamp expectedTimestamp1 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        final Timestamp expectedTimestamp2 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin")).toLocalDateTime());
        checker(basicSchema("{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}"), "TIMESTAMP", "long_timestamp")
                .withInputValues(List.of(Long.valueOf(millis), Long.valueOf(0)))
                .assertResultSet(table().row(expectedTimestamp1).row(expectedTimestamp2).matches());
    }

    @Test
    void importsFloat() throws IOException, SQLException {
        final BigDecimal eps = BigDecimal.valueOf(0.0001);
        checker(basicSchema("\"float\""), "FLOAT", "float").withInputValues(List.of(Float.valueOf(3.14f), Float.valueOf(2.71f)))
                .assertResultSet(table().row(CellMatcherFactory.cellMatcher(3.14, STRICT, eps))
                        .row(CellMatcherFactory.cellMatcher(2.71, STRICT, eps)).matches());
    }

    @Test
    void importsDouble() throws IOException, SQLException {
        checker(basicSchema("\"double\""), "DOUBLE", "double").withInputValues(List.of(3.14, 2.71))
                .assertResultSet(table().row(Double.valueOf(3.14)).row(Double.valueOf(2.71)).matches());
    }

    @Test
    void importsBytes() throws IOException, SQLException {
        checker(basicSchema("\"bytes\""), "VARCHAR(20)", "bytes")
                .withInputValues(List.of(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8))))
                .assertResultSet(table().row("hello").matches());
    }

    @Test
    void importsBytesDecimal() throws IOException, SQLException {
        final ByteBuffer decimal1 = ByteBuffer.wrap(new BigDecimal("123456").unscaledValue().toByteArray());
        final ByteBuffer decimal2 = ByteBuffer.wrap(new BigDecimal("12345678").unscaledValue().toByteArray());
        checker(basicSchema("{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":8,\"scale\":3}"), "DECIMAL(8,3)",
                "bytes_decimal").withInputValues(List.of(decimal1, decimal2))
                .assertResultSet(table().row(Double.valueOf(123.456)).row(Double.valueOf(12345.678))
                        .matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsFixed() throws IOException, SQLException {
        final String schema = basicSchema("{\"type\":\"fixed\",\"name\":\"fixed\", \"size\":5}");
        final Schema fixedSchema = new Schema.Parser().parse(schema).getField("column").schema();
        final GenericData.Fixed fixedData = new GenericData.Fixed(fixedSchema);
        fixedData.bytes("fixed".getBytes(StandardCharsets.UTF_8));
        checker(schema, "VARCHAR(20)", "fixed").withInputValues(List.of(fixedData))
                .assertResultSet(table().row("fixed").matches());
    }

    @Test
    void importsFixedDecimal() throws IOException, SQLException {
        final String inner = "{\"type\":\"fixed\",\"name\":\"fx\",\"size\":7,\"logicalType\":\"decimal\",\"precision\":7,\"scale\":4}";
        final String schema = basicSchema(inner);
        final Schema fixedSchema = new Schema.Parser().parse(schema).getField("column").schema();
        final org.apache.avro.generic.GenericFixed fixedData = new Conversions.DecimalConversion().toFixed(new BigDecimal("0.0123"),
                fixedSchema, LogicalTypes.decimal(7, 4));
        checker(schema, "DECIMAL(7,4)", "fixed_decimal").withInputValues(List.of(fixedData))
                .assertResultSet(table().row(Double.valueOf(0.0123)).matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsString() throws IOException, SQLException {
        checker(basicSchema("\"string\""), "VARCHAR(20)", "string_table").withInputValues(List.of("hello", "worldÜüß"))
                .assertResultSet(table().row("hello").row("worldÜüß").matches());
    }

    @Test
    void importsEnum() throws IOException, SQLException {
        final String schema = basicSchema("{\"type\":\"enum\",\"name\":\"lttrs\",\"symbols\":[\"A\",\"B\",\"C\"]}");
        final Schema enumSchema = new Schema.Parser().parse(schema).getField("column").schema();
        checker(schema, "VARCHAR(20)", "enum_table").withInputValues(List.of(new GenericData.EnumSymbol(enumSchema, "B")))
                .assertResultSet(table().row("B").matches());
    }

    @Test
    void importsUnion() throws IOException, SQLException {
        checker(basicSchema("[\"string\", \"null\"]"), "VARCHAR(20)", "union_table")
                .withInputValues(listWithNull("str-value")).assertResultSet(table().row("str-value").row((Object) null).matches());
    }

    @Test
    void importsArrayAndMapTypes() throws IOException, SQLException {
        checker(basicSchema("{\"type\":\"array\",\"items\":\"string\"}"), "VARCHAR(20)", "array_strings")
                .withInputValues(List.of(List.of("a", "b"), List.of("c", "d")))
                .assertResultSet(table().row("[\"a\",\"b\"]").row("[\"c\",\"d\"]").matches());
        checker(basicSchema("{\"type\":\"array\",\"items\":\"int\"}"), "VARCHAR(20)", "array_ints")
                .withInputValues(List.of(List.of(4, 5, 6))).assertResultSet(table().row("[4,5,6]").matches());
        checker(basicSchema("{\"type\":\"array\",\"items\":\"double\"}"), "VARCHAR(60)", "array_doubles")
                .withInputValues(List.of(List.of(1.01, 3.14, 2.71))).assertResultSet(table().row("[1.01,3.14,2.71]").matches());
        checker(basicSchema("{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}}"), "VARCHAR(20)",
                "array_arrays").withInputValues(List.of(List.of(List.of(1, 2, 3), List.of(5, 6))))
                .assertResultSet(table().row("[[1,2,3],[5,6]]").matches());
        checker(basicSchema("{\"type\":\"map\",\"values\":\"int\"}"), "VARCHAR(30)", "map_table")
                .withInputValues(List.of(java.util.Map.of("key1", 3, "key2", 6, "key3", 9)))
                .assertResultSet(table().row("{\"key1\":3,\"key2\":6,\"key3\":9}").matches());
        checker(basicSchema("{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":\"int\"}}"), "VARCHAR(20)",
                "map_array_values").withInputValues(List.of(java.util.Map.of("k1", List.of(1, 2, 3))))
                .assertResultSet(table().row("{\"k1\":[1,2,3]}").matches());
    }

    @Test
    void importsNestedRecord() throws IOException, SQLException {
        final String inner = "{\"type\":\"record\",\"name\":\"Record\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},"
                + "{\"name\":\"weight\",\"type\":\"double\"}]}";
        final Schema innerSchema = new Schema.Parser().parse(inner);
        final String schema = basicSchema("{\"type\":\"array\",\"items\":" + inner + "}");
        final GenericData.Record recordOne = new GenericData.Record(innerSchema);
        recordOne.put("name", "one");
        recordOne.put("weight", Double.valueOf(67.14));
        final GenericData.Record recordTwo = new GenericData.Record(innerSchema);
        recordTwo.put("name", "two");
        recordTwo.put("weight", Double.valueOf(78.71));
        checker(schema, "VARCHAR(65)", "nested_records_table").withInputValues(List.of(List.of(recordOne, recordTwo)))
                .assertResultSet(table().row("[{\"name\":\"one\",\"weight\":67.14},{\"name\":\"two\",\"weight\":78.71}]")
                        .matches());
    }

    private String basicSchema(final String avroType) {
        return "{ \"type\": \"record\", \"namespace\": \"avro.types\", \"name\": \"basic\", \"fields\": ["
                + "{\"name\": \"column\", \"type\": " + avroType + "}]}";
    }

    private AvroChecker checker(final String avroSchema, final String exaColumn, final String tableName) {
        return new AvroChecker(avroSchema, exaColumn, tableName);
    }

    private static <T> List<T> listWithNull(final T value) {
        final java.util.ArrayList<T> list = new java.util.ArrayList<>();
        list.add(value);
        list.add(null);
        return list;
    }

    private final class AvroChecker extends AbstractChecker {
        private final Schema avroSchema;

        private AvroChecker(final String avroSchema, final String exaColumn, final String tableName) {
            super(exaColumn, tableName);
            this.avroSchema = new Schema.Parser().parse(avroSchema);
        }

        private AvroChecker withInputValues(final List<?> values) throws IOException {
            WRITER.writeDataValues(values, addFile(), this.avroSchema);
            return this;
        }
    }
}
