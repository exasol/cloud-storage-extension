package com.exasol.cloudetl

import java.io.File
import java.nio.file.Paths
import java.sql.Connection
import java.time.Duration
import java.time.Instant

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

  val DEFAULT_EXASOL_DOCKER_IMAGE = "7.1.8"
  val network = DockerNamedNetwork("it-tests", true)
  val exasolContainer = {
    val c: ExasolContainer[_] = new ExasolContainer("7.1.8")
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
    executeStmt(s"DROP SCHEMA IF EXISTS $schemaName CASCADE")
    factory = new ExasolObjectFactory(getConnection())
    logger.info("Creating schema " + schemaName)
    schema = factory.createSchema(schemaName)
    createImportDeploymentScripts()
    createExportDeploymentScripts()
    uploadJarToBucket()
  }

  def executeStmt(sql: String): Unit = {
    logger.info(s"Executing statement '$sql'...")
    val start = Instant.now()
    try {
      getConnection().createStatement().execute(sql)
      logger.info(s"Statement finished after ${Duration.between(start, Instant.now())}")
    } catch {
      case exception: Exception =>
        throw new IllegalStateException(s"Failed executing SQL '$sql': ${exception.getMessage()}", exception)
    }
    ()
  }

  def executeQuery(sql: String): java.sql.ResultSet =
    getConnection().createStatement().executeQuery(sql)

  private[this] def getAssembledJarName(): String =
    findFileOrDirectory("target/", JAR_NAME_PATTERN)

  private[this] def getConnection(): java.sql.Connection = {
    if (connection == null) {
      logger.info("Creating new JDBC connection")
      connection = exasolContainer.createConnection("")
      configureUdfRemoteLog(connection)
    }
    connection
  }

  def configureUdfRemoteLog(connection: Connection): Unit = {
    // val host = System.getProperty("com.exasol.virtualschema.debug.host")
    // val port = System.getProperty("com.exasol.virtualschema.debug.port")
    val host = "192.168.56.6"
    val port = "3000"
    if (host != null && port != null) {
      val stmt = s"ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS='$host:$port'"
      logger.info(s"Enabling remote log by executing '$stmt'")
      val _ = connection
        .createStatement()
        .execute(stmt)
    }
  }

  private[this] def createImportDeploymentScripts(): Unit = {
    val jarPath = s"/buckets/bfsdefault/default/$assembledJarName"
    schema
      .createUdfBuilder("IMPORT_PATH")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator", jarPath)
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
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesMetadataReader", jarPath)
      .build()
    schema
      .createUdfBuilder("IMPORT_FILES")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits()
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesDataImporter", jarPath)
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
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator", jarPath)
      .build()
    schema
      .createUdfBuilder("EXPORT_TABLE")
      .language(UdfScript.Language.JAVA)
      .inputType(UdfScript.InputType.SET)
      .emits(new Column("rows_affected", "INT"))
      .bucketFsContent("com.exasol.cloudetl.scriptclasses.TableDataExporter", jarPath)
      .build()
    ()
  }

  private[this] def uploadJarToBucket(): Unit = {
    val jarPath = Paths.get("target", assembledJarName).toAbsolutePath()
    logger.info(s"Uploading JAR $jarPath to bucket at $assembledJarName...")
    exasolContainer.getDefaultBucket.uploadFile(jarPath, assembledJarName)
    logger.info(s"Upload to $assembledJarName finished.")
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
    val dockerVersion = System.getenv("EXASOL_DB_VERSION")
    if (dockerVersion == null) {
      DEFAULT_EXASOL_DOCKER_IMAGE
    } else {
      dockerVersion
    }
  }
}
