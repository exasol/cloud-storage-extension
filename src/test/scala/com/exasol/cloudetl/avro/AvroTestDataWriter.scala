package com.exasol.cloudetl.avro

import java.io.File

import org.apache.avro._
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.hadoop.fs.{Path => HPath}

trait AvroTestDataWriter {

  private[this] val AVRO_SYNC_INTERVAL_SIZE = 64 * 1024 * 1024

  final def getAvroWriter(path: HPath, schema: Schema): DataFileWriter[GenericRecord] = {
    val writer = new DataFileWriter[GenericRecord](new SpecificDatumWriter[GenericRecord]())
    writer.setFlushOnEveryBlock(false)
    writer.setSyncInterval(AVRO_SYNC_INTERVAL_SIZE)
    writer.create(schema, new File(path.toUri()))
    writer
  }

  def writeDataValues[T](values: List[T], path: HPath, schema: Schema): Unit = {
    val writer = getAvroWriter(path, schema)
    values.foreach { value =>
      val record = new GenericData.Record(schema)
      record.put("column", value)
      writer.append(record)
    }
    writer.close()
  }

}
