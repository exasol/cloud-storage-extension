package com.exasol.cloudetl.bucket;

import static com.exasol.cloudetl.bucket.AzureAbfsBucketTest.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.apache.hadoop.fs.azure.*;
import org.junit.jupiter.api.Test;

class AzureBlobBucketTest extends AbstractBucketTest {
    private final Map<String, String> defaultProperties = Map.of(PATH,
            "wasbs://container1@account1.blob.core.windows.net/orc-data/", FORMAT, "ORC");
    private final String secretKey = "secret";
    private final String sasToken = "token";

    @Test
    void createThrowsIfAzureBlobPathIsNotValid() {
        final String path = "wasb://container@account1.blob.windows.net/data/";
        this.properties = with(this.defaultProperties, PATH, path, "CONNECTION_NAME", "connection_info");
        final BucketValidationException thrown = assertThrows(BucketValidationException.class,
                () -> getBucket(this.properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")).getConfiguration());
        assertTrue(thrown.getMessage().startsWith("E-CSE-19"));
        assertTrue(thrown.getMessage().contains("path '" + path + "' scheme is not valid."));
    }

    @Test
    void createThrowsIfAzureBlobPathDoesNotMatchTheExpectedFormat() {
        for (final String path : List.of("wasbs://container1@account1.blob.core.windows.net",
                "wasbs://container1@.blob.core.windows.net/orc-data/",
                "wasbs://container1@account1.dfs.core.windows.net/orc-data/",
                "wasbs://container1@account1.blob.windows.net/orc-data/")) {
            this.properties = with(this.defaultProperties, PATH, path, "CONNECTION_NAME", "connection_info");
            final BucketValidationException thrown = assertThrows(BucketValidationException.class,
                    () -> getBucket(this.properties, mockConnectionInfo("", "AZURE_SECRET_KEY=" + this.secretKey))
                            .getConfiguration());
            assertTrue(thrown.getMessage().startsWith("E-CSE-19"));
            assertTrue(thrown.getMessage().contains("path '" + path + "' scheme is not valid."));
        }
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
    void createThrowsIfSasTokenIsProvidedAsParameter() {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.put("AZURE_ACCOUNT_NAME", "account1");
        properties.put("AZURE_SAS_TOKEN", "token");
        properties.put("AZURE_CONTAINER_NAME", "container1");
        assertForbiddenProperty(() -> getBucket(properties).validate());
    }

    @Test
    void createReturnsSecretFromPasswordOfConnectionObjectWithAccountName() {
        final Map<String, String> properties = with(this.defaultProperties, "AZURE_ACCOUNT_NAME", "account1",
                "CONNECTION_NAME", "connection_info");
        assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")),
                Map.of("fs.azure.account.key.account1.blob.core.windows.net", "secret"));
    }

    @Test
    void createReturnsSecretFromPasswordOfConnectionObject() {
        final Map<String, String> properties = with(this.defaultProperties, "CONNECTION_NAME", "connection_info");
        assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret")),
                Map.of("fs.azure.account.key.account1.blob.core.windows.net", "secret"));
    }

    @Test
    void createParsesAccountNameFromAzureBlobPaths() {
        for (final AzurePath path : azureBlobPaths()) {
            final Map<String, String> properties = with(this.defaultProperties, PATH, path.path, "CONNECTION_NAME",
                    "connection_info");
            assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=" + this.secretKey)),
                    Map.of("fs.azure.account.key." + path.expectedAccountName + ".blob.core.windows.net", this.secretKey));
        }
    }

    @Test
    void createParsesAccountAndContainerNamesFromAzureBlobPathsForSasToken() {
        for (final AzurePath path : azureBlobPaths()) {
            final Map<String, String> properties = with(this.defaultProperties, PATH, path.path, "CONNECTION_NAME",
                    "connection_info");
            assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SAS_TOKEN=" + this.sasToken)),
                    Map.of("fs.azure.sas." + path.expectedContainerName + "." + path.expectedAccountName
                            + ".blob.core.windows.net", this.sasToken));
        }
    }

    @Test
    void createReturnsSasTokenFromPasswordOfConnectionWithAccountContainerName() {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.put("AZURE_ACCOUNT_NAME", "account1");
        properties.put("AZURE_CONTAINER_NAME", "container1");
        properties.put("CONNECTION_NAME", "connection_info");
        assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SAS_TOKEN=token")),
                Map.of("fs.azure.sas.container1.account1.blob.core.windows.net", "token"));
    }

    @Test
    void createReturnsSasTokenFromPasswordOfConnectionObject() {
        final Map<String, String> properties = with(this.defaultProperties, "CONNECTION_NAME", "connection_info");
        assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SAS_TOKEN=token")),
                Map.of("fs.azure.sas.container1.account1.blob.core.windows.net", "token"));
    }

    @Test
    void createReturnsSasFromConnectionObjectIfBothSasAndSecretAreProvided() {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.put("AZURE_ACCOUNT_NAME", "account1");
        properties.put("AZURE_CONTAINER_NAME", "container1");
        properties.put("CONNECTION_NAME", "connection_info");
        assertAzureBlobBucket(getBucket(properties, mockConnectionInfo("", "AZURE_SECRET_KEY=secret;AZURE_SAS_TOKEN=token")),
                Map.of("fs.azure.sas.container1.account1.blob.core.windows.net", "token"));
    }

    private void assertAzureBlobBucket(final Bucket bucket, final Map<String, String> extraMappings) {
        assertInstanceOf(AzureBlobBucket.class, bucket);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        final Map<String, String> defaultMappings = Map.of("fs.azure", NativeAzureFileSystem.class.getName(),
                "fs.wasb.impl", NativeAzureFileSystem.class.getName(), "fs.wasbs.impl",
                NativeAzureFileSystem.class.getName(), "fs.AbstractFileSystem.wasb.impl", Wasb.class.getName(),
                "fs.AbstractFileSystem.wasbs.impl", Wasbs.class.getName());
        withAll(defaultMappings, extraMappings).forEach((given, expected) -> assertEquals(expected, conf.get(given)));
    }

    private List<AzurePath> azureBlobPaths() {
        return List.of(new AzurePath("wasbs://container1@account1.blob.core.windows.net/orc-data/", "account1",
                "container1"), new AzurePath("wasb://container-2@account2.blob.core.windows.net/parquet-data/year=2026/file.parquet",
                        "account2", "container-2"));
    }

    private static final class AzurePath {
        private final String path;
        private final String expectedAccountName;
        private final String expectedContainerName;

        private AzurePath(final String path, final String expectedAccountName, final String expectedContainerName) {
            this.path = path;
            this.expectedAccountName = expectedAccountName;
            this.expectedContainerName = expectedContainerName;
        }
    }
}
