package com.exasol.cloudetl.kinesis

import java.sql.ResultSet

import org.testcontainers.containers.localstack.LocalStackContainer

class KinesisShardDataImporterIT extends KinesisAbstractIntegrationTest {

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

  test("returns primitive data from a shard") {
    val streamName = "Test_stream_1"
    createKinesisStream(streamName, 1)
    val columns =
      """sensorId DECIMAL(18,0),
        |currentTemperature DECIMAL(18,0),
        |status VARCHAR(100),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    putRecordsIntoStream(streamName)
    val resultSet =
      this.executeKinesisImportScript("VALUES (('shardId-000000000000', null))", streamName)
    val expected = List(
      (17, 147, "WARN", "shardId-000000000000", true),
      (20, 15, "OK", "shardId-000000000000", true)
    )
    val values = collectResultSet(resultSet)(extractTuple)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordsIntoStream(streamName: String): Unit = {
    val partitionKey = "partitionKey-1"
    putRecordIntoStream(17, 147, "WARN", partitionKey, streamName)
    putRecordIntoStream(20, 15, "OK", partitionKey, streamName)
    putRecordIntoStream(36, 65, "OK", partitionKey, streamName)
  }

  test("returns nested data from a shard") {
    val streamName = "Test_stream_2"
    createKinesisStream(streamName, 1)
    val columns =
      """sensorId DECIMAL(18,0),
        |statuses VARCHAR(1000),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
    putRecordsWithNestedDataIntoStream(streamName)
    val resultSet =
      this.executeKinesisImportScript("VALUES (('shardId-000000000000', null))", streamName)
    val expected = List(
      (17, "{\"max\":35,\"min\":14,\"cur\":29}", "shardId-000000000000", true),
      (20, "{\"max\":25,\"min\":11,\"cur\":16}", "shardId-000000000000", true)
    )
    val values = collectResultSet(resultSet)(extractTupleWithNestedData)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordsWithNestedDataIntoStream(streamName: String): Unit = {
    val partitionKey = "partitionKey-1"
    putRecordWithNestedDataIntoStream(17, 35, 14, 29, partitionKey, streamName)
    putRecordWithNestedDataIntoStream(20, 25, 11, 16, partitionKey, streamName)
    putRecordWithNestedDataIntoStream(36, 24, 17, 20, partitionKey, streamName)
  }

  private[this] def executeKinesisImportScript(
    tableImitatingValues: String,
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
      """.stripMargin.replace("\n", "").strip()
    statement.executeQuery(
      s"""|SELECT KINESIS_IMPORT($properties, KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          | FROM (
          |   $tableImitatingValues AS t(KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          |) ORDER BY KINESIS_SHARD_ID
      """.stripMargin
    )
  }
}
