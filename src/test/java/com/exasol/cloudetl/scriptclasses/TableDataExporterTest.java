package com.exasol.cloudetl.scriptclasses;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import com.exasol.*;
import com.exasol.cloudetl.*;

class TableDataExporterTest extends StorageTest {
    private Path outputPath;
    private final DataRecords records = new DataRecords();
    private final List<String> srcColumns = List.of("c_int", "c_long", "c_decimal", "c_double", "c_string", "c_boolean",
            "c_date", "c_timestamp");
    private ExaMetadata metadata;
    private ExaIterator iterator;
    private final Map<String, String> defaultProperties = Map.of("DATA_FORMAT", "PARQUET");

    @BeforeEach
    void beforeEach() throws Exception {
        this.outputPath = TestFileManager.createTemporaryFolder("exportTableTest");
        this.metadata = createMockedMetadata();
        this.iterator = createMockedIterator(this.outputPath.toUri().toString(), this.defaultProperties);
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputPath);
    }

    @Test
    void runExportsTableRows() throws Exception {
        TableDataExporter.run(this.metadata, this.iterator);
        verify(this.metadata, times(1)).getInputColumnCount();
        for (int index = 3; index <= 10; index++) {
            verify(this.metadata, times(1)).getInputColumnType(index);
            verify(this.metadata, times(1)).getInputColumnPrecision(index);
            verify(this.metadata, times(1)).getInputColumnScale(index);
            verify(this.metadata, times(1)).getInputColumnLength(index);
        }
        verify(this.iterator, times(2)).getInteger(3);
        verify(this.iterator, times(2)).getLong(4);
        verify(this.iterator, times(2)).getBigDecimal(5);
        verify(this.iterator, times(2)).getDouble(6);
        verify(this.iterator, times(2)).getString(7);
        verify(this.iterator, times(2)).getBoolean(8);
        verify(this.iterator, times(2)).getDate(9);
        verify(this.iterator, times(2)).getTimestamp(10);
        verify(this.iterator, times(1)).emit(Long.valueOf(2));
    }

    @Test
    void importsExportedRowsFromAPath() throws Exception {
        TableDataExporter.run(this.metadata, this.iterator);
        final Map<String, String> properties = Map.of("BUCKET_PATH", this.testResourceDir, "DATA_FORMAT", "PARQUET");
        final ExaIterator importIter = mockExasolIterator(properties);
        final List<String> exportedFiles = getOutputPathFiles();
        when(importIter.next()).thenReturn(false);
        when(importIter.getString(2)).thenReturn(exportedFiles.get(0));
        when(importIter.getLong(3)).thenReturn(0L);
        when(importIter.getLong(4)).thenReturn(1L);

        FilesDataImporter.run(mock(ExaMetadata.class), importIter);

        verify(importIter, times(2)).emit(anyObjects());
        verify(this.iterator, times(1)).emit(Long.valueOf(2));
    }

    @Test
    void exportCreatesFileWithoutCompressionExtensionIfCompressionCodecIsNotSet() throws Exception {
        TableDataExporter.run(this.metadata, this.iterator);
        Assertions.assertTrue(Files.exists(this.outputPath));
        try (var list = Files.list(this.outputPath)) {
            Assertions.assertEquals(2, list.count());
        }
        checkExportFileExtensions("");
    }

    @Test
    void exportCreatesFileWithCompressionExtensionIfCompressionCodecIsSet() throws Exception {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.put("PARQUET_COMPRESSION_CODEC", "SNAPPY");
        this.iterator = createMockedIterator(this.outputPath.toUri().toString(), properties);
        TableDataExporter.run(this.metadata, this.iterator);
        Assertions.assertTrue(Files.exists(this.outputPath));
        try (var list = Files.list(this.outputPath)) {
            Assertions.assertEquals(2, list.count());
        }
        checkExportFileExtensions(".snappy");
    }

    private ExaIterator createMockedIterator(final String resourceDir, final Map<String, String> extraProperties)
            throws Exception {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.put("BUCKET_PATH", resourceDir);
        properties.putAll(extraProperties);
        final ExaIterator mockedIterator = mockExasolIterator(properties);
        when(mockedIterator.getString(2)).thenReturn(String.join(".", this.srcColumns));
        when(mockedIterator.next()).thenReturn(true, false);
        when(mockedIterator.size()).thenReturn(2L);
        when(mockedIterator.getInteger(3)).thenReturn(1, 2);
        when(mockedIterator.getLong(4)).thenReturn(3L, 4L);
        when(mockedIterator.getBigDecimal(5)).thenReturn(this.records.bigDecimalValue1, this.records.bigDecimalValue2);
        when(mockedIterator.getDouble(6)).thenReturn(3.14, 0.13);
        when(mockedIterator.getString(7)).thenReturn("xyz", "abc");
        when(mockedIterator.getBoolean(8)).thenReturn(true, false);
        when(mockedIterator.getDate(9)).thenReturn(this.records.dateValue1, this.records.dateValue2);
        when(mockedIterator.getTimestamp(10)).thenReturn(this.records.timestampValue1, this.records.timestampValue2);
        return mockedIterator;
    }

    private ExaMetadata createMockedMetadata() throws Exception {
        final ExaMetadata mockedMetadata = mock(ExaMetadata.class);
        when(mockedMetadata.getInputColumnCount()).thenReturn(11L);
        final List<Object[]> returns = List.of(//
                new Object[] { 3, Integer.class, 0L, 0L, 0L }, //
                new Object[] { 4, Long.class, 0L, 0L, 0L }, //
                new Object[] { 5, java.math.BigDecimal.class, 36L, 5L, 0L }, //
                new Object[] { 6, Double.class, 0L, 0L, 0L }, //
                new Object[] { 7, String.class, 0L, 0L, 3L }, //
                new Object[] { 8, Boolean.class, 0L, 0L, 0L }, //
                new Object[] { 9, java.sql.Date.class, 0L, 0L, 0L }, //
                new Object[] { 10, java.sql.Timestamp.class, 0L, 0L, 0L });
        for (final Object[] row : returns) {
            final int index = (Integer) row[0];
            doReturn(row[1]).when(mockedMetadata).getInputColumnType(index);
            when(mockedMetadata.getInputColumnPrecision(index)).thenReturn((Long) row[2]);
            when(mockedMetadata.getInputColumnScale(index)).thenReturn((Long) row[3]);
            when(mockedMetadata.getInputColumnLength(index)).thenReturn((Long) row[4]);
        }
        return mockedMetadata;
    }

    private void checkExportFileExtensions(final String compressionCodec) {
        Assertions.assertTrue(getOutputPathFiles().stream().allMatch(path -> path.endsWith(compressionCodec + ".parquet")));
    }

    private List<String> getOutputPathFiles() {
        try (var list = Files.list(this.outputPath)) {
            return list.map(path -> path.toUri().toString()).filter(path -> path.endsWith(".parquet"))
                    .collect(Collectors.toList());
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
