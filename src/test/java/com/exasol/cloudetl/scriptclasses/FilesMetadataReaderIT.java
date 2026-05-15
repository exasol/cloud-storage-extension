package com.exasol.cloudetl.scriptclasses;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.*;

class FilesMetadataReaderIT extends BaseS3IntegrationTest {
    private static final String SCHEMA_NAME = "DATA_SCHEMA";
    private static final String TABLE_NAME = "DATA_TABLE";
    private static final String BUCKET_NAME = "filesmetadata";
    private Path outputDirectory;

    @BeforeAll
    void beforeAll() {
        prepareExasolDatabase(SCHEMA_NAME);
        createS3ConnectionObject();
    }

    @AfterAll
    void afterAll() {
        deleteBucket(BUCKET_NAME);
    }

    @BeforeEach
    void beforeEach() throws IOException {
        this.outputDirectory = TestFileManager.createTemporaryFolder("data-files-");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputDirectory);
        deleteBucketObjects(BUCKET_NAME);
    }

    @Test
    void partitionsFilesUsingUserProvidedParallelismValue() throws SQLException, IOException {
        createAndUploadFiles(3);
        verify(runMetadataScript("2", getPropertiesString()),
                table().row("s3a://filesmetadata/part-001", "0").row("s3a://filesmetadata/part-002", "1")
                        .row("s3a://filesmetadata/part-003", "0").matches());
    }

    @Test
    void partitionsFilesUsingUserProvidedParallelismValueAsNprocCombination() throws SQLException, IOException {
        createAndUploadFiles(3);
        verify(runMetadataScript("nproc()*1", getPropertiesString()),
                table().row("s3a://filesmetadata/part-001", "0").row("s3a://filesmetadata/part-002", "0")
                        .row("s3a://filesmetadata/part-003", "0").matches());
    }

    @Test
    void partitionsFilesAutomatically() throws SQLException, IOException {
        createAndUploadFiles(3);
        verify(runMetadataScript("65536", getPropertiesString()),
                table().row("s3a://filesmetadata/part-001", "0").row("s3a://filesmetadata/part-002", "1")
                        .row("s3a://filesmetadata/part-003", "0").matches());
    }

    @Test
    void partitionsFilesUsingUdfMemoryParameterValue() throws SQLException, IOException {
        createAndUploadFiles(6);
        final String properties = getPropertiesString() + ";UDF_MEMORY -> 750";
        verify(runMetadataScript("65536", properties),
                table().row("s3a://filesmetadata/part-001", "0").row("s3a://filesmetadata/part-002", "0")
                        .row("s3a://filesmetadata/part-003", "0").row("s3a://filesmetadata/part-004", "0")
                        .row("s3a://filesmetadata/part-005", "0").row("s3a://filesmetadata/part-006", "0").matches());
    }

    @Test
    void filtersHiddenAndMetadataFiles() throws SQLException, IOException {
        uploadFiles(List.of("_SUCCESS", ".METADATA"));
        importFromS3IntoExasol(SCHEMA_NAME, this.schema.createTable(TABLE_NAME, "C1", "VARCHAR(5)"), BUCKET_NAME, "*",
                "parquet");
        verify(executeQuery("SELECT * FROM " + getTableName()), table("VARCHAR").matches());
    }

    private String getTableName() {
        return '"' + SCHEMA_NAME + "\".\"" + TABLE_NAME + '"';
    }

    private String getPropertiesString() {
        return "BUCKET_PATH -> s3a://filesmetadata/*;DATA_FORMAT -> ORC;" + "S3_ENDPOINT -> " + this.s3Endpoint
                + ";CONNECTION_NAME -> S3_CONNECTION";
    }

    private void verify(final ResultSet resultSet, final Matcher<ResultSet> matcher) throws SQLException {
        try (resultSet) {
            assertThat(resultSet, matcher);
        }
    }

    private ResultSet runMetadataScript(final String parallelism, final String properties) throws SQLException {
        return executeQuery("SELECT filename, partition_index\n"
                + "FROM (SELECT IMPORT_METADATA('" + BUCKET_NAME + "', '" + properties + "', " + parallelism + "))\n"
                + "ORDER BY filename");
    }

    private void createAndUploadFiles(final int numOfFiles) throws IOException {
        for (int fileId = 1; fileId <= numOfFiles; fileId++) {
            uploadFiles(List.of(String.format("part-%03d", Integer.valueOf(fileId))));
        }
    }

    private void uploadFiles(final List<String> files) throws IOException {
        for (final String filename : files) {
            Files.createFile(this.outputDirectory.resolve(filename));
            uploadFileToS3(BUCKET_NAME, new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(), filename));
        }
    }
}
