package com.exasol.cloudetl.scriptclasses

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.ExaIterator

import org.mockito.Mockito.when
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar

trait BaseImportSuite extends FunSuite with Matchers with MockitoSugar {

  val testSchema = "my_schema"

  val s3BucketPath = "s3a://my_bucket/folder1/*"
  val s3Endpoint = "s3.eu-central-1.com"
  val s3AccessKey = "s3_access_key"
  val s3SecretKey = "s3_secret_key"

  val params: Map[String, String] = Map(
    "S3_BUCKET_PATH" -> s3BucketPath,
    "S3_ENDPOINT" -> s3Endpoint,
    "S3_ACCESS_KEY" -> s3AccessKey,
    "S3_SECRET_KEY" -> s3SecretKey
  )

  val resourcePath = norm(Paths.get(getClass.getResource("/parquet").toURI))
  val resourceBucket = s"$resourcePath/*.parquet"

  def norm(path: Path): String =
    path.toUri.toString.replaceAll("/$", "").replaceAll("///", "/")

  def commonExaIterator(bucket: String): ExaIterator = {
    val mockIter = mock[ExaIterator]
    when(mockIter.getString(0)).thenReturn(bucket)
    when(mockIter.getString(1)).thenReturn("a")
    when(mockIter.getString(2)).thenReturn("b")
    when(mockIter.getString(3)).thenReturn("c")

    mockIter
  }
}
