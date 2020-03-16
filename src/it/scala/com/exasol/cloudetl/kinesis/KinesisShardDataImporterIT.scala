package com.exasol.cloudetl.kinesis

import java.sql.ResultSet

import org.testcontainers.containers.localstack.LocalStackContainer

class KinesisShardDataImporterIT extends KinesisAbstractIntegrationTest {
  val TEST_STREAM_NAME = "Test_stream"

  override final def beforeAll(): Unit = {
    prepareContainers()
    createKinesisStream(TEST_STREAM_NAME, 1)
    setupExasol()
    val columns =
      """sensorId DECIMAL(18,0),
        |currentTemperature DECIMAL(18,0),
        |status VARCHAR(100),
        |kinesis_shard_id VARCHAR(2000),
        |shard_sequence_number VARCHAR(2000)"""
    createKinesisImportScript(columns)
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

  test("returns data from a shard") {
    putRecordsIntoStream()
    val resultSet = this.executeKinesisImportScript("VALUES (('shardId-000000000000', null))")
    val expected = List(
      (17, 147, "WARN", "shardId-000000000000", true),
      (20, 15, "OK", "shardId-000000000000", true)
    )
    val values = collectResultSet(resultSet)(extractTuple)
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def putRecordsIntoStream(): Unit = {
    val partitionKey = "partitionKey-1"
    putRecordIntoStream(17, 147, "WARN", partitionKey, TEST_STREAM_NAME)
    putRecordIntoStream(20, 15, "OK", partitionKey, TEST_STREAM_NAME)
    putRecordIntoStream(36, 65, "OK", partitionKey, TEST_STREAM_NAME)
  }

  private[this] def executeKinesisImportScript(tableImitatingValues: String): ResultSet = {
    val endpointConfiguration =
      kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
    val endpointInsideDocker =
      endpointConfiguration.getServiceEndpoint.replaceAll("127.0.0.1", DOCKER_IP_ADDRESS)
    val properties =
      s"""|'CONNECTION_NAME -> KINESIS_CONNECTION
          |;REGION -> ${endpointConfiguration.getSigningRegion}
          |;STREAM_NAME -> $TEST_STREAM_NAME
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
