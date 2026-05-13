package com.exasol.cloudetl.orc.converter;

import static org.apache.orc.TypeDescription.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class OrcConverterPrimitiveTypesTest extends BaseOrcConverterTest {
    @Test
    void readsBooleanAsLongValue() throws Exception {
        write(createStruct().addField("boolean", createBoolean()), Arrays.asList(true, false, null));
        final var records = getRecords();
        assertEquals(true, records.get(0).get(0));
        assertEquals(false, records.get(1).get(0));
        assertTrue(records.get(2).isNullAt(0));
    }

    @Test
    void readsByteAsByteValue() throws Exception {
        write(createStruct().addField("byte", createByte()), Arrays.asList(13, null));
        assertEquals((byte) 13, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsShortAsShortValue() throws Exception {
        write(createStruct().addField("short", createShort()), Arrays.asList(314, null));
        assertEquals((short) 314, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsIntAsIntegerValue() throws Exception {
        write(createStruct().addField("int", createInt()), Arrays.asList(314, null));
        assertEquals(314, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsLongAsLongValue() throws Exception {
        write(createStruct().addField("long", createLong()), Arrays.asList(3L, null));
        assertEquals(3L, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsFloatAsFloatValue() throws Exception {
        write(createStruct().addField("float", createFloat()), Arrays.asList(3.14f, null));
        assertEquals(3.14f, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsDoubleAsDoubleValue() throws Exception {
        write(createStruct().addField("double", createDouble()), Arrays.asList(2.71, null));
        assertEquals(2.71, getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsDecimalValueAsJavaMathDecimal() throws Exception {
        write(createStruct().addField("decimal", createDecimal()), Arrays.asList("173.433", null));
        final var records = getRecords();
        assertInstanceOf(java.math.BigDecimal.class, records.get(0).get(0));
        assertEquals(173.433, ((java.math.BigDecimal) records.get(0).get(0)).doubleValue());
        assertTrue(records.get(1).isNullAt(0));
    }

    @Test
    void readsDateAsJavaSqlDateValue() throws Exception {
        write(createStruct().addField("date", createDate()), Arrays.asList(0, 1, null));
        final var records = getRecords();
        assertEquals(java.sql.Date.valueOf("1970-01-01"), records.get(0).get(0));
        assertEquals(java.sql.Date.valueOf("1970-01-02"), records.get(1).get(0));
        assertTrue(records.get(2).isNullAt(0));
    }

    @Test
    void readsTimestampAsJavaSqlTimestampValue() throws Exception {
        final java.sql.Timestamp timestamp1 = java.sql.Timestamp.from(java.time.Instant.EPOCH);
        final java.sql.Timestamp timestamp2 = new java.sql.Timestamp(System.currentTimeMillis());
        write(createStruct().addField("timestamp", createTimestamp()), Arrays.asList(timestamp1, timestamp2, null));
        final var records = getRecords();
        assertEquals(timestamp1, records.get(0).get(0));
        assertEquals(timestamp2, records.get(1).get(0));
        assertTrue(records.get(2).isNullAt(0));
    }

    @Test
    void readsCharAsStringValue() throws Exception {
        write(createStruct().addField("string", createChar()), Arrays.asList("value", null));
        assertEquals("value", getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsStringAsStringValue() throws Exception {
        write(createStruct().addField("string", createString()), Arrays.asList("välue", null));
        assertEquals("välue", getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsVarcharAsStringValue() throws Exception {
        write(createStruct().addField("string", createVarchar()), Arrays.asList("smiley ☺", null));
        assertEquals("smiley ☺", getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }

    @Test
    void readsBinaryAsStringValue() throws Exception {
        write(createStruct().addField("string", createBinary()), Arrays.asList("str", null));
        assertEquals("str", getRecords().get(0).get(0));
        assertTrue(getRecords().get(1).isNullAt(0));
    }
}
