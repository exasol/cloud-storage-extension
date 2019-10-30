package com.exasol.cloudetl.bucket

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

class AbstractBucketTest extends FunSuite with BeforeAndAfterEach {

  private[bucket] val PATH: String = "BUCKET_PATH"
  private[bucket] val FORMAT: String = "DATA_FORMAT"
  private[bucket] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

}
