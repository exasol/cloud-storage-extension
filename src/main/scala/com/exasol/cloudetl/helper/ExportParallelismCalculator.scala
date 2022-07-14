package com.exasol.cloudetl.helper

import com.exasol.ExaMetadata
import com.exasol.cloudetl.parallelism.FixedUdfCountCalculator
import com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator
import com.exasol.cloudetl.storage.StorageProperties

/**
 * A class that generates parallelism string for an export.
 */
final case class ExportParallelismCalculator(metadata: ExaMetadata, properties: StorageProperties) {

  /**
   * Returns a SQL string for parallel UDF instances when exporting data.
   *
   * It uses user provided value; if it is not provided, it uses calculated
   * cores per node in combination with {@code iproc()}.
   *
   * @return parallelism string
   */
  def getParallelism(): String =
    properties.getParallelism().getOrElse {
      val multiplier = getCoresPerNode()
      String.format("iproc(), mod(rownum,%d)", multiplier)
    }

  private[this] def getCoresPerNode(): Int = {
    val coresPerNode = Runtime.getRuntime().availableProcessors()
    math.min(coresPerNode, getCoresLimitedByMemory())
  }

  private[this] def getCoresLimitedByMemory(): Int =
    if (properties.hasUdfMemory()) {
      MemoryUdfCountCalculator(metadata, properties.getUdfMemory()).getNumberOfCoresLimitedByMemoryPerNode()
    } else {
      FixedUdfCountCalculator(metadata).getNumberOfCoresLimitedByMemoryPerNode()
    }

}
