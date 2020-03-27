package com.exasol.cloudetl.scriptclasses

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import com.exasol.ExaMetadata
import com.exasol.cloudetl.kafka.KafkaIntegrationTest

import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito._

@SuppressWarnings(
  Array("org.wartremover.warts.AsInstanceOf", "org.wartremover.contrib.warts.SymbolicName")
)
class KafkaMetadataIT extends KafkaIntegrationTest {

  // Default case where Exasol table is empty.
  test("run emits default partitionId maxOffset pairs with single topic partition") {
    val iter = mockExasolIterator(properties, Seq(0), Seq(-1))
    KafkaMetadata.run(mock[ExaMetadata], iter)
    verify(iter, times(1)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    verify(iter, times(1)).emit(JInt.valueOf(0), JLong.valueOf(-1))
  }

  // Default case where Exasol table is empty.
  test("run emits default partitionId maxOffset pairs with more topic partitions") {
    createCustomTopic(topic, partitions = 3)
    val iter = mockExasolIterator(properties, Seq(0), Seq(-1))
    KafkaMetadata.run(mock[ExaMetadata], iter)
    verify(iter, times(3)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    Seq(0, 1, 2).foreach { partitionId =>
      verify(iter, times(1)).emit(JInt.valueOf(partitionId), JLong.valueOf(-1))
    }
  }

  test("run emits partitionId maxOffset pairs with additional topic partitions") {
    createCustomTopic(topic, partitions = 3)
    val partitions = Seq(0, 1)
    val offsets = Seq(3L, 4L)
    val iter = mockExasolIterator(properties, partitions, offsets)
    KafkaMetadata.run(mock[ExaMetadata], iter)

    verify(iter, times(3)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    partitions.zip(offsets).foreach {
      case (partitionId, maxOffset) =>
        verify(iter, times(1)).emit(JInt.valueOf(partitionId), JLong.valueOf(maxOffset))
    }
    verify(iter, times(1)).emit(JInt.valueOf(2), JLong.valueOf(-1))
  }

  // Do not emit partitionId maxOffset pairs if partitionId is not
  // available in topic partitions
  test("run emits partitionId maxOffset pairs with fewer topic partitions") {
    createCustomTopic(topic, partitions = 2)
    val iter = mockExasolIterator(properties, Seq(1, 3), Seq(7, 17))
    KafkaMetadata.run(mock[ExaMetadata], iter)

    verify(iter, times(2)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    verify(iter, times(1)).emit(JInt.valueOf(0), JLong.valueOf(-1))
    verify(iter, times(1)).emit(JInt.valueOf(1), JLong.valueOf(7))
  }

  test("run throws if it cannot create KafkConsumer") {
    createCustomTopic(topic)
    val newProperties = properties + ("BOOTSTRAP_SERVERS" -> "kafka01.internal:9092")
    val iter = mockExasolIterator(newProperties, Seq(0), Seq(-1))
    val thrown = intercept[org.apache.kafka.common.KafkaException] {
      KafkaMetadata.run(mock[ExaMetadata], iter)
    }
    assert(thrown.getMessage === "Failed to construct kafka consumer")
  }

}
