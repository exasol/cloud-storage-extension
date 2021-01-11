package com.exasol.cloudetl.scriptclasses

import java.nio.file.Files
import java.nio.file.Path

import scala.collection.JavaConverters._

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.DataRecords
import com.exasol.cloudetl.TestFileManager

import org.mockito.ArgumentMatchers.any
import org.mockito.ExtraMockito
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach

class ExportTableTest
    extends StorageTest
    with BeforeAndAfterEach
    with DataRecords
    with TestFileManager {

  private[this] var outputPath: Path = _
  private[this] val srcColumns: Seq[String] = Seq(
    "c_int",
    "c_long",
    "c_decimal",
    "c_double",
    "c_string",
    "c_boolean",
    "c_date",
    "c_timestamp"
  )

  private[this] var metadata: ExaMetadata = _
  private[this] var iterator: ExaIterator = _
  private[this] val defaultProperties = Map("DATA_FORMAT" -> "PARQUET")

  final def createMockedIterator(
    resourceDir: String,
    extraProperties: Map[String, String]
  ): ExaIterator = {
    val properties = defaultProperties ++ Map("BUCKET_PATH" -> resourceDir) ++ extraProperties
    val mockedIterator = mockExasolIterator(properties)

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

  @SuppressWarnings(Array("org.wartremover.warts.JavaSerializable"))
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
    metadata = createMockedMetadata()
    iterator = createMockedIterator(outputPath.toUri.toString, defaultProperties)
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputPath)
    ()
  }

  test("run exports table rows") {
    ExportTable.run(metadata, iterator)

    verify(metadata, times(1)).getInputColumnCount
    for { idx <- 3 to 10 } {
      verify(metadata, times(1)).getInputColumnType(idx)
      verify(metadata, times(1)).getInputColumnPrecision(idx)
      verify(metadata, times(1)).getInputColumnScale(idx)
      verify(metadata, times(1)).getInputColumnLength(idx)
    }

    verify(iterator, times(2)).getInteger(3)
    verify(iterator, times(2)).getLong(4)
    verify(iterator, times(2)).getBigDecimal(5)
    verify(iterator, times(2)).getDouble(6)
    verify(iterator, times(2)).getString(7)
    verify(iterator, times(2)).getBoolean(8)
    verify(iterator, times(2)).getDate(9)
    verify(iterator, times(2)).getTimestamp(10)
  }

  test("imports exported rows from a path") {
    ExportTable.run(metadata, iterator)

    val properties = Map("BUCKET_PATH" -> testResourceDir, "DATA_FORMAT" -> "PARQUET")
    val importIter = mockExasolIterator(properties)
    when(importIter.next()).thenReturn(false)
    when(importIter.getString(2)).thenReturn(outputPath.toUri.toString)

    ImportFiles.run(mock[ExaMetadata], importIter)

    val totalRecords = 2
    verify(importIter, times(totalRecords)).emit(Seq(any[Object]): _*)
  }

  test("export creates file without compression extension if compression codec is not set") {
    ExportTable.run(metadata, iterator)
    assert(Files.exists(outputPath) === true)
    assert(Files.list(outputPath).count() === 2)
    checkExportFileExtensions(outputPath, "")
  }

  test("export creates file with compression extension if compression codec is set") {
    val properties = defaultProperties ++ Map("PARQUET_COMPRESSION_CODEC" -> "SNAPPY")
    iterator = createMockedIterator(outputPath.toUri.toString, properties)
    ExportTable.run(metadata, iterator)
    assert(Files.exists(outputPath) === true)
    assert(Files.list(outputPath).count() === 2)
    checkExportFileExtensions(outputPath, ".snappy")
  }

  private[this] def checkExportFileExtensions(
    outputPath: Path,
    compressionCodec: String
  ): Unit = {
    val filtered = Files.list(outputPath).iterator().asScala.filter(_.endsWith(".crc"))
    assert(filtered.forall(_.endsWith(s"$compressionCodec.parquet")))
    ()
  }

}
