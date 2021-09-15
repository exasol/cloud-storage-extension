package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._

class FilesDataImporterTest extends StorageTest {

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
    verify(iter, times(expectedNumberOfRecords)).emit(Seq(any[Object]): _*)
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
      Seq(582244536L, 2, 96982, 1, 0.56, null, null),
      Seq(582177839L, 6, 96982, 2, 0.56, null, null),
      Seq(582370207L, 0, 96982, 1, 0.56, null, null),
      Seq(582344312L, 0, 96982, 5, 0.56, null, null),
      Seq(582344274L, 1, 96982, 1, 0.56, null, null)
    ).map { seq =>
      seq.map(_.asInstanceOf[AnyRef])
    }

    verify(iter, times(totalRecords)).emit(Seq(any[Object]): _*)
    records.foreach { case rows =>
      verify(iter, times(1)).emit(rows: _*)
    }
  }

}
