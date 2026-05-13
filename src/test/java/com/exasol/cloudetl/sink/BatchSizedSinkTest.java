package com.exasol.cloudetl.sink;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.*;
import com.exasol.cloudetl.bucket.LocalBucket;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.common.data.Row;

class BatchSizedSinkTest extends DataRecords {
    private Path outputPath;
    private final Map<String, String> properties = Map.of("BUCKET_PATH", "a/path", "DATA_FORMAT", "avro");
    private final List<ExaColumnInfo> columnMetadata = List.of(//
            new ExaColumnInfo("c_int", Integer.class, 0, 0, 0, true), //
            new ExaColumnInfo("c_long", Long.class, 0, 0, 0, true), //
            new ExaColumnInfo("c_decimal", java.math.BigDecimal.class, 36, 5, 0, true), //
            new ExaColumnInfo("c_double", Double.class, 0, 0, 0, true), //
            new ExaColumnInfo("c_string", String.class, 0, 0, 3, true), //
            new ExaColumnInfo("c_boolean", Boolean.class, 0, 0, 0, true), //
            new ExaColumnInfo("c_date", java.sql.Date.class, 0, 0, 0, true), //
            new ExaColumnInfo("c_timestamp", java.sql.Timestamp.class, 0, 0, 0, true));
    private final List<Row> rows = this.rawRecords.stream().map(values -> Row.apply(ScalaConverters.seqFromJava(values)))
            .collect(Collectors.toList());

    @BeforeEach
    void beforeEach() throws IOException {
        this.outputPath = TestFileManager.createTemporaryFolder("batchSizedSinkTest");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputPath);
    }

    @Test
    void exportSingleFileWithDefaultBatchSize() {
        final LocalBucket bucket = createBucket("4");
        final BatchSizedSink sink = new BatchSizedSink(1L, "vm1", 2, this.columnMetadata, bucket);
        this.rows.forEach(sink::write);
        sink.close();
        assertEquals(2L, sink.getTotalRecords());
    }

    @Test
    void exportSeveralFilesWithBatchSizeSmallerThanTotalRecords() {
        final LocalBucket bucket = createBucket("3");
        final BatchSizedSink sink = new BatchSizedSink(1L, "vm1", 7, this.columnMetadata, bucket);
        final List<Row> newRows = new ArrayList<>();
        newRows.addAll(this.rows);
        newRows.addAll(this.rows);
        newRows.addAll(this.rows);
        newRows.add(this.rows.get(0));
        newRows.forEach(sink::write);
        sink.close();
        assertEquals(7L, sink.getTotalRecords());
    }

    private LocalBucket createBucket(final String exportBatchSize) {
        final Map<String, String> params = new HashMap<>(this.properties);
        params.put("EXPORT_BATCH_SIZE", exportBatchSize);
        return new LocalBucket(this.outputPath.toUri().toString(), new StorageProperties(params));
    }
}
