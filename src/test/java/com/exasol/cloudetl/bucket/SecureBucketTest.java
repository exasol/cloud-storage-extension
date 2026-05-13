package com.exasol.cloudetl.bucket;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.junit.jupiter.api.Test;

import com.exasol.ExaMetadata;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;

class SecureBucketTest extends AbstractBucketTest {
    @Test
    void validateThrowsIfNoConnectionNameIsProvided() {
        assertNoConnectionName(() -> new BaseSecureBucket(this.properties, null).validate());
    }

    @Test
    void validateThrowsIfAccessPropertiesAreProvided() {
        this.properties = Map.of("accountNameProperty", "user", "accountSecretProperty", "secret");
        assertForbiddenProperty(() -> new BaseSecureBucket(this.properties, null).validate());
    }

    private static final class BaseSecureBucket extends Bucket implements SecureBucket {
        private final StorageProperties properties;

        private BaseSecureBucket(final Map<String, String> params, final ExaMetadata metadata) {
            this.properties = metadata == null ? new StorageProperties(params) : new StorageProperties(params, metadata);
        }

        @Override
        public StorageProperties properties() {
            return this.properties;
        }

        @Override
        public String bucketPath() {
            return "local_path";
        }

        @Override
        public scala.collection.immutable.Seq<String> getRequiredProperties() {
            return ScalaConverters.seqFromJava(List.of());
        }

        @Override
        public scala.collection.immutable.Seq<String> getSecureProperties() {
            return ScalaConverters.seqFromJava(List.of("accountSecretProperty"));
        }

        @Override
        public Configuration getConfiguration() {
            return new Configuration();
        }

        @Override
        public void validate() {
            validateRequiredProperties();
            validateConnectionProperties();
        }
    }
}
