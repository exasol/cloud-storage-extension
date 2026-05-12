package com.exasol.cloudetl

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

package object helper {
  lazy val ExasolColumnValueProvider: ExasolColumnValueProviderFactory.type = ExasolColumnValueProviderFactory
  lazy val ExportParallelismCalculator: ExportParallelismCalculatorFactory.type = ExportParallelismCalculatorFactory
  lazy val ParquetSchemaConverter: ParquetSchemaConverterFactory.type = ParquetSchemaConverterFactory
}

object ExasolColumnValueProviderFactory {
  def apply(iterator: ExaIterator): _root_.com.exasol.cloudetl.helper.ExasolColumnValueProvider =
    new _root_.com.exasol.cloudetl.helper.ExasolColumnValueProvider(iterator)
}

object ExportParallelismCalculatorFactory {
  def apply(
    metadata: ExaMetadata,
    properties: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.helper.ExportParallelismCalculator =
    new _root_.com.exasol.cloudetl.helper.ExportParallelismCalculator(metadata, properties)
}

object ParquetSchemaConverterFactory {
  lazy val DECIMAL_MAX_PRECISION: Int = _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter.DECIMAL_MAX_PRECISION
  lazy val DECIMAL_MAX_INT_DIGITS: Int = _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter.DECIMAL_MAX_INT_DIGITS
  lazy val DECIMAL_MAX_LONG_DIGITS: Int = _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter.DECIMAL_MAX_LONG_DIGITS
  lazy val PRECISION_TO_BYTE_SIZE: java.util.List[Integer] =
    _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter.PRECISION_TO_BYTE_SIZE

  def apply(
    isLowercaseSchemaEnabled: Boolean
  ): _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter =
    new _root_.com.exasol.cloudetl.helper.ParquetSchemaConverter(isLowercaseSchemaEnabled)
}
