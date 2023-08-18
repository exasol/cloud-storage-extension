package com.exasol.cloudetl.extension;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

class S3Setup implements AutoCloseable {
    private final LocalStackContainer container;
    private final AmazonS3 client;

    private S3Setup(final LocalStackContainer container) {
        this.container = container;
        this.client = createS3Client();
    }

    static S3Setup create() {
        @SuppressWarnings("resource")
        final LocalStackContainer container = new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:2.2")).withServices(Service.S3).withReuse(true);
        container.start();
        return new S3Setup(container);
    }

    AmazonS3 createS3Client() {
        return AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(getS3EndpointConfig())
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(this.container.getAccessKey(), this.container.getSecretKey())))
                .disableChunkedEncoding().build();
    }

    AwsClientBuilder.EndpointConfiguration getS3EndpointConfig() {
        return new AwsClientBuilder.EndpointConfiguration(
                this.container.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
                this.container.getRegion());
    }

    String getS3Endpoint() {
        final String s3ContainerNetworkGatewayAddress = this.container.getContainerInfo().getNetworkSettings()
                .getNetworks().values().iterator().next().getGateway();
        return getS3EndpointConfig().getServiceEndpoint().replaceAll("127.0.0.1", s3ContainerNetworkGatewayAddress);
    }

    String getSecret() {
        return "S3_ACCESS_KEY=" + this.container.getAccessKey() + ";S3_SECRET_KEY=" + this.container.getSecretKey();
    }

    String createBucket() {
        final String uniqueBucketName = "testing-bucket-" + System.currentTimeMillis();
        this.client.createBucket(uniqueBucketName);
        return uniqueBucketName;
    }

    void deleteBucket(final String bucket) {
        this.client.listObjects(bucket).getObjectSummaries()
                .forEach(object -> this.client.deleteObject(object.getBucketName(), object.getKey()));
        this.client.deleteBucket(bucket);
    }

    @Override
    public void close() {
        this.container.close();
    }
}
