package com.exasol.cloudetl.bucket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.common.CommonConstants;
import com.exasol.errorreporting.ExaError;

/** Bucket implementation for Google Cloud Storage. */
public final class GCSBucket extends AbstractConfiguredBucket {
    private static final String GCS_PROJECT_ID = "GCS_PROJECT_ID";
    private static final String GCS_KEYFILE_PATH = "GCS_KEYFILE_PATH";
    private static final String GCS_KEYFILE_CONTENT = "GCS_KEYFILE_CONTENT";

    /** Create a bucket. */
    public GCSBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof GCSBucket) && hasSameConfiguration((GCSBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(GCSBucket.class);
    }

    @Override
    public void validate() {
        validateRequiredProperties();
        validateKeyfileProperties();
        validateConnectionProperties();
    }

    private void validateKeyfileProperties() {
        if (properties().containsKey(GCS_KEYFILE_PATH) && properties().hasNamedConnection()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-30")
                    .message("Both properties {{GCS_KEYFILE_PATH}} and {{CONNECTION_NAME}} are specified.",
                            GCS_KEYFILE_PATH, CommonConstants.CONNECTION_NAME())
                    .mitigation("Please specify only one of them.").toString());
        }
        if (!properties().containsKey(GCS_KEYFILE_PATH) && !properties().hasNamedConnection()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-31")
                    .message("Neither of properties {{GCS_KEYFILE_PATH}} or {{CONNECTION_NAME}} is specified.",
                            GCS_KEYFILE_PATH, CommonConstants.CONNECTION_NAME())
                    .mitigation("Please specify exactly one of them.").toString());
        }
    }

    private void validateConnectionProperties() {
        if (properties().hasNamedConnection()) {
            final String connectionName = properties().getString(CommonConstants.CONNECTION_NAME());
            final String content = getKeyfileContentFromConnection(connectionName);
            if (!content.trim().startsWith("{")) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-33")
                        .message(
                                "The connection {{connectionName}} does not contain valid JSON in property {{GCS_KEYFILE_CONTENT}}.",
                                connectionName, GCS_KEYFILE_CONTENT)
                        .mitigation("Please check the connection properties.").toString());
            }
        }
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of(GCS_PROJECT_ID));
    }

    @Override
    public Configuration getConfiguration() {
        validate();
        final Configuration conf = new Configuration();
        conf.set("fs.gs.impl", com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem.class.getName());
        conf.setBoolean("fs.gs.auth.service.account.enable", true);
        conf.set("fs.gs.project.id", properties().getString(GCS_PROJECT_ID));
        conf.set("fs.gs.auth.type", "SERVICE_ACCOUNT_JSON_KEYFILE");
        conf.set("fs.gs.auth.service.account.json.keyfile", getKeyFilePath());
        if (properties().getProxyHost().isDefined()) {
            final String proxyHost = properties().getProxyHost().get();
            if (properties().getProxyPort().isDefined()) {
                conf.set("fs.gs.proxy.address", proxyHost + ":" + properties().getProxyPort().get());
            }
            properties().getProxyUsername().foreach(username -> {
                conf.set("fs.gs.proxy.username", username);
                return scala.runtime.BoxedUnit.UNIT;
            });
            properties().getProxyPassword().foreach(password -> {
                conf.set("fs.gs.proxy.password", password);
                return scala.runtime.BoxedUnit.UNIT;
            });
        }
        return conf;
    }

    private String getKeyFilePath() {
        if (properties().containsKey(GCS_KEYFILE_PATH)) {
            return properties().getString(GCS_KEYFILE_PATH);
        }
        final String connectionName = properties().getString(CommonConstants.CONNECTION_NAME());
        return writeToTempFile(getKeyfileContentFromConnection(connectionName));
    }

    private String getKeyfileContentFromConnection(final String connectionName) {
        final Map<String, String> map = ScalaConverters.javaMapCopy(properties().getConnectionProperties(null));
        final String content = map.get(GCS_KEYFILE_CONTENT);
        if (content != null) {
            return content;
        }
        throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-32")
                .message("The connection {{connectionName}} does not contain {{GCS_KEYFILE_CONTENT}} property.",
                        connectionName, GCS_KEYFILE_CONTENT)
                .mitigation("Please check the connection properties.").toString());
    }

    private String writeToTempFile(final String jsonContent) {
        try {
            final java.nio.file.Path tempPath = Files.createTempFile("gcs-credentials", ".json");
            Files.writeString(tempPath, jsonContent, StandardCharsets.UTF_8);
            return tempPath.toString();
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
