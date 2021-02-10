package com.exasol.cloudetl

import java.io.File
import java.nio.file.Paths

import com.exasol.containers.ExasolContainer
import com.exasol.dbbuilder.dialects.Column
import com.exasol.dbbuilder.dialects.Table
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript

import org.apache.hadoop.fs.{Path => HPath}
import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

trait BaseIntegrationTest extends AnyFunSuite with BeforeAndAfterAll {
  private[this] val JAR_DIRECTORY_PATTERN = "scala-"
  private[this] val JAR_NAME_PATTERN = "exasol-cloud-storage-extension-"

  private[this] val DEFAULT_EXASOL_DOCKER_IMAGE = "7.0.6"
  private[this] val DEFAULT_LOCALSTACK_DOCKER_IMAGE =
    DockerImageName.parse("localstack/localstack:0.12.5")

  val exasolContainer = new ExasolContainer(getExasolDockerImageVersion())
  val s3Container = new LocalStackContainer(DEFAULT_LOCALSTACK_DOCKER_IMAGE)
    .withServices(S3)
    .withReuse(true)
  val assembledJarName = getAssembledJarName()

  var schema: ExasolSchema = _
  var s3: AmazonS3 = _

  def startContainers(): Unit = {
    exasolContainer.start()
    s3Container.start()
  }

  def prepareExasolDatabase(schemaName: String): Unit = {
    executeStmt(s"DROP SCHEMA IF EXISTS $schemaName CASCADE;")
    val factory = new ExasolObjectFactory(getConnection())
    schema = factory.createSchema(schemaName)
    createDeploymentScripts()
    createConnectionObject(factory)
    uploadJarToBucket()
  }

  def prepareS3Client(): Unit =
    s3 = AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(s3Container.getEndpointConfiguration(S3))
      .withCredentials(s3Container.getDefaultCredentialsProvider())
      .disableChunkedEncoding()
      .build()

  def executeStmt(sql: String): Unit = {
    getConnection().createStatement().execute(sql)
    ()
  }

  def executeQuery(sql: String): java.sql.ResultSet =
    getConnection().createStatement().executeQuery(sql)

  def importIntoExasol(
    schemaName: String,
    table: Table,
    bucket: String,
    file: String,
    dataFormat: String
  ): Unit = {
    val s3Endpoint = s3Container
      .getEndpointConfiguration(S3)
      .getServiceEndpoint()
      .replaceAll("127.0.0.1", "172.17.0.1")
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
  }

  def uploadFileToS3(bucket: String, file: HPath): Unit = {
    s3.createBucket(new CreateBucketRequest(bucket))
    val request = new PutObjectRequest(bucket, file.getName(), new File(file.toUri()))
    s3.putObject(request)
    Thread.sleep(3 * 1000)
    ()
  }

  private[this] def getAssembledJarName(): String = {
    val jarDir = findFileOrDirectory("target", JAR_DIRECTORY_PATTERN)
    findFileOrDirectory("target/" + jarDir, JAR_NAME_PATTERN)
  }

  private[this] def getConnection(): java.sql.Connection =
    exasolContainer.createConnection("")

  private[this] def createDeploymentScripts(): Unit = {
    val jarPath = s"/buckets/bfsdefault/default/$assembledJarName"
    schema
      .createUdfBuilder("IMPORT_PATH")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent(
        "com.exasol.cloudetl.scriptclasses.DockerFilesImportQueryGenerator",
        jarPath
      )
      .build()
    schema
      .createUdfBuilder("IMPORT_METADATA")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SCALAR)
      .emits(
        new Column("filename", "VARCHAR(2000)"),
        new Column("partition_index", "VARCHAR(100)")
      )
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerFilesMetadataReader", jarPath)
      .build()
    schema
      .createUdfBuilder("IMPORT_FILES")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerFilesDataImporter", jarPath)
      .build()
    ()
  }

  private[this] def createConnectionObject(factory: ExasolObjectFactory): Unit = {
    val credentials = s3Container.getDefaultCredentialsProvider().getCredentials()
    val awsAccessKey = credentials.getAWSAccessKeyId()
    val awsSecretKey = credentials.getAWSSecretKey()
    val secret = s"S3_ACCESS_KEY=$awsAccessKey;S3_SECRET_KEY=$awsSecretKey"
    factory.createConnectionDefinition("S3_CONNECTION", "", "dummy_user", secret)
    ()
  }

  private[this] def uploadJarToBucket(): Unit = {
    val jarDir = findFileOrDirectory("target", JAR_DIRECTORY_PATTERN)
    val jarPath = Paths.get("target", jarDir, assembledJarName)
    exasolContainer.getDefaultBucket.uploadFile(jarPath, assembledJarName)
  }

  private[this] def findFileOrDirectory(searchDirectory: String, name: String): String = {
    val files = listDirectoryFiles(searchDirectory)
    val jarFile = files.find(_.getName.contains(name))
    jarFile match {
      case Some(jarFilename) => jarFilename.getName
      case None =>
        throw new IllegalArgumentException(
          s"Cannot find a file or a directory with pattern '$name' in '$searchDirectory'"
        )
    }
  }

  private[this] def listDirectoryFiles(directoryName: String): List[File] = {
    val directory = new File(directoryName)
    if (directory.exists && directory.isDirectory) {
      directory.listFiles.toList
    } else {
      List.empty[File]
    }
  }

  private[this] def getExasolDockerImageVersion(): String =
    System.getProperty("EXASOL_DOCKER_VERSION", DEFAULT_EXASOL_DOCKER_IMAGE)

  override final def afterAll(): Unit = {
    exasolContainer.stop()
    s3Container.stop()
  }

}
