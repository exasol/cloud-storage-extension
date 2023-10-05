package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.parquetio.data.ChunkInterval
import com.exasol.parquetio.data.ChunkIntervalImpl

import org.mockito.Mockito._
import org.scalatest.matchers.should._

class FilesDataImporterTest extends StorageTest with Matchers {

  private[this] val properties = Map(
    "BUCKET_PATH" -> testResourceParquetPath,
    "DATA_FORMAT" -> "PARQUET"
  )

  test("run throws if file format is not supported") {
    val file = s"$testResourceDir/import/parquet/sales_positions1.snappy.parquet"
    val iter = mockFileIterator("CSV", file)
    val thrown = intercept[IllegalArgumentException] {
      FilesDataImporter.run(mock[ExaMetadata], iter)
    }
    assert(thrown.getMessage().startsWith("E-CSE-17"))
    assert(thrown.getMessage().contains("file format 'CSV' is not supported."))
  }

  test("run emits all records from a source") {
    val file1 = s"$testResourceDir/import/parquet/sales_positions1.snappy.parquet"
    val file2 = s"$testResourceDir/import/parquet/sales_positions2.snappy.parquet"
    val expectedNumberOfRecords = 1000

    val iter = mockExasolIterator(properties)
    when(iter.next()).thenReturn(true, false)
    when(iter.getString(2)).thenReturn(file1, file2)
    when(iter.getLong(3)).thenReturn(0L, 0L)
    when(iter.getLong(4)).thenReturn(1L, 1L)

    FilesDataImporter.run(mock[ExaMetadata], iter)
    verify(iter, times(expectedNumberOfRecords)).emit(anyObjects())
  }

  /**
   * +---------+-----------+----------+------+-----+----------+--------+
   * |sales_id |position_id|article_id|amount|price|voucher_id|canceled|
   * +---------+-----------+----------+------+-----+----------+--------+
   * |582244536|2          |96982     |1     |0.56 |null      |null    |
   * |582177839|6          |96982     |2     |0.56 |null      |null    |
   * |582370207|0          |96982     |1     |0.56 |null      |null    |
   * |582344312|0          |96982     |5     |0.56 |null      |null    |
   * |582344274|1          |96982     |1     |0.56 |null      |null    |
   * +---------+-----------+----------+------+-----+----------+--------+
   */
  test("run emits correct sequence of records from PARQUET file") {
    val parquetFile = s"$testResourceDir/import/parquet/sales_positions_small.snappy.parquet"
    val iter = mockFileIterator("PARQUET", parquetFile)
    FilesDataImporter.run(mock[ExaMetadata], iter)
    verifySmallFilesImport(iter)
  }

  test("run emits correct sequence of records from AVRO file") {
    val avroFile = s"$testResourceDir/import/avro/sales_positions_small.avro"
    val iter = mockFileIterator("AVRO", avroFile)
    FilesDataImporter.run(mock[ExaMetadata], iter)
    verifySmallFilesImport(iter)
  }

  test("collectFiles for empty iterator (will never happen in a UDF)") {
    val result = FilesDataImporter.collectFiles(ExaIteratorMock.empty())
    assert(result.size == 1)
    result.get(null).get.should(contain).theSameElementsAs(List(chunk(0, 0)))
  }

  test("collectFiles for iterator with single entry") {
    val result =
      FilesDataImporter.collectFiles(new ExaIteratorMock(Array(Array(null, null, "file1.parquet", 17L, 42L))))
    result.get("file1.parquet").get.should(contain).theSameElementsAs(List(chunk(17, 42)))
  }

  test("collectFiles for iterator with single file but multiple chunks") {
    val result =
      FilesDataImporter.collectFiles(
        new ExaIteratorMock(
          Array(Array(null, null, "file1.parquet", 17L, 42L), Array(null, null, "file1.parquet", 1L, 2L))
        )
      )
    assert(result.size == 1)
    result.get("file1.parquet").get.should(contain).theSameElementsAs(List(chunk(17, 42), chunk(1, 2)))
  }

  test("collectFiles for iterator with multiple files and multiple chunks") {
    val result =
      FilesDataImporter.collectFiles(
        new ExaIteratorMock(
          Array(
            Array(null, null, "file1.parquet", 17L, 42L),
            Array(null, null, "file1.parquet", 1L, 2L),
            Array(null, null, "file2.parquet", 0L, 1L)
          )
        )
      )
    assert(result.size == 2)
    result.get("file1.parquet").get.should(contain).theSameElementsAs(List(chunk(17, 42), chunk(1, 2)))
    result.get("file2.parquet").get.should(contain).theSameElementsAs(List(chunk(0, 1)))
  }

  test("collectFiles for iterator with two files") {
    val result =
      FilesDataImporter.collectFiles(
        new ExaIteratorMock(
          Array(Array(null, null, "file1.parquet", 17L, 42L), Array(null, null, "file2.parquet", 1L, 2L))
        )
      )
    assert(result.size == 2)
    result.get("file1.parquet").get.should(contain).theSameElementsAs(List(chunk(17, 42)))
    result.get("file2.parquet").get.should(contain).theSameElementsAs(List(chunk(1, 2)))
  }

  private[this] def chunk(start: Long, end: Long): ChunkInterval =
    new ChunkIntervalImpl(start, end)

  private[this] def mockFileIterator(fileFormat: String, filename: String): ExaIterator = {
    val iter = mockExasolIterator(properties ++ Map("DATA_FORMAT" -> fileFormat))
    when(iter.next()).thenReturn(false)
    when(iter.getString(2)).thenReturn(filename)
    when(iter.getLong(3)).thenReturn(0L)
    when(iter.getLong(4)).thenReturn(1L)
    iter
  }

  private[this] def verifySmallFilesImport(iter: ExaIterator): Unit = {
    val totalRecords = 5
    val records: Seq[Seq[Object]] = Seq(
      Seq[Any](582244536L, 2, 96982, 1, 0.56, null, null),
      Seq[Any](582177839L, 6, 96982, 2, 0.56, null, null),
      Seq[Any](582370207L, 0, 96982, 1, 0.56, null, null),
      Seq[Any](582344312L, 0, 96982, 5, 0.56, null, null),
      Seq[Any](582344274L, 1, 96982, 1, 0.56, null, null)
    ).map { seq =>
      seq.map(_.asInstanceOf[AnyRef])
    }

    verify(iter, times(totalRecords)).emit(anyObjects())
    records.foreach { case rows =>
      verify(iter, times(1)).emit(rows: _*)
    }
  }

}
