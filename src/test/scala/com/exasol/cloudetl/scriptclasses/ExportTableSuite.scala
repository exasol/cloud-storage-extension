package com.exasol.cloudetl.scriptclasses

import java.nio.file.Path

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.TestUtils

import org.mockito.ArgumentMatchers.any
import org.mockito.ExtraMockito
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach

@SuppressWarnings(Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Var"))
class ExportTableSuite extends BaseSuite with BeforeAndAfterEach with TestUtils {

  private var outputPath: Path = _
  private var exasolMetadata: ExaMetadata = _
  private var exasolIterator: ExaIterator = _
  private val srcColumns: Seq[String] = Seq(
    "c_int",
    "c_long",
    "c_decimal",
    "c_double",
    "c_string",
    "c_boolean",
    "c_date",
    "c_timestamp"
  )

  final def createMockedIterator(resourceDir: String): ExaIterator = {
    val mockedIterator = commonExaIterator(resourceDir)
    when(mockedIterator.getString(2)).thenReturn(srcColumns.mkString("."))
    when(mockedIterator.next()).thenReturn(true, false)
    when(mockedIterator.size()).thenReturn(2L)

    when(mockedIterator.getInteger(3)).thenReturn(1, 2)
    when(mockedIterator.getLong(4)).thenReturn(3L, 4L)
    when(mockedIterator.getBigDecimal(5)).thenReturn(BIG_DECIMAL_VALUE1, BIG_DECIMAL_VALUE2)
    when(mockedIterator.getDouble(6)).thenReturn(3.14, 0.13)
    when(mockedIterator.getString(7)).thenReturn("xyz", "abc")
    when(mockedIterator.getBoolean(8)).thenReturn(true, false)
    when(mockedIterator.getDate(9)).thenReturn(DATE_VALUE1, DATE_VALUE2)
    when(mockedIterator.getTimestamp(10)).thenReturn(TIMESTAMP_VALUE1, TIMESTAMP_VALUE2)

    mockedIterator
  }

  final def createMockedMetadata(): ExaMetadata = {
    val mockedMetadata = mock[ExaMetadata]
    when(mockedMetadata.getInputColumnCount()).thenReturn(11L)
    val returns = Seq(
      (3, classOf[java.lang.Integer], 0L, 0L, 0L),
      (4, classOf[java.lang.Long], 0L, 0L, 0L),
      (5, classOf[java.math.BigDecimal], 36L, 5L, 0L),
      (6, classOf[java.lang.Double], 0L, 0L, 0L),
      (7, classOf[java.lang.String], 0L, 0L, 3L),
      (8, classOf[java.lang.Boolean], 0L, 0L, 0L),
      (9, classOf[java.sql.Date], 0L, 0L, 0L),
      (10, classOf[java.sql.Timestamp], 0L, 0L, 0L)
    )
    returns.foreach {
      case (idx, cls, prec, scale, len) =>
        ExtraMockito.doReturn(cls).when(mockedMetadata).getInputColumnType(idx)
        when(mockedMetadata.getInputColumnPrecision(idx)).thenReturn(prec)
        when(mockedMetadata.getInputColumnScale(idx)).thenReturn(scale)
        when(mockedMetadata.getInputColumnLength(idx)).thenReturn(len)
    }

    mockedMetadata
  }

  override final def beforeEach(): Unit = {
    outputPath = createTemporaryFolder("exportTableTest")
    exasolMetadata = createMockedMetadata()
    exasolIterator = createMockedIterator(outputPath.toUri.toString)
    ()
  }

  override final def afterEach(): Unit = {
    deleteFiles(outputPath)
    ()
  }

  test("`run` should export the Exasol rows from ExaIterator") {
    ExportTable.run(exasolMetadata, exasolIterator)

    verify(exasolMetadata, times(1)).getInputColumnCount
    for { idx <- 3 to 10 } {
      verify(exasolMetadata, times(1)).getInputColumnType(idx)
      verify(exasolMetadata, times(1)).getInputColumnPrecision(idx)
      verify(exasolMetadata, times(1)).getInputColumnScale(idx)
      verify(exasolMetadata, times(1)).getInputColumnLength(idx)
    }

    verify(exasolIterator, times(2)).getInteger(3)
    verify(exasolIterator, times(2)).getLong(4)
    verify(exasolIterator, times(2)).getBigDecimal(5)
    verify(exasolIterator, times(2)).getDouble(6)
    verify(exasolIterator, times(2)).getString(7)
    verify(exasolIterator, times(2)).getBoolean(8)
    verify(exasolIterator, times(2)).getDate(9)
    verify(exasolIterator, times(2)).getTimestamp(10)
  }

  test("import exported rows from a file") {
    ExportTable.run(exasolMetadata, exasolIterator)

    val importIter = commonExaIterator(resourceImportBucket)
    when(importIter.next()).thenReturn(false)
    when(importIter.getString(2)).thenReturn(outputPath.toUri.toString)

    ImportFiles.run(mock[ExaMetadata], importIter)

    val totalRecords = 2
    verify(importIter, times(totalRecords)).emit(Seq(any[Object]): _*)
  }

}
