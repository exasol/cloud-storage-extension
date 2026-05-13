package com.exasol.cloudetl.bucket;

import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;

/** Bucket implementation for Alluxio. */
public final class AlluxioBucket extends AbstractConfiguredBucket {
    /** Create a bucket. */
    public AlluxioBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof AlluxioBucket) && hasSameConfiguration((AlluxioBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(AlluxioBucket.class);
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
        conf.set("fs.alluxio.impl", alluxio.hadoop.FileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.alluxio.impl", alluxio.hadoop.AlluxioFileSystem.class.getName());
        return conf;
    }
}
