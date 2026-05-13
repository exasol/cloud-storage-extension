package com.exasol.cloudetl.bucket;

import static com.exasol.cloudetl.bucket.AzureAbfsBucketTest.with;
import static com.exasol.cloudetl.bucket.AzureAbfsBucketTest.withAll;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class AzureAdlsBucketTest extends AbstractBucketTest {
    private final Map<String, String> defaultProperties = Map.of(PATH, "adl://container1.azuredatalakestore.net/avro-data/*",
            FORMAT, "AVRO");
    private final String clientId = "clientID";
    private final String clientSecret = "clientSecret";
    private final String directoryId = "directoryID";
    private final Map<String, String> configMappings = Map.of("dfs.adls.oauth2.client.id", this.clientId,
            "dfs.adls.oauth2.credential", this.clientSecret, "dfs.adls.oauth2.refresh.url",
            "https://login.microsoftonline.com/" + this.directoryId + "/oauth2/token");

    @Test
    void createThrowsIfNoConnectionNameIsProvided() {
        assertNoConnectionName(() -> getBucket(this.defaultProperties).validate());
    }

    @Test
    void createThrowsIfClientIdClientSecretOrDirectoryIdIsProvidedAsParameter() {
        final Map<String, String> properties = new java.util.HashMap<>(this.defaultProperties);
        properties.put("AZURE_CLIENT_ID", this.clientId);
        properties.put("AZURE_CLIENT_SECRET", this.clientSecret);
        properties.put("AZURE_DIRECTORY_ID", this.directoryId);
        assertForbiddenProperty(() -> getBucket(properties).validate());
    }

    @Test
    void createReturnsWithCredentialsFromUsernameAndPasswordOfConnectionObject() {
        final Map<String, String> properties = with(this.defaultProperties, "CONNECTION_NAME", "connection_info");
        final Bucket bucket = getBucket(properties,
                mockConnectionInfo(this.clientId,
                        "AZURE_CLIENT_SECRET=" + this.clientSecret + ";AZURE_DIRECTORY_ID=" + this.directoryId));
        assertAzureAdlsBucket(bucket, this.configMappings);
    }

    @Test
    void createReturnsWithCredentialsFromPasswordOfConnectionObject() {
        final Map<String, String> properties = with(this.defaultProperties, "CONNECTION_NAME", "connection_info");
        final String connectionInfoPassword = "AZURE_CLIENT_ID=" + this.clientId + ";AZURE_CLIENT_SECRET="
                + this.clientSecret + ";AZURE_DIRECTORY_ID=" + this.directoryId;
        assertAzureAdlsBucket(getBucket(properties, mockConnectionInfo("", connectionInfoPassword)), this.configMappings);
    }

    private void assertAzureAdlsBucket(final Bucket bucket, final Map<String, String> extraMappings) {
        assertInstanceOf(AzureAdlsBucket.class, bucket);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        final Map<String, String> defaultMappings = Map.of("fs.adl.impl",
                org.apache.hadoop.fs.adl.AdlFileSystem.class.getName(), "fs.AbstractFileSystem.adl.impl",
                org.apache.hadoop.fs.adl.Adl.class.getName(), "dfs.adls.oauth2.access.token.provider.type",
                "ClientCredential");
        withAll(defaultMappings, extraMappings).forEach((given, expected) -> assertEquals(expected, conf.get(given)));
    }
}
