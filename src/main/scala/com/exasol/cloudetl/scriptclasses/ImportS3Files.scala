package com.exasol.cloudetl.scriptclasses

import java.net.URI

import scala.collection.mutable.ListBuffer

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.source.ParquetSource

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

object ImportS3Files {

  def run(meta: ExaMetadata, iter: ExaIterator): Unit = {
    val s3Bucket = iter.getString(0)
    val s3Endpoint = iter.getString(1)
    val s3AccessKey = iter.getString(2)
    val s3SecretKey = iter.getString(3)
    val files = groupFiles(iter, 4)

    val conf: Configuration = new Configuration()
    conf.set("fs.s3a.impl", classOf[org.apache.hadoop.fs.s3a.S3AFileSystem].getName)
    conf.set("fs.s3a.endpoint", s3Endpoint)
    conf.set("fs.s3a.access.key", s3AccessKey)
    conf.set("fs.s3a.secret.key", s3SecretKey)

    val fs: FileSystem = FileSystem.get(new URI(s3Bucket), conf)

    val source = createNewSource(files, fs, conf)

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

  private[this] def createNewSource(
    files: Seq[String],
    fs: FileSystem,
    conf: Configuration
  ): ParquetSource = {
    val paths = files.map(f => new Path(f))
    ParquetSource(paths, fs, conf)
  }

  private[this] def readAndEmit(src: ParquetSource, ctx: ExaIterator): Unit =
    src.stream.foreach { iter =>
      iter.foreach { row =>
        val columns: Seq[Object] = row.values.map(_.asInstanceOf[AnyRef])
        ctx.emit(columns: _*)
      }
    }

}
