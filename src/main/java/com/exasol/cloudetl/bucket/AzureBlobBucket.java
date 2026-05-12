package com.exasol.cloudetl.bucket;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/** Bucket implementation for Azure Blob Storage. */
public final class AzureBlobBucket extends AbstractConfiguredBucket implements SecureBucket {
    private static final String AZURE_ACCOUNT_NAME = "AZURE_ACCOUNT_NAME";
    private static final String AZURE_CONTAINER_NAME = "AZURE_CONTAINER_NAME";
    private static final String AZURE_SAS_TOKEN = "AZURE_SAS_TOKEN";
    private static final String AZURE_SECRET_KEY = "AZURE_SECRET_KEY";
    private static final Pattern AZURE_BLOB_PATH_REGEX = Pattern
            .compile("wasbs?://(.*)@([^.]+).blob.core.windows.net/(.*)$");

    /** Create a bucket. */
    public AzureBlobBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof AzureBlobBucket) && hasSameConfiguration((AzureBlobBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(AzureBlobBucket.class);
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of());
    }

    @Override
    public scala.collection.immutable.Seq<String> getSecureProperties() {
        return ScalaConverters.seqFromJava(List.of(AZURE_SECRET_KEY, AZURE_SAS_TOKEN));
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
        conf.set("fs.azure", org.apache.hadoop.fs.azure.NativeAzureFileSystem.class.getName());
        conf.set("fs.wasb.impl", org.apache.hadoop.fs.azure.NativeAzureFileSystem.class.getName());
        conf.set("fs.wasbs.impl", org.apache.hadoop.fs.azure.NativeAzureFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.wasb.impl", org.apache.hadoop.fs.azure.Wasb.class.getName());
        conf.set("fs.AbstractFileSystem.wasbs.impl", org.apache.hadoop.fs.azure.Wasbs.class.getName());
        final StorageProperties mergedProperties = properties().hasNamedConnection() ? properties().merge(AZURE_ACCOUNT_NAME)
                : properties();
        final AccountAndContainer accountAndContainer = regexParsePath(bucketPath());
        final String accountName = mergedProperties.get(AZURE_ACCOUNT_NAME).isDefined()
                ? mergedProperties.get(AZURE_ACCOUNT_NAME).get()
                : accountAndContainer.accountName;
        if (mergedProperties.containsKey(AZURE_SAS_TOKEN)) {
            final String containerName = mergedProperties.get(AZURE_CONTAINER_NAME).isDefined()
                    ? mergedProperties.get(AZURE_CONTAINER_NAME).get()
                    : accountAndContainer.containerName;
            conf.set("fs.azure.sas." + containerName + "." + accountName + ".blob.core.windows.net",
                    mergedProperties.getString(AZURE_SAS_TOKEN));
        } else {
            conf.set("fs.azure.account.key." + accountName + ".blob.core.windows.net",
                    mergedProperties.getString(AZURE_SECRET_KEY));
        }
        return conf;
    }

    private AccountAndContainer regexParsePath(final String path) {
        final Matcher matcher = AZURE_BLOB_PATH_REGEX.matcher(path);
        if (matcher.matches()) {
            return new AccountAndContainer(matcher.group(2), matcher.group(1));
        }
        throw new BucketValidationException(ExaError.messageBuilder("E-CSE-19")
                .message("Azure blob storage path {{PATH}} scheme is not valid.", path)
                .mitigation("It should be either 'wasb' or 'wasbs'.").toString());
    }

    private static final class AccountAndContainer {
        private final String accountName;
        private final String containerName;

        private AccountAndContainer(final String accountName, final String containerName) {
            this.accountName = accountName;
            this.containerName = containerName;
        }
    }
}
