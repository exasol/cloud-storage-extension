package com.exasol.cloudetl.parquet

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.example.data.simple.SimpleGroup
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema._

trait ParquetTestDataWriter {

  private[this] val PARQUET_BLOCK_SIZE = 64L * 1024 * 1024

  final def getParquetWriter(path: HPath, schema: MessageType, dictionaryEncoding: Boolean): ParquetWriter[Group] =
    BaseGroupWriterBuilder(path, schema)
      .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
      .withRowGroupSize(PARQUET_BLOCK_SIZE)
      .withDictionaryEncoding(dictionaryEncoding)
      .build()

  final def writeDataValues[T](data: List[T], path: HPath, schema: MessageType): Unit = {
    val writer = getParquetWriter(path, schema, true)
    data.foreach { value =>
      val record = new SimpleGroup(schema)
      if (!isNull(value)) {
        appendValue(value, record)
      }
      writer.write(record)
    }
    writer.close()
  }

  private[this] def isNull(obj: Any): Boolean = !Option(obj).isDefined

  private def appendValue(value: Any, record: SimpleGroup): Unit = {
    value match {
      case v: Boolean => record.append("column", v)
      case v: Int     => record.append("column", v)
      case v: Long    => record.append("column", v)
      case v: Float   => record.append("column", v)
      case v: Double  => record.append("column", v)
      case v: String  => record.append("column", v)
      case v: Binary  => record.append("column", v)
      case _          => throw new IllegalArgumentException("Unknown Parquet value!")
    }
    ()
  }

  private[this] case class BaseGroupWriteSupport(schema: MessageType) extends WriteSupport[Group] {
    var writer: GroupWriter = null

    override def prepareForWrite(recordConsumer: RecordConsumer): Unit =
      writer = new GroupWriter(recordConsumer, schema)

    override def init(configuration: Configuration): WriteSupport.WriteContext =
      new WriteSupport.WriteContext(schema, new java.util.HashMap[String, String]())

    override def write(record: Group): Unit =
      writer.write(record)
  }

  private[this] case class BaseGroupWriterBuilder(path: HPath, schema: MessageType)
      extends ParquetWriter.Builder[Group, BaseGroupWriterBuilder](path) {
    override def getWriteSupport(conf: Configuration): WriteSupport[Group] = BaseGroupWriteSupport(schema)
    override def self(): BaseGroupWriterBuilder = this
  }

}
