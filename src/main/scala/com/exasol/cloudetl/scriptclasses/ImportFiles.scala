package com.exasol.cloudetl.scriptclasses

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket._
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.source._

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.fs.Path

object ImportFiles extends LazyLogging {

  def run(meta: ExaMetadata, ctx: ExaIterator): Unit = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    val rest = ctx.getString(1)
    val params = Bucket.keyValueStringToMap(rest)
    val format = Bucket.optionalParameter(params, "DATA_FORMAT", "PARQUET")
    val bucket = Bucket(params)

    val files = groupFiles(ctx, 2)
    val nodeId = meta.getNodeId
    val vmId = meta.getVmId
    logger.info(s"The total number of files for node: $nodeId, vm: $vmId is '${files.size}'.")

    files.foreach { file =>
      logger.debug(s"Importing from file: '$file'")
      val source = createSource(format, file, bucket)
      readAndEmit(source.stream(), ctx)
      source.close()
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.MutableDataStructures"))
  private[this] def groupFiles(
    exasolIterator: ExaIterator,
    fileStartingIndex: Int
  ): Seq[String] = {
    val files = ListBuffer[String]()
    do {
      files.append(exasolIterator.getString(fileStartingIndex))
    } while (exasolIterator.next())
    files.toSeq
  }

  private[this] def createSource(format: String, file: String, bucket: Bucket): Source =
    format.toLowerCase match {
      case "avro" => AvroSource(new Path(file), bucket.getConfiguration(), bucket.fileSystem)
      case "orc"  => OrcSource(new Path(file), bucket.getConfiguration(), bucket.fileSystem)
      case "parquet" =>
        ParquetSource(new Path(file), bucket.getConfiguration(), bucket.fileSystem)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported storage format: '$format'")
    }

  private[this] def readAndEmit(rowIterator: Iterator[Row], ctx: ExaIterator): Unit =
    rowIterator.foreach { row =>
      val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
      ctx.emit(columns: _*)
    }

}
