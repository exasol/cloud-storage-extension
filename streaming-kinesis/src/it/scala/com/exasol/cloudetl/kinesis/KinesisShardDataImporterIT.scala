package com.exasol.cloudetl.kinesis

import java.nio.ByteBuffer
import java.sql.ResultSet

import com.exasol.cloudetl.kinesis.KinesisConstants.{
  KINESIS_SHARD_ID_COLUMN_NAME,
  SHARD_SEQUENCE_NUMBER_COLUMN_NAME
}

import org.testcontainers.containers.localstack.LocalStackContainer

class KinesisShardDataImporterIT extends KinesisAbstractIntegrationTest {
  private val partitionKey = "partitionKey-1"
  private val shardId = "shardId-000000000000"

  override final def beforeAll(): Unit = {
    prepareContainers()
    setupExasol()
    val credentials = kinesisLocalStack.getDefaultCredentialsProvider.getCredentials
    statement.execute(
      s"""CREATE OR REPLACE CONNECTION KINESIS_CONNECTION
         | TO '' USER '' IDENTIFIED BY
         | 'AWS_ACCESS_KEY=${credentials.getAWSAccessKeyId};
         | AWS_SECRET_KEY=${credentials.getAWSSecretKey};'""".stripMargin
        .replace("'\n", "")
    )
    ()
  }

  test("returns number data from a shard") {
    val streamName = "Test_stream_numbers"
    createKinesisStream(streamName, 1)
    putRecordIntoStream(17, 25.3, streamName)
    putRecordIntoStream(20, 21.0, streamName)
    val columns =
      """sensorId DECIMAL(18,0),
        |currentTemperature DOUBLE PRECISION,
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    val expected = List(
      (17, 25.3, shardId, true),
      (20, 21.0, shardId, true)
    )
    val resultSet = this.executeKinesisImportScript(streamName)
    val actual = collectResultSet(resultSet)(extractTupleNumbers)
    assert(actual === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordIntoStream(
    sensorId: Int,
    currentTemperature: Double,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"sensorId": $sensorId,
         | "currentTemperature": $currentTemperature
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def extractTupleNumbers(resultSet: ResultSet): (Int, Double, String, Boolean) =
    (
      resultSet.getInt("sensorId"),
      resultSet.getDouble("currentTemperature"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  test("returns string data from a shard") {
    val streamName = "Test_stream_strings"
    createKinesisStream(streamName, 1)
    putRecordIntoStream("1", "WARN", streamName)
    putRecordIntoStream("2", "OK", streamName)
    val columns =
      """sensorId CHAR(1),
        |status VARCHAR(100),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    val resultSet = this.executeKinesisImportScript(streamName)
    val expected = List(
      ("1", "WARN", shardId, true),
      ("2", "OK", shardId, true)
    )
    val values = collectResultSet(resultSet)(extractTupleStrings)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordIntoStream(
    sensorId: String,
    status: String,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"sensorId": "$sensorId",
         | "status": "$status"
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def extractTupleStrings(resultSet: ResultSet): (String, String, String, Boolean) =
    (
      resultSet.getString("sensorId"),
      resultSet.getString("status"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  test("returns boolean data from a shard") {
    val streamName = "Test_stream_booleans"
    createKinesisStream(streamName, 1)
    putRecordIntoStream(true, false, streamName)
    putRecordIntoStream(false, true, streamName)
    val columns =
      """first_sensor_status BOOLEAN,
        |second_sensor_status BOOLEAN,
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    val resultSet = this.executeKinesisImportScript(streamName)
    val expected = List(
      (true, false, shardId, true),
      (false, true, shardId, true)
    )
    val values = collectResultSet(resultSet)(extractTupleBooleans)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordIntoStream(
    firstSensorStatus: Boolean,
    secondSensorStatus: Boolean,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"first_sensor_status": $firstSensorStatus,
         | "second_sensor_status": $secondSensorStatus
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def extractTupleBooleans(
    resultSet: ResultSet
  ): (Boolean, Boolean, String, Boolean) =
    (
      resultSet.getBoolean("first_sensor_status"),
      resultSet.getBoolean("second_sensor_status"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  test("returns nested data from a shard") {
    val streamName = "Test_stream_nested"
    createKinesisStream(streamName, 1)
    val columns =
      """sensorId DECIMAL(18,0),
        |statuses VARCHAR(1000),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    putRecordWithNestedDataIntoStream(17, 35, 14, 29, partitionKey, streamName)
    putRecordWithNestedDataIntoStream(20, 25, 11, 16, partitionKey, streamName)
    val resultSet = this.executeKinesisImportScript(streamName)
    val expected = List(
      (17, "{\"max\":35,\"min\":14,\"cur\":29}", shardId, true),
      (20, "{\"max\":25,\"min\":11,\"cur\":16}", shardId, true)
    )
    val values = collectResultSet(resultSet)(extractTupleWithNestedData)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordWithNestedDataIntoStream(
    sensorId: Int,
    maxTemperature: Int,
    minTemperature: Int,
    currentTemperature: Int,
    partitionKey: String,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"sensorId": $sensorId,
         | "statuses": {"max": $maxTemperature,
         | "min": $minTemperature,"cur": $currentTemperature}
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[kinesis] def extractTupleWithNestedData(
    resultSet: ResultSet
  ): (Int, String, String, Boolean) =
    (
      resultSet.getInt("sensorId"),
      resultSet.getString("statuses"),
      resultSet.getString(KINESIS_SHARD_ID_COLUMN_NAME),
      resultSet.getString(SHARD_SEQUENCE_NUMBER_COLUMN_NAME) != null
    )

  test("returns array data from a shard") {
    val streamName = "Test_stream_array"
    createKinesisStream(streamName, 1)
    val columns =
      """sensorId DECIMAL(18,0),
        |statuses VARCHAR(1000),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    putRecordWithArrayIntoStream(17, 35, 14, 29, partitionKey, streamName)
    putRecordWithArrayIntoStream(20, 25, 11, 16, partitionKey, streamName)
    val resultSet = this.executeKinesisImportScript(streamName)
    val expected = List(
      (17, "[35,14,29]", shardId, true),
      (20, "[25,11,16]", shardId, true)
    )
    val values = collectResultSet(resultSet)(extractTupleWithNestedData)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordWithArrayIntoStream(
    sensorId: Int,
    maxTemperature: Int,
    minTemperature: Int,
    currentTemperature: Int,
    partitionKey: String,
    streamName: String
  ): Unit = {
    val recordData =
      s"""{"sensorId": $sensorId,
         | "statuses": [$maxTemperature, $minTemperature, $currentTemperature]
         | }""".stripMargin.replace("\n", "")
    val data = ByteBuffer.wrap(recordData.getBytes())
    kinesisClient.putRecord(streamName, data, partitionKey)
    ()
  }

  private[this] def executeKinesisImportScript(
    streamName: String
  ): ResultSet = {
    val endpointConfiguration =
      kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
    val endpointInsideDocker =
      endpointConfiguration.getServiceEndpoint.replaceAll("127.0.0.1", DOCKER_IP_ADDRESS)
    val properties =
      s"""|'CONNECTION_NAME -> KINESIS_CONNECTION
          |;REGION -> ${endpointConfiguration.getSigningRegion}
          |;STREAM_NAME -> $streamName
          |;MAX_RECORDS_PER_RUN -> 2
          |;AWS_SERVICE_ENDPOINT -> $endpointInsideDocker
          |'
          |
      """.stripMargin.replace("\n", "").strip()
    statement.executeQuery(
      s"""|SELECT KINESIS_IMPORT($properties, KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          | FROM (
          |   VALUES (('$shardId', null)) AS t(KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          |) ORDER BY KINESIS_SHARD_ID
      """.stripMargin
    )
  }
}
