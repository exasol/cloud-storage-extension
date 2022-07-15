package com.exasol.cloudetl.parallelism

import java.math.BigInteger

import com.exasol.ExaMetadata

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class AbstractUdfCountCalculatorTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {
  private val MB = 1000000
  private val GB = new BigInteger("1000000000")
  private val totalNumberOfNodes = 2
  private val metadata: ExaMetadata = mock[ExaMetadata]

  override final def beforeEach(): Unit = {
    when(metadata.getNodeCount()).thenReturn(totalNumberOfNodes)
    ()
  }

  test("calculates fixed based number of UDF instances") {
    // memory size in GB, cores per node, expected result
    val testData = Seq(
      (2L, 20, 8), // limited by memory (2GB / 500MB = 4, 2 nodes * Min(4, 20 cores) = 8 instances)
      (4L, 20, 16), // limited by memory (4GB / 500MB = 8, 2 nodes * Min(8, 20 cores) = 16 instances)
      (20L, 10, 20) // limited by cores (20GB / 500MB = 40, 2 nodes * Min(20, 10 cores) = 20 instances)
    )
    testData.foreach { case (memoryPerNode, coresPerNode, expectedResult) =>
      when(metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(memoryPerNode)))
      assertThat(FixedUdfCountCalculator(metadata).getUdfCount(coresPerNode), equalTo(expectedResult))
    }
  }

  test("calculates memory based number of UDF instances") {
    // memory size in GB, cores per node, udf memory in MB, expected result
    val testData = Seq(
      (2L, 20, 500L, 8), // same as above
      (4L, 20, 150L, 40), // limited by cores (4GB / 150MB = 26, 2 nodes * Min(26, 20) = 40 instances)
      (20L, 10, 1000L, 20), // limited by cores (20GB / 1000MB = 20, 2 nodes * Min(20, 10) = 20 instances)
      (10L, 25, 700L, 28) // limited by memory (10GB / 700MB = 14, 2 nodes * Min(14, 25) = 28 instances)
    )
    testData.foreach { case (memoryPerNode, coresPerNode, udfMemory, expectedResult) =>
      when(metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(memoryPerNode)))
      assertThat(MemoryUdfCountCalculator(metadata, udfMemory * MB).getUdfCount(coresPerNode), equalTo(expectedResult))
    }
  }

}
