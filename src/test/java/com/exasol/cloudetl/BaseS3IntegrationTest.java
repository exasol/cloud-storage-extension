package com.exasol.cloudetl;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import com.exasol.dbbuilder.dialects.Table;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseS3IntegrationTest extends BaseIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(BaseS3IntegrationTest.class.getName());
    protected final int intMin = Integer.MIN_VALUE;
    protected final int intMax = Integer.MAX_VALUE;
    protected final long longMin = Long.MIN_VALUE;
    protected final long longMax = Long.MAX_VALUE;
    protected final LocalStackContainer s3Container = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.2"))
            .withServices(Service.S3).withReuse(true);
    protected S3Client s3;
    protected String s3Endpoint;

    @BeforeAll
    void baseS3BeforeAll() {
        this.s3Container.start();
        prepareS3Client();
    }

    @AfterAll
    void baseS3AfterAll() {
        this.s3Container.stop();
    }

    protected void prepareS3Client() {
        final var endpoint = this.s3Container.getEndpointOverride(Service.S3);
        final Region region = Region.of(this.s3Container.getRegion());
        final S3Configuration s3Config = S3Configuration.builder().pathStyleAccessEnabled(true)
                .chunkedEncodingEnabled(false).build();
        final AwsBasicCredentials s3Creds = AwsBasicCredentials.create(this.s3Container.getAccessKey(),
                this.s3Container.getSecretKey());
        this.s3 = S3Client.builder().region(region).endpointOverride(endpoint).serviceConfiguration(s3Config)
                .credentialsProvider(StaticCredentialsProvider.create(s3Creds)).build();
        this.s3Endpoint = endpoint.toString().replaceAll("127.0.0.1", getS3ContainerNetworkGatewayAddress());
    }

    protected void deleteBucketObjects(final String bucketName) {
        listObjects(bucketName).forEach(s3Object -> this.s3
                .deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build()));
    }

    protected List<S3Object> listObjects(final String bucketName) {
        return this.s3.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build()).contents();
    }

    protected void deleteBucket(final String bucketName) {
        this.s3.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
    }

    protected void createS3ConnectionObject() {
        final String secret = "S3_ACCESS_KEY=" + getAwsAccessKey() + ";S3_SECRET_KEY=" + getAwsSecretKey();
        this.factory.createConnectionDefinition("S3_CONNECTION", "", "dummy_user", secret);
    }

    protected String getAwsAccessKey() {
        return this.s3Container.getAccessKey();
    }

    protected String getAwsSecretKey() {
        return this.s3Container.getSecretKey();
    }

    protected boolean doesBucketExists(final String bucket) {
        try {
            this.s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            return true;
        } catch (final S3Exception exception) {
            return false;
        }
    }

    protected void uploadFileToS3(final String bucket, final Path file) {
        if (!doesBucketExists(bucket)) {
            createBucket(bucket);
        }
        LOGGER.info(() -> "Uploading file " + file + " to bucket " + bucket);
        this.s3.putObject(PutObjectRequest.builder().bucket(bucket).key(file.getName()).build(),
                RequestBody.fromFile(new File(file.toUri())));
    }

    protected void createBucket(final String bucket) {
        this.s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
    }

    protected void importFromS3IntoExasol(final String schemaName, final Table table, final String bucket,
            final String file, final String dataFormat) {
        final String bucketPath = "s3a://" + bucket + "/" + file;
        executeStmt(String.format("IMPORT INTO %s\n" +
"FROM SCRIPT %s.IMPORT_PATH WITH\n" +
"BUCKET_PATH              = '%s'\n" +
"DATA_FORMAT              = '%s'\n" +
"S3_ENDPOINT              = '%s'\n" +
"S3_CHANGE_DETECTION_MODE = 'none'\n" +
"TRUNCATE_STRING          = 'true'\n" +
"CONNECTION_NAME          = 'S3_CONNECTION'\n" +
"PARALLELISM              = 'nproc()';\n" +
"\n", table.getFullyQualifiedName(), schemaName, bucketPath, dataFormat, this.s3Endpoint));
    }

    protected void exportIntoS3(final String schemaName, final String tableName, final String bucket) {
        executeStmt(String.format("EXPORT %s\n" +
"INTO SCRIPT %s.EXPORT_PATH WITH\n" +
"BUCKET_PATH     = 's3a://%s/'\n" +
"DATA_FORMAT     = 'PARQUET'\n" +
"S3_ENDPOINT     = '%s'\n" +
"CONNECTION_NAME = 'S3_CONNECTION'\n" +
"PARALLELISM     = 'iproc()';\n" +
"\n", tableName, schemaName, bucket, this.s3Endpoint));
    }

    private String getS3ContainerNetworkGatewayAddress() {
        return this.s3Container.getContainerInfo().getNetworkSettings().getNetworks().values().iterator().next().getGateway();
    }
}
