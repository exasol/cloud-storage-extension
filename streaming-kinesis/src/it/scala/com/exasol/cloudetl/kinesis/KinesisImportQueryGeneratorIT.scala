package com.exasol.cloudetl.kinesis

import java.nio.ByteBuffer
import java.sql.ResultSet

import com.exasol.cloudetl.kinesis.KinesisConstants.{
  KINESIS_SHARD_ID_COLUMN_NAME,
  SHARD_SEQUENCE_NUMBER_COLUMN_NAME
}

import org.scalatest.BeforeAndAfterEach
import org.testcontainers.containers.localstack.LocalStackContainer

class KinesisImportQueryGeneratorIT
    extends KinesisAbstractIntegrationTest
    with BeforeAndAfterEach {
  final val TEST_TABLE_NAME = "kinesis_table"

  override final def beforeAll(): Unit = {
    prepareContainers()
    setupExasol()
    createKinesisMetadataScript()
    createKinesisImportScript("...")
    val credentials = kinesisLocalStack.getDefaultCredentialsProvider.getCredentials
    statement.execute(
      s"""CREATE OR REPLACE CONNECTION KINESIS_CONNECTION
         | TO '' USER '' IDENTIFIED BY
         | 'AWS_ACCESS_KEY=${credentials.getAWSAccessKeyId};
         | AWS_SECRET_KEY=${credentials.getAWSSecretKey};'""".stripMargin
        .replace("'\n", "")
    )
    statement.execute(
      s"""CREATE OR REPLACE JAVA SET SCRIPT KINESIS_PATH (...)
         |EMITS (...) AS
         |     %jvmoption -Dcom.amazonaws.sdk.disableCbor=true;
         |     %scriptclass com.exasol.cloudetl.kinesis.KinesisImportQueryGenerator;
         |     %jar /buckets/bfsdefault/default/${findAssembledJarName()};
         |/
         |""".stripMargin
    )
    ()
  }

  test("KinesisImportQueryGenerator runs with credentials") {
    createTable()
    val streamName = "Stream_1"
    createKinesisStream(streamName, 1)
    val partitionKey = "partitionKey-1"
    putRecordIntoStream(17, 147, "WARN", partitionKey, streamName)
    putRecordIntoStream(20, 15, "OK", partitionKey, streamName)
    executeKinesisPathScriptWithoutConnection(streamName)
    val expected = List(
      (17, 147, "WARN", "shardId-000000000000", true),
      (20, 15, "OK", "shardId-000000000000", true)
    )
    assertResultSet(expected)
    executeKinesisPathScriptWithoutConnection(streamName)
    assertResultSet(expected)
  }

  private[this] def assertResultSet(expected: List[(Int, Int, String, String, Boolean)]): Unit = {
    val resultSet = statement.executeQuery(s"SELECT * FROM $TEST_TABLE_NAME")
    val values = collectResultSet(resultSet)(extractTuple)
    assert(values === expected)
    assert(resultSet.next() === false)
    ()
  }

  private[this] def extractTuple(resultSet: ResultSet): (Int, Int, String, String, Boolean) =
    (
      resultSet.getInt("sensorId"),
      resultSet.getInt("currentTemperature"),
      resultSet.getString("status"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  private[this] def putRecordIntoStream(
    sensorId: Int,
    currentTemperature: Int,
    status: String,
    partitionKey: String,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"sensorId": $sensorId,
         | "currentTemperature": $currentTemperature,
         | "status": "$status"
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def createTable(): Unit = {
    val createTableDDL =
      s"""|CREATE OR REPLACE TABLE $TEST_TABLE_NAME(
          |sensorId DECIMAL(18,0),
          |currentTemperature DECIMAL(18,0),
          |status VARCHAR(100),
          |kinesis_shard_id VARCHAR(2000),
          |shard_sequence_number VARCHAR(2000)
          |)
        """.stripMargin
    statement.execute(createTableDDL)
    ()
  }

  test("KinesisImportQueryGenerator runs with connection name") {
    createTable()
    val streamName = "Stream_2"
    createKinesisStream(streamName, 2)
    putRecordIntoStream(17, 147, "WARN", "partitionKey-1", streamName)
    putRecordIntoStream(20, 15, "OK", "partitionKey-1", streamName)
    executeKinesisPathScriptWithConnection(streamName)
    val expected = List(
      (17, 147, "WARN", "shardId-000000000000", true),
      (20, 15, "OK", "shardId-000000000000", true)
    )
    assertResultSet(expected)

    putRecordIntoStream(67, 154, "FAIL", "partitionKey-2", streamName)
    putRecordIntoStream(54, 4, "OK", "partitionKey-2", streamName)
    executeKinesisPathScriptWithConnection(streamName)
    val expected2 = List(
      (17, 147, "WARN", "shardId-000000000000", true),
      (20, 15, "OK", "shardId-000000000000", true),
      (67, 154, "FAIL", "shardId-000000000001", true),
      (54, 4, "OK", "shardId-000000000001", true)
    )
    assertResultSet(expected2)
  }

  test("KinesisImportQueryGenerator imports all data types") {
    val streamName = "Stream_3"
    createKinesisStream(streamName, 1)
    putRecordSWithAllDataTypesIntoStream(streamName)
    createTableWithAllDataTypes()
    executeKinesisPathScriptWithConnection(streamName)
    val expected = List(
      (
        "[\"first\",\"second\"]",
        true,
        10,
        10.55,
        "{\"firstNestedValue\":10,\"secondNestedValue\":\"second\"}",
        null,
        "shardId-000000000000",
        true
      )
    )
    assertResultSetAllDataTypes(expected)
  }

  private[kinesis] def putRecordSWithAllDataTypesIntoStream(
    streamName: String
  ): Unit = {
    val partitionKey = "shardId-000000000000"
    val recordData =
      s"""{"array_val": ["first", "second"],
         | "bool_val": true,
         | "int_val": 10,
         | "double_val": 10.55,
         | "object_val": {"firstNestedValue": 10,"secondNestedValue": "second"},
         | "string_val": null
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def createTableWithAllDataTypes(): Unit = {
    val createTableDDL =
      s"""|CREATE OR REPLACE TABLE $TEST_TABLE_NAME(
          |array_val VARCHAR(100),
          |bool_val BOOLEAN,
          |int_val DECIMAL(18,0),
          |double_val DOUBLE PRECISION,
          |object_val VARCHAR(100),
          |string_val VARCHAR(100),
          |kinesis_shard_id VARCHAR(2000),
          |shard_sequence_number VARCHAR(2000)
          |)
        """.stripMargin
    statement.execute(createTableDDL)
    ()
  }

  private def assertResultSetAllDataTypes(
    expected: List[(String, Boolean, Int, Double, String, String, String, Boolean)]
  ): Unit = {
    val resultSet = statement.executeQuery(s"SELECT * FROM $TEST_TABLE_NAME")
    val values = collectResultSet(resultSet)(extractTupleAllDataTypes)
    assert(values === expected)
    assert(resultSet.next() === false)
    ()
  }

  private[this] def extractTupleAllDataTypes(
    resultSet: ResultSet
  ): (String, Boolean, Int, Double, String, String, String, Boolean) =
    (
      resultSet.getString("array_val"),
      resultSet.getBoolean("bool_val"),
      resultSet.getInt("int_val"),
      resultSet.getDouble("double_val"),
      resultSet.getString("object_val"),
      resultSet.getString("string_val"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  private[this] def executeKinesisPathScriptWithConnection(streamName: String): Unit = {
    val endpointConfiguration =
      kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
    val endpointInsideDocker =
      endpointConfiguration.getServiceEndpoint.replaceAll("127.0.0.1", DOCKER_IP_ADDRESS)
    statement.execute(
      s"""IMPORT INTO $TEST_TABLE_NAME
         |FROM SCRIPT KINESIS_PATH WITH
         |  TABLE_NAME     = '$TEST_TABLE_NAME'
         |  CONNECTION_NAME  = 'KINESIS_CONNECTION'
         |  STREAM_NAME    = '$streamName'
         |  REGION          = '${endpointConfiguration.getSigningRegion}'
         |  AWS_SERVICE_ENDPOINT = '$endpointInsideDocker'
      """.stripMargin
    )
    ()
  }

  private[this] def executeKinesisPathScriptWithoutConnection(streamName: String): Unit = {
    val endpointConfiguration =
      kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
    val endpointInsideDocker =
      endpointConfiguration.getServiceEndpoint.replaceAll("127.0.0.1", DOCKER_IP_ADDRESS)
    val credentials = kinesisLocalStack.getDefaultCredentialsProvider.getCredentials
    statement.execute(
      s"""IMPORT INTO $TEST_TABLE_NAME
         |FROM SCRIPT KINESIS_PATH WITH
         |  TABLE_NAME     = '$TEST_TABLE_NAME'
         |  AWS_ACCESS_KEY  = '${credentials.getAWSAccessKeyId}'
         |  AWS_SECRET_KEY  = '${credentials.getAWSSecretKey}'
         |  STREAM_NAME    = '$streamName'
         |  REGION          = '${endpointConfiguration.getSigningRegion}'
         |  AWS_SERVICE_ENDPOINT = '$endpointInsideDocker'
      """.stripMargin
    )
    ()
  }
}
