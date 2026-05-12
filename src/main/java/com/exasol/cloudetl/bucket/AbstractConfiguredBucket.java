package com.exasol.cloudetl.bucket;

import java.util.Objects;

import com.exasol.cloudetl.storage.StorageProperties;

abstract class AbstractConfiguredBucket extends Bucket {
    private final String path;
    private final StorageProperties properties;

    AbstractConfiguredBucket(final String path, final StorageProperties properties) {
        this.path = path;
        this.properties = properties;
    }

    @Override
    public final String bucketPath() {
        return this.path;
    }

    @Override
    public final StorageProperties properties() {
        return this.properties;
    }

    protected final boolean hasSameConfiguration(final AbstractConfiguredBucket other) {
        return Objects.equals(this.path, other.path) && Objects.equals(this.properties, other.properties);
    }

    protected final int configuredHashCode(final Class<?> bucketClass) {
        return Objects.hash(bucketClass, this.path, this.properties);
    }
}
