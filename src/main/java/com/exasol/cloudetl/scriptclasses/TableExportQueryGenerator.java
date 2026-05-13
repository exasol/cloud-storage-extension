package com.exasol.cloudetl.scriptclasses;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.Path;

import com.exasol.ExaExportSpecification;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.helper.ExportParallelismCalculator;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/** Generates export SQL. */
public final class TableExportQueryGenerator {
    private TableExportQueryGenerator() {
        // Script class.
    }

    /** Generate SQL for an export spec. */
    public static String generateSqlForExportSpec(final ExaMetadata metadata, final ExaExportSpecification exportSpec) {
        final StorageProperties storageProperties = StorageProperties.apply(exportSpec.getParameters(), metadata);
        final Bucket bucket = Bucket.create(storageProperties);
        bucket.validate();
        deleteBucketPathIfRequired(bucket);
        final String bucketPath = bucket.bucketPath();
        final String parallelism = new ExportParallelismCalculator(metadata, storageProperties).getParallelism();
        final String storagePropertiesStr = storageProperties.mkString();
        final String scriptSchema = metadata.getScriptSchema();
        final List<String> srcColumns = getSourceColumns(exportSpec);
        final String srcColumnsStr = String.join(".", srcColumns);
        return "SELECT\n" + "  " + scriptSchema + ".EXPORT_TABLE(\n" + "    '" + bucketPath + "', '"
                + storagePropertiesStr + "', '" + srcColumnsStr + "', " + String.join(", ", srcColumns) + "\n" + ")\n"
                + "FROM\n" + "  DUAL\n" + "GROUP BY\n" + "  " + parallelism + ";\n";
    }

    private static void deleteBucketPathIfRequired(final Bucket bucket) {
        if (bucket.properties().isOverwrite()) {
            final org.apache.hadoop.fs.FileSystem fileSystem = bucket.fileSystem();
            final Path bucketPath = new Path(bucket.bucketPath());
            try {
                if (fileSystem.exists(bucketPath)) {
                    fileSystem.delete(bucketPath, true);
                }
            } catch (final java.io.IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
    }

    private static List<String> getSourceColumns(final ExaExportSpecification spec) {
        final List<String> result = new ArrayList<>();
        for (final String columnName : spec.getSourceColumnNames()) {
            result.add(getColumnName(columnName));
        }
        return result;
    }

    private static String getColumnName(final String columnName) {
        final String[] parts = columnName.split("\\.");
        if (parts.length == 1) {
            return parts[0];
        } else if (parts.length == 2) {
            return parts[1];
        }
        throw new TableExporterException(ExaError.messageBuilder("E-CSE-34")
                .message("Could not parse the column name from given column syntax {{COLUMN}}.")
                .parameter("COLUMN", columnName).toString());
    }
}
