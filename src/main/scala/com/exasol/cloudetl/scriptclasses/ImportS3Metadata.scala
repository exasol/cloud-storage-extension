package com.exasol.cloudetl.scriptclasses

import java.net.URI

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.util.FsUtil

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

object ImportS3Metadata {

  def run(meta: ExaMetadata, iter: ExaIterator): Unit = {
    val s3Bucket = iter.getString(0)
    val s3Endpoint = iter.getString(1)
    val s3AccessKey = iter.getString(2)
    val s3SecretKey = iter.getString(3)
    val parallelism = iter.getInteger(4)

    val conf: Configuration = new Configuration()
    conf.set("fs.s3a.impl", classOf[org.apache.hadoop.fs.s3a.S3AFileSystem].getName)
    conf.set("fs.s3a.endpoint", s3Endpoint)
    conf.set("fs.s3a.access.key", s3AccessKey)
    conf.set("fs.s3a.secret.key", s3SecretKey)

    val fs: FileSystem = FileSystem.get(new URI(s3Bucket), conf)

    val paths = FsUtil.globWithPattern(s3Bucket, fs)

    paths.zipWithIndex.foreach {
      case (filename, idx) =>
        iter.emit(filename.toString, s"${idx % parallelism}")
    }
  }

}
