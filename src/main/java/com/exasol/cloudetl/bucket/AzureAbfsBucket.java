package com.exasol.cloudetl.bucket;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/** Bucket implementation for Azure Data Lake Gen2 Storage. */
public final class AzureAbfsBucket extends AbstractConfiguredBucket implements SecureBucket {
    private static final String AZURE_ACCOUNT_NAME = "AZURE_ACCOUNT_NAME";
    private static final String AZURE_SECRET_KEY = "AZURE_SECRET_KEY";
    private static final Pattern AZURE_ABFS_PATH_REGEX = Pattern
            .compile("abfss?://(.*)@([^.]+).dfs.core.windows.net/(.*)$");

    /** Create a bucket. */
    public AzureAbfsBucket(final String path, final StorageProperties params) {
        super(path, params);
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof AzureAbfsBucket) && hasSameConfiguration((AzureAbfsBucket) obj));
    }

    @Override
    public int hashCode() {
        return configuredHashCode(AzureAbfsBucket.class);
    }

    @Override
    public scala.collection.immutable.Seq<String> getRequiredProperties() {
        return ScalaConverters.seqFromJava(List.of());
    }

    @Override
    public scala.collection.immutable.Seq<String> getSecureProperties() {
        return ScalaConverters.seqFromJava(List.of(AZURE_SECRET_KEY));
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
        conf.set("fs.abfs.impl", org.apache.hadoop.fs.azurebfs.AzureBlobFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.abfs.impl", org.apache.hadoop.fs.azurebfs.Abfs.class.getName());
        conf.set("fs.abfss.impl", org.apache.hadoop.fs.azurebfs.SecureAzureBlobFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.abfss.impl", org.apache.hadoop.fs.azurebfs.Abfss.class.getName());
        final StorageProperties mergedProperties = properties().hasNamedConnection() ? properties().merge(AZURE_ACCOUNT_NAME)
                : properties();
        final AccountAndContainer accountAndContainer = regexParsePath(bucketPath());
        final String accountName = mergedProperties.get(AZURE_ACCOUNT_NAME).isDefined()
                ? mergedProperties.get(AZURE_ACCOUNT_NAME).get()
                : accountAndContainer.accountName;
        conf.set("fs.azure.account.key." + accountName + ".dfs.core.windows.net",
                mergedProperties.getString(AZURE_SECRET_KEY));
        return conf;
    }

    private AccountAndContainer regexParsePath(final String path) {
        final Matcher matcher = AZURE_ABFS_PATH_REGEX.matcher(path);
        if (matcher.matches()) {
            return new AccountAndContainer(matcher.group(2), matcher.group(1));
        }
        throw new BucketValidationException(ExaError.messageBuilder("E-CSE-20")
                .message("Azure datalake storage path {{PATH}} scheme is not valid.", path)
                .mitigation("It should be either 'abfs' or 'abfss'.").toString());
    }

    private static final class AccountAndContainer {
        private final String accountName;

        private AccountAndContainer(final String accountName, final String containerName) {
            this.accountName = accountName;
        }
    }
}
