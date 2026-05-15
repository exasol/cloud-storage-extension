package com.exasol.cloudetl.it.scriptclasses;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;

import com.exasol.cloudetl.BaseS3IntegrationTest;
import com.exasol.dbbuilder.dialects.Table;

class ExportParallelismIT extends BaseS3IntegrationTest {
    private static final String SCHEMA_NAME = "EXPORT_PARALLELISM";
    private static final String BUCKET_NAME = "export-parallelism";
    private Table exportTable;

    @BeforeAll
    void beforeAll() {
        prepareExasolDatabase(SCHEMA_NAME);
        createS3ConnectionObject();
        createBucket(BUCKET_NAME);
        this.exportTable = this.schema.createTable("T1_PARALLELISM", "C1", "DECIMAL(18,0)")
                .bulkInsert(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(List::of));
    }

    @AfterEach
    void afterEach() {
        deleteBucketObjects(BUCKET_NAME);
    }

    @Test
    void exportsUsingUserProvidedParallelism() {
        exportData("PARALLELISM = 'iproc()'");
        assertThat(listObjects(BUCKET_NAME).size(), equalTo(1));
    }

    @Test
    void exportsUsingUserProvidedParallelismAsIprocCombination() {
        exportData("PARALLELISM = 'iproc(), mod(rownum,3)'");
        assertThat(listObjects(BUCKET_NAME).size(), equalTo(3));
    }

    @Test
    void exportsUsingAutoParallelism() {
        exportData("");
        assertThat(listObjects(BUCKET_NAME).size(), equalTo(2));
    }

    @Test
    void exportsUsingAutoParallelismWithUdfMemoryParameter() {
        exportData("UDF_MEMORY = '250'");
        assertThat(Integer.valueOf(listObjects(BUCKET_NAME).size()), lessThanOrEqualTo(Integer.valueOf(5)));
    }

    private void exportData(final String parallelism) {
        executeStmt(String.format("EXPORT %s%n"
                + "INTO SCRIPT %s.EXPORT_PATH WITH%n"
                + "BUCKET_PATH     = 's3a://%s/'%n"
                + "DATA_FORMAT     = 'PARQUET'%n"
                + "S3_ENDPOINT     = '%s'%n"
                + "CONNECTION_NAME = 'S3_CONNECTION'%n"
                + "%s%n"
                + ";%n", this.exportTable.getFullyQualifiedName(), SCHEMA_NAME, BUCKET_NAME, this.s3Endpoint,
                parallelism));
    }
}
