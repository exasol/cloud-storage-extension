package com.exasol.cloudetl.sink

import java.nio.file.Path

import com.exasol.cloudetl.DummyRecordsTest
import com.exasol.cloudetl.bucket.LocalBucket
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.data.Row
import com.exasol.cloudetl.storage.StorageProperties

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

class BatchSizedSinkTest extends FunSuite with BeforeAndAfterEach with DummyRecordsTest {

  private var outputPath: Path = _
  private val properties = Map("BUCKET_PATH" -> "a/path", "DATA_FORMAT" -> "avro")

  private val columnMetadata: Seq[ExaColumnInfo] = Seq(
    ExaColumnInfo("c_int", classOf[java.lang.Integer], 0, 0, 0),
    ExaColumnInfo("c_long", classOf[java.lang.Long], 0, 0, 0),
    ExaColumnInfo("c_decimal", classOf[java.math.BigDecimal], 36, 5, 0),
    ExaColumnInfo("c_double", classOf[java.lang.Double], 0, 0, 0),
    ExaColumnInfo("c_string", classOf[java.lang.String], 0, 0, 3),
    ExaColumnInfo("c_boolean", classOf[java.lang.Boolean], 0, 0, 0),
    ExaColumnInfo("c_date", classOf[java.sql.Date], 0, 0, 0),
    ExaColumnInfo("c_timestamp", classOf[java.sql.Timestamp], 0, 0, 0)
  )

  private val rows: Seq[Row] = rawRecords.map(Row(_))

  override final def beforeEach(): Unit = {
    outputPath = createTemporaryFolder("batchSizedSinkTest")
    ()
  }

  override final def afterEach(): Unit = {
    deleteFiles(outputPath)
    ()
  }

  test("export single file with default batch size") {
    val bucket = LocalBucket(
      outputPath.toUri.toString,
      StorageProperties(properties ++ Map("EXPORT_BATCH_SIZE" -> "4"))
    )
    val sink = new BatchSizedSink(1L, "vm1", 2, columnMetadata, bucket)
    rows.foreach { row =>
      sink.write(row)
    }
    sink.close()
    assert(sink.getTotalRecords() === 2)
  }

  test("export several files with batch size smaller than total records") {
    val bucket = LocalBucket(
      outputPath.toUri.toString,
      StorageProperties(properties ++ Map("EXPORT_BATCH_SIZE" -> "3"))
    )
    val sink = new BatchSizedSink(1L, "vm1", 7, columnMetadata, bucket)
    val newRows = rows ++ rows ++ rows ++ rows.take(1)
    newRows.foreach { row =>
      sink.write(row)
    }
    sink.close()
    assert(sink.getTotalRecords() === 7)
  }

}
