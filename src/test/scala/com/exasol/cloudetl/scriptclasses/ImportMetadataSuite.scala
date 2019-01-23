package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito._

class ImportMetadataSuite extends BaseSuite {

  test("`run` should create a list of files names") {
    val exaIter = commonExaIterator(resourceBucket)
    when(exaIter.getInteger(2)).thenReturn(2)

    ImportMetadata.run(mock[ExaMetadata], exaIter)

    verify(exaIter, times(3)).emit(anyString(), anyString())
    verify(exaIter, times(1)).emit(s"$resourcePath/sales_positions1.snappy.parquet", "0")
    verify(exaIter, times(1)).emit(s"$resourcePath/sales_positions2.snappy.parquet", "1")
    verify(exaIter, times(1)).emit(s"$resourcePath/sales_positions_small.snappy.parquet", "0")
  }

}
