package com.exasol.cloudetl.bucket;

import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;

/** Bucket implementation for HDFS. */
public final class HDFSBucket extends AbstractConfiguredBucket {
    /** Create a bucket. */
    public HDFSBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof HDFSBucket) && hasSameConfiguration((HDFSBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(HDFSBucket.class);
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of());
    }

    @Override
    public void validate() {
        validateRequiredProperties();
    }

    @Override
    public Configuration getConfiguration() {
        validate();
        final Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        return conf;
    }
}
