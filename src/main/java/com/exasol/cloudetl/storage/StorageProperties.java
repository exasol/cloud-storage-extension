package com.exasol.cloudetl.storage;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.fs.Path;

import com.exasol.ExaMetadata;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.common.AbstractProperties;
import com.exasol.common.PropertiesParser;
import com.exasol.errorreporting.ExaError;

import scala.Option;

/**
 * Storage import and export UDF properties.
 */
public class StorageProperties extends AbstractProperties {
    /** Separator for property entries. */
    public static final String STORAGE_PROPERTY_SEPARATOR = " -> ";
    /** Separator for key-value pairs. */
    public static final String STORAGE_KEY_VALUE_SEPARATOR = ";";
    /** Bucket path property key. */
    public static final String BUCKET_PATH = "BUCKET_PATH";
    /** Data format property key. */
    public static final String DATA_FORMAT = "DATA_FORMAT";
    /** Parallelism property key. */
    public static final String PARALLELISM = "PARALLELISM";
    /** UDF memory property key. */
    public static final String UDF_MEMORY = "UDF_MEMORY";
    /** Overwrite property key. */
    public static final String OVERWRITE = "OVERWRITE";
    /** Parquet chunk size property key. */
    public static final String CHUNK_SIZE = "CHUNK_SIZE";
    /** Parquet lowercase-schema property key. */
    public static final String PARQUET_LOWERCASE_SCHEMA = "PARQUET_LOWERCASE_SCHEMA";
    /** Proxy host property key. */
    public static final String PROXY_HOST = "PROXY_HOST";
    /** Proxy port property key. */
    public static final String PROXY_PORT = "PROXY_PORT";
    /** Proxy username property key. */
    public static final String PROXY_USERNAME = "PROXY_USERNAME";
    /** Proxy password property key. */
    public static final String PROXY_PASSWORD = "PROXY_PASSWORD";

    private static final long DEFAULT_CHUNK_SIZE = 64L * 1024L * 1024L;
    private static final String AZURE_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.AzureLogStore";
    private static final String S3_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore";
    private static final String HDFS_DELTA_LOG_STORE_CLASS = "org.apache.spark.sql.delta.storage.HDFSLogStore";
    private static final PropertiesParser PARSER = PropertiesParser.apply(STORAGE_KEY_VALUE_SEPARATOR,
            STORAGE_PROPERTY_SEPARATOR);

    private final scala.collection.immutable.Map<String, String> properties;
    private final Option<ExaMetadata> exaMetadata;

    /**
     * Create storage properties.
     *
     * @param properties key-value properties
     * @param exaMetadata optional metadata
     */
    public StorageProperties(final scala.collection.immutable.Map<String, String> properties,
            final Option<ExaMetadata> exaMetadata) {
        super(properties);
        this.properties = properties;
        this.exaMetadata = exaMetadata;
    }

    /** Create storage properties without metadata. */
    public StorageProperties(final scala.collection.immutable.Map<String, String> properties) {
        this(properties, Option.empty());
    }

    /** Create storage properties from Java properties. */
    public StorageProperties(final Map<String, String> properties) {
        this(ScalaConverters.mapFromJava(properties), Option.empty());
    }

    /** Create storage properties from Java properties with metadata. */
    public StorageProperties(final Map<String, String> properties, final ExaMetadata metadata) {
        this(ScalaConverters.mapFromJava(properties), Option.apply(metadata));
    }

    /** Factory for Java callers. */
    public static StorageProperties apply(final Map<String, String> params) {
        return new StorageProperties(params);
    }

    /** Factory for Java callers. */
    public static StorageProperties apply(final Map<String, String> params, final ExaMetadata metadata) {
        return new StorageProperties(params, metadata);
    }

    /** Factory for Java callers. */
    public static StorageProperties apply(final String string) {
        return new StorageProperties(PARSER.mapFromString(string), Option.empty());
    }

    /** Factory for Java callers. */
    public static StorageProperties apply(final String string, final ExaMetadata metadata) {
        return new StorageProperties(PARSER.mapFromString(string), Option.apply(metadata));
    }

    /** @return storage path */
    public final String getStoragePath() {
        return getString(BUCKET_PATH);
    }

    /** @return storage path scheme */
    public final String getStoragePathScheme() {
        return new Path(getStoragePath()).toUri().getScheme();
    }

    /** @return Delta LogStore implementation class name */
    public final String getDeltaFormatLogStoreClassName() {
        switch (getStoragePathScheme()) {
        case "abfs":
        case "abfss":
        case "adl":
        case "wasb":
        case "wasbs":
            return AZURE_DELTA_LOG_STORE_CLASS;
        case "gs":
            throw new UnsupportedOperationException(ExaError.messageBuilder("E-CSE-16")
                    .message("Delta format LogStore API is not supported in Google Cloud Storage yet.").toString());
        case "s3a":
            return S3_DELTA_LOG_STORE_CLASS;
        default:
            return HDFS_DELTA_LOG_STORE_CLASS;
        }
    }

    /** @return selected file format */
    public final FileFormat getFileFormat() {
        return FileFormat.fromString(getString(DATA_FORMAT));
    }

    /** @return chunk size */
    public final long getChunkSize() {
        return Long.parseLong(ScalaConverters.optionOrElse(get(CHUNK_SIZE), Long.toString(DEFAULT_CHUNK_SIZE)));
    }

    /** @return user-provided parallelism */
    public final Option<String> getParallelism() {
        return get(PARALLELISM);
    }

    /** @return {@code true} if UDF memory is set */
    public final boolean hasUdfMemory() {
        return containsKey(UDF_MEMORY);
    }

    /** @return UDF memory in bytes */
    public final long getUdfMemory() {
        return Long.parseLong(ScalaConverters.optionOrElse(get(UDF_MEMORY), "500")) * 1_000_000L;
    }

    /** @return {@code true} if overwrite is enabled */
    public final boolean isOverwrite() {
        return isEnabled(OVERWRITE);
    }

    /** @return {@code true} if Parquet schema fields should be lower-case */
    public final boolean isParquetLowercaseSchema() {
        return Boolean.parseBoolean(ScalaConverters.optionOrElse(get(PARQUET_LOWERCASE_SCHEMA), "true"));
    }

    /** @return proxy host */
    public final Option<String> getProxyHost() {
        return get(PROXY_HOST);
    }

    /** @return proxy port */
    public final Option<String> getProxyPort() {
        return get(PROXY_PORT);
    }

    /** @return proxy username */
    public final Option<String> getProxyUsername() {
        return get(PROXY_USERNAME);
    }

    /** @return proxy password */
    public final Option<String> getProxyPassword() {
        return get(PROXY_PASSWORD);
    }

    /** Merge properties from the configured named connection. */
    public final StorageProperties merge(final String accountName) {
        final Map<String, String> merged = new LinkedHashMap<>(ScalaConverters.javaMapCopy(this.properties));
        merged.putAll(ScalaConverters.javaMapCopy(parseConnectionInfo(accountName, this.exaMetadata)));
        return new StorageProperties(ScalaConverters.mapFromJava(merged), this.exaMetadata);
    }

    /** Get parsed named-connection properties. */
    public final scala.collection.immutable.Map<String, String> getConnectionProperties(final String keyForUsername) {
        return parseConnectionInfo(keyForUsername, this.exaMetadata);
    }

    /** @return sorted property string */
    public final String mkString() {
        return mkString(STORAGE_KEY_VALUE_SEPARATOR, STORAGE_PROPERTY_SEPARATOR);
    }
}
