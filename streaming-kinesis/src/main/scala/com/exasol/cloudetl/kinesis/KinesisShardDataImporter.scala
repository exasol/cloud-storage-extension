package com.exasol.cloudetl.kinesis

import java.util

import scala.collection.JavaConverters._

import com.exasol._
import com.exasol.cloudetl.util.JsonMapper

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model._

/**
 * This object imports data from a single Kinesis Stream's shard.
 */
object KinesisShardDataImporter {
  val PROPERTIES_STRING_INDEX: Int = 0
  val SHARD_ID_INDEX: Int = 1
  val SHARD_SEQUENCE_NUMBER_INDEX: Int = 2

  /**
   * Emits data from a shard.
   *
   * This is a function which is called inside Exasol when a SELECT query refers to a script
   * based on this object.
   */
  def run(exaMetadata: ExaMetadata, exaIterator: ExaIterator): Unit = {
    val kinesisUserProperties = KinesisUserProperties(
      exaIterator.getString(PROPERTIES_STRING_INDEX)
    )
    val shardId = exaIterator.getString(SHARD_ID_INDEX)
    val shardSequenceNumber = exaIterator.getString(SHARD_SEQUENCE_NUMBER_INDEX)
    val streamName = kinesisUserProperties.getStreamName()
    val amazonKinesis =
      KinesisClientFactory.createKinesisClient(kinesisUserProperties, exaMetadata)
    val shardIteratorRequest =
      createShardIteratorRequest(streamName, shardId, shardSequenceNumber)
    val limit: Option[Int] = getLimit(kinesisUserProperties)
    val records = getRecords(amazonKinesis, shardIteratorRequest, limit)
    try {
      records.foreach(record => {
        exaIterator.emit(createTableValuesListFromRecord(record, shardId): _*)
      })
    } catch {
      case exception @ (_: ExaDataTypeException | _: ExaIterationException) =>
        throw new KinesisConnectorException(
          "KinesisShardDataImporter cannot emit records. Caused: " + exception.getMessage,
          exception
        )
    } finally {
      amazonKinesis.shutdown()
    }
  }

  private[kinesis] def getLimit(kinesisUserProperties: KinesisUserProperties): Option[Int] =
    if (kinesisUserProperties.containsMaxRecordsPerRun()) {
      Option(kinesisUserProperties.getMaxRecordsPerRun())
    } else {
      None
    }

  private[kinesis] def createShardIteratorRequest(
    streamName: String,
    shardId: String,
    shardSequenceNumber: String
  ): GetShardIteratorRequest = {
    val getShardIteratorRequest = new GetShardIteratorRequest
    getShardIteratorRequest.setStreamName(streamName)
    getShardIteratorRequest.setShardId(shardId)
    if (shardSequenceNumber != null) {
      getShardIteratorRequest.setShardIteratorType(ShardIteratorType.AFTER_SEQUENCE_NUMBER)
      getShardIteratorRequest.setStartingSequenceNumber(shardSequenceNumber)
    } else {
      getShardIteratorRequest.setShardIteratorType(ShardIteratorType.TRIM_HORIZON)
    }
    getShardIteratorRequest
  }

  private[kinesis] def getRecords(
    amazonKinesis: AmazonKinesis,
    shardIteratorRequest: GetShardIteratorRequest,
    limitOption: Option[Int]
  ): List[Record] = {
    val shardIteratorResult = amazonKinesis.getShardIterator(shardIteratorRequest)
    val shardIterator = shardIteratorResult.getShardIterator
    val getRecordsRequest = new GetRecordsRequest
    getRecordsRequest.setShardIterator(shardIterator)
    limitOption.fold({}) { limitValue =>
      getRecordsRequest.setLimit(limitValue)
    }
    val getRecordsResult = amazonKinesis.getRecords(getRecordsRequest)
    getRecordsResult.getRecords.asScala.toList
  }

  private[kinesis] def createTableValuesListFromRecord(
    record: Record,
    shardId: String
  ): Seq[AnyRef] = {
    val data = record.getData
    val parsedValuesMap = JsonMapper
      .parseJson[util.LinkedHashMap[String, AnyRef]](new String(data.array()))
      .values()
      .stream()
      .toArray
      .toSeq
      .map {
        case element: Map[_, _] => JsonMapper.toJson(element)
        case element: List[_]   => JsonMapper.toJson(element)
        case element            => element
      }
    parsedValuesMap ++ Seq(shardId, record.getSequenceNumber)
  }
}
