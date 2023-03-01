package com.exasol.cloudetl

import java.io.File
import java.nio.file.Paths

import com.exasol.containers.ExasolContainer
import com.exasol.dbbuilder.dialects.Column
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

trait BaseIntegrationTest extends AnyFunSuite with BeforeAndAfterAll with LazyLogging {
  private[this] val JAR_NAME_PATTERN = "exasol-cloud-storage-extension-"
  private[this] val DEFAULT_EXASOL_DOCKER_IMAGE = "7.1.18"

  val network = DockerNamedNetwork("it-tests", true)
  val exasolContainer = {
    val c: ExasolContainer[_] = new ExasolContainer(getExasolDockerImageVersion())
    c.withNetwork(network)
    c.withReuse(true)
    c
  }
  var factory: ExasolObjectFactory = _
  var schema: ExasolSchema = _
  var connection: java.sql.Connection = null
  val assembledJarName = getAssembledJarName()

  override def beforeAll(): Unit =
    exasolContainer.start()

  override def afterAll(): Unit = {
    if (connection != null) {
      connection.close()
    }
    exasolContainer.stop()
  }

  def prepareExasolDatabase(schemaName: String): Unit = {
    executeStmt(s"DROP SCHEMA IF EXISTS $schemaName CASCADE;")
    factory = new ExasolObjectFactory(getConnection())
    schema = factory.createSchema(schemaName)
    createImportDeploymentScripts()
    createExportDeploymentScripts()
    uploadJarToBucket()
  }

  def executeStmt(sql: String): Unit = {
    getConnection().createStatement().execute(sql)
    ()
  }

  def executeQuery(sql: String): java.sql.ResultSet =
    getConnection().createStatement().executeQuery(sql)

  private[this] def getAssembledJarName(): String =
    findFileOrDirectory("target/", JAR_NAME_PATTERN)

  private[this] def getConnection(): java.sql.Connection = {
    if (connection == null) {
      connection = exasolContainer.createConnection("")
    }
    connection
  }

  private[this] def createImportDeploymentScripts(): Unit = {
    val jarPath = s"/buckets/bfsdefault/default/$assembledJarName"
    schema
      .createUdfBuilder("IMPORT_PATH")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerFilesImportQueryGenerator", jarPath)
      .build()
    schema
      .createUdfBuilder("IMPORT_METADATA")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SCALAR)
      .emits(
        new Column("filename", "VARCHAR(2000)"),
        new Column("partition_index", "VARCHAR(100)"),
        new Column("start_index", "DECIMAL(36, 0)"),
        new Column("end_index", "DECIMAL(36, 0)")
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

  private[this] def createExportDeploymentScripts(): Unit = {
    val jarPath = s"/buckets/bfsdefault/default/$assembledJarName"
    schema
      .createUdfBuilder("EXPORT_PATH")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerTableExportQueryGenerator", jarPath)
      .build()
    schema
      .createUdfBuilder("EXPORT_TABLE")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits(new Column("rows_affected", "INT"))
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.DockerTableDataExporter", jarPath)
      .build()
    ()
  }

  private[this] def uploadJarToBucket(): Unit = {
    val jarPath = Paths.get("target", assembledJarName)
    exasolContainer.getDefaultBucket.uploadFile(jarPath, assembledJarName)
  }

  private[this] def findFileOrDirectory(searchDirectory: String, name: String): String = {
    val files = listDirectoryFiles(searchDirectory)
    val jarFile = files.find(_.getName.contains(name))
    jarFile match {
      case Some(jarFilename) => jarFilename.getName()
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

  private[this] def getExasolDockerImageVersion(): String = {
    val dockerVersion = System.getenv("EXASOL_DOCKER_VERSION")
    if (dockerVersion == null) {
      logger.info(
        s"No 'EXASOL_DOCKER_VERSION' environment variable is not set, " +
          s"using default '$DEFAULT_EXASOL_DOCKER_IMAGE' version."
      )
      DEFAULT_EXASOL_DOCKER_IMAGE
    } else {
      logger.info(s"Using docker '$dockerVersion' version from environment.")
      dockerVersion
    }
  }

}
