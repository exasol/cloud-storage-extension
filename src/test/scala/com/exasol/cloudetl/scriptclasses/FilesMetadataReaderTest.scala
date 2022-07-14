package com.exasol.cloudetl.scriptclasses

import java.lang.Long

import com.exasol.ExaMetadata

import org.mockito.Mockito._
import java.math.BigInteger

class FilesMetadataReaderTest extends StorageTest {

  test("run returns the list of file names") {
    val properties = Map(
      "BUCKET_PATH" -> testResourceParquetPath,
      "DATA_FORMAT" -> "PARQUET"
    )
    val expectedParquetFiles = Map(
      s"$testResourceDir/import/parquet/sales_positions1.snappy.parquet" -> "0",
      s"$testResourceDir/import/parquet/sales_positions2.snappy.parquet" -> "0",
      s"$testResourceDir/import/parquet/sales_positions_small.snappy.parquet" -> "1"
    )

    val iter = mockExasolIterator(properties)
    when(iter.getInteger(2)).thenReturn(2)
    val meta = mock[ExaMetadata]
    when(meta.getNodeCount()).thenReturn(1)
    when(meta.getMemoryLimit()).thenReturn(new BigInteger("2000000000")) // 2GB
    FilesMetadataReader.run(meta, iter)
    expectedParquetFiles.foreach { case (filename, partitionId) =>
      verify(iter, times(1)).emit(filename, partitionId, Long.valueOf(0), Long.valueOf(1))
    }
  }

}
