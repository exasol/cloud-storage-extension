package com.exasol.cloudetl.bucket;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/** Bucket implementation for AWS S3. */
public final class S3Bucket extends AbstractConfiguredBucket implements SecureBucket {
    private static final String S3_ENDPOINT = "S3_ENDPOINT";
    private static final String S3_ENDPOINT_REGION = "S3_ENDPOINT_REGION";
    private static final String S3_ACCESS_KEY = "S3_ACCESS_KEY";
    private static final String S3_SECRET_KEY = "S3_SECRET_KEY";
    private static final String S3_SESSION_TOKEN = "S3_SESSION_TOKEN";
    private static final String S3_SSL_ENABLED = "S3_SSL_ENABLED";
    private static final String S3_PATH_STYLE_ACCESS = "S3_PATH_STYLE_ACCESS";
    private static final String S3_CHANGE_DETECTION_MODE = "S3_CHANGE_DETECTION_MODE";

    /** Create a bucket. */
    public S3Bucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of(S3_ENDPOINT));
    }

    @Override
    public scala.collection.immutable.Seq<String> getSecureProperties() {
        return ScalaConverters.seqFromJava(List.of(S3_SECRET_KEY, S3_SESSION_TOKEN));
    }

    @Override
    public void validate() {
        validateBucketPath();
        validateRequiredProperties();
        validateConnectionProperties();
    }

    private void validateBucketPath() {
        if (new Path(bucketPath()).toUri().getHost() == null) {
            throw new BucketValidationException(ExaError.messageBuilder("E-CSE-28")
                    .message("The S3 bucket {{bucketPath}} path does not obey bucket naming rules.", bucketPath())
                    .mitigation("Please check that S3 bucket name does not contain underscores, end with a number or a hyphen.")
                    .mitigation("Please read the bucket naming rules "
                            + "'https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html'.")
                    .toString());
        }
    }

    private boolean isAnonymousAwsParams(final StorageProperties properties) {
        return properties.getString(S3_ACCESS_KEY).isEmpty() && properties.getString(S3_SECRET_KEY).isEmpty();
    }

    @Override
    public Configuration getConfiguration() {
        validate();
        final Configuration conf = new Configuration();
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.set("fs.s3a.impl", org.apache.hadoop.fs.s3a.S3AFileSystem.class.getName());
        conf.set("fs.s3a.endpoint", properties().getString(S3_ENDPOINT));
        setIfPresent(conf, "fs.s3a.endpoint.region", S3_ENDPOINT_REGION);
        setIfPresent(conf, "fs.s3a.change.detection.mode", S3_CHANGE_DETECTION_MODE);
        setIfPresent(conf, "fs.s3a.path.style.access", S3_PATH_STYLE_ACCESS);
        setIfPresent(conf, "fs.s3a.connection.ssl.enabled", S3_SSL_ENABLED);
        final StorageProperties mergedProperties = properties().hasNamedConnection() ? properties().merge(S3_ACCESS_KEY)
                : properties();
        if (isAnonymousAwsParams(mergedProperties)) {
            conf.set("fs.s3a.aws.credentials.provider",
                    org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider.class.getName());
        } else {
            conf.set("fs.s3a.access.key", mergedProperties.getString(S3_ACCESS_KEY));
            conf.set("fs.s3a.secret.key", mergedProperties.getString(S3_SECRET_KEY));
            if (mergedProperties.containsKey(S3_SESSION_TOKEN)) {
                conf.set("fs.s3a.aws.credentials.provider",
                        org.apache.hadoop.fs.s3a.TemporaryAWSCredentialsProvider.class.getName());
                conf.set("fs.s3a.session.token", mergedProperties.getString(S3_SESSION_TOKEN));
            }
        }
        if (properties().getProxyHost().isDefined()) {
            conf.set("fs.s3a.proxy.host", properties().getProxyHost().get());
            if (properties().getProxyPort().isDefined()) {
                conf.set("fs.s3a.proxy.port", properties().getProxyPort().get());
            }
            if (properties().getProxyUsername().isDefined()) {
                conf.set("fs.s3a.proxy.username", properties().getProxyUsername().get());
            }
            if (properties().getProxyPassword().isDefined()) {
                conf.set("fs.s3a.proxy.password", properties().getProxyPassword().get());
            }
        }
        return conf;
    }

    private void setIfPresent(final Configuration conf, final String hadoopKey, final String propertyKey) {
        if (properties().containsKey(propertyKey)) {
            conf.set(hadoopKey, properties().getString(propertyKey));
        }
    }
}
