package com.exasol.cloudetl;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import com.exasol.dbbuilder.dialects.Table;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseDataImporter extends BaseS3IntegrationTest {
    protected abstract String schemaName();

    protected abstract String bucketName();

    protected abstract String dataFormat();

    protected Path outputDirectory;
    protected final List<org.apache.hadoop.fs.Path> paths = new ArrayList<>();
    protected final String baseFileName = "part-";

    @BeforeEach
    void baseDataImporterBeforeEach() throws IOException {
        this.outputDirectory = TestFileManager.createTemporaryFolder(dataFormat() + "-tests-");
    }

    @AfterEach
    void baseDataImporterAfterEach() throws IOException {
        this.paths.clear();
        TestFileManager.deletePathFiles(this.outputDirectory);
    }

    @BeforeAll
    void baseDataImporterBeforeAll() {
        createBucket(bucketName());
        prepareExasolDatabase(schemaName());
        createS3ConnectionObject();
    }

    @AfterAll
    void baseDataImporterAfterAll() {
        executeStmt("DROP SCHEMA IF EXISTS " + schemaName() + " CASCADE;");
    }

    protected org.apache.hadoop.fs.Path addFile() {
        final String fileCounter = String.format("%04d", this.paths.size());
        final org.apache.hadoop.fs.Path newPath = new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(),
                this.baseFileName + fileCounter + "." + dataFormat());
        this.paths.add(newPath);
        return newPath;
    }

    protected abstract class AbstractChecker extends AbstractMultiColChecker {
        protected AbstractChecker(final String exaColumnType, final String tableName) {
            super(Map.of("COLUMN", exaColumnType), tableName);
        }
    }

    protected abstract class AbstractMultiColChecker {
        private final Map<String, String> columns;
        private final String tableName;

        protected AbstractMultiColChecker(final Map<String, String> columns, final String tableName) {
            this.columns = columns;
            this.tableName = tableName;
        }

        protected AbstractMultiColChecker withResultSet(final ResultSetConsumer block) throws SQLException {
            BaseDataImporter.this.paths.forEach(path -> uploadFileToS3(bucketName(), path));
            final var tableBuilder = BaseDataImporter.this.schema.createTableBuilder(this.tableName.toUpperCase(Locale.ENGLISH));
            this.columns.forEach(tableBuilder::column);
            final Table table = tableBuilder.build();
            importFromS3IntoExasol(schemaName(), table, bucketName(), BaseDataImporter.this.baseFileName + "*", dataFormat());
            final ResultSet resultSet = executeQuery("SELECT * FROM " + table.getFullyQualifiedName());
            try {
                block.accept(resultSet);
            } finally {
                resultSet.close();
            }
            return this;
        }

        public void assertResultSet(final Matcher<ResultSet> matcher) throws SQLException {
            withResultSet(resultSet -> assertThat(resultSet, matcher));
        }

        public void assertFails(final Matcher<String> errorMessageMatcher) {
            BaseDataImporter.this.paths.forEach(path -> uploadFileToS3(bucketName(), path));
            final var tableBuilder = BaseDataImporter.this.schema.createTableBuilder(this.tableName.toUpperCase(Locale.ENGLISH));
            this.columns.forEach(tableBuilder::column);
            final Table table = tableBuilder.build();
            final IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
                    () -> importFromS3IntoExasol(schemaName(), table, bucketName(), BaseDataImporter.this.baseFileName + "*",
                            dataFormat()));
            assertThat(exception.getCause().getMessage(), errorMessageMatcher);
        }
    }

    @FunctionalInterface
    protected interface ResultSetConsumer {
        void accept(ResultSet resultSet) throws SQLException;
    }
}
