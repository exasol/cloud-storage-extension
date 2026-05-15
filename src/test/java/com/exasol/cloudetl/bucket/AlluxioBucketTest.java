package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class AlluxioBucketTest extends AbstractBucketTest {
    @Test
    void createSetsCorrectConfiguration() {
        final Map<String, String> properties = Map.of(PATH, "alluxio://alluxio/fs/path", FORMAT, "parquet");
        final Bucket bucket = getBucket(properties, mockConnectionInfo("", ""));
        final org.apache.hadoop.conf.Configuration conf = bucket.getConfiguration();
        assertInstanceOf(AlluxioBucket.class, bucket);
        assertEquals(alluxio.hadoop.FileSystem.class.getName(), conf.get("fs.alluxio.impl"));
        assertEquals(alluxio.hadoop.AlluxioFileSystem.class.getName(),
                conf.get("fs.AbstractFileSystem.alluxio.impl"));
    }
}
