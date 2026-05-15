package com.exasol.cloudetl.bucket;

import java.util.Map;
import java.util.stream.Stream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.cloudetl.storage.StorageProperties;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class BucketContractTest {
    @ParameterizedTest
    @MethodSource("bucketImplementations")
    void verifyBucketImplementationContracts(final Class<?> classType) throws Exception {
        final StorageProperties storageProperties1 = new StorageProperties(Map.of("a", "1"));
        final StorageProperties storageProperties2 = new StorageProperties(Map.of("c", "2"));
        final FileSystem localFilesystem = FileSystem.get(new Configuration());
        final FileSystem s3aFilesystem = FileSystem.get(new Path("s3a://tmp").toUri(), new Configuration());
        EqualsVerifier.forClass(classType)
                .withPrefabValues(FileSystem.class, localFilesystem, s3aFilesystem)
                .withPrefabValues(StorageProperties.class, storageProperties1, storageProperties2)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    private static Stream<Class<?>> bucketImplementations() {
        return Stream.of(AlluxioBucket.class, AzureAbfsBucket.class, AzureAdlsBucket.class, AzureBlobBucket.class,
                GCSBucket.class, HDFSBucket.class, LocalBucket.class, S3Bucket.class);
    }
}
