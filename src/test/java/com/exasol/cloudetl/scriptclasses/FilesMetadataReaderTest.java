package com.exasol.cloudetl.scriptclasses;

import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.ExaMetadata;

class FilesMetadataReaderTest extends StorageTest {
    @Test
    void runReturnsTheListOfFileNames() throws Exception {
        final Map<String, String> properties = Map.of("BUCKET_PATH", this.testResourceParquetPath, "DATA_FORMAT", "PARQUET");
        final Map<String, String> expectedParquetFiles = Map.of(//
                this.testResourceDir + "/import/parquet/sales_positions1.snappy.parquet", "0", //
                this.testResourceDir + "/import/parquet/sales_positions2.snappy.parquet", "0", //
                this.testResourceDir + "/import/parquet/sales_positions_small.snappy.parquet", "1");

        final com.exasol.ExaIterator iter = mockExasolIterator(properties);
        when(iter.getInteger(2)).thenReturn(2);
        final ExaMetadata meta = mock(ExaMetadata.class);
        when(meta.getNodeCount()).thenReturn(1L);
        when(meta.getMemoryLimit()).thenReturn(new BigInteger("2000000000"));
        FilesMetadataReader.run(meta, iter);
        for (final Map.Entry<String, String> entry : expectedParquetFiles.entrySet()) {
            verify(iter, times(1)).emit(entry.getKey(), entry.getValue(), Long.valueOf(0), Long.valueOf(1));
        }
    }
}
