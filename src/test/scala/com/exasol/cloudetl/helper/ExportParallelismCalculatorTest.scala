package com.exasol.cloudetl.helper

import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class ExportParallelismCalculatorTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {
  private val metadata: ExaMetadata = mock[ExaMetadata]

  override def beforeEach(): Unit = {
    when(metadata.getNodeCount()).thenReturn(1)
    when(metadata.getMemoryLimit()).thenReturn(new java.math.BigInteger("2000000000")) // 2GB
    ()
  }

  test("getParallelism() returns user provided value") {
    val properties = StorageProperties(Map("PARALLELISM" -> "iproc()"))
    assert(ExportParallelismCalculator(metadata, properties).getParallelism() === "iproc()")
  }

  test("getParallelism() returns calculated value") {
    assert(
      ExportParallelismCalculator(metadata, StorageProperties(Map.empty[String, String]))
        .getParallelism()
        .contains("iproc(), mod(rownum,")
    )
  }

}
