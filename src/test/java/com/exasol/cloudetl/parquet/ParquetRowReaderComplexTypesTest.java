package com.exasol.cloudetl.parquet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.MessageTypeParser;
import org.junit.jupiter.api.Test;

class ParquetRowReaderComplexTypesTest extends BaseParquetReaderTest {
    @Test
    void readsArrayOfStringsAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group names (LIST) {\n" +
"    repeated group list {\n" +
"      required binary name (UTF8);\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var names = record.addGroup(0);
            names.addGroup(0).append("name", "John");
            names.addGroup(0).append("name", "Jane");
            writer.write(record);
        });
        assertEquals(row("[\"John\",\"Jane\"]"), getRecords().get(0));
    }

    @Test
    void readsArrayOfIntsAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group ages (LIST) {\n" +
"    repeated group list {\n" +
"      required int32 age;\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var ages = record.addGroup(0);
            ages.addGroup(0).append("age", 3);
            ages.addGroup(0).append("age", 4);
            writer.write(record);
        });
        assertEquals(row("[3,4]"), getRecords().get(0));
    }

    @Test
    void readsArrayOfDoublesAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group prices (LIST) {\n" +
"    repeated group list {\n" +
"      required double price;\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var prices = record.addGroup(0);
            prices.addGroup(0).append("price", 3.14);
            prices.addGroup(0).append("price", 2.71);
            writer.write(record);
        });
        assertEquals(row("[3.14,2.71]"), getRecords().get(0));
    }

    @Test
    void readsNonStandardArrayAsJsonArrayString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group heights (LIST) {\n" +
"    repeated int32 height;\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var prices = record.addGroup(0);
            prices.append("height", 314);
            prices.append("height", 271);
            writer.write(record);
        });
        assertEquals(row("[314,271]"), getRecords().get(0));
    }

    @Test
    void readsRepeatedFieldAsJsonArray() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  repeated binary name (UTF8);\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.add(0, "John");
            record.add(0, "Jane");
            writer.write(record);
        });
        assertEquals(row("[\"John\",\"Jane\"]"), getRecords().get(0));
    }

    @Test
    void readsRepeatedGroupWithSingleElementAsJsonArrayString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  repeated group person {\n" +
"    required binary name (UTF8);\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            var person = record.addGroup(0);
            person.append("name", "John");
            person = record.addGroup(0);
            person.append("name", "Jane");
            writer.write(record);
        });
        assertEquals(row("[\"John\",\"Jane\"]"), getRecords().get(0));
    }

    @Test
    void readsRepeatedGroupManyElementsAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  repeated group person {\n" +
"    required binary name (UTF8);\n" +
"    optional int32 age;\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            var person = record.addGroup(0);
            person.append("name", "John").append("age", 24);
            person = record.addGroup(0);
            person.append("name", "Jane").append("age", 22);
            writer.write(record);
        });
        assertEquals(row("[{\"name\":\"John\",\"age\":24},{\"name\":\"Jane\",\"age\":22}]"), getRecords().get(0));
    }

    @Test
    void readsArrayOfArraysAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group arrays (LIST) {\n" +
"    repeated group list {\n" +
"      required group inner (LIST) {\n" +
"        repeated group list {\n" +
"          required int32 element;\n" +
"        }\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var arrays = record.addGroup(0).addGroup(0);
            var inner = arrays.addGroup("inner");
            inner.addGroup(0).append("element", 1);
            inner.addGroup(0).append("element", 2);
            inner = arrays.addGroup("inner");
            inner.addGroup(0).append("element", 3);
            writer.write(record);
        });
        assertEquals(row("[[1,2],[3]]"), getRecords().get(0));
    }

    @Test
    void readsArrayOfMapsAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group maps (LIST) {\n" +
"    repeated group list {\n" +
"      optional group map (MAP) {\n" +
"        repeated group key_value {\n" +
"          required binary key (UTF8);\n" +
"          optional double price;\n" +
"        }\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var array = record.addGroup(0).addGroup(0);
            var map = array.addGroup("map");
            map.addGroup("key_value").append("key", "key1").append("price", 3.14);
            map.addGroup("key_value").append("key", "key2").append("price", 2.71);
            map = array.addGroup("map");
            map.addGroup("key_value").append("key", "a").append("price", 100.0);
            writer.write(record);
        });
        assertEquals(row("[{\"key1\":3.14,\"key2\":2.71},{\"a\":100.0}]"), getRecords().get(0));
    }

    @Test
    void readsMapAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group map (MAP) {\n" +
"    repeated group key_value {\n" +
"      required binary key (UTF8);\n" +
"      required int64 value;\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var map = record.addGroup(0);
            map.addGroup("key_value").append("key", "key1").append("value", 314L);
            map.addGroup("key_value").append("key", "key2").append("value", 271L);
            writer.write(record);
        });
        assertEquals(row("{\"key1\":314,\"key2\":271}"), getRecords().get(0));
    }

    @Test
    void readsMapWithArrayValuesAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group map (MAP) {\n" +
"    repeated group key_value {\n" +
"      required binary key (UTF8);\n" +
"      optional group prices (LIST) {\n" +
"        repeated group list {\n" +
"          required double price;\n" +
"        }\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var map = record.addGroup(0).addGroup("key_value");
            final var prices = map.append("key", "key1").addGroup("prices");
            prices.addGroup(0).append("price", 3.14);
            prices.addGroup(0).append("price", 2.71);
            writer.write(record);
        });
        assertEquals(row("{\"key1\":[3.14,2.71]}"), getRecords().get(0));
    }

    @Test
    void readsMapWithGroupValuesAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group maps (MAP) {\n" +
"    repeated group key_value {\n" +
"      required binary name (UTF8);\n" +
"      required group values {\n" +
"        optional int32 height;\n" +
"        optional int32 weight;\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var maps = record.addGroup(0);
            var map = maps.addGroup("key_value");
            map.append("name", "John").addGroup("values").append("height", 170).append("weight", 70);
            map = maps.addGroup("key_value");
            map.append("name", "Jane").addGroup("values").append("height", 160).append("weight", 60);
            writer.write(record);
        });
        assertEquals(row("{\"John\":{\"weight\":70,\"height\":170},\"Jane\":{\"weight\":60,\"height\":160}}"),
                getRecords().get(0));
    }

    @Test
    void readsArrayOfRepeatedGroupAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group array (LIST) {\n" +
"    repeated group list {\n" +
"      repeated group values {\n" +
"        required binary key (UTF8);\n" +
"        optional double price;\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var array = record.addGroup(0).addGroup(0);
            array.addGroup("values").append("key", "key1").append("price", 3.14);
            array.addGroup("values").append("key", "key2").append("price", 2.71);
            array.addGroup("values").append("key", "a").append("price", 100.0);
            writer.write(record);
        });
        assertEquals(row("[[{\"price\":3.14,\"key\":\"key1\"},{\"price\":2.71,\"key\":\"key2\"},{\"price\":100.0,\"key\":\"a\"}]]"),
                getRecords().get(0));
    }

    @Test
    void readsMapWithRepeatedGroupValuesAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  optional group maps (MAP) {\n" +
"    repeated group key_value {\n" +
"      required binary name (UTF8);\n" +
"      repeated group values {\n" +
"        required int32 year;\n" +
"        optional int32 height;\n" +
"        optional int32 weight;\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, true), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            final var maps = record.addGroup(0);
            var map = maps.addGroup("key_value");
            map.append("name", "John");
            map.addGroup(1).append("year", 2019).append("height", 170).append("weight", 70);
            map.addGroup(1).append("year", 2020).append("height", 170).append("weight", 80);
            map = maps.addGroup("key_value");
            map.append("name", "Jane");
            map.addGroup(1).append("year", 2019).append("height", 160);
            writer.write(record);
        });
        final String expected = ("{\"John\":[{\"year\":2019,\"weight\":70,\"height\":170},{\"year\":2020,\"weight\":80,\"height\":170}],\n" +
"\"Jane\":[{\"year\":2019,\"weight\":null,\"height\":160}]}\n" +
"\n").replaceAll("\n", "");
        assertEquals(row(expected), getRecords().get(0));
    }

    @Test
    void readsGroupAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary name (UTF8);\n" +
"  optional group contacts {\n" +
"    required binary name (UTF8);\n" +
"    optional binary phoneNumber (UTF8);\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.add(0, "John");
            record.addGroup(1).append("name", "Jane").append("phoneNumber", "1337");
            writer.write(record);
        });
        assertEquals(row("John", "{\"phoneNumber\":\"1337\",\"name\":\"Jane\"}"), getRecords().get(0));
    }

    @Test
    void readsGroupWithRepeatedGroupAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary name (UTF8);\n" +
"  optional group contacts {\n" +
"    repeated group person {\n" +
"      required binary name (UTF8);\n" +
"      optional binary phoneNumber (UTF8);\n" +
"    }\n" +
"    optional int32 count;\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.add(0, "John");
            final var contacts = record.addGroup(1);
            contacts.addGroup(0).append("name", "Jane").append("phoneNumber", "1337");
            contacts.addGroup(0).append("name", "Jake");
            contacts.append("count", 2);
            writer.write(record);
        });
        final String expected = ("{\"person\":\n" +
"  [\n" +
"    {\"phoneNumber\":\"1337\",\"name\":\"Jane\"},\n" +
"    {\"phoneNumber\":null,\"name\":\"Jake\"}\n" +
"  ],\n" +
"\"count\":2\n" +
"}\n" +
"\n").replaceAll("\\s+", "");
        assertEquals(row("John", expected), getRecords().get(0));
    }

    @Test
    void readsNestedGroupsAsJsonString() throws Exception {
        final var schema = MessageTypeParser.parseMessageType(("message test {\n" +
"  required binary name (UTF8);\n" +
"  optional group contacts (MAP) {\n" +
"    repeated group key_value {\n" +
"      required binary name (UTF8);\n" +
"      optional group numbers (LIST) {\n" +
"        repeated group list {\n" +
"          optional binary phoneNumber (UTF8);\n" +
"        }\n" +
"      }\n" +
"    }\n" +
"  }\n" +
"}\n" +
"\n"));
        withResource(getParquetWriter(schema, false), writer -> {
            final SimpleGroup record = new SimpleGroup(schema);
            record.add(0, "John");
            final var phoneNumbers = record.addGroup(1).addGroup(0).append("name", "Jane").addGroup("numbers");
            phoneNumbers.addGroup(0).append("phoneNumber", "1337");
            writer.write(record);
        });
        assertEquals(row("John", "{\"Jane\":[\"1337\"]}"), getRecords().get(0));
    }
}
