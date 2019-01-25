package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._

@SuppressWarnings(Array("org.wartremover.warts.Any"))
class ImportFilesSuite extends BaseSuite {

  test("`run` should emit total number of records") {
    val file1 = s"$resourcePath/sales_positions1.snappy.parquet"
    val file2 = s"$resourcePath/sales_positions2.snappy.parquet"

    val exaIter = commonExaIterator(resourceBucket)

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
  test("`run` should emit correct sequence of records") {
    val file = s"$resourcePath/sales_positions_small.snappy.parquet"

    val exaIter = commonExaIterator(resourceBucket)

    when(exaIter.next()).thenReturn(false)
    when(exaIter.getString(2)).thenReturn(file)

    ImportFiles.run(mock[ExaMetadata], exaIter)

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

    verify(exaIter, times(totalRecords)).emit(Seq(any[Object]): _*)
    records.foreach {
      case rows =>
        verify(exaIter, times(1)).emit(rows: _*)
    }
  }

}
