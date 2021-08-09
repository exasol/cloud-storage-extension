package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito._

class FilesMetadataReaderTest extends StorageTest {

  test("run returns the list of file names") {
    val properties = Map(
      "BUCKET_PATH" -> testResourceParquetPath,
      "DATA_FORMAT" -> "PARQUET"
    )
    val expectedParquetFiles = Map(
      s"$testResourceDir/import/parquet/sales_positions1.snappy.parquet" -> "0",
      s"$testResourceDir/import/parquet/sales_positions2.snappy.parquet" -> "1",
      s"$testResourceDir/import/parquet/sales_positions_small.snappy.parquet" -> "0"
    )

    val iter = mockExasolIterator(properties)
    when(iter.getInteger(2)).thenReturn(2)

    FilesMetadataReader.run(mock[ExaMetadata], iter)
    verify(iter, times(3)).emit(anyString(), anyString())
    expectedParquetFiles.foreach { case (filename, partitionId) =>
      verify(iter, times(1)).emit(filename, partitionId)
    }
  }

}
