package com.exasol.cloudetl.kinesis

import scala.collection.JavaConverters._
import scala.collection.mutable

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model._
import com.exasol._
import java.util

/**
 * This object reads shards' metadata from Kinesis Streams.
 */
object KinesisShardsMetadataReader {
  val PROPERTIES_STRING_INDEX: Int = 0
  val SHARD_ID_INDEX: Int = 1
  val SEQUENCE_NUMBER_INDEX: Int = 2

  /**
   * Emits enabled shards' names from a stream.
   *
   * This is a function which is called inside Exasol when a SELECT query refers to a script
   * based on this object.
   */
  def run(exaMetadata: ExaMetadata, exaIterator: ExaIterator): Unit = {
    val kinesisUserProperties = KinesisUserProperties(
      exaIterator.getString(PROPERTIES_STRING_INDEX)
    )
    val streamName = kinesisUserProperties.getStreamName()
    val amazonKinesis =
      KinesisClientFactory.createKinesisClient(kinesisUserProperties, exaMetadata)
    val shardsMetadataFromTable = getShardsMetadataFromTable(exaIterator)
    val shards = getAllShardsFromStream(streamName, amazonKinesis)
    val shardsMetadataFromStream = collectShardsIds(shards)
    val kinesisMetadata =
      combineShardsMetadata(shardsMetadataFromTable, shardsMetadataFromStream)
    try {
      kinesisMetadata.foreach {
        case (shardId, sequenceNumber) => exaIterator.emit(shardId, sequenceNumber)
      }
    } catch {
      case exception @ (_: ExaDataTypeException | _: ExaIterationException) =>
        val message = exception.getMessage
        throw new KinesisConnectorException(
          s"KinesisShardsMetadataReader cannot emit shards' ids. Caused: $message",
          exception
        )
    } finally {
      amazonKinesis.shutdown()
    }
  }

  private[kinesis] def getShardsMetadataFromTable(
    exaIterator: ExaIterator
  ): Map[String, String] = {
    val shardsMetadata: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]
    do {
      val shardId = exaIterator.getString(SHARD_ID_INDEX)
      val sequenceNumber = exaIterator.getString(SEQUENCE_NUMBER_INDEX)
      shardsMetadata += (shardId -> sequenceNumber)
    } while (exaIterator.next())
    shardsMetadata.toMap
  }

  // Obtains shards from a Stream until there is no more shards.
  // https://docs.aws.amazon.com/streams/latest/dev/kinesis-using-sdk-java-retrieve-shards.html
  private[kinesis] def getAllShardsFromStream(
    streamName: String,
    amazonKinesis: AmazonKinesis
  ): List[Shard] = {
    val describeStreamRequest = new DescribeStreamRequest
    describeStreamRequest.setStreamName(streamName)
    val shards = new util.ArrayList[Shard]
    var exclusiveStartShardId: String = null
    do {
      describeStreamRequest.setExclusiveStartShardId(exclusiveStartShardId)
      val describeStreamResult = amazonKinesis.describeStream(describeStreamRequest)
      val _ = shards.addAll(describeStreamResult.getStreamDescription.getShards)
      if (describeStreamResult.getStreamDescription.getHasMoreShards && !shards.isEmpty) {
        exclusiveStartShardId = shards.get(shards.size - 1).getShardId
      } else {
        exclusiveStartShardId = null
      }
    } while ({
      exclusiveStartShardId != null
    })
    shards.asScala.toList
  }

  private[kinesis] def collectShardsIds(shards: List[Shard]): List[String] =
    shards.toStream.map(shard => shard.getShardId).toList

  private[kinesis] def combineShardsMetadata(
    shardsMetadataFromTable: Map[String, String],
    shardsMetadataFromStream: List[String]
  ): Map[String, String] = {
    val kinesisMetadata: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]
    shardsMetadataFromStream.foreach(shardId => {
      val sequenceNumber: String = shardsMetadataFromTable.getOrElse(shardId, null)
      kinesisMetadata += (shardId -> sequenceNumber)
    })
    kinesisMetadata.toMap
  }
}
