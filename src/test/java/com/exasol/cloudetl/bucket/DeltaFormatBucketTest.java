package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

import org.apache.spark.sql.*;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.*;
import com.exasol.cloudetl.parquet.ParquetSourceTest;
import com.exasol.common.data.Row;

class DeltaFormatBucketTest extends AbstractBucketTest {
    private static SparkSession spark;
    private String path;
    private java.nio.file.Path tmpDir;

    @BeforeAll
    static void beforeAll() {
        spark = SparkSession.builder().config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
                .appName("DeltaFormatTest").master("local[2]").getOrCreate();
    }

    @BeforeEach
    void beforeEach() throws IOException {
        this.tmpDir = TestFileManager.createTemporaryFolder("deltaPath");
        this.path = this.tmpDir.toUri().toString();
        this.properties = Map.of(PATH, this.path, FORMAT, "DELTA");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.tmpDir);
    }

    @AfterAll
    static void afterAll() {
        spark.close();
    }

    @Test
    void getPathsThrowsIfThePathIsNotDeltaFormat() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> getBucket(this.properties).getPaths());
        assertTrue(thrown.getMessage().startsWith("F-CSE-3"));
        assertTrue(thrown.getMessage().contains("path '" + this.path + "' is not a Delta formatted"));
    }

    @Test
    void getPathsReturnsDeltaLogFilesWithLatestSnapshot() {
        saveSparkDataset(spark.range(1, 101).repartition(20), null);

        final Bucket bucket = getBucket(this.properties);
        assertEquals(20, ScalaConverters.asJavaList(bucket.getPaths()).size());
    }

    @Test
    void getPathsReturnsDeltaLogFilesWithTrailingStarBucketPath() {
        this.properties = Map.of(PATH, this.path.substring(0, this.path.length() - 1) + "/*", FORMAT, "DELTA");
        saveSparkDataset(spark.range(1, 5).coalesce(1), null);

        final Bucket bucket = getBucket(this.properties);
        final List<org.apache.hadoop.fs.Path> paths = ScalaConverters.asJavaList(bucket.getPaths());
        assertEquals(1, paths.size());
        assertFalse(paths.stream().anyMatch(path -> path.toUri().toString().contains("/*")));
    }

    @Test
    void getPathsReturnsDeltaLogFilesWithOverwriteSnapshot() {
        saveSparkDataset(spark.range(1, 101).repartition(20), null);
        saveSparkDataset(spark.range(101, 110).coalesce(2), "overwrite");

        final Bucket bucket = getBucket(this.properties);
        assertEquals(2, ScalaConverters.asJavaList(bucket.getPaths()).size());
    }

    @Test
    void streamReturnsRecordsFromTheLatestDeltaSnapshot() throws IOException {
        saveSparkDataset(spark.range(1, 6), null);
        assertEquals(Set.of(1L, 2L, 3L, 4L, 5L), collectToSet(getBucket(this.properties)));
    }

    @Test
    void streamReturnsRecordsFromTheOverwriteDeltaSnapshot() throws IOException {
        saveSparkDataset(spark.range(1, 6), null);
        saveSparkDataset(spark.range(10, 13), "overwrite");
        assertEquals(Set.of(10L, 11L, 12L), collectToSet(getBucket(this.properties)));
    }

    private void saveSparkDataset(final Dataset<?> dataFrame, final String saveMode) {
        final DataFrameWriter<?> writer = dataFrame.write().format("delta");
        if (saveMode == null) {
            writer.save(this.path);
        } else {
            writer.mode(saveMode).save(this.path);
        }
    }

    private Set<Long> collectToSet(final Bucket bucket) throws IOException {
        final Set<Long> set = new HashSet<>();
        final var conf = bucket.getConfiguration();
        for (final org.apache.hadoop.fs.Path path : ScalaConverters.asJavaList(bucket.getPaths())) {
            try (ParquetSourceTest source = new ParquetSourceTest(path, conf)) {
                final scala.collection.Iterator<Row> iterator = source.stream();
                while (iterator.hasNext()) {
                    set.add(((Number) iterator.next().get(0)).longValue());
                }
            }
        }
        return set;
    }
}
