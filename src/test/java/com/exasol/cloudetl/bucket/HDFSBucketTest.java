package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class HDFSBucketTest extends AbstractBucketTest {
    @Test
    void createSetsCorrectConfiguration() {
        final Map<String, String> properties = Map.of(PATH, "hdfs://dir/path", FORMAT, "orc");
        final Bucket bucket = getBucket(properties, mockConnectionInfo("", ""));
        assertInstanceOf(HDFSBucket.class, bucket);
        assertEquals(org.apache.hadoop.hdfs.DistributedFileSystem.class.getName(),
                bucket.getConfiguration().get("fs.hdfs.impl"));
    }
}
