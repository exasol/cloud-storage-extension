package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem;

class BucketTest extends AbstractBucketTest {
    @Test
    void createThrowsIfTheSchemeIsNotSupported() {
        this.properties = Map.of(PATH, "xyz:/bucket/files*", FORMAT, "ORC");
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> getBucket(this.properties));
        assertTrue(thrown.getMessage().startsWith("F-CSE-4"));
        assertTrue(thrown.getMessage().contains("Provided path scheme 'xyz' is not supported."));
    }

    @Test
    void createReturnsLocalBucket() {
        this.properties = Map.of(PATH, "file://local/path/bucket/", FORMAT, "ORC");
        assertInstanceOf(LocalBucket.class, getBucket(this.properties));
    }

    @Test
    void createReturnsGcsBucket() {
        this.properties = Map.of(PATH, "gs://my-bucket/", FORMAT, "AVRO", "GCS_PROJECT_ID", "projX",
                "GCS_KEYFILE_PATH", "/bucketfs/bucket1/projX.json");
        final Bucket bucket = getBucket(this.properties);
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        assertInstanceOf(GCSBucket.class, bucket);
        assertEquals(GoogleHadoopFileSystem.class.getName(), conf.get("fs.gs.impl"));
        assertEquals("projX", conf.get("fs.gs.project.id"));
        assertEquals("/bucketfs/bucket1/projX.json", conf.get("fs.gs.auth.service.account.json.keyfile"));
    }
}
