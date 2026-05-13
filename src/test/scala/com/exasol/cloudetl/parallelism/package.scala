package com.exasol.cloudetl

import com.exasol.ExaMetadata

package object parallelism {
  val FixedUdfCountCalculator: FixedUdfCountCalculatorFactory.type = FixedUdfCountCalculatorFactory
  val MemoryUdfCountCalculator: MemoryUdfCountCalculatorFactory.type = MemoryUdfCountCalculatorFactory
}

object FixedUdfCountCalculatorFactory {
  def apply(metadata: ExaMetadata): _root_.com.exasol.cloudetl.parallelism.FixedUdfCountCalculator =
    new _root_.com.exasol.cloudetl.parallelism.FixedUdfCountCalculator(metadata)
}

object MemoryUdfCountCalculatorFactory {
  def apply(
    metadata: ExaMetadata,
    memory: Long
  ): _root_.com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator =
    new _root_.com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator(metadata, memory)
}
