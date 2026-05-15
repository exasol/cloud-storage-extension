package com.exasol.cloudetl.bucket;

import static com.exasol.cloudetl.bucket.AzureAbfsBucketTest.withAll;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GCSBucketTest extends AbstractBucketTest {
    private final Map<String, String> defaultProperties = Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO",
            "GCS_KEYFILE_PATH", "/keyfile.json", "GCS_PROJECT_ID", "myProject");

    @Test
    void constructsBucketProperly() {
        final Bucket bucket = getBucket(this.defaultProperties);
        bucket.validate();
        assertInstanceOf(GCSBucket.class, bucket);
    }

    @Test
    void validationFailsWhenBothKeyfileAndConnectionAreSpecified() {
        final Bucket bucket = getBucket(Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO", "GCS_PROJECT_ID",
                "myProject", "GCS_KEYFILE_PATH", "/keyfile.json", "CONNECTION_NAME", "GCS_CONNECTION"));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, bucket::validate);
        assertEquals("E-CSE-30: Both properties 'GCS_KEYFILE_PATH' and 'CONNECTION_NAME' are specified. "
                + "Please specify only one of them.", thrown.getMessage());
    }

    @Test
    void validationFailsWhenBothKeyfileAndConnectionAreMissing() {
        final Bucket bucket = getBucket(Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO", "GCS_PROJECT_ID", "myProject"));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, bucket::validate);
        assertEquals("E-CSE-31: Neither of properties 'GCS_KEYFILE_PATH' or 'CONNECTION_NAME' is specified. "
                + "Please specify exactly one of them.", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidConnectionValues")
    void validationFailsForInvalidConnectionValues(final String connectionContent, final String expectedErrorMessage) {
        final Bucket bucket = getBucket(Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO", "GCS_PROJECT_ID",
                "myProject", "CONNECTION_NAME", "connection_info"), mockConnectionInfo("", connectionContent));
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, bucket::validate);
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    void keyfilePathConfiguredWhenPathPropertySpecified() {
        final Bucket bucket = getBucket(this.defaultProperties);
        bucket.validate();
        assertInstanceOf(GCSBucket.class, bucket);
        assertEquals("/keyfile.json", bucket.getConfiguration().get("fs.gs.auth.service.account.json.keyfile"));
    }

    @Test
    void keyfilePathConfiguredWhenConnectionSpecified() throws IOException {
        final Bucket bucket = getBucket(Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO", "GCS_PROJECT_ID",
                "myProject", "CONNECTION_NAME", "connection_info"),
                mockConnectionInfo("", "GCS_KEYFILE_CONTENT={\"key\":\"value\"}"));
        bucket.validate();
        assertInstanceOf(GCSBucket.class, bucket);
        final String tempFilePath = bucket.getConfiguration().get("fs.gs.auth.service.account.json.keyfile");
        assertFalse(tempFilePath.isBlank());
        assertEquals("{\"key\":\"value\"}", Files.readString(Path.of(tempFilePath)));
    }

    @Test
    void proxySettingsShouldBeAddedWhenPresent() {
        final Bucket bucket = getBucket(withAll(this.defaultProperties, Map.of("PROXY_HOST", "myproxy.net",
                "PROXY_PORT", "3198", "PROXY_USERNAME", "user", "PROXY_PASSWORD", "password")));
        bucket.validate();
        assertInstanceOf(GCSBucket.class, bucket);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        assertEquals("myproxy.net:3198", conf.get("fs.gs.proxy.address"));
        assertEquals("user", conf.get("fs.gs.proxy.username"));
        assertEquals("password", conf.get("fs.gs.proxy.password"));
    }

    @Test
    void proxySettingsShouldNotBeAddedWithoutHost() {
        final Bucket bucket = getBucket(withAll(this.defaultProperties,
                Map.of("PROXY_PORT", "3198", "PROXY_USERNAME", "user", "PROXY_PASSWORD", "password")));
        bucket.validate();
        assertInstanceOf(GCSBucket.class, bucket);
        assertTrue(bucket.getConfiguration().getValByRegex("fs\\.s3a\\.proxy.+").isEmpty());
    }

    private static Stream<Arguments> invalidConnectionValues() {
        final String invalidFormatMessage = "E-IEUCS-4: Properties input string does not contain key-value assignment '='."
                + " Please make sure that key-value pairs encoded correctly.";
        return Stream.of(//
                Arguments.of("", invalidFormatMessage), //
                Arguments.of("wrong-format", invalidFormatMessage), //
                Arguments.of("wrong_key=value",
                        "E-CSE-32: The connection 'connection_info' does not contain 'GCS_KEYFILE_CONTENT' property."
                                + " Please check the connection properties."), //
                Arguments.of("GCS_KEYFILE_CONTENT=invalid_json",
                        "E-CSE-33: The connection 'connection_info' does not contain valid JSON in property"
                                + " 'GCS_KEYFILE_CONTENT'. Please check the connection properties."));
    }
}
