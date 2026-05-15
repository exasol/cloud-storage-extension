package com.exasol.cloudetl.orc;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.matcher.TypeMatchMode.NO_JAVA_TYPE_CHECK;
import static com.exasol.matcher.TypeMatchMode.STRICT;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.*;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.BaseDataImporter;
import com.exasol.matcher.CellMatcherFactory;

class OrcDataImporterIT extends BaseDataImporter {
    private static final OrcTestDataWriter WRITER = new OrcTestDataWriter();
    private final Configuration conf = new Configuration();

    @Override
    protected String schemaName() {
        return "ORC_SCHEMA";
    }

    @Override
    protected String bucketName() {
        return "orc-bucket";
    }

    @Override
    protected String dataFormat() {
        return "orc";
    }

    @Test
    void importsPrimitiveTypes() throws IOException, SQLException {
        checker("struct<f:boolean>", "BOOLEAN", "boolean_table").withInputValues(values(Boolean.TRUE, Boolean.FALSE, (Object) null))
                .assertResultSet(table().row(Boolean.TRUE).row(Boolean.FALSE).row((Object) null).matches());
        checker("struct<f:tinyint>", "DECIMAL(3,0)", "byte_table").withInputValues(values(Byte.valueOf("11"), (Object) null))
                .assertResultSet(table().row(Byte.valueOf("11")).row((Object) null).matches(NO_JAVA_TYPE_CHECK));
        checker("struct<f:smallint>", "DECIMAL(9,0)", "short_table").withInputValues(values(Short.valueOf("13"), (Object) null))
                .assertResultSet(table().row(Short.valueOf("13")).row((Object) null).matches(NO_JAVA_TYPE_CHECK));
        checker("struct<f:int>", "DECIMAL(10,0)", "int_table")
                .withInputValues(values(Integer.valueOf(this.intMin), Integer.valueOf(999), (Object) null, Integer.valueOf(this.intMax)))
                .assertResultSet(table().row(Integer.valueOf(this.intMin)).row(Integer.valueOf(999)).row((Object) null)
                        .row(Integer.valueOf(this.intMax)).matches(NO_JAVA_TYPE_CHECK));
        checker("struct<f:bigint>", "DECIMAL(19,0)", "long_table")
                .withInputValues(values(Long.valueOf(this.longMin), Long.valueOf(1234), (Object) null, Long.valueOf(this.longMax)))
                .assertResultSet(table().row(Long.valueOf(this.longMin)).row(Long.valueOf(1234)).row((Object) null)
                        .row(Long.valueOf(this.longMax)).matches(NO_JAVA_TYPE_CHECK));
        final BigDecimal eps = BigDecimal.valueOf(0.0001);
        checker("struct<f:float>", "FLOAT", "float_table").withInputValues(values(Float.valueOf(3.14f), (Object) null))
                .assertResultSet(table().row(CellMatcherFactory.cellMatcher(3.14, STRICT, eps)).row((Object) null).matches());
        checker("struct<f:double>", "DOUBLE", "double_table").withInputValues(values(Double.valueOf(2.71), (Object) null))
                .assertResultSet(table().row(Double.valueOf(2.71)).row((Object) null).matches());
    }

    @Test
    void importsTextAndDecimalTypes() throws IOException, SQLException {
        checker("struct<f:char(3)>", "VARCHAR(3)", "char_table").withInputValues(values("a✅a", (Object) null))
                .assertResultSet(table().row("a✅a").row((Object) null).matches());
        checker("struct<f:varchar(5)>", "VARCHAR(5)", "varchar_table").withInputValues(values("hello", "world", (Object) null))
                .assertResultSet(table().row("hello").row("world").row((Object) null).matches());
        checker("struct<f:string>", "VARCHAR(10)", "binary_table").withInputValues(values("中文", (Object) null))
                .assertResultSet(table().row("中文").row((Object) null).matches());
        checker("struct<f:string>", "VARCHAR(10)", "string_table").withInputValues(values("value", (Object) null))
                .assertResultSet(table().row("value").row((Object) null).matches());
        checker("struct<f:decimal(6,3)>", "DECIMAL(6,3)", "decimal_table")
                .withInputValues(values("333.333", "0.666", (Object) null)).assertResultSet(table()
                        .row(Double.valueOf(333.333)).row(Double.valueOf(0.666)).row((Object) null).matches(NO_JAVA_TYPE_CHECK));
    }

    @Test
    void importsDateAndTimestamp() throws IOException, SQLException {
        checker("struct<f:date>", "DATE", "date_table").withInputValues(values(Integer.valueOf(0), Integer.valueOf(1), (Object) null))
                .assertResultSet(table().row(java.sql.Date.valueOf("1970-01-01")).row(java.sql.Date.valueOf("1970-01-02")).row((Object) null)
                        .matches());
        final long millis1 = Instant.EPOCH.toEpochMilli();
        final long millis2 = System.currentTimeMillis();
        final Timestamp expectedTimestamp1 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        final Timestamp expectedTimestamp2 = Timestamp
                .valueOf(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.of("Europe/Berlin")).toLocalDateTime());
        checker("struct<f:timestamp>", "TIMESTAMP", "timestamp_table")
                .withInputValues(values(new Timestamp(millis1), new Timestamp(millis2), (Object) null))
                .assertResultSet(table().row(expectedTimestamp1).row(expectedTimestamp2).row((Object) null).matches());
    }

    @Test
    void importsComplexTypes() throws IOException, SQLException {
        checker("struct<f:array<string>>", "VARCHAR(30)", "list_strings")
                .withInputValues(values(values("Brandon", "Eddard", (Object) null)))
                .assertResultSet(table().row("[\"Brandon\",\"Eddard\",null]").matches());
        checker("struct<f:array<double>>", "VARCHAR(20)", "list_doubles")
                .withInputValues(values(values(Double.valueOf(1.11), Double.valueOf(2.22), (Object) null)))
                .assertResultSet(table().row("[1.11,2.22,null]").matches());
        checker("struct<f:array<int>>", "VARCHAR(10)", "list_ints").withInputValues(values(List.of(3, 7, 1)))
                .assertResultSet(table().row("[3,7,1]").matches());
        checker("struct<f:array<string>>", "VARCHAR(10)", "empty_list").withInputValues(values((Object) null))
                .assertResultSet(table().row("[]").matches());
        checker("struct<f:array<array<int>>>", "VARCHAR(30)", "list_lists")
                .withInputValues(values(List.of(List.of(314, 271), List.of(1, 2, 4))))
                .assertResultSet(table().row("[[314,271],[1,2,4]]").matches());
        checker("struct<f:array<map<string,int>>>", "VARCHAR(30)", "list_map")
                .withInputValues(values(List.of(mapOf("p", 314), mapOf("a", 1, "b", 2))))
                .assertResultSet(table().row("[{\"p\":314},{\"a\":1,\"b\":2}]").matches());
        checker("struct<f:map<string,int>>", "VARCHAR(30)", "map_table")
                .withInputValues(values(mapOf("a", 3, "b", 5, "c", 7)))
                .assertResultSet(table().row("{\"a\":3,\"b\":5,\"c\":7}").matches());
        checker("struct<f:map<string,array<double>>>", "VARCHAR(40)", "map_list_values")
                .withInputValues(values(mapOf("consts", List.of(3.14, 2.71), "nums", List.of(1.0, 0.5))))
                .assertResultSet(table().row("{\"nums\":[1.0,0.5],\"consts\":[3.14,2.71]}").matches());
        checker("struct<f:struct<name:string,phoneNumber:int,pets:array<string>>>", "VARCHAR(60)", "nested_struct_table")
                .withInputValues(values(mapOf("name", "Jon", "phoneNumber", 1337, "pets", List.of("cat", "direwolf"))))
                .assertResultSet(table().row("{\"pets\":[\"cat\",\"direwolf\"],\"phoneNumber\":1337,\"name\":\"Jon\"}")
                        .matches());
    }

    @Test
    void importsUnion() throws IOException, SQLException {
        final String orcType = "struct<f:uniontype<int,string>>";
        final TypeDescription orcSchema = TypeDescription.fromString(orcType);
        final org.apache.hadoop.fs.Path path = addFile();
        try (Writer writer = OrcFile.createWriter(path, OrcFile.writerOptions(this.conf).setSchema(orcSchema))) {
            final VectorizedRowBatch batch = orcSchema.createRowBatch();
            batch.size = 3;
            final UnionColumnVector unionVector = (UnionColumnVector) batch.cols[0];
            unionVector.noNulls = false;
            unionVector.tags[1] = 0;
            ((BytesColumnVector) unionVector.fields[1]).setVal(0, "str".getBytes(StandardCharsets.UTF_8));
            unionVector.tags[0] = 1;
            ((LongColumnVector) unionVector.fields[0]).vector[1] = 23;
            unionVector.isNull[2] = true;
            writer.addRowBatch(batch);
        }
        checker(orcType, "VARCHAR(30)", "union_table").assertResultSet(table().row("{\"STRING\":\"str\",\"INT\":null}")
                .row("{\"STRING\":null,\"INT\":23}").row("{\"STRING\":null,\"INT\":null}").matches());
    }

    private OrcChecker checker(final String orcColumn, final String exaColumn, final String tableName) {
        return new OrcChecker(orcColumn, exaColumn, tableName);
    }

    private static List<Object> values(final Object... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    private static Map<String, Object> mapOf(final Object... values) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (int index = 0; index < values.length; index += 2) {
            result.put((String) values[index], values[index + 1]);
        }
        return result;
    }

    private final class OrcChecker extends AbstractChecker {
        private final TypeDescription orcSchema;

        private OrcChecker(final String orcColumn, final String exaColumn, final String tableName) {
            super(exaColumn, tableName);
            this.orcSchema = TypeDescription.fromString(orcColumn);
        }

        private OrcChecker withInputValues(final List<?> values) throws IOException {
            WRITER.writeDataValues(values, addFile(), this.orcSchema);
            return this;
        }

        @Override
        public void assertResultSet(final Matcher<ResultSet> matcher) throws SQLException {
            super.assertResultSet(matcher);
        }
    }
}
