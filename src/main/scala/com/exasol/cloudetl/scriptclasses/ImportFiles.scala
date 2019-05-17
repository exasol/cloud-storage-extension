package com.exasol.cloudetl.scriptclasses

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket._
import com.exasol.cloudetl.source._

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path

object ImportFiles extends LazyLogging {

  def run(meta: ExaMetadata, iter: ExaIterator): Unit = {
    val rest = iter.getString(1)
    val params = Bucket.keyValueStringToMap(rest)
    val format = Bucket.optionalParameter(params, "DATA_FORMAT", "PARQUET")
    val bucket = Bucket(params)

    val files = groupFiles(iter, 2)
    val nodeId = meta.getNodeId
    val vmId = meta.getVmId
    logger.info(s"The total number of files for node: $nodeId, vm: $vmId is '${files.size}'.")

    val source = createSource(format, files, bucket)
    readAndEmit(source, iter)
  }

  @SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
  private[this] def groupFiles(iter: ExaIterator, iterIndex: Int): Seq[String] = {
    val files = ListBuffer[String]()

    do {
      files.append(iter.getString(iterIndex))
    } while (iter.next())

    files.toSeq
  }

  private[this] def createSource(format: String, files: Seq[String], bucket: Bucket): Source = {
    val paths = files.map(f => new Path(f))
    format.toLowerCase match {
      case "avro"    => AvroSource(paths, bucket.fileSystem, bucket.getConfiguration())
      case "orc"     => OrcSource(paths, bucket.fileSystem, bucket.getConfiguration())
      case "parquet" => ParquetSource(paths, bucket.fileSystem, bucket.getConfiguration())
      case _ =>
        throw new IllegalArgumentException(s"Unsupported storage format: '$format'")
    }
  }

  private[this] def readAndEmit(src: Source, ctx: ExaIterator): Unit =
    src.stream.foreach { iter =>
      iter.foreach { row =>
        val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
        ctx.emit(columns: _*)
      }
    }

}
