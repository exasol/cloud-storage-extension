package com.exasol.cloudetl.scriptclasses;

import com.exasol.ExaImportSpecification;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.storage.StorageProperties;

/** Generates import SQL. */
public final class FilesImportQueryGenerator {
    private static final String DEFAULT_PARALLELISM = "65536";

    private FilesImportQueryGenerator() {
        // Script class.
    }

    /** Generate SQL for an import spec. */
    public static String generateSqlForImportSpec(final ExaMetadata metadata,
            final ExaImportSpecification importSpecification) {
        final StorageProperties storageProperties = StorageProperties.apply(importSpecification.getParameters());
        final Bucket bucket = Bucket.create(storageProperties);
        bucket.validate();
        final String scriptSchema = metadata.getScriptSchema();
        final String bucketPath = bucket.bucketPath();
        final String parallelism = storageProperties.getParallelism().isDefined()
                ? storageProperties.getParallelism().get()
                : DEFAULT_PARALLELISM;
        final String storagePropertiesAsString = storageProperties.mkString();
        return "SELECT\n" + "  " + scriptSchema + ".IMPORT_FILES(\n" + "    '" + bucketPath + "', '"
                + storagePropertiesAsString + "', filename, start_index, end_index\n" + ")\n" + "FROM (\n"
                + "  SELECT " + scriptSchema + ".IMPORT_METADATA(\n" + "    '" + bucketPath + "', '"
                + storagePropertiesAsString + "', " + parallelism + "\n" + "  )\n" + ")\n" + "GROUP BY\n"
                + "  partition_index;\n";
    }
}
