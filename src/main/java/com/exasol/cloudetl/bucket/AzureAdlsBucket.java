package com.exasol.cloudetl.bucket;

import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;

/** Bucket implementation for Azure Data Lake Storage. */
public final class AzureAdlsBucket extends AbstractConfiguredBucket implements SecureBucket {
    private static final String AZURE_CLIENT_ID = "AZURE_CLIENT_ID";
    private static final String AZURE_CLIENT_SECRET = "AZURE_CLIENT_SECRET";
    private static final String AZURE_DIRECTORY_ID = "AZURE_DIRECTORY_ID";

    /** Create a bucket. */
    public AzureAdlsBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of());
    }

    @Override
    public scala.collection.immutable.Seq<String> getSecureProperties() {
        return ScalaConverters.seqFromJava(List.of(AZURE_CLIENT_ID, AZURE_CLIENT_SECRET, AZURE_DIRECTORY_ID));
    }

    @Override
    public void validate() {
        validateRequiredProperties();
        validateConnectionProperties();
    }

    @Override
    public Configuration getConfiguration() {
        validate();
        final Configuration conf = new Configuration();
        final StorageProperties mergedProperties = properties().hasNamedConnection() ? properties().merge(AZURE_CLIENT_ID)
                : properties();
        final String clientId = mergedProperties.getString(AZURE_CLIENT_ID);
        final String clientSecret = mergedProperties.getString(AZURE_CLIENT_SECRET);
        final String directoryId = mergedProperties.getString(AZURE_DIRECTORY_ID);
        conf.set("fs.adl.impl", org.apache.hadoop.fs.adl.AdlFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.adl.impl", org.apache.hadoop.fs.adl.Adl.class.getName());
        conf.set("dfs.adls.oauth2.access.token.provider.type", "ClientCredential");
        conf.set("dfs.adls.oauth2.client.id", clientId);
        conf.set("dfs.adls.oauth2.credential", clientSecret);
        conf.set("dfs.adls.oauth2.refresh.url", "https://login.microsoftonline.com/" + directoryId + "/oauth2/token");
        return conf;
    }
}
