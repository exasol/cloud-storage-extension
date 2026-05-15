package com.exasol.cloudetl;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    protected final String baseFileName = "part-";
    private final List<org.apache.hadoop.fs.Path> pendingImportFiles = new ArrayList<>();
    private int fileCounter;

    @BeforeEach
    void baseDataImporterBeforeEach() throws IOException {
        this.outputDirectory = TestFileManager.createTemporaryFolder(dataFormat() + "-tests-");
        this.fileCounter = 0;
        deleteBucketObjects(bucketName());
    }

    @AfterEach
    void baseDataImporterAfterEach() throws IOException {
        this.pendingImportFiles.clear();
        deleteBucketObjects(bucketName());
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
        final String formattedFileCounter = String.format("%04d", this.fileCounter++);
        final org.apache.hadoop.fs.Path newPath = new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(),
                this.baseFileName + formattedFileCounter + "." + dataFormat());
        this.pendingImportFiles.add(newPath);
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
            try {
                final Table table = createTable();
                importPendingFiles(table);
                final ResultSet resultSet = executeQuery("SELECT * FROM " + table.getFullyQualifiedName());
                try {
                    block.accept(resultSet);
                } finally {
                    resultSet.close();
                }
            } finally {
                clearPendingImport();
            }
            return this;
        }

        public void assertResultSet(final Matcher<ResultSet> matcher) throws SQLException {
            withResultSet(resultSet -> assertThat(resultSet, matcher));
        }

        public void assertFails(final Matcher<String> errorMessageMatcher) {
            try {
                final Table table = createTable();
                final IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
                        () -> importPendingFiles(table));
                assertThat(exception.getCause().getMessage(), errorMessageMatcher);
            } finally {
                clearPendingImport();
            }
        }

        private Table createTable() {
            final var tableBuilder = BaseDataImporter.this.schema
                    .createTableBuilder(this.tableName.toUpperCase(Locale.ENGLISH));
            this.columns.forEach(tableBuilder::column);
            return tableBuilder.build();
        }

        private void importPendingFiles(final Table table) {
            deleteBucketObjects(bucketName());
            BaseDataImporter.this.pendingImportFiles.forEach(path -> uploadFileToS3(bucketName(), path));
            importFromS3IntoExasol(schemaName(), table, bucketName(), BaseDataImporter.this.baseFileName + "*", dataFormat());
        }

        private void clearPendingImport() {
            BaseDataImporter.this.pendingImportFiles.clear();
            deleteBucketObjects(bucketName());
        }
    }

    @FunctionalInterface
    protected interface ResultSetConsumer {
        void accept(ResultSet resultSet) throws SQLException;
    }
}
