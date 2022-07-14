package com.exasol.cloudetl.parallelism

import java.math.BigInteger

import com.exasol.ExaMetadata

/**
 * An implementation of {@link UdfCountCalculator} that uses fixed memory value for an UDF.
 */
final case class FixedUdfCountCalculator(metadata: ExaMetadata)
    extends AbstractUdfCountCalculator(metadata, 500 * 1000000)

/**
 * An implementation of {@link UdfCountCalculator} that uses dynamic memory value for an UDF.
 */
final case class MemoryUdfCountCalculator(metadata: ExaMetadata, memory: Long)
    extends AbstractUdfCountCalculator(metadata, memory)

/**
 * An abstract class for {@link UdfCountCalculator} that contains common methods.
 */
abstract class AbstractUdfCountCalculator(metadata: ExaMetadata, memory: Long) extends UdfCountCalculator {

  override def getNumberOfNodes(): Int =
    metadata.getNodeCount().toInt

  override def getNumberOfCoresLimitedByMemoryPerNode(): Int = {
    val memoryLimit = metadata.getMemoryLimit()
    val coresPerNode = memoryLimit.divide(BigInteger.valueOf(memory)).intValue()
    logger.info(
      s"Calculated cores limited by memory ($memoryLimit MB memory per node / $memory MB UDF memory = $coresPerNode)."
    )
    coresPerNode
  }

}
