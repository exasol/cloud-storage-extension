package com.exasol.cloudetl.bucket

import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageProperties

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
class AbstractBucketTest extends FunSuite with BeforeAndAfterEach {

  private[bucket] val PATH: String = "BUCKET_PATH"
  private[bucket] val FORMAT: String = "DATA_FORMAT"
  private[bucket] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  protected[this] final def getBucket(params: Map[String, String]): Bucket =
    Bucket(StorageProperties(params))

  protected[this] final def getBucket(
    params: Map[String, String],
    exaMetadata: ExaMetadata
  ): Bucket =
    Bucket(StorageProperties(params, exaMetadata))
}
