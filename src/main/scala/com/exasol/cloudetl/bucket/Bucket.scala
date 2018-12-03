package com.exasol.cloudetl.bucket

import java.net.URI

import com.exasol.cloudetl.util.FsUtil

import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

abstract class Bucket {

  val bucketPath: String

  def validate(): Unit

  def createConfiguration(): Configuration

  lazy val fs: FileSystem =
    FileSystem.get(new URI(bucketPath), createConfiguration())

  final def getPaths(): Seq[Path] =
    FsUtil.globWithPattern(bucketPath, fs)

}

final case class S3Bucket(path: String, params: Map[String, String]) extends Bucket {

  override val bucketPath: String = path

  override def validate(): Unit =
    Bucket.validate(params, Bucket.S3_PARAMETERS)

  override def createConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.s3a.impl", classOf[org.apache.hadoop.fs.s3a.S3AFileSystem].getName)
    conf.set("fs.s3a.endpoint", Bucket.requiredParam(params, "S3_ENDPOINT"))
    conf.set("fs.s3a.access.key", Bucket.requiredParam(params, "S3_ACCESS_KEY"))
    conf.set("fs.s3a.secret.key", Bucket.requiredParam(params, "S3_SECRET_KEY"))

    conf
  }

}

final case class LocalBucket(path: String, params: Map[String, String]) extends Bucket {

  override val bucketPath: String = path

  override def validate(): Unit = ()

  override def createConfiguration(): Configuration =
    new Configuration()

}

object Bucket extends LazyLogging {

  def apply(params: Map[String, String]): Bucket = {
    val path = requiredParam(params, BUCKET_PATH)
    val scheme = getScheme(path)

    scheme match {
      case "s3a"  => S3Bucket(path, params)
      case "gs"   => S3Bucket(path, params)
      case "file" => LocalBucket(path, params)
      case _      => throw new IllegalArgumentException(s"Unknown path scheme $scheme")
    }
  }

  def getScheme(path: String): String =
    new URI(path).getScheme

  def validate(params: Map[String, String], keys: Seq[String]): Unit =
    keys.foreach { key =>
      requiredParam(params, key)
    }

  def requiredParam(params: Map[String, String], key: String): String = {
    val opt = params.get(key)
    opt.fold {
      throw new IllegalArgumentException(s"The required parameter $key is not defined!")
    }(identity)
  }

  def optionalParam(params: Map[String, String], key: String, defaultValue: String): String =
    params.get(key).fold(defaultValue)(identity)

  def mapToStr(params: Map[String, String]): String = {
    val selectedParams = (params -- Seq("PARALLELISM"))
    selectedParams.map { case (k, v) => s"$k=$v" }.mkString(";")
  }

  def strToMap(str: String): Map[String, String] =
    // val KV_PATTERN = """(\w+)=(\w+)""".r
    str
      .split(";")
      .map { word =>
        val kv = word.split("=")
        kv(0) -> kv(1)
      }
      .toMap

  final val BUCKET_PATH: String = "BUCKET_PATH"

  final val S3_PARAMETERS: Seq[String] =
    Seq("S3_ENDPOINT", "S3_ACCESS_KEY", "S3_SECRET_KEY")

}
