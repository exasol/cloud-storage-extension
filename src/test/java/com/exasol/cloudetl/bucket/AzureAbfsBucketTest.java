package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.apache.hadoop.fs.azurebfs.*;
import org.junit.jupiter.api.Test;

class AzureAbfsBucketTest extends AbstractBucketTest {
    private final Map<String, String> defaultProperties = Map.of(PATH,
            "abfs://container1@account1.dfs.core.windows.net/data/", FORMAT, "PARQUET");

    @Test
    void createThrowsIfAzureAbfsPathIsNotValid() {
        final String path = "abfss://container@account1.dfs.windows.net/data/";
        this.properties = with(this.defaultProperties, PATH, path, "CONNECTION_NAME", "connection_info");
        final BucketValidationException thrown = assertThrows(BucketValidationException.class,
                () -> getBucket(this.properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")).getConfiguration());
        assertTrue(thrown.getMessage().startsWith("E-CSE-20"));
        assertTrue(thrown.getMessage().contains("path '" + path + "' scheme is not valid."));
    }

    @Test
    void createThrowsIfNoConnectionNameIsProvided() {
        assertNoConnectionName(() -> getBucket(this.defaultProperties).validate());
    }

    @Test
    void createThrowsIfSecretKeyIsProvidedAsParameter() {
        assertForbiddenProperty(() -> getBucket(with(this.defaultProperties, "AZURE_SECRET_KEY", "secret")).validate());
    }

    @Test
    void createReturnsSecretFromConnectionObjectAccountNameFromPath() {
        final Map<String, String> properties = with(this.defaultProperties, "CONNECTION_NAME", "connection_info");
        assertAzureAbfsBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")),
                Map.of("fs.azure.account.key.account1.dfs.core.windows.net", "secret"));
    }

    @Test
    void createReturnsSecretFromConnectionObjectWithAccountName() {
        final Map<String, String> properties = with(this.defaultProperties, "AZURE_ACCOUNT_NAME", "account1",
                "CONNECTION_NAME", "connection_info");
        assertAzureAbfsBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")),
                Map.of("fs.azure.account.key.account1.dfs.core.windows.net", "secret"));
    }

    private void assertAzureAbfsBucket(final Bucket bucket, final Map<String, String> extraMappings) {
        assertInstanceOf(AzureAbfsBucket.class, bucket);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        final Map<String, String> defaultMappings = Map.of("fs.abfs.impl", AzureBlobFileSystem.class.getName(),
                "fs.abfss.impl", SecureAzureBlobFileSystem.class.getName(), "fs.AbstractFileSystem.abfs.impl",
                Abfs.class.getName(), "fs.AbstractFileSystem.abfss.impl", Abfss.class.getName());
        final Map<String, String> mappings = withAll(defaultMappings, extraMappings);
        mappings.forEach((given, expected) -> assertEquals(expected, conf.get(given)));
    }

    static Map<String, String> with(final Map<String, String> input, final String key, final String value) {
        final Map<String, String> result = new HashMap<>(input);
        result.put(key, value);
        return result;
    }

    static Map<String, String> with(final Map<String, String> input, final String key1, final String value1,
            final String key2, final String value2) {
        final Map<String, String> result = with(input, key1, value1);
        result.put(key2, value2);
        return result;
    }

    static Map<String, String> withAll(final Map<String, String> first, final Map<String, String> second) {
        final Map<String, String> result = new HashMap<>(first);
        result.putAll(second);
        return result;
    }
}
