package com.exasol.cloudetl.scriptclasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.exasol.*;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.parquetio.data.*;

class FilesDataImporterTest extends StorageTest {
    private final Map<String, String> properties = Map.of("BUCKET_PATH", this.testResourceParquetPath, "DATA_FORMAT",
            "PARQUET");

    @Test
    void runThrowsIfFileFormatIsNotSupported() throws Exception {
        final String file = this.testResourceDir + "/import/parquet/sales_positions1.snappy.parquet";
        final ExaIterator iter = mockFileIterator("CSV", file);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> FilesDataImporter.run(mock(ExaMetadata.class), iter));
        assertTrue(thrown.getMessage().startsWith("E-CSE-17"));
        assertTrue(thrown.getMessage().contains("file format 'CSV' is not supported."));
    }

    @Test
    void runEmitsAllRecordsFromASource() throws Exception {
        final String file1 = this.testResourceDir + "/import/parquet/sales_positions1.snappy.parquet";
        final String file2 = this.testResourceDir + "/import/parquet/sales_positions2.snappy.parquet";
        final StorageProperties storageProperties = new StorageProperties(this.properties);
        final ExaIteratorMock iter = exaIteratorMock(iteratorRow(storageProperties, file1, 0L, 1L),
                iteratorRow(storageProperties, file2, 0L, 1L));
        FilesDataImporter.run(mock(ExaMetadata.class), iter);
        assertEquals(1000, iter.getEmittedRows().size());
    }

    @Test
    void runEmitsRecordsForSingleInterval() throws Exception {
        final String file1 = this.testResourceDir + "/import/parquet/sales_positions1.snappy.parquet";
        final StorageProperties storageProperties = new StorageProperties(this.properties);
        final ExaIteratorMock iter = exaIteratorMock(iteratorRow(storageProperties, file1, 0L, 1L));
        FilesDataImporter.run(mock(ExaMetadata.class), iter);
        assertEquals(500, iter.getEmittedRows().size());
    }

    @Test
    void runEmitsRecordsForDuplicateIntervals() throws Exception {
        final String file = this.testResourceDir + "/import/parquet/sales_positions1.snappy.parquet";
        final StorageProperties storageProperties = new StorageProperties(this.properties);
        final ExaIteratorMock iter = exaIteratorMock(iteratorRow(storageProperties, file, 0L, 1L),
                iteratorRow(storageProperties, file, 0L, 1L));
        FilesDataImporter.run(mock(ExaMetadata.class), iter);
        assertEquals(500, iter.getEmittedRows().size());
    }

    @Test
    void runEmitsCorrectSequenceOfRecordsFromParquetFile() throws Exception {
        final String parquetFile = this.testResourceDir + "/import/parquet/sales_positions_small.snappy.parquet";
        final ExaIterator iter = mockFileIterator("PARQUET", parquetFile);
        FilesDataImporter.run(mock(ExaMetadata.class), iter);
        verifySmallFilesImport(iter);
    }

    @Test
    void runEmitsCorrectSequenceOfRecordsFromAvroFile() throws Exception {
        final String avroFile = this.testResourceDir + "/import/avro/sales_positions_small.avro";
        final ExaIterator iter = mockFileIterator("AVRO", avroFile);
        FilesDataImporter.run(mock(ExaMetadata.class), iter);
        verifySmallFilesImport(iter);
    }

    @Test
    void collectFilesForEmptyIterator() throws Exception {
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(ExaIteratorMock.empty());
        assertEquals(1, result.size());
        assertChunks(result.get(null), chunk(0, 0));
    }

    @Test
    void collectFilesForIteratorWithSingleEntry() throws Exception {
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(exaIteratorMock(iteratorRow("file1.parquet", 17L, 42L)));
        assertChunks(result.get("file1.parquet"), chunk(17, 42));
    }

    @Test
    void collectFilesForIteratorWithSingleFileButMultipleChunks() throws Exception {
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(
                exaIteratorMock(iteratorRow("file1.parquet", 17L, 42L), iteratorRow("file1.parquet", 1L, 2L)));
        assertEquals(1, result.size());
        assertChunks(result.get("file1.parquet"), chunk(17, 42), chunk(1, 2));
    }

    @Test
    void collectFilesForIteratorWithMultipleFilesAndMultipleChunks() throws Exception {
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(exaIteratorMock(
                iteratorRow("file1.parquet", 17L, 42L), iteratorRow("file1.parquet", 1L, 2L),
                iteratorRow("file2.parquet", 0L, 1L)));
        assertEquals(2, result.size());
        assertChunks(result.get("file1.parquet"), chunk(17, 42), chunk(1, 2));
        assertChunks(result.get("file2.parquet"), chunk(0, 1));
    }

    @Test
    void collectFilesForIteratorWithTwoFiles() throws Exception {
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(
                exaIteratorMock(iteratorRow("file1.parquet", 17L, 42L), iteratorRow("file2.parquet", 1L, 2L)));
        assertEquals(2, result.size());
        assertChunks(result.get("file1.parquet"), chunk(17, 42));
        assertChunks(result.get("file2.parquet"), chunk(1, 2));
    }

    @Test
    void collectFilesWith40kFiles() throws Exception {
        final List<Object[]> files = IntStream.range(0, 40_000)
                .mapToObj(index -> iteratorRow("file" + index + ".parquet", 0L, 1L)).collect(Collectors.toList());
        final Map<String, java.util.List<ChunkInterval>> result = collectFiles(ExaIteratorMock.fromList(files));
        assertEquals(40_000, result.size());
        result.values().forEach(intervals -> assertEquals(1, intervals.size()));
    }

    private ExaIteratorMock exaIteratorMock(final Object[]... files) {
        return ExaIteratorMock.fromList(Arrays.asList(files));
    }

    private Object[] iteratorRow(final String file, final Long intervalStart, final Long intervalEnd) {
        return new Object[] { null, null, file, intervalStart, intervalEnd };
    }

    private Object[] iteratorRow(final StorageProperties storageProperties, final String file, final Long intervalStart,
            final Long intervalEnd) {
        return new Object[] { storageProperties.getStoragePath(), storageProperties.mkString(), file, intervalStart,
                intervalEnd };
    }

    private ChunkInterval chunk(final long start, final long end) {
        return new ChunkIntervalImpl(start, end);
    }

    private ExaIterator mockFileIterator(final String fileFormat, final String filename) throws Exception {
        final Map<String, String> params = new HashMap<>(this.properties);
        params.put("DATA_FORMAT", fileFormat);
        final ExaIterator iter = mockExasolIterator(params);
        when(iter.next()).thenReturn(false);
        when(iter.getString(2)).thenReturn(filename);
        when(iter.getLong(3)).thenReturn(0L);
        when(iter.getLong(4)).thenReturn(1L);
        return iter;
    }

    private Map<String, java.util.List<ChunkInterval>> collectFiles(final ExaIterator iter) throws Exception {
        return ScalaConverters.javaMapCopy(FilesDataImporter.collectFiles(iter));
    }

    private void assertChunks(final java.util.List<ChunkInterval> actual, final ChunkInterval... expected) {
        assertNotNull(actual);
        assertEquals(expected.length, actual.size());
        assertTrue(actual.containsAll(Arrays.asList(expected)));
    }

    private void verifySmallFilesImport(final ExaIterator iter) throws Exception {
        final List<List<Object>> records = List.of(//
                Arrays.asList(582244536L, 2, 96982, 1, 0.56, null, null), //
                Arrays.asList(582177839L, 6, 96982, 2, 0.56, null, null), //
                Arrays.asList(582370207L, 0, 96982, 1, 0.56, null, null), //
                Arrays.asList(582344312L, 0, 96982, 5, 0.56, null, null), //
                Arrays.asList(582344274L, 1, 96982, 1, 0.56, null, null));

        verify(iter, times(5)).emit(anyObjects());
        for (final List<Object> row : records) {
            verify(iter, times(1)).emit(row.toArray());
        }
    }
}
