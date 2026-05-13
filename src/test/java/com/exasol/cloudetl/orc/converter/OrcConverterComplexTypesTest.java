package com.exasol.cloudetl.orc.converter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.orc.TypeDescription.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.OrcFile;
import org.junit.jupiter.api.Test;

class OrcConverterComplexTypesTest extends BaseOrcConverterTest {
    @Test
    void readsListOfStringsAsJsonString() throws Exception {
        write(createStruct().addField("column", createList(createString())), List.of(Arrays.asList("John", "Jana", null)));
        assertEquals(row("[\"John\",\"Jana\",null]"), getRecords().get(0));
    }

    @Test
    void readsListOfDoublesAsJsonString() throws Exception {
        write(createStruct().addField("column", createList(createDouble())), List.of(List.of(3.14, 2.71)));
        assertEquals(row("[3.14,2.71]"), getRecords().get(0));
    }

    @Test
    void readsNullListAsJsonString() throws Exception {
        write(createStruct().addField("column", createList(createDouble())), Arrays.asList((Object) null));
        assertEquals(row("[]"), getRecords().get(0));
    }

    @Test
    void readsListOfListIntsAsJsonString() throws Exception {
        final var list = createList(createList(createInt()));
        write(createStruct().addField("column", list), List.of(List.of(List.of(314, 271), List.of(1, 2, 3))));
        assertEquals(row("[[314,271],[1,2,3]]"), getRecords().get(0));
    }

    @Test
    void readsListOfMapsAsJsonString() throws Exception {
        final var inner = createList(createMap(createString(), createInt()));
        write(createStruct().addField("column", inner), List.of(List.of(Map.of("pi", 314), orderedMap("a", 1, "c", 3))));
        assertEquals(row("[{\"pi\":314},{\"a\":1,\"c\":3}]"), getRecords().get(0));
    }

    @Test
    void readsMapAsJsonString() throws Exception {
        write(createStruct().addField("column", createMap(createString(), createInt())), List.of(orderedMap("a", 1, "b", 2, "c", 3)));
        assertEquals(row("{\"a\":1,\"b\":2,\"c\":3}"), getRecords().get(0));
    }

    @Test
    void readsMapWithListValuesAsJsonString() throws Exception {
        final var inner = createMap(createString(), createList(createDouble()));
        write(createStruct().addField("column", inner),
                List.of(orderedMap("consts", List.of(3.14, 2.71), "nums", List.of(1.0, 0.5))));
        assertEquals(row("{\"nums\":[1.0,0.5],\"consts\":[3.14,2.71]}"), getRecords().get(0));
    }

    @Test
    void readsNestedStructAsJsonString() throws Exception {
        final var inner = createStruct().addField("name", createString()).addField("phoneNumber", createInt())
                .addField("pets", createList(createString()));
        write(createStruct().addField("column", inner),
                List.of(orderedMap("name", "John", "phoneNumber", 1337, "pets", List.of("cat", "dog"))));
        assertEquals(row("{\"pets\":[\"cat\",\"dog\"],\"phoneNumber\":1337,\"name\":\"John\"}"), getRecords().get(0));
    }

    @Test
    void readsUnionAsJsonString() throws Exception {
        final var schema = createStruct().addField("column", createUnion().addUnionChild(createInt()).addUnionChild(createString()));
        final var writer = OrcFile.createWriter(this.path, OrcFile.writerOptions(this.conf).setSchema(schema));
        final var batch = schema.createRowBatch();
        batch.size = 3;

        final UnionColumnVector unionVector = (UnionColumnVector) batch.cols[0];
        unionVector.noNulls = false;
        unionVector.tags[0] = 0;
        ((LongColumnVector) unionVector.fields[0]).vector[0] = 23;
        unionVector.tags[1] = 1;
        ((BytesColumnVector) unionVector.fields[1]).setVal(1, "str".getBytes(UTF_8));
        unionVector.isNull[2] = true;
        writer.addRowBatch(batch);
        writer.close();

        final var records = getRecords();
        assertEquals(row("{\"STRING\":null,\"INT\":23}"), records.get(0));
        assertEquals(row("{\"STRING\":\"str\",\"INT\":null}"), records.get(1));
        assertEquals(row("{\"STRING\":null,\"INT\":null}"), records.get(2));
    }

    private static Map<String, Object> orderedMap(final Object... keyValues) {
        final Map<String, Object> map = new LinkedHashMap<>();
        for (int index = 0; index < keyValues.length; index += 2) {
            map.put((String) keyValues[index], keyValues[index + 1]);
        }
        return map;
    }
}
