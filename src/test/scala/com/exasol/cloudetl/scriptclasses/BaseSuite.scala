package com.exasol.cloudetl.scriptclasses

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.ExaIterator
import com.exasol.cloudetl.bucket.Bucket

import org.mockito.Mockito.when
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar

trait BaseSuite extends FunSuite with Matchers with MockitoSugar {

  val testSchema = "my_schema"

  val s3BucketPath = "s3a://my_bucket/folder1/*"
  val s3Endpoint = "s3.eu-central-1.com"
  val s3AccessKey = "s3_access_key"
  val s3SecretKey = "s3_secret_key"

  val params: Map[String, String] = Map(
    "BUCKET_PATH" -> s3BucketPath,
    "FORMAT" -> "PARQUET",
    "S3_ENDPOINT" -> s3Endpoint,
    "S3_ACCESS_KEY" -> s3AccessKey,
    "S3_SECRET_KEY" -> s3SecretKey
  )

  val rest =
    s"""BUCKET_PATH:=:$s3BucketPath;FORMAT:=:PARQUET;S3_ACCESS_KEY:=:$s3AccessKey;""" +
      s"""S3_ENDPOINT:=:$s3Endpoint;S3_SECRET_KEY:=:$s3SecretKey"""

  val resourcePath: String = norm(Paths.get(getClass.getResource("/data").toURI))
  val resourceImportBucket: String = s"$resourcePath/import/parquet/sales_pos*.parquet"

  final def norm(path: Path): String =
    path.toUri.toString.replaceAll("/$", "").replaceAll("///", "/")

  final def commonExaIterator(bucket: String): ExaIterator = {
    val mockIter = mock[ExaIterator]
    val newParams = params + (Bucket.BUCKET_PATH -> bucket)

    when(mockIter.getString(0)).thenReturn(bucket)
    when(mockIter.getString(1)).thenReturn(Bucket.keyValueMapToString(newParams))

    mockIter
  }
}
