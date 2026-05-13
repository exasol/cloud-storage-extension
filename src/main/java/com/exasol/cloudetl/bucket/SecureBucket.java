package com.exasol.cloudetl.bucket;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/** Mixin interface for buckets that require secure credentials. */
public interface SecureBucket {
    /** @return secure property keys */
    scala.collection.immutable.Seq<String> getSecureProperties();

    /** @return storage properties */
    StorageProperties properties();

    /** Validate connection credential properties. */
    default void validateConnectionProperties() {
        if (hasSecureProperties()) {
            throw new BucketValidationException(ExaError.messageBuilder("E-CSE-5")
                    .message("Using credentials as parameters is forbidded.")
                    .mitigation("Please use an Exasol named connection object via CONNECTION_NAME property.").toString());
        }
        if (!properties().hasNamedConnection()) {
            throw new BucketValidationException(ExaError.messageBuilder("E-CSE-6")
                    .message("No CONNECTION_NAME property is defined.")
                    .mitigation("Please use connection object to provide access credentials.").toString());
        }
    }

    private boolean hasSecureProperties() {
        for (final String key : ScalaConverters.asJavaList(getSecureProperties())) {
            if (properties().containsKey(key)) {
                return true;
            }
        }
        return false;
    }
}
