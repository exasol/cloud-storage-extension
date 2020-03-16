package com.exasol.cloudetl.kafka

import com.exasol.ExaIterator

import net.manub.embeddedkafka.schemaregistry.EmbeddedKafka
import org.apache.avro.AvroRuntimeException
import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

trait KafkaIntegrationTest
    extends AnyFunSuite
    with BeforeAndAfterEach
    with MockitoSugar
    with EmbeddedKafka {

  val topic = "exasol-kafka-topic"

  val properties = Map(
    "BOOTSTRAP_SERVERS" -> "localhost:6001",
    "SCHEMA_REGISTRY_URL" -> "http://localhost:6002",
    "TOPICS" -> topic,
    "TABLE_NAME" -> "exasolTable"
  )

  override final def beforeEach(): Unit = {
    EmbeddedKafka.start()
    ()
  }

  override final def afterEach(): Unit = {
    EmbeddedKafka.stop()
    ()
  }

  final def mockExasolIterator(
    params: Map[String, String],
    partitions: Seq[Int],
    offsets: Seq[Long]
  ): ExaIterator = {
    val mockedIterator = mock[ExaIterator]
    when(mockedIterator.getString(0)).thenReturn(KafkaConsumerProperties(params).mkString())

    val bHead :: bTail = Seq.fill(partitions.size - 1)(true) ++ Seq(false)
    when(mockedIterator.next()).thenReturn(bHead, bTail: _*)

    val pHead :: pTail = partitions.map(Integer.valueOf)
    when(mockedIterator.getInteger(1)).thenReturn(pHead, pTail: _*)

    val oHead :: oTail = offsets.map(java.lang.Long.valueOf)
    when(mockedIterator.getLong(2)).thenReturn(oHead, oTail: _*)

    mockedIterator
  }

  private[this] val avroRecordSchema =
    new Schema.Parser().parse(s"""{
                                 | "namespace": "com.exasol.cloudetl",
                                 | "type": "record",
                                 | "name": "AvroRecordSchemaForIT",
                                 | "fields": [
                                 |     {"name": "col_str", "type": "string"},
                                 |     {"name": "col_int", "type": "int"},
                                 |     {"name": "col_long", "type": "long"}
                                 | ]
                                 |}""".stripMargin)

  case class AvroRecord(var col_str: String, var col_int: Int, var col_long: Long)
      extends SpecificRecordBase {
    def this() = this("", 0, 0)

    override def get(index: Int): AnyRef = index match {
      case 0 => col_str
      case 1 => col_int.asInstanceOf[AnyRef]
      case 2 => col_long.asInstanceOf[AnyRef]
      case _ => throw new AvroRuntimeException(s"Unknown index $index!")
    }

    override def put(index: Int, value: Any): Unit = index match {
      case 0 =>
        col_str = value match {
          case (utf8: org.apache.avro.util.Utf8) => utf8.toString
          case _                                 => value.asInstanceOf[String]
        }
      case 1 =>
        col_int = value.asInstanceOf[Int]
      case 2 =>
        col_long = value.asInstanceOf[Long]
      case _ => throw new AvroRuntimeException(s"Unknown index $index!")
    }

    override def getSchema(): Schema = avroRecordSchema
  }

}
