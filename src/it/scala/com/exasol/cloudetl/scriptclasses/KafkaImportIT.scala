package com.exasol.cloudetl.scriptclasses

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import com.exasol.ExaMetadata
import com.exasol.cloudetl.kafka.KafkaIntegrationTest

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class KafkaImportIT extends KafkaIntegrationTest {

  test("run emits records from starting initial offset") {
    createCustomTopic(topic)
    publishToKafka(topic, AvroRecord("abc", 3, 13))
    publishToKafka(topic, AvroRecord("hello", 4, 14))

    val iter = mockExasolIterator(properties, Seq(0), Seq(-1))
    KafkaImport.run(mock[ExaMetadata], iter)

    verify(iter, times(2)).emit(Seq(any[Object]): _*)
    verify(iter, times(2)).emit(
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong],
      anyString(),
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong]
    )
    verify(iter, times(1)).emit(new JInt(0), new JLong(0), "abc", new JInt(3), new JLong(13))
    verify(iter, times(1)).emit(new JInt(0), new JLong(1), "hello", new JInt(4), new JLong(14))
  }

  test("run emits records starting from provided offset") {
    createCustomTopic(topic)
    publishToKafka(topic, AvroRecord("abc", 3, 13))
    publishToKafka(topic, AvroRecord("hello", 4, 14))
    publishToKafka(topic, AvroRecord("def", 7, 17))
    publishToKafka(topic, AvroRecord("xyz", 13, 23))

    // records at 0, 1 are already read, committed
    val iter = mockExasolIterator(properties, Seq(0), Seq(1))
    KafkaImport.run(mock[ExaMetadata], iter)

    verify(iter, times(2)).emit(Seq(any[Object]): _*)
    verify(iter, times(2)).emit(
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong],
      anyString(),
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong]
    )
    verify(iter, times(1)).emit(new JInt(0), new JLong(2), "def", new JInt(7), new JLong(17))
    verify(iter, times(1)).emit(new JInt(0), new JLong(3), "xyz", new JInt(13), new JLong(23))
  }

  test("run emits records within min / max records per run") {
    val newProperties = properties ++ Map(
      "MAX_POLL_RECORDS" -> "2",
      "MIN_RECORDS_PER_RUN" -> "2",
      "MAX_RECORDS_PER_RUN" -> "4"
    )
    createCustomTopic(topic)
    publishToKafka(topic, AvroRecord("abc", 3, 13))
    publishToKafka(topic, AvroRecord("hello", 4, 14))
    publishToKafka(topic, AvroRecord("def", 7, 17))
    publishToKafka(topic, AvroRecord("xyz", 13, 23))

    // comsumer in two batches each with 2 records
    val iter = mockExasolIterator(newProperties, Seq(0), Seq(-1))
    KafkaImport.run(mock[ExaMetadata], iter)

    verify(iter, times(4)).emit(Seq(any[Object]): _*)
    verify(iter, times(4)).emit(
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong],
      anyString(),
      anyInt().asInstanceOf[JInt],
      anyLong().asInstanceOf[JLong]
    )
  }

}
