package com.exasol.cloudetl.parallelism

import com.typesafe.scalalogging.LazyLogging

/**
 * An interface for calculating number of parallel {@code UDF} instances.
 */
trait UdfCountCalculator extends LazyLogging {

  /**
   * Returns total number of nodes in current cluster.
   *
   * @return number of nodes
   */
  def getNumberOfNodes(): Int

  /**
   * Calculates number of cores that are restricted by memory limit per node.
   *
   * This method calculates the number of cores where each core has at least some predefined memory.
   *
   * @return calculated number of cores
   */
  def getNumberOfCoresLimitedByMemoryPerNode(): Int

  /**
   * Calculates the number of possible UDF instances.
   *
   * @param numberOfCoresPerNode number of cores available in a node
   * @return number of UDF instances
   */
  def getUdfCount(numberOfCoresPerNode: Int): Int = {
    val totalNodesInCluster = getNumberOfNodes()
    val numberOfCoresLimitedByMemory = getNumberOfCoresLimitedByMemoryPerNode()
    val autoParallelism = totalNodesInCluster * Math.min(numberOfCoresLimitedByMemory, numberOfCoresPerNode)
    logger.info(
      s"Calculated maximum UDF parallelism as, $totalNodesInCluster total nodes * min("
        + s"$numberOfCoresPerNode cores per node, $numberOfCoresLimitedByMemory"
        + s" cores limited by memory) = $autoParallelism."
    )
    autoParallelism
  }

}
