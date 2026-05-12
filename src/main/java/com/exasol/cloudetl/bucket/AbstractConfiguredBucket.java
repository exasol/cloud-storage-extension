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

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractConfiguredBucket other = (AbstractConfiguredBucket) obj;
        return Objects.equals(this.path, other.path) && Objects.equals(this.properties, other.properties);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), this.path, this.properties);
    }
}
