package com.exasol.cloudetl

import java.io.File
import java.lang.Long

import com.exasol.dbbuilder.dialects.Table

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model._
import org.apache.hadoop.fs.{Path => HPath}
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.utility.DockerImageName

trait BaseS3IntegrationTest extends BaseIntegrationTest {
  val INT_MIN = Integer.MIN_VALUE
  val INT_MAX = Integer.MAX_VALUE
  val LONG_MIN = Long.MIN_VALUE
  val LONG_MAX = Long.MAX_VALUE

  val s3Container = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.14"))
    .withServices(S3)
    .withReuse(true)
  var s3: AmazonS3 = _
  var s3Endpoint: String = _

  override def beforeAll(): Unit = {
    super.beforeAll()
    s3Container.start()
    prepareS3Client()
  }

  override def afterAll(): Unit = {
    s3Container.stop()
    super.afterAll()
  }

  def prepareS3Client(): Unit = {
    s3 = AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(getS3EndpointConfiguration())
      .withCredentials(
        new AWSStaticCredentialsProvider(
          new BasicAWSCredentials(s3Container.getAccessKey(), s3Container.getSecretKey())
        )
      )
      .disableChunkedEncoding()
      .build()

    s3Endpoint = getS3EndpointConfiguration()
      .getServiceEndpoint()
      .replaceAll("127.0.0.1", getS3ContainerNetworkGatewayAddress())
  }

  def deleteBucketObjects(bucketName: String): Unit =
    listObjects(bucketName).forEach(summary => s3.deleteObject(bucketName, summary.getKey()))

  def listObjects(bucketName: String): java.util.List[S3ObjectSummary] =
    s3.listObjects(bucketName).getObjectSummaries()

  def deleteBucket(bucketName: String): Unit =
    s3.deleteBucket(bucketName)

  private[this] def getS3EndpointConfiguration(): AwsClientBuilder.EndpointConfiguration =
    new AwsClientBuilder.EndpointConfiguration(
      s3Container.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
      s3Container.getRegion()
    )

  def createS3ConnectionObject(): Unit = {
    val secret = s"S3_ACCESS_KEY=${getAWSAccessKey()};S3_SECRET_KEY=${getAWSSecretKey()}"
    factory.createConnectionDefinition("S3_CONNECTION", "", "dummy_user", secret)
    ()
  }

  def getAWSAccessKey(): String = s3Container.getAccessKey()

  def getAWSSecretKey(): String = s3Container.getSecretKey()

  def uploadFileToS3(bucket: String, file: HPath): Unit = {
    createBucket(bucket)
    val request = new PutObjectRequest(bucket, file.getName(), new File(file.toUri()))
    s3.putObject(request)
    ()
  }

  def createBucket(bucket: String): Unit = {
    s3.createBucket(new CreateBucketRequest(bucket))
    ()
  }

  def importFromS3IntoExasol(schemaName: String, table: Table, bucket: String, file: String, dataFormat: String): Unit =
    executeStmt(
      s"""|IMPORT INTO ${table.getFullyQualifiedName()}
          |FROM SCRIPT $schemaName.IMPORT_PATH WITH
          |BUCKET_PATH              = 's3a://$bucket/$file'
          |DATA_FORMAT              = '$dataFormat'
          |S3_ENDPOINT              = '$s3Endpoint'
          |S3_CHANGE_DETECTION_MODE = 'none'
          |TRUNCATE_STRING          = 'true'
          |CONNECTION_NAME          = 'S3_CONNECTION'
          |PARALLELISM              = 'nproc()';
        """.stripMargin
    )

  def exportIntoS3(schemaName: String, tableName: String, bucket: String): Unit =
    executeStmt(
      s"""|EXPORT $tableName
          |INTO SCRIPT $schemaName.EXPORT_PATH WITH
          |BUCKET_PATH     = 's3a://$bucket/'
          |DATA_FORMAT     = 'PARQUET'
          |S3_ENDPOINT     = '$s3Endpoint'
          |CONNECTION_NAME = 'S3_CONNECTION'
          |PARALLELISM     = 'iproc()';
      """.stripMargin
    )

  private[this] def getS3ContainerNetworkGatewayAddress(): String =
    s3Container
      .getContainerInfo()
      .getNetworkSettings()
      .getNetworks()
      .values()
      .iterator()
      .next()
      .getGateway()

}
