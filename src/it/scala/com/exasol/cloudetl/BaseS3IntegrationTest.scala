package com.exasol.cloudetl

import java.io.File

import com.exasol.dbbuilder.dialects.Table

import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.hadoop.fs.{Path => HPath}
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3

trait BaseS3IntegrationTest extends BaseIntegrationTest {

  val LOCALSTACK_DOCKER_IMAGE = DockerImageName.parse("localstack/localstack:0.12.15")
  val s3Container = new LocalStackContainer(LOCALSTACK_DOCKER_IMAGE)
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
      .withEndpointConfiguration(s3Container.getEndpointConfiguration(S3))
      .withCredentials(s3Container.getDefaultCredentialsProvider())
      .disableChunkedEncoding()
      .build()
    s3Endpoint = s3Container
      .getEndpointConfiguration(S3)
      .getServiceEndpoint()
      .replaceAll("127.0.0.1", getS3ContainerNetworkGatewayAddress())
  }

  def createS3ConnectionObject(): Unit = {
    val credentials = s3Container.getDefaultCredentialsProvider().getCredentials()
    val awsAccessKey = credentials.getAWSAccessKeyId()
    val awsSecretKey = credentials.getAWSSecretKey()
    val secret = s"S3_ACCESS_KEY=$awsAccessKey;S3_SECRET_KEY=$awsSecretKey"
    factory.createConnectionDefinition("S3_CONNECTION", "", "dummy_user", secret)
    ()
  }

  def createBucket(bucket: String): Unit = {
    s3.createBucket(new CreateBucketRequest(bucket))
    ()
  }

  def uploadFileToS3(bucket: String, file: HPath): Unit = {
    createBucket(bucket)
    val request = new PutObjectRequest(bucket, file.getName(), new File(file.toUri()))
    s3.putObject(request)
    Thread.sleep(3 * 1000)
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
          |CONNECTION_NAME          = 'S3_CONNECTION'
          |PARALLELISM              = 'nproc()';
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
