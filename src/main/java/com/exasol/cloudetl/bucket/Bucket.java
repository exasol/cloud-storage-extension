package com.exasol.cloudetl.bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.delta.DeltaLog;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.filesystem.FileSystemManager;
import com.exasol.cloudetl.storage.FileFormat;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;


/** Abstract representation of a cloud storage bucket path. */
public abstract class Bucket {
    private FileSystem fileSystem;

    /** @return bucket path */
    public abstract String bucketPath();

    /** @return storage properties */
    public abstract StorageProperties properties();

    /** @return required property keys */
    public abstract scala.collection.immutable.Seq<String> getRequiredProperties();

    /** @return Hadoop configuration */
    public abstract Configuration getConfiguration();

    /** Validate bucket properties. */
    public abstract void validate();

    /** Validate that all required properties exist. */
    protected final void validateRequiredProperties() {
        for (final String key : ScalaConverters.asJavaList(getRequiredProperties())) {
            if (!properties().containsKey(key)) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-2")
                        .message("Required {{KEY}} property value is missing.", key)
                        .mitigation("Please provide value for a key as parameter.").toString());
            }
        }
    }

    /** @return Hadoop filesystem for this bucket */
    public final FileSystem fileSystem() {
        if (this.fileSystem == null) {
            try {
                this.fileSystem = FileSystem.get(new Path(bucketPath()).toUri(), getConfiguration());
            } catch (final java.io.IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
        return this.fileSystem;
    }

    /** @return all paths in this bucket */
    public final scala.collection.immutable.Seq<Path> getPaths() {
        if (properties().getFileFormat() == FileFormat.DELTA) {
            return getPathsFromDeltaLog();
        }
        try {
            return new FileSystemManager(fileSystem()).getFiles(bucketPath());
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private scala.collection.immutable.Seq<Path> getPathsFromDeltaLog() {
        final SparkSession spark = createSparkSession();
        final String strippedBucketPath = stripTrailingStar(bucketPath());
        final DeltaLog deltaLog = DeltaLog.forTable(spark, strippedBucketPath);
        if (!deltaLog.tableExists()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("F-CSE-3")
                    .message("The provided path {{PATH}} is not a Delta formatted directory.", bucketPath())
                    .mitigation("Please use valid Delta format path.").toString());
        }
        final List<Path> paths = new ArrayList<>();
        final Object[] files = (Object[]) deltaLog.update(deltaLog.update$default$1(), deltaLog.update$default$2(),
                deltaLog.update$default$3()).allFiles().collect();
        for (final Object file : files) {
            paths.add(new Path(strippedBucketPath + "/" + ((org.apache.spark.sql.delta.actions.AddFile) file).path()));
        }
        return ScalaConverters.seqFromJava(paths);
    }

    private SparkSession createSparkSession() {
        final SparkSession spark = SparkSession.builder()
                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
                .config("spark.delta.logStore.class", properties().getDeltaFormatLogStoreClassName()).master("local[*]")
                .getOrCreate();
        for (final Entry<String, String> entry : getConfiguration()) {
            spark.sparkContext().hadoopConfiguration().set(entry.getKey(), entry.getValue());
        }
        return spark;
    }

    private String stripTrailingStar(final String path) {
        return path.endsWith("/*") ? path.substring(0, path.length() - 1) : path;
    }

    /** Create the specific bucket implementation for the storage path scheme. */
    public static Bucket create(final StorageProperties storageProperties) {
        final String path = storageProperties.getStoragePath();
        final String scheme = storageProperties.getStoragePathScheme();
        switch (scheme) {
        case "s3a":
            return new S3Bucket(path, storageProperties);
        case "gs":
            return new GCSBucket(path, storageProperties);
        case "abfs":
        case "abfss":
            return new AzureAbfsBucket(path, storageProperties);
        case "adl":
            return new AzureAdlsBucket(path, storageProperties);
        case "alluxio":
            return new AlluxioBucket(path, storageProperties);
        case "wasb":
        case "wasbs":
            return new AzureBlobBucket(path, storageProperties);
        case "hdfs":
            return new HDFSBucket(path, storageProperties);
        case "file":
            return new LocalBucket(path, storageProperties);
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("F-CSE-4")
                    .message("Provided path scheme {{SCHEME}} is not supported.", scheme)
                    .mitigation("Please check out the user guide for supported storage systems and their path schemes.")
                    .toString());
        }
    }
}
