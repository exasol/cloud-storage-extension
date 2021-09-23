package com.exasol.cloudetl.parquet

import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.column.ParquetProperties
import org.apache.parquet.hadoop.ParquetFileWriter
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.schema.MessageType

object ParquetRowWriter {

  private[this] class Builder(path: Path, messageType: MessageType) extends ParquetWriter.Builder[Row, Builder](path) {

    override def getWriteSupport(conf: Configuration): WriteSupport[Row] =
      new RowWriteSupport(messageType)

    override def self(): Builder = this
  }

  def apply(
    path: Path,
    conf: Configuration,
    messageType: MessageType,
    options: ParquetWriteOptions
  ): ParquetWriter[Row] =
    new Builder(path, messageType)
      .withRowGroupSize(options.blockSize.toLong)
      .withPageSize(options.pageSize)
      .withCompressionCodec(options.compressionCodec)
      .withDictionaryEncoding(options.enableDictionaryEncoding)
      .withValidation(options.enableValidation)
      .withWriteMode(ParquetFileWriter.Mode.CREATE)
      .withWriterVersion(ParquetProperties.WriterVersion.PARQUET_1_0)
      .withConf(conf)
      .build()

}
