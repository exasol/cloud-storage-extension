package com.exasol.cloudetl.storage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.*;
import com.exasol.cloudetl.ScalaConverters;

class StoragePropertiesTest {
    private Map<String, String> properties;

    @BeforeEach
    void beforeEach() {
        this.properties = Map.of();
    }

    @Test
    void getStoragePathReturnsStoragePathPropertyValue() {
        final String path = "a/bucket/path";
        this.properties = Map.of(StorageProperties.BUCKET_PATH, path);
        assertEquals(path, baseProperties().getStoragePath());
    }

    @Test
    void getStoragePathThrowsIfStoragePathPropertyIsNotSet() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> baseProperties().getStoragePath());
        assertTrue(thrown.getMessage().startsWith("E-IEUCS-2"));
        assertTrue(thrown.getMessage()
                .contains("Failed to get value for '" + StorageProperties.BUCKET_PATH + "' property"));
    }

    @Test
    void getStoragePathSchemeReturnsPathSchemeValue() {
        for (final String scheme : List.of("s3a", "s3", "wasbs", "adls", "file")) {
            this.properties = Map.of(StorageProperties.BUCKET_PATH, scheme + "://a/path");
            assertEquals(scheme, baseProperties().getStoragePathScheme());
        }
    }

    @Test
    void getStoragePathSchemeReturnsPathSchemeWithRegexPattern() {
        this.properties = Map.of(StorageProperties.BUCKET_PATH, "s3a://bucket/{year=2019/month=1,year=2019/month=2}/*");
        assertEquals("s3a", baseProperties().getStoragePathScheme());
    }

    @Test
    void getDeltaFormatLogStoreClassNameReturnsStorageClassNameForScheme() {
        final Map<String, String> data = Map.of("s3a", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore",
                "abfs", "org.apache.spark.sql.delta.storage.AzureLogStore", "abfss",
                "org.apache.spark.sql.delta.storage.AzureLogStore", "adl",
                "org.apache.spark.sql.delta.storage.AzureLogStore", "wasbs",
                "org.apache.spark.sql.delta.storage.AzureLogStore", "wasb",
                "org.apache.spark.sql.delta.storage.AzureLogStore", "file",
                "org.apache.spark.sql.delta.storage.HDFSLogStore");
        data.forEach((scheme, expected) -> {
            this.properties = Map.of(StorageProperties.BUCKET_PATH, scheme + "://a/path");
            assertEquals(expected, baseProperties().getDeltaFormatLogStoreClassName());
        });
    }

    @Test
    void getDeltaFormatLogStoreClassNameThrowsForUnsupportedStorageSystems() {
        this.properties = Map.of(StorageProperties.BUCKET_PATH, "gs://a/path");
        final UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class,
                () -> baseProperties().getDeltaFormatLogStoreClassName());
        assertTrue(thrown.getMessage().startsWith("E-CSE-16"));
        assertTrue(thrown.getMessage().contains("is not supported in Google Cloud Storage"));
    }

    @Test
    void getFileFormatReturnsSupportedFileFormatValue() {
        this.properties = Map.of(StorageProperties.BUCKET_PATH, "path", StorageProperties.DATA_FORMAT, "orc");
        assertEquals(FileFormat.ORC, baseProperties().getFileFormat());
    }

    @Test
    void getFileFormatThrowsIfFileFormatIsNotSupported() {
        final String fileFormat = "a-non-supported-file-format";
        this.properties = Map.of(StorageProperties.BUCKET_PATH, "path", StorageProperties.DATA_FORMAT, fileFormat);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> baseProperties().getFileFormat());
        assertTrue(thrown.getMessage().startsWith("E-CSE-17"));
        assertTrue(thrown.getMessage().contains("file format '" + fileFormat + "' is not supported."));
    }

    @Test
    void getParallelismReturnsUserProvidedValue() {
        this.properties = Map.of(StorageProperties.PARALLELISM, "2");
        assertEquals("2", ScalaConverters.optionOrElse(baseProperties().getParallelism(), "default"));
    }

    @Test
    void getParallelismReturnsDefaultValueIfParallelismIsNotSet() {
        assertEquals("nproc()", ScalaConverters.optionOrElse(baseProperties().getParallelism(), "nproc()"));
    }

    @Test
    void isOverwriteReturnsTrueIsSet() {
        this.properties = Map.of(StorageProperties.OVERWRITE, "true");
        assertTrue(baseProperties().isOverwrite());
    }

    @Test
    void isOverwriteReturnsDefaultFalseValueIfItIsNotSet() {
        assertFalse(baseProperties().isOverwrite());
    }

    @Test
    void isParquetLowercaseSchemaReturnsTrueByDefault() {
        assertTrue(baseProperties().isParquetLowercaseSchema());
    }

    @Test
    void isParquetLowercaseSchemaReturnsUserSetValue() {
        this.properties = Map.of(StorageProperties.PARQUET_LOWERCASE_SCHEMA, "false");
        assertFalse(baseProperties().isParquetLowercaseSchema());
    }

    @Test
    void mergeReturnsStoragePropertiesWithNewProperties() {
        this.properties = Map.of("CONNECTION_NAME", "connection_info");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        mockConnection(metadata, newConnectionInformation("", "KEY1=secret1==;KEY2=sec=ret2;KEY3=secret"));
        final StorageProperties storageProperties = new StorageProperties(this.properties, metadata).merge("");
        assertEquals("secret1==", storageProperties.getString("KEY1"));
        assertEquals("sec=ret2", storageProperties.getString("KEY2"));
        assertEquals("secret", storageProperties.getString("KEY3"));
    }

    @Test
    void mergeReturnsWithKeyForUsernameMappedToConnectionUsername() {
        this.properties = Map.of("CONNECTION_NAME", "connection_info");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        mockConnection(metadata, newConnectionInformation("usernameValue", "KEY1=secret1"));
        final StorageProperties storageProperties = new StorageProperties(this.properties, metadata).merge("usernameKey");
        assertEquals("usernameValue", storageProperties.getString("usernameKey"));
        assertEquals("secret1", storageProperties.getString("KEY1"));
    }

    @Test
    void mergeReturnsWithKeyForUsernameOverwrittenByPassword() {
        this.properties = Map.of("CONNECTION_NAME", "connection_info");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        mockConnection(metadata, newConnectionInformation("usernameValue", "KEY1=secret1;usernameKey=newUsername"));
        final StorageProperties storageProperties = new StorageProperties(this.properties, metadata).merge("usernameKey");
        assertEquals("newUsername", storageProperties.getString("usernameKey"));
        assertEquals("secret1", storageProperties.getString("KEY1"));
    }

    @Test
    void mergeThrowsIfItCannotFindKeyValuePairsInConnectionPassword() {
        this.properties = Map.of("CONNECTION_NAME", "connection_info");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        mockConnection(metadata, newConnectionInformation("", "secret1;key=value"));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new StorageProperties(this.properties, metadata).merge(""));
        assertTrue(thrown.getMessage().contains("does not contain key-value assignment '='"));
    }

    @Test
    void mkStringReturnsEmptyStringByDefault() {
        assertEquals("", baseProperties().mkString());
    }

    @Test
    void mkStringReturnsKeyValuePropertiesString() {
        this.properties = Map.of("k1", "v1", "k3", "v3", "k2", "v2");
        assertEquals("k1 -> v1;k2 -> v2;k3 -> v3", baseProperties().mkString());
    }

    @Test
    void applyMapReturnsCorrectStorageProperties() {
        this.properties = Map.of("a", "b");
        assertEquals(baseProperties(), StorageProperties.apply(this.properties));
    }

    @Test
    void applyMapWithExasolMetadataReturnsCorrectStorageProperties() {
        this.properties = Map.of("k", "v");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        assertEquals(new StorageProperties(this.properties, metadata), StorageProperties.apply(this.properties, metadata));
    }

    @Test
    void applyStringThrowsIfInputStringDoesNotContainSeparator() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> StorageProperties.apply(""));
        assertTrue(thrown.getMessage().startsWith("E-IEUCS-4"));
        assertTrue(thrown.getMessage().contains("does not contain key-value assignment ' -> '"));
    }

    @Test
    void applyStringReturnsCorrectStorageProperties() {
        this.properties = Map.of("k3", "v3", "k2", "v2");
        final StorageProperties baseProperty = baseProperties();
        assertEquals(baseProperty, StorageProperties.apply(baseProperty.mkString()));
    }

    @Test
    void applyStringWithExasolMetadataReturnsCorrectStorageProperties() {
        this.properties = Map.of("k1", "v1", "k2", "v2");
        final ExaMetadata metadata = mock(ExaMetadata.class);
        assertEquals(new StorageProperties(this.properties, metadata),
                StorageProperties.apply(baseProperties().mkString(), metadata));
    }

    private StorageProperties baseProperties() {
        return new StorageProperties(this.properties);
    }

    private ExaConnectionInformation newConnectionInformation(final String username, final String password) {
        return new ExaConnectionInformation() {
            @Override
            public ConnectionType getType() {
                return ConnectionType.PASSWORD;
            }

            @Override
            public String getAddress() {
                return "";
            }

            @Override
            public String getUser() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }
        };
    }

    private void mockConnection(final ExaMetadata metadata, final ExaConnectionInformation connectionInformation) {
        try {
            when(metadata.getConnection("connection_info")).thenReturn(connectionInformation);
        } catch (final ExaConnectionAccessException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
