package com.exasol.cloudetl.kinesis

import java.sql.ResultSet

import org.testcontainers.containers.localstack.LocalStackContainer

class KinesisShardsMetadataReaderIT extends KinesisAbstractIntegrationTest {
  val TEST_STREAM_NAME = "Test_stream"

  override final def beforeAll(): Unit = {
    prepareContainers()
    createKinesisStream(TEST_STREAM_NAME, 3)
    setupExasol()
    createKinesisMetadataScript()
  }

  test("returns shards from a stream") {
    val resultSet = this.executeKinesisMetadataScript("VALUES (('0', '0'))")
    val values = collectResultSet(resultSet)(extractLocalTuple)
    val expected = List(
      ("shardId-000000000000", null),
      ("shardId-000000000001", null),
      ("shardId-000000000002", null)
    )
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[kinesis] def extractLocalTuple(resultSet: ResultSet): (String, String) =
    (
      resultSet.getString("KINESIS_SHARD_ID"),
      resultSet.getString("SHARD_SEQUENCE_NUMBER")
    )

  test("returns shards from a stream with existing metadata in table") {
    val resultSet = this.executeKinesisMetadataScript(
      """VALUES (('shardId-000000000000', '1234'),
        |       ('shardId-000000000001', '5678'),
        |       ('shardId-000000000004', '9012'))""".stripMargin
    )
    val values = collectResultSet(resultSet)(extractLocalTuple)
    val expected = List(
      ("shardId-000000000000", "1234"),
      ("shardId-000000000001", "5678"),
      ("shardId-000000000002", null)
    )
    assert(values === expected)
    assert(resultSet.next() === false)
  }

  private[this] def executeKinesisMetadataScript(tableImitatingValues: String): ResultSet = {
    val endpointConfiguration =
      kinesisLocalStack.getEndpointConfiguration(LocalStackContainer.Service.KINESIS)
    val endpointInsideDocker =
      endpointConfiguration.getServiceEndpoint.replaceAll("127.0.0.1", DOCKER_IP_ADDRESS)
    val credentials = kinesisLocalStack.getDefaultCredentialsProvider.getCredentials
    val properties =
      s"""|'AWS_ACCESS_KEY -> ${credentials.getAWSAccessKeyId}
          |;AWS_SECRET_KEY -> ${credentials.getAWSSecretKey}
          |;REGION -> ${endpointConfiguration.getSigningRegion}
          |;STREAM_NAME -> $TEST_STREAM_NAME
          |;AWS_SERVICE_ENDPOINT -> $endpointInsideDocker
          |'
      """.stripMargin.replace("\n", "").strip()
    statement.executeQuery(
      s"""|SELECT KINESIS_METADATA($properties, KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          | FROM (
          |   $tableImitatingValues AS t(KINESIS_SHARD_ID, SHARD_SEQUENCE_NUMBER)
          |) ORDER BY KINESIS_SHARD_ID
      """.stripMargin
    )
  }
}
