package com.exasol.cloudetl

import java.io.File

import com.exasol.dbbuilder.dialects.Table

import org.apache.hadoop.fs.{Path => HPath}
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model._

trait BaseS3IntegrationTest extends BaseIntegrationTest {
  val INT_MIN: Int = Int.MinValue
  val INT_MAX: Int = Int.MaxValue
  val LONG_MIN: Long = Long.MinValue
  val LONG_MAX: Long = Long.MaxValue

  val s3Container = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.2"))
    .withServices(S3)
    .withReuse(true)
  var s3: S3Client = _
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
    val endpoint = s3Container.getEndpointOverride(LocalStackContainer.Service.S3)
    val region = Region.of(s3Container.getRegion)
    val s3_config = S3Configuration
      .builder()
      .pathStyleAccessEnabled(true)
      .chunkedEncodingEnabled(false)
      .build()
    val s3_creds = AwsBasicCredentials.create(s3Container.getAccessKey, s3Container.getSecretKey)

    s3 = S3Client
      .builder()
      .region(region)
      .endpointOverride(endpoint)
      .serviceConfiguration(s3_config)
      .credentialsProvider(StaticCredentialsProvider.create(s3_creds))
      .build()

    s3Endpoint = endpoint.toString
      .replaceAll("127.0.0.1", getS3ContainerNetworkGatewayAddress())
  }

  def deleteBucketObjects(bucketName: String): Unit =
    listObjects(bucketName).forEach { s3_obj =>
      s3.deleteObject(
        DeleteObjectRequest
          .builder()
          .bucket(bucketName)
          .key(s3_obj.key())
          .build()
      )
      ()
    }

  def listObjects(bucketName: String): java.util.List[S3Object] =
    s3.listObjectsV2(
      ListObjectsV2Request
        .builder()
        .bucket(bucketName)
        .build()
    ).contents()

  def deleteBucket(bucketName: String): Unit = {
    s3.deleteBucket(
      DeleteBucketRequest
        .builder()
        .bucket(bucketName)
        .build()
    )
    ()
  }

  def createS3ConnectionObject(): Unit = {
    val secret = s"S3_ACCESS_KEY=${getAWSAccessKey()};S3_SECRET_KEY=${getAWSSecretKey()}"
    factory.createConnectionDefinition("S3_CONNECTION", "", "dummy_user", secret)
    ()
  }

  def getAWSAccessKey(): String = s3Container.getAccessKey

  def getAWSSecretKey(): String = s3Container.getSecretKey

  def doesBucketExists(bucket: String): Boolean =
    try {
      s3.headBucket(
        HeadBucketRequest
          .builder()
          .bucket(bucket)
          .build()
      )
      true
    } catch {
      case _: S3Exception => false
    }

  def uploadFileToS3(bucket: String, file: HPath): Unit = {
    if (!doesBucketExists(bucket)) {
      createBucket(bucket)
    }
    logger.debug(s"Uploading file $file to bucket $bucket")
    s3.putObject(
      PutObjectRequest
        .builder()
        .bucket(bucket)
        .key(file.getName)
        .build(),
      RequestBody.fromFile(new File(file.toUri))
    )
    ()
  }

  def createBucket(bucket: String): Unit = {
    s3.createBucket(
      CreateBucketRequest
        .builder()
        .bucket(bucket)
        .build()
    )
    ()
  }

  def importFromS3IntoExasol(
    schemaName: String,
    table: Table,
    bucket: String,
    file: String,
    dataFormat: String
  ): Unit = {
    val bucketPath = s"s3a://$bucket/$file"
    logger.info(s"Importing $bucketPath of format $dataFormat into table ${table.getFullyQualifiedName()}...")
    executeStmt(
      s"""|IMPORT INTO ${table.getFullyQualifiedName()}
          |FROM SCRIPT $schemaName.IMPORT_PATH WITH
          |BUCKET_PATH              = '$bucketPath'
          |DATA_FORMAT              = '$dataFormat'
          |S3_ENDPOINT              = '$s3Endpoint'
          |S3_CHANGE_DETECTION_MODE = 'none'
          |TRUNCATE_STRING          = 'true'
          |CONNECTION_NAME          = 'S3_CONNECTION'
          |PARALLELISM              = 'nproc()';
          """.stripMargin
    )
  }

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
