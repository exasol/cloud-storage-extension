package com.exasol.cloudetl.it.delta;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;
import java.util.Collections;

import org.apache.spark.sql.*;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.BaseS3IntegrationTest;
import com.exasol.cloudetl.helper.StringGenerator;
import com.exasol.dbbuilder.dialects.Table;

class DeltaDataImporterIT extends BaseS3IntegrationTest {
    private SparkSession spark;
    private static final String SCHEMA_NAME = "DELTA_SCHEMA";
    private static final String DATA_FORMAT = "delta";

    @BeforeAll
    void beforeAll() {
        prepareExasolDatabase(SCHEMA_NAME);
        createS3ConnectionObject();
        this.spark = SparkSession.builder()
                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
                .config("spark.hadoop.fs.s3a.endpoint", this.s3Endpoint)
                .config("spark.hadoop.fs.s3a.access.key", getAwsAccessKey())
                .config("spark.hadoop.fs.s3a.secret.key", getAwsSecretKey()).appName("DeltaFormatIT")
                .master("local[2]").getOrCreate();
    }

    @AfterAll
    void afterAll() {
        this.spark.stop();
    }

    @Test
    void importsDeltaFormatData() throws SQLException {
        final String bucketName = "delta-table-long";
        final Table testTable = this.schema.createTable("DELTA_TABLE_LONG", "C1", "INTEGER");
        createBucket(bucketName);
        writeSparkDataset(this.spark.range(1, 4), bucketName);
        importFromS3IntoExasol(SCHEMA_NAME, testTable, bucketName, "*", DATA_FORMAT);
        verifyImport("SELECT * FROM " + testTable.getFullyQualifiedName() + " ORDER BY C1 ASC",
                table().row(Long.valueOf(1)).row(Long.valueOf(2)).row(Long.valueOf(3)).matches());
    }

    @Test
    void importTruncatesLongStringValues() throws SQLException {
        final String bucketName = "delta-table-string";
        final Table testTable = this.schema.createTable("DELTA_TABLE_STRING", "C1", "VARCHAR(2000000)");
        createBucket(bucketName);
        final String longString = StringGenerator.getRandomString(2000005);
        final Dataset<String> data = this.spark.createDataset(Collections.singletonList(longString), Encoders.STRING());
        writeSparkDataset(data, bucketName);
        importFromS3IntoExasol(SCHEMA_NAME, testTable, bucketName, "*", DATA_FORMAT);
        verifyImport("SELECT * FROM " + testTable.getFullyQualifiedName(),
                table().row(longString.substring(0, 2000000)).matches());
    }

    private <T> void writeSparkDataset(final Dataset<T> dataset, final String bucketName) {
        dataset.write().format("delta").mode("overwrite").save("s3a://" + bucketName + "/");
    }

    private void verifyImport(final String query, final Matcher<ResultSet> matcher) throws SQLException {
        try (ResultSet resultSet = executeQuery(query)) {
            assertThat(resultSet, matcher);
        }
    }
}
