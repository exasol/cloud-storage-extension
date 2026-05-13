package com.exasol.cloudetl.bucket;

import static com.exasol.cloudetl.bucket.AzureAbfsBucketTest.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.apache.hadoop.fs.s3a.S3AFileSystem;
import org.junit.jupiter.api.Test;

class S3BucketTest extends AbstractBucketTest {
    private final Map<String, String> defaultProperties = Map.of(PATH, "s3a://my-bucket/", FORMAT, "AVRO",
            "CONNECTION_NAME", "connection_info", "S3_ENDPOINT", "eu-central-1");
    private final String accessKey = "access";
    private final String secretKey = "secret";
    private final String sessionToken = "token";
    private final Map<String, String> accessProperties = withAll(this.defaultProperties,
            Map.of("S3_ACCESS_KEY", this.accessKey, "S3_SECRET_KEY", this.secretKey, "S3_SESSION_TOKEN", this.sessionToken));
    private final Map<String, String> configMappings = Map.of("fs.s3a.access.key", this.accessKey, "fs.s3a.secret.key",
            this.secretKey, "fs.s3a.session.token", this.sessionToken);

    @Test
    void createThrowsWhenNoConnectionNameIsProvided() {
        final Map<String, String> properties = new HashMap<>(this.defaultProperties);
        properties.remove("CONNECTION_NAME");
        assertNoConnectionName(() -> getBucket(properties).validate());
    }

    @Test
    void createThrowsWithAccessSecretOrSessionTokenParameters() {
        assertForbiddenProperty(() -> getBucket(this.accessProperties).validate());
    }

    @Test
    void createReturnsS3BucketWithSecretFromConnection() {
        assertConfigurationProperties(getBucket(this.defaultProperties, mockConnectionInfo("access", "S3_SECRET_KEY=secret")),
                withoutSessionToken(this.configMappings));
    }

    @Test
    void createReturnsSpecificCredentialsProviderForPublicAccessConfiguration() {
        final Bucket bucket = getBucket(this.defaultProperties, mockConnectionInfo("access", "S3_ACCESS_KEY=;S3_SECRET_KEY="));
        assertEquals("org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider",
                bucket.getConfiguration().get("fs.s3a.aws.credentials.provider"));
    }

    @Test
    void createReturnsS3BucketWithSecretAndSessionTokenFromConnection() {
        final Bucket bucket = getBucket(this.defaultProperties,
                mockConnectionInfo("access", "S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token"));
        assertConfigurationProperties(bucket, this.configMappings);
    }

    @Test
    void createReturnsS3BucketWithAccessAndSecretFromConnection() {
        final Bucket bucket = getBucket(this.defaultProperties,
                mockConnectionInfo("", "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret"));
        assertConfigurationProperties(bucket, withoutSessionToken(this.configMappings));
    }

    @Test
    void createReturnsS3BucketWithAccessSecretAndSessionTokenFromConnection() {
        assertConfigurationProperties(bucketWithDefaultConnectionString(this.defaultProperties), this.configMappings);
    }

    @Test
    void createReturnsS3BucketWithChangeDetectionMode() {
        final Map<String, String> properties = with(this.defaultProperties, "S3_CHANGE_DETECTION_MODE", "none");
        assertConfigurationProperties(bucketWithDefaultConnectionString(properties),
                with(this.configMappings, "fs.s3a.change.detection.mode", "none"));
    }

    @Test
    void createReturnsS3BucketWithPathStyleAccess() {
        final Map<String, String> properties = with(this.defaultProperties, "S3_PATH_STYLE_ACCESS", "true");
        assertConfigurationProperties(bucketWithDefaultConnectionString(properties),
                with(this.configMappings, "fs.s3a.path.style.access", "true"));
    }

    @Test
    void createReturnsS3BucketWithEndpointRegion() {
        final Map<String, String> properties = with(this.defaultProperties, "S3_ENDPOINT_REGION", "eu-central-1");
        assertConfigurationProperties(bucketWithDefaultConnectionString(properties),
                with(this.configMappings, "fs.s3a.endpoint.region", "eu-central-1"));
    }

    @Test
    void createReturnsS3BucketWithSslEnabledByDefault() {
        assertConfigurationProperties(bucketWithDefaultConnectionString(this.defaultProperties),
                with(this.configMappings, "fs.s3a.connection.ssl.enabled", "true"));
    }

    @Test
    void createReturnsS3BucketWithSslEnabledByUser() {
        final Map<String, String> properties = with(this.defaultProperties, "S3_SSL_ENABLED", "falsy");
        assertConfigurationProperties(bucketWithDefaultConnectionString(properties),
                with(this.configMappings, "fs.s3a.connection.ssl.enabled", "falsy"));
    }

    @Test
    void proxySettingsShouldBeAddedWhenPresent() {
        final Map<String, String> properties = withAll(this.defaultProperties,
                Map.of("PROXY_HOST", "my_proxy.net", "PROXY_PORT", "2345"));
        assertConfigurationProperties(bucketWithDefaultConnectionString(properties),
                withAll(this.configMappings, Map.of("fs.s3a.proxy.host", "my_proxy.net", "fs.s3a.proxy.port", "2345")));
    }

    @Test
    void proxySettingsShouldNotBeAddedWithoutHost() {
        final Map<String, String> properties = withAll(this.defaultProperties,
                Map.of("PROXY_USERNAME", "myUser", "PROXY_PASSWORD", "mySecretPassword", "PROXY_PORT", "2345"));
        assertTrue(bucketWithDefaultConnectionString(properties).getConfiguration().getValByRegex("fs\\.s3a\\.proxy.+")
                .isEmpty());
    }

    @Test
    void throwsForS3BucketNameThatEndWithANumber() {
        assertInvalidBucketName("s3a://my-bucket.test.s3.007", "end with a number");
    }

    @Test
    void throwsForS3BucketNameThatContainUnderscore() {
        assertInvalidBucketName("s3a://my_bucket.test.s3", "contain underscores");
    }

    @Test
    void throwsForS3BucketNameThatEndWithAHyphen() {
        assertInvalidBucketName("s3a://my-bucket-", "end with a number or a hyphen");
    }

    private void assertConfigurationProperties(final Bucket bucket, final Map<String, String> extraMappings) {
        assertInstanceOf(S3Bucket.class, bucket);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        final Map<String, String> defaultMappings = Map.of("fs.s3a.impl", S3AFileSystem.class.getName(),
                "fs.s3a.endpoint", "eu-central-1");
        withAll(defaultMappings, extraMappings).forEach((given, expected) -> assertEquals(expected, conf.get(given)));
    }

    private Bucket bucketWithDefaultConnectionString(final Map<String, String> properties) {
        final String identifier = "S3_ACCESS_KEY=access;S3_SECRET_KEY=secret;S3_SESSION_TOKEN=token";
        return getBucket(properties, mockConnectionInfo("", identifier));
    }

    private Map<String, String> withoutSessionToken(final Map<String, String> input) {
        final Map<String, String> result = new HashMap<>(input);
        result.remove("fs.s3a.session.token");
        return result;
    }

    private void assertInvalidBucketName(final String path, final String expectedMessagePart) {
        final Map<String, String> properties = with(this.defaultProperties, PATH, path);
        final BucketValidationException thrown = assertThrows(BucketValidationException.class,
                () -> bucketWithDefaultConnectionString(properties).getConfiguration());
        assertTrue(thrown.getMessage().startsWith("E-CSE-28"));
        assertTrue(thrown.getMessage().contains(expectedMessagePart));
    }
}
