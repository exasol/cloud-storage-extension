package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._

class ImportFilesSuite extends BaseSuite {

  test("`run` should throw if the source is not supported") {
    val file = s"$resourcePath/import/parquet/sales_positions1.snappy.parquet"
    val exaIter = commonExaIterator(resourceImportBucket, "CSV")
    when(exaIter.next()).thenReturn(false)
    when(exaIter.getString(2)).thenReturn(file)

    val thrown = intercept[IllegalArgumentException] {
      ImportFiles.run(mock[ExaMetadata], exaIter)
    }
    assert(thrown.getMessage === "Unsupported storage format: 'CSV'")
  }

  test("`run` should emit total number of records from a source") {
    val file1 = s"$resourcePath/import/parquet/sales_positions1.snappy.parquet"
    val file2 = s"$resourcePath/import/parquet/sales_positions2.snappy.parquet"

    val exaIter = commonExaIterator(resourceImportBucket)
    when(exaIter.next()).thenReturn(true, false)
    when(exaIter.getString(2)).thenReturn(file1, file2)

    ImportFiles.run(mock[ExaMetadata], exaIter)

    val totalRecords = 1000
    verify(exaIter, times(totalRecords)).emit(Seq(any[Object]): _*)
  }

  /**
   *
   * +---------+-----------+----------+------+-----+----------+--------+
   * |sales_id |position_id|article_id|amount|price|voucher_id|canceled|
   * +---------+-----------+----------+------+-----+----------+--------+
   * |582244536|2          |96982     |1     |0.56 |null      |null    |
   * |582177839|6          |96982     |2     |0.56 |null      |null    |
   * |582370207|0          |96982     |1     |0.56 |null      |null    |
   * |582344312|0          |96982     |5     |0.56 |null      |null    |
   * |582344274|1          |96982     |1     |0.56 |null      |null    |
   * +---------+-----------+----------+------+-----+----------+--------+
   *
   */
  test("`run` should emit correct sequence of records from parquet file") {
    val file = s"$resourcePath/import/parquet/sales_positions_small.snappy.parquet"

    val exaIter = commonExaIterator(resourceImportBucket)
    when(exaIter.next()).thenReturn(false)
    when(exaIter.getString(2)).thenReturn(file)

    ImportFiles.run(mock[ExaMetadata], exaIter)

    verifySmallFilesImport(exaIter)
  }

  test("`run` should emit correct sequence of records from avro file") {
    val file = s"$resourcePath/import/avro/sales_positions_small.avro"

    val exaIter = commonExaIterator(resourceImportBucket, "AVRO")
    when(exaIter.next()).thenReturn(false)
    when(exaIter.getString(2)).thenReturn(file)

    ImportFiles.run(mock[ExaMetadata], exaIter)

    verifySmallFilesImport(exaIter)
  }

  private def verifySmallFilesImport(iter: ExaIterator): Unit = {
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
    records.foreach {
      case rows =>
        verify(iter, times(1)).emit(rows: _*)
    }
  }

}
