package com.exasol.cloudetl.util;

import static org.apache.parquet.schema.LogicalTypeAnnotation.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.parquet.schema.*;
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName;
import org.apache.parquet.schema.Type.Repetition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.cloudetl.helper.ParquetSchemaConverter;

class ParquetSchemaConverterTest {
    @Test
    void createParquetMessageTypeThrowsIfTypeIsUnknown() {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(false);
        final var columns = ScalaConverters
                .seqFromJava(List.of(new ExaColumnInfo("c_short", Short.class, 0, 0, 0, false)));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> converter.createParquetMessageType(columns, "test_schema"));
        assertTrue(thrown.getMessage().startsWith("F-CSE-22"));
        assertTrue(thrown.getMessage().contains("Exasol type 'class java.lang.Short' to Parquet type."));
    }

    @Test
    void createParquetMessageTypeReturnsParquetSchemaFromExasolColumns() {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(true);
        final var columns = ScalaConverters.seqFromJava(List.of(//
                new ExaColumnInfo("c_int", Integer.class, 0, 0, 0, true), //
                new ExaColumnInfo("c_int", Integer.class, 1, 0, 0, true), //
                new ExaColumnInfo("c_int", Integer.class, 9, 0, 0, true), //
                new ExaColumnInfo("c_long", Long.class, 0, 0, 0, false), //
                new ExaColumnInfo("c_long", Long.class, 18, 0, 0, true), //
                new ExaColumnInfo("c_decimal_int", java.math.BigDecimal.class, 9, 0, 0, false), //
                new ExaColumnInfo("c_decimal_long", java.math.BigDecimal.class, 17, 0, 0, false), //
                new ExaColumnInfo("c_decimal", java.math.BigDecimal.class, 38, 10, 16, false), //
                new ExaColumnInfo("c_double", Double.class, 0, 0, 0, true), //
                new ExaColumnInfo("c_string", String.class, 0, 0, 0, false), //
                new ExaColumnInfo("c_string", String.class, 0, 0, 20, false), //
                new ExaColumnInfo("c_boolean", Boolean.class, 0, 0, 0, false), //
                new ExaColumnInfo("c_date", java.sql.Date.class, 0, 0, 0, false), //
                new ExaColumnInfo("c_timestamp", java.sql.Timestamp.class, 0, 0, 0, false)));
        final String schemaName = "exasol_export_schema";
        final MessageType expectedMessageType = new MessageType(schemaName, //
                new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.INT32, "c_int"), //
                Types.primitive(PrimitiveTypeName.INT32, Repetition.OPTIONAL).as(decimalType(0, 1)).named("c_int"), //
                Types.primitive(PrimitiveTypeName.INT32, Repetition.OPTIONAL).as(decimalType(0, 9)).named("c_int"), //
                new PrimitiveType(Repetition.REQUIRED, PrimitiveTypeName.INT64, "c_long"), //
                Types.primitive(PrimitiveTypeName.INT64, Repetition.OPTIONAL).as(decimalType(0, 18)).named("c_long"), //
                Types.primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED).length(4)
                        .as(decimalType(0, 9)).named("c_decimal_int"), //
                Types.primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED).length(8)
                        .as(decimalType(0, 17)).named("c_decimal_long"), //
                Types.primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, Repetition.REQUIRED).length(16)
                        .as(decimalType(10, 38)).named("c_decimal"), //
                new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.DOUBLE, "c_double"), //
                Types.primitive(PrimitiveTypeName.BINARY, Repetition.REQUIRED).as(stringType()).named("c_string"), //
                Types.primitive(PrimitiveTypeName.BINARY, Repetition.REQUIRED).length(20).as(stringType())
                        .named("c_string"), //
                new PrimitiveType(Repetition.REQUIRED, PrimitiveTypeName.BOOLEAN, "c_boolean"), //
                Types.primitive(PrimitiveTypeName.INT32, Repetition.REQUIRED).as(dateType()).named("c_date"), //
                new PrimitiveType(Repetition.REQUIRED, PrimitiveTypeName.INT96, "c_timestamp"));
        assertEquals(expectedMessageType, converter.createParquetMessageType(columns, schemaName));
    }

    @Test
    void createParquetMessageTypeThrowsIfIntegerPrecisionIsLargerThanAllowed() {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(false);
        final var columns = ScalaConverters.seqFromJava(List.of(new ExaColumnInfo("c_int", Integer.class, 10, 0, 0, true)));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> converter.createParquetMessageType(columns, "test"));
        assertEquals("requirement failed: Got an 'Integer' type with more than '9' precision.", thrown.getMessage());
    }

    @Test
    void createParquetMessageTypeThrowsIfLongPrecisionIsLargerThanAllowed() {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(false);
        final var columns = ScalaConverters.seqFromJava(List.of(new ExaColumnInfo("c_long", Long.class, 20, 0, 0, true)));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> converter.createParquetMessageType(columns, "test"));
        assertEquals("requirement failed: Got a 'Long' type with more than '18' precision.", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void createParquetMessageTypeRemovesQuotesFromFieldNames(final boolean lowercase) {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(lowercase);
        final var columns = ScalaConverters.seqFromJava(List.of(//
                new ExaColumnInfo("\"column int\"", Integer.class, 9, 0, 0, true), //
                new ExaColumnInfo("\"c_long\"", Long.class, 12, 0, 0, true)));
        final MessageType schema = converter.createParquetMessageType(columns, "test");
        assertEquals("column int", schema.getFieldName(0));
        assertEquals("c_long", schema.getFieldName(1));
    }

    @ParameterizedTest
    @CsvSource({ "true, c_long", "false, c_LONG" })
    void createParquetMessageTypeConvertsFieldNamesToLowercaseWhenEnabled(final boolean lowercase,
            final String expectedFieldName) {
        final ParquetSchemaConverter converter = new ParquetSchemaConverter(lowercase);
        final var columns = ScalaConverters
                .seqFromJava(List.of(new ExaColumnInfo("\"c_LONG\"", Long.class, 12, 0, 0, true)));
        final MessageType schema = converter.createParquetMessageType(columns, "test");
        assertEquals(expectedFieldName, schema.getFieldName(0));
    }
}
