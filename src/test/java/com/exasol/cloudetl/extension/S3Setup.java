package com.exasol.cloudetl.extension;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;

// TODO: this class heavily overlaps with BaseS3IntegrationTest.scala
class S3Setup implements AutoCloseable {
    private final LocalStackContainer container;
    private final S3Client client;

    private S3Setup(final LocalStackContainer container) {
        this.container = container;
        this.client = createS3Client();
    }

    static S3Setup create() {
        final LocalStackContainer container = new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:2.2")).withServices(Service.S3).withReuse(true);
        container.start();
        return new S3Setup(container);
    }

    S3Client createS3Client() {
        final Region region = Region.of(this.container.getRegion());
        final URI endpoint = this.container.getEndpointOverride(Service.S3);
        final S3Configuration config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .chunkedEncodingEnabled(false)
                .build();
        final AwsBasicCredentials creds = AwsBasicCredentials.create(
                this.container.getAccessKey(),
                this.container.getSecretKey());

        return S3Client.builder()
                .region(region)
                .endpointOverride(endpoint)
                .serviceConfiguration(config)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    String getS3Endpoint() {
        final URI endpoint = this.container.getEndpointOverride(Service.S3);
        final String s3ContainerNetworkGatewayAddress = this.container.getContainerInfo().getNetworkSettings()
                .getNetworks().values().iterator().next().getGateway();
        assert s3ContainerNetworkGatewayAddress != null;
        return endpoint.toString().replaceAll("127.0.0.1", s3ContainerNetworkGatewayAddress);
    }

    String getSecret() {
        return "S3_ACCESS_KEY=" + this.container.getAccessKey() + ";S3_SECRET_KEY=" + this.container.getSecretKey();
    }

    String createBucket() {
        final String uniqueBucketName = "testing-bucket-" + System.currentTimeMillis();
        this.client.createBucket(
                CreateBucketRequest.builder()
                        .bucket(uniqueBucketName).build()
        );
        return uniqueBucketName;
    }

    void deleteBucket(final String bucket) {
        final ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket).build();
        this.client.listObjectsV2(request).contents()
                .forEach(object -> this.client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(bucket)
                                .key(object.key())
                                .build()
                        ));
        this.client.deleteBucket(
                DeleteBucketRequest.builder()
                        .bucket(bucket).build()
        );
    }

    @Override
    public void close() {
        this.container.close();
    }
}
