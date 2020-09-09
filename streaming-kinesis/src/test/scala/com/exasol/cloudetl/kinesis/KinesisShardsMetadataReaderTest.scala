package com.exasol.cloudetl.kinesis

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model._
import com.exasol.ExaIterator
import com.exasol.cloudetl.kinesis.KinesisShardsMetadataReader._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import scala.collection.JavaConverters._

class KinesisShardsMetadataReaderTest extends AnyFunSuite with MockitoSugar {
  test("getAllShardsFromStream returns shards") {
    val amazonKinesis = mock[AmazonKinesis]
    val describeStreamResult = mock[DescribeStreamResult]
    val streamDescription = mock[StreamDescription]
    val firstShard = new Shard
    firstShard.setShardId("shardId-000000000002")
    val secondShard = new Shard
    when(amazonKinesis.describeStream(any[DescribeStreamRequest]))
      .thenReturn(describeStreamResult)
    when(describeStreamResult.getStreamDescription).thenReturn(streamDescription)
    when(streamDescription.getShards)
      .thenReturn(List(firstShard).asJava, List(secondShard).asJava)
    when(streamDescription.getHasMoreShards).thenReturn(true, false)
    val streamName = "Test_stream"
    assert(
      KinesisShardsMetadataReader.getAllShardsFromStream(streamName, amazonKinesis) === List(
        firstShard,
        secondShard
      )
    )
  }

  test("collectShardsIds returns list of strings") {
    val firstShard = new Shard
    val firstShardId = "shardId-000000000001"
    firstShard.setShardId(firstShardId)
    val secondShard = new Shard
    val secondShardId = "shardId-000000000002"
    secondShard.setShardId(secondShardId)
    assert(
      KinesisShardsMetadataReader
        .collectShardsIds(List(firstShard, secondShard)) === List(
        firstShardId,
        secondShardId
      )
    )
  }

  test("getShardsMetadataFromTable returns shards metadata") {
    val exaIterator = mock[ExaIterator]
    when(exaIterator.next()).thenReturn(true, false)
    val firstShardId = "shardId-000000000001"
    val secondShardId = "shardId-000000000002"
    when(exaIterator.getString(SHARD_ID_INDEX)).thenReturn(firstShardId, secondShardId)
    val firstSequenceNumber = "496048322173300079339896866"
    val secondSequenceNumber = "496048322173300079339896814"
    when(exaIterator.getString(SEQUENCE_NUMBER_INDEX))
      .thenReturn(firstSequenceNumber, secondSequenceNumber)
    assert(
      KinesisShardsMetadataReader
        .getShardsMetadataFromTable(exaIterator) === Map(
        (firstShardId -> firstSequenceNumber),
        (secondShardId -> secondSequenceNumber)
      )
    )
  }

  test("combineShardsMetadata returns final metadata") {
    val firstShardId = "shardId-000000000001"
    val secondShardId = "shardId-000000000002"
    val thirdShardId = "shardId-000000000003"
    val shardsMetadataFromStream = List(firstShardId, secondShardId, thirdShardId)
    val firstSequenceNumber = "496048322173300079339896866"
    val secondSequenceNumber = "496048322173300079339896814"
    val shardsMetadata = Map[String, String](
      firstShardId -> firstSequenceNumber,
      secondShardId -> secondSequenceNumber,
      "0" -> "0"
    )
    assert(
      KinesisShardsMetadataReader
        .combineShardsMetadata(shardsMetadata, shardsMetadataFromStream) === Map(
        (firstShardId -> firstSequenceNumber),
        (secondShardId -> secondSequenceNumber),
        (thirdShardId -> null)
      )
    )
  }
}
