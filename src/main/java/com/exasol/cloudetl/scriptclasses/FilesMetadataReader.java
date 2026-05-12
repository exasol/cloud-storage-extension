package com.exasol.cloudetl.scriptclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.emitter.FilesMetadataEmitter;
import com.exasol.cloudetl.parallelism.FixedUdfCountCalculator;
import com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator;
import com.exasol.cloudetl.storage.StorageProperties;

/** Reads file metadata. */
public final class FilesMetadataReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesMetadataReader.class);
    private static final int BUCKET_PATH_INDEX = 0;
    private static final int STORAGE_PROPERTIES_INDEX = 1;
    private static final int PARALLELISM_INDEX = 2;

    private FilesMetadataReader() {
        // Script class.
    }

    /** Run the metadata reader. */
    public static void run(final ExaMetadata metadata, final ExaIterator iterator)
            throws com.exasol.ExaIterationException, com.exasol.ExaDataTypeException {
        final String bucketPath = iterator.getString(BUCKET_PATH_INDEX);
        final StorageProperties storageProperties = StorageProperties.apply(iterator.getString(STORAGE_PROPERTIES_INDEX),
                metadata);
        final int parallelism = calculateParallelism(iterator.getInteger(PARALLELISM_INDEX), metadata, storageProperties);
        LOGGER.info("Reading metadata from bucket path '{}' with parallelism '{}'.", bucketPath, parallelism);
        new FilesMetadataEmitter(storageProperties, parallelism).emit(iterator);
    }

    private static int calculateParallelism(final int userRequestedParallelism, final ExaMetadata metadata,
            final StorageProperties storageProperties) {
        return Math.min(userRequestedParallelism, getUdfCount(metadata, storageProperties));
    }

    private static int getUdfCount(final ExaMetadata metadata, final StorageProperties storageProperties) {
        final int coresPerNode = Runtime.getRuntime().availableProcessors();
        if (storageProperties.hasUdfMemory()) {
            return new MemoryUdfCountCalculator(metadata, storageProperties.getUdfMemory()).getUdfCount(coresPerNode);
        }
        return new FixedUdfCountCalculator(metadata).getUdfCount(coresPerNode);
    }
}
