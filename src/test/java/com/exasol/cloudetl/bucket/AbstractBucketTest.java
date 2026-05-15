package com.exasol.cloudetl.bucket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import com.exasol.*;
import com.exasol.cloudetl.storage.StorageProperties;

abstract class AbstractBucketTest {
    protected static final String PATH = "BUCKET_PATH";
    protected static final String FORMAT = "DATA_FORMAT";
    protected Map<String, String> properties;

    @BeforeEach
    void abstractBucketBeforeEach() {
        this.properties = Map.of();
    }

    protected final Bucket getBucket(final Map<String, String> params) {
        return Bucket.create(new StorageProperties(params));
    }

    protected final Bucket getBucket(final Map<String, String> params, final ExaMetadata exaMetadata) {
        return Bucket.create(new StorageProperties(params, exaMetadata));
    }

    protected final ExaMetadata mockConnectionInfo(final String username, final String password) {
        final ExaMetadata metadata = mock(ExaMetadata.class);
        final ExaConnectionInformation connectionInformation = new ExaConnectionInformation() {
            @Override
            public ConnectionType getType() {
                return ConnectionType.PASSWORD;
            }

            @Override
            public String getAddress() {
                return "";
            }

            @Override
            public String getUser() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }
        };
        try {
            when(metadata.getConnection("connection_info")).thenReturn(connectionInformation);
        } catch (final ExaConnectionAccessException exception) {
            throw new IllegalStateException(exception);
        }
        return metadata;
    }

    protected final void assertNoConnectionName(final ThrowingRunnable runnable) {
        final BucketValidationException thrown = assertThrows(BucketValidationException.class, runnable);
        final String message = thrown.getMessage();
        assertTrue(message.contains("No CONNECTION_NAME property is defined"));
        assertTrue(message.contains("Please use connection object to provide access credentials"));
    }

    protected final void assertForbiddenProperty(final ThrowingRunnable runnable) {
        final BucketValidationException thrown = assertThrows(BucketValidationException.class, runnable);
        final String message = thrown.getMessage();
        assertTrue(message.contains("Using credentials as parameters is forbidded"));
        assertTrue(message.contains("Please use an Exasol named connection object"));
    }

    @FunctionalInterface
    protected interface ThrowingRunnable extends org.junit.jupiter.api.function.Executable {
        @Override
        void execute();
    }
}
