package com.exasol.cloudetl.bucket;

import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;

/** Bucket implementation for local file paths. */
public final class LocalBucket extends AbstractConfiguredBucket {
    /** Create a bucket. */
    public LocalBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof LocalBucket) && hasSameConfiguration((LocalBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(LocalBucket.class);
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
        return new Configuration();
    }
}
