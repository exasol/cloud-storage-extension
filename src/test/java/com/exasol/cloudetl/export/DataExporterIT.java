package com.exasol.cloudetl.export;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.BaseS3IntegrationTest;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.matcher.TypeMatchMode;

class DataExporterIT extends BaseS3IntegrationTest {
    private static final String SCHEMA_NAME = "DATA_SCHEMA";

    @BeforeAll
    void beforeAll() {
        prepareExasolDatabase(SCHEMA_NAME);
        createS3ConnectionObject();
    }

    @Test
    void exportsAndImportsBoolean() throws SQLException {
        new ExportImportChecker(mapOf("C_BOOLEAN", "BOOLEAN"),
                List.of(row(1L, (Object) null), row(2L, Boolean.TRUE), row(3L, Boolean.FALSE)), "boolean-bucket").assertRows();
    }

    @Test
    void exportsAndImportsVarchar() throws SQLException {
        new ExportImportChecker(mapOf("NAME", "VARCHAR(40)"), List.of(row(1L, "Cat"), row(2L, "Dog")),
                "varchar-bucket").assertRows();
    }

    @Test
    void exportsAndImportsCharacter() throws SQLException {
        new ExportImportChecker(mapOf("C_CHAR20", "CHAR(20)"),
                List.of(row(1L, (Object) null), row(2L, "foo                 "), row(3L, "0123456789abcdefghij")),
                "character-bucket").assertRows();
    }

    @Test
    void exportsAndImportsNumeric() throws SQLException {
        new ExportImportChecker(mapOf("C_DECIMAL", "DECIMAL(3,2)", "C_DOUBLE", "DOUBLE PRECISION"),
                List.of(row(1L, (Object) null, (Object) null), row(2L, 1.23, 3.14159265358979323846264338327950288),
                        row(3L, 0.0, 0.0), row(4L, -9.99, -5.555555555555),
                        row(5L, -1.11, -111.11111111111), row(6L, 9.99, 9.9999999999999),
                        row(7L, -9.99, -9.9999999999999)),
                "numeric-bucket").assertRows(TypeMatchMode.NO_JAVA_TYPE_CHECK);
    }

    @Test
    void exportsAndImportsNumericMinimumAndMaximum() throws SQLException {
        new ExportImportChecker(mapOf("C_INT", "INTEGER", "C_LONG", "BIGINT"),
                List.of(row(1L, (Object) null, (Object) null), row(2L, Integer.valueOf(this.intMin), Long.valueOf(this.longMin)),
                        row(3L, Integer.valueOf(this.intMax), Long.valueOf(this.longMax))),
                "numeric-min-max-bucket").assertRows(TypeMatchMode.NO_JAVA_TYPE_CHECK);
    }

    @Test
    void exportsAndImportsNumericAlias() throws SQLException {
        new ExportImportChecker(
                mapOf("C_INTEGER", "INTEGER", "C_DOUBLE", "DOUBLE", "C_FLOAT", "FLOAT", "C_SHORTINT", "SHORTINT",
                        "C_SMALLINT", "SMALLINT", "C_TINYINT", "TINYINT"),
                List.of(row(1L, (Object) null, (Object) null, (Object) null, (Object) null, (Object) null, (Object) null), row(2L, 100, 3.1415, 1.0f, 7, 12, 5)),
                "numeric-alias-bucket").assertRows(TypeMatchMode.NO_JAVA_TYPE_CHECK);
    }

    @Test
    void exportsAndImportsDateTimestamp() throws SQLException {
        new ExportImportChecker(mapOf("C_DATE", "DATE", "C_TIMESTAMP", "TIMESTAMP"),
                List.of(row(1L, java.sql.Date.valueOf("0001-01-01"), Timestamp.valueOf("0001-01-01 01:01:01.0")),
                        row(2L, java.sql.Date.valueOf("1970-01-01"), Timestamp.valueOf("2001-01-01 01:01:01")),
                        row(3L, java.sql.Date.valueOf("9999-12-31"), Timestamp.valueOf("9999-12-31 23:59:59"))),
                "date-timestamp-bucket").assertRows();
    }

    @Test
    void exportsAndImportsIdentifierCases() throws SQLException {
        new ExportImportChecker(
                mapOf("C_VARCHAR_mixedcase", "VARCHAR(20)", "C_VARCHAR_Mixed_Case", "VARCHAR(20)",
                        "C_VARCHAR_REGULAR", "VARCHAR(50)"),
                List.of(row(1L, "Quoted, lower case", "Quoted, mixed case", "Not quoted, automatically turned to upper case"),
                        row(2L, "Cats", "Dogs", "Ducks")),
                "delimited-bucket").assertRows();
    }

    @Test
    void exportsAndImportsDecimalsWithTrailingZeros() throws SQLException {
        new ExportImportChecker(mapOf("C_DECIMAL", "DECIMAL(18, 4)"), List.<Object[]>of(row(1L, 238316.38)),
                "decimal-trailing-zeros-bucket").assertWithMatcher(
                        table().row(Long.valueOf(1), java.math.BigDecimal.valueOf(238316.3800)).matches());
    }

    @Test
    void exportsAndImportsFromView() throws SQLException {
        final String bucket = "decimal-cast-view";
        final Table importTable = this.schema.createTable("DECIMAL_IMPORT_TABLE", "ID", "DECIMAL(18,0)", "C_DECIMAL",
                "DECIMAL(18,2)");
        final Table decimalTable = this.schema
                .createTable("DECIMAL_EXPORT_TABLE", "ID", "DECIMAL(18,0)", "C_DECIMAL", "DECIMAL(18,2)")
                .insert("1", Double.valueOf(238316.38));
        final String viewName = SCHEMA_NAME + ".DECIMAL_TABLE_V";
        executeStmt("CREATE OR REPLACE VIEW " + viewName + "\n"
                + "AS SELECT\n"
                + "   ID AS ID,\n"
                + "   CAST(C_DECIMAL AS DECIMAL(18,4)) AS C_DECIMAL\n"
                + "FROM " + decimalTable.getFullyQualifiedName());

        createBucket(bucket);
        exportIntoS3(SCHEMA_NAME, viewName, bucket);
        importFromS3IntoExasol(SCHEMA_NAME, importTable, bucket, "*", "PARQUET");

        try (ResultSet resultSet = executeQuery("SELECT * FROM " + importTable.getFullyQualifiedName())) {
            assertThat(resultSet, table().row(Long.valueOf(1), Double.valueOf(238316.3800))
                    .matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    private static LinkedHashMap<String, String> mapOf(final String... values) {
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (int index = 0; index < values.length; index += 2) {
            result.put(values[index], values[index + 1]);
        }
        return result;
    }

    private static Object[] row(final Object... values) {
        return values;
    }

    private final class ExportImportChecker {
        private final LinkedHashMap<String, String> columns;
        private final List<Object[]> tableValues;
        private final String bucket;
        private final Table exportTable;
        private final Table importTable;

        private ExportImportChecker(final LinkedHashMap<String, String> columns, final List<Object[]> tableValues,
                final String bucket) {
            this.columns = columns;
            this.tableValues = tableValues;
            this.bucket = bucket;
            this.exportTable = createTable(getTableName("EXPORT"));
            for (final Object[] rows : tableValues) {
                this.exportTable.insert(rows);
            }
            this.importTable = createTable(getTableName("IMPORT"));
        }

        private Table createTable(final String name) {
            final var builder = DataExporterIT.this.schema.createTableBuilder(name).column("ID", "DECIMAL(18,0)");
            this.columns.forEach(builder::column);
            return builder.build();
        }

        private String getTableName(final String suffix) {
            return this.bucket.replace("bucket", suffix).replace("-", "_").toUpperCase(Locale.ENGLISH);
        }

        private Matcher<ResultSet> getMatcher(final TypeMatchMode typeMatchMode) {
            var matcher = table();
            for (final Object[] rows : this.tableValues) {
                matcher = matcher.row(rows);
            }
            return matcher.matches(typeMatchMode);
        }

        private void withResultSet(final ResultSetConsumer block) throws SQLException {
            try (ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + this.importTable.getFullyQualifiedName() + " ORDER BY ID ASC")) {
                block.accept(resultSet);
            }
        }

        private void exportAndImport() {
            createBucket(this.bucket);
            exportIntoS3(SCHEMA_NAME, this.exportTable.getFullyQualifiedName(), this.bucket);
            importFromS3IntoExasol(SCHEMA_NAME, this.importTable, this.bucket, "*", "PARQUET");
        }

        private void assertRows() throws SQLException {
            assertRows(TypeMatchMode.STRICT);
        }

        private void assertRows(final TypeMatchMode typeMatchMode) throws SQLException {
            exportAndImport();
            withResultSet(resultSet -> assertThat(resultSet, getMatcher(typeMatchMode)));
        }

        private void assertWithMatcher(final Matcher<ResultSet> matcher) throws SQLException {
            exportAndImport();
            withResultSet(resultSet -> assertThat(resultSet, matcher));
        }
    }

    @FunctionalInterface
    private interface ResultSetConsumer {
        void accept(ResultSet resultSet) throws SQLException;
    }
}
