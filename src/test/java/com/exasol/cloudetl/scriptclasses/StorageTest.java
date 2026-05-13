package com.exasol.cloudetl.scriptclasses;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.exasol.ExaIterator;
import com.exasol.cloudetl.storage.StorageProperties;

abstract class StorageTest {
    protected final String testResourceDir = normalize(resourcePath("/data"));
    protected final String testResourceParquetPath = this.testResourceDir + "/import/parquet/sales_pos*.parquet";

    protected String normalize(final Path path) {
        return path.toUri().toString().replaceAll("/$", "").replaceAll("///", "/");
    }

    protected ExaIterator mockExasolIterator(final Map<String, String> params) {
        final StorageProperties storageProperties = new StorageProperties(params);
        final String bucketPath = storageProperties.getStoragePath();
        final ExaIterator mockedIterator = mock(ExaIterator.class);
        try {
            when(mockedIterator.getString(0)).thenReturn(bucketPath);
            when(mockedIterator.getString(1)).thenReturn(storageProperties.mkString());
        } catch (final com.exasol.ExaIterationException | com.exasol.ExaDataTypeException exception) {
            throw new IllegalStateException(exception);
        }
        return mockedIterator;
    }

    protected Object[] anyObjects() {
        return any(Object[].class);
    }

    private static Path resourcePath(final String resource) {
        try {
            return Paths.get(StorageTest.class.getResource(resource).toURI());
        } catch (final URISyntaxException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
