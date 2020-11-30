package com.exasol.cloudetl.parquet

import java.io.Closeable
import java.nio.file.Path

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.source.ParquetSource
import com.exasol.common.data.Row

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.GroupWriter
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

trait BaseParquetReaderTest extends AnyFunSuite with BeforeAndAfterEach with DummyRecordsTest {

  private[this] var conf: Configuration = _
  private[this] var fileSystem: FileSystem = _
  private[this] var outputDirectory: Path = _
  private[this] var path: HPath = _

  override final def beforeEach(): Unit = {
    conf = new Configuration
    fileSystem = FileSystem.get(conf)
    outputDirectory = createTemporaryFolder("parquetRowReaderTest")
    path = new HPath(outputDirectory.toUri.toString, "part-00000.parquet")
    ()
  }

  override final def afterEach(): Unit = {
    deleteFiles(outputDirectory)
    ()
  }

  protected final def withResource[T <: Closeable](writer: T)(block: T => Unit): Unit = {
    block(writer)
    writer.close()
  }

  protected final def getRecords(): Seq[Row] =
    ParquetSource(path, conf, fileSystem)
      .stream()
      .toSeq

  protected final def getParquetWriter(
    schema: MessageType,
    dictionaryEncoding: Boolean
  ): ParquetWriter[Group] =
    BaseGroupWriterBuilder(path, schema)
      .withDictionaryEncoding(dictionaryEncoding)
      .build()

  private[this] case class BaseGroupWriteSupport(schema: MessageType)
      extends WriteSupport[Group] {
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
    override def getWriteSupport(conf: Configuration): WriteSupport[Group] =
      BaseGroupWriteSupport(schema)
    override def self(): BaseGroupWriterBuilder = this
  }

}
