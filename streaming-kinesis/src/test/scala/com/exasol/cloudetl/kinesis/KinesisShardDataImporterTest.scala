package com.exasol.cloudetl.kinesis

import java.nio.ByteBuffer

import scala.collection.JavaConverters._

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class KinesisShardDataImporterTest extends AnyFunSuite with MockitoSugar {
  test("createShardIteratorRequest with existing shardSequenceNumber") {
    val streamName = "Test_stream"
    val shardId = "shardId-000000000002"
    val shardSequenceNumber = "49604832218991145411663169279930025628073300079339896866"
    val getShardIteratorRequest = KinesisShardDataImporter
      .createShardIteratorRequest(streamName, shardId, shardSequenceNumber)
    assert(getShardIteratorRequest.isInstanceOf[GetShardIteratorRequest])
    assert(getShardIteratorRequest.getStreamName === streamName)
    assert(getShardIteratorRequest.getShardId === shardId)
    assert(
      getShardIteratorRequest.getShardIteratorType ===
        ShardIteratorType.AFTER_SEQUENCE_NUMBER.toString
    )
    assert(getShardIteratorRequest.getStartingSequenceNumber === shardSequenceNumber)
  }

  test("createShardIteratorRequest without shardSequenceNumber") {
    val streamName = "Test_stream"
    val shardId = "shardId-000000000002"
    val shardSequenceNumber = null
    val getShardIteratorRequest = KinesisShardDataImporter
      .createShardIteratorRequest(streamName, shardId, shardSequenceNumber)
    assert(getShardIteratorRequest.isInstanceOf[GetShardIteratorRequest])
    assert(getShardIteratorRequest.getStreamName === streamName)
    assert(getShardIteratorRequest.getShardId === shardId)
    assert(
      getShardIteratorRequest.getShardIteratorType ===
        ShardIteratorType.TRIM_HORIZON.toString
    )
    assert(getShardIteratorRequest.getStartingSequenceNumber === shardSequenceNumber)
  }

  test("getRecords returns List of records") {
    val amazonKinesis = mock[AmazonKinesis]
    val shardIteratorRequest = mock[GetShardIteratorRequest]
    val shardIteratorResult = mock[GetShardIteratorResult]
    val getRecordsResult = mock[GetRecordsResult]
    val shardIterator = "EvJjIYI+3jw/4aqgH9Fif"
    val records = List(new Record, new Record)
    when(amazonKinesis.getShardIterator(shardIteratorRequest)).thenReturn(shardIteratorResult)
    when(shardIteratorResult.getShardIterator).thenReturn(shardIterator)
    when(amazonKinesis.getRecords(any(classOf[GetRecordsRequest]))).thenReturn(getRecordsResult)
    when(getRecordsResult.getRecords).thenReturn(records.asJava)
    assert(
      KinesisShardDataImporter
        .getRecords(amazonKinesis, shardIteratorRequest, None) === records
    )
  }

  test("createTableValuesListFromRecord returns table values") {
    val record = new Record
    val byteBuffer = ByteBuffer.wrap(
      "{\"sensorId\": 17,\"currentTemperature\": 147,\"status\": \"WARN\"}".getBytes()
    )
    record.setData(byteBuffer)
    val value = "49604832218991145411663169279930025628073300079339896866"
    record.setSequenceNumber(value)
    val shardId = "shardId-000000000002"
    val values = KinesisShardDataImporter.createTableValuesListFromRecord(record, shardId)
    assert(values === Seq(17, 147, "WARN", shardId, value))
  }

  test("getLimit returns Option with value") {
    val kinesisUserProperties = mock[KinesisUserProperties]
    when(kinesisUserProperties.containsMaxRecordsPerRun()).thenReturn(true)
    when(kinesisUserProperties.getMaxRecordsPerRun()).thenReturn(100)
    KinesisShardDataImporter
      .getLimit(kinesisUserProperties)
      .fold {
        assert(false)
        ()
      } { value =>
        assert(value === 100)
        ()
      }
  }

  test("getLimit returns empty Option") {
    val kinesisUserProperties = mock[KinesisUserProperties]
    when(kinesisUserProperties.containsMaxRecordsPerRun()).thenReturn(false)
    assert(KinesisShardDataImporter.getLimit(kinesisUserProperties) === None)
  }
}
