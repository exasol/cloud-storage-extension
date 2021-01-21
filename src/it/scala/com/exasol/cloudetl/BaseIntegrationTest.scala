package com.exasol.cloudetl

import java.io.File
import java.nio.file.Paths

import com.exasol.containers.ExasolContainer
import com.exasol.dbbuilder.dialects.Column
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript

import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.localstack.LocalStackContainer
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

trait BaseIntegrationTest extends AnyFunSuite with BeforeAndAfterAll {
  private[this] val JAR_DIRECTORY_PATTERN = "scala-"
  private[this] val JAR_NAME_PATTERN = "exasol-cloud-storage-extension-"

  private[this] val DEFAULT_EXASOL_DOCKER_IMAGE = "7.0.5"
  private[this] val DEFAULT_LOCALSTACK_DOCKER_IMAGE =
    DockerImageName.parse("localstack/localstack:0.12.5")

  val exasolContainer = new ExasolContainer(getExasolDockerImageVersion())
  val s3Container = new LocalStackContainer(DEFAULT_LOCALSTACK_DOCKER_IMAGE)
    .withServices(LocalStackContainer.Service.S3)
    .withReuse(true)
  val assembledJarName = getAssembledJarName()

  var schema: ExasolSchema = _
  var factory: ExasolObjectFactory = _

  def startContainers(): Unit = {
    exasolContainer.start()
    s3Container.start()
  }

  def prepareExasolDatabase(schemaName: String): Unit = {
    execute(s"DROP SCHEMA IF EXISTS $schemaName CASCADE;")
    factory = new ExasolObjectFactory(getConnection())
    schema = factory.createSchema(schemaName)
    createDeploymentScripts()
    createConnectionObject()
    uploadJarToBucket()
  }

  def execute(sql: String): Unit = {
    getConnection().createStatement().execute(sql)
    ()
  }

  def query(sql: String): java.sql.ResultSet =
    getConnection().createStatement().executeQuery(sql)

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
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerImportQueryGenerator", jarPath)
      .build()
    schema
      .createUdfBuilder("IMPORT_METADATA")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SCALAR)
      .emits(
        new Column("filename", "VARCHAR(2000)"),
        new Column("partition_index", "VARCHAR(100)")
      )
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerImportMetadataReader", jarPath)
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

  private[this] def createConnectionObject(): Unit = {
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
