package com.exasol.cloudetl.kinesis

import java.io.File
import java.nio.file.Paths
import java.sql.ResultSet

import com.exasol.containers.{ExasolContainer, ExasolContainerConstants}

import com.amazonaws.SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY
import com.amazonaws.services.kinesis.{AmazonKinesis, AmazonKinesisClientBuilder}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.testcontainers.containers.localstack.LocalStackContainer

trait KinesisAbstractIntegrationTest extends AnyFunSuite with BeforeAndAfterAll {
  val JAR_DIRECTORY_PATTERN = "scala-"
  val JAR_NAME_PATTERN = "cloud-storage-etl-udfs-"
  val DOCKER_IP_ADDRESS = "172.17.0.1"
  val TEST_SCHEMA_NAME = "kinesis_schema"
  var assembledJarName: String = _

  val exasolContainer = new ExasolContainer(
    ExasolContainerConstants.EXASOL_DOCKER_IMAGE_REFERENCE
  )
  val kinesisLocalStack: LocalStackContainer =
    new LocalStackContainer().withServices(LocalStackContainer.Service.KINESIS)

  private[this] var connection: java.sql.Connection = _
  var statement: java.sql.Statement = _
  private[kinesis] var kinesisClient: AmazonKinesis = _

  private[kinesis] def prepareContainers(): Unit = {
    exasolContainer.start()
    connection = exasolContainer.createConnectionForUser(
      exasolContainer.getUsername,
      exasolContainer.getPassword
    )
    statement = connection.createStatement()
    kinesisLocalStack.start()
    kinesisClient = AmazonKinesisClientBuilder.standard
      .withEndpointConfiguration(
        kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
      )
      .withCredentials(kinesisLocalStack.getDefaultCredentialsProvider)
      .build
    System.setProperty(AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true")
    ()
  }

  private[kinesis] def setupExasol(): Unit = {
    assembledJarName = findAssembledJarName()
    uploadJarToBucket(assembledJarName)
    statement.execute(s"CREATE SCHEMA $TEST_SCHEMA_NAME")
    statement.execute(s"OPEN SCHEMA $TEST_SCHEMA_NAME")
    ()
  }

  private[this] def uploadJarToBucket(assembledJarName: String): Unit = {
    val pathToJar = Paths.get("target", getScalaDirectory(), assembledJarName)
    exasolContainer.getDefaultBucket.uploadFile(pathToJar, assembledJarName)
  }

  private[this] def getScalaDirectory(): String =
    findFileOrDirectory("target", JAR_DIRECTORY_PATTERN)

  private[this] def findFileOrDirectory(directoryToSearch: String, name: String): String = {
    val files = listDirectoryFiles(directoryToSearch)
    val jarFile = files.find(_.getName.contains(name))
    jarFile match {
      case Some(jarFilename) => jarFilename.getName
      case None =>
        throw new IllegalArgumentException(
          "Cannot find a file or a directory with pattern" + name + " in " + directoryToSearch
        )
    }
  }

  private[kinesis] def createKinesisStream(streamName: String, shardsCounter: Integer): Unit = {
    kinesisClient.createStream(streamName, shardsCounter)
    // We have to wait until stream is ready to be accessed.
    Thread.sleep(30 * 1000)
  }

  def findAssembledJarName(): String = {
    val scalaDirectory = getScalaDirectory()
    findFileOrDirectory("target/" + scalaDirectory, JAR_NAME_PATTERN)
  }

  private[this] def listDirectoryFiles(directoryName: String): List[File] = {
    val directory = new File(directoryName)
    if (directory.exists && directory.isDirectory) {
      directory.listFiles.toList
    } else {
      List.empty[File]
    }
  }

  private[kinesis] def createKinesisMetadataScript(): Unit = {
    statement.execute(
      s"""CREATE OR REPLACE JAVA SET SCRIPT KINESIS_METADATA (...)
         |EMITS (KINESIS_SHARD_ID VARCHAR(130), SHARD_SEQUENCE_NUMBER VARCHAR(2000)) AS
         |     %jvmoption -Dcom.amazonaws.sdk.disableCbor=true;
         |     %scriptclass com.exasol.cloudetl.kinesis.KinesisShardsMetadataReader;
         |     %jar /buckets/bfsdefault/default/$assembledJarName;
         |/
         |""".stripMargin
    )
    ()
  }

  private[kinesis] def createKinesisImportScript(emits: String): Unit = {
    statement.execute(
      s"""CREATE OR REPLACE JAVA SET SCRIPT KINESIS_IMPORT (...)
         |EMITS ($emits) AS
         |     %jvmoption -Dcom.amazonaws.sdk.disableCbor=true;
         |     %scriptclass com.exasol.cloudetl.kinesis.KinesisShardDataImporter;
         |     %jar /buckets/bfsdefault/default/$assembledJarName;
         |/
         |""".stripMargin
    )
    ()
  }

  private[kinesis] def collectResultSet[T](resultSet: ResultSet)(func: ResultSet => T): List[T] =
    new Iterator[T] {
      def hasNext = resultSet.next()
      def next() = func(resultSet)
    }.toList

  override final def afterAll(): Unit = {
    connection.close()
    statement.close()
    kinesisClient.shutdown()
    exasolContainer.stop()
    kinesisLocalStack.stop()
  }
}
