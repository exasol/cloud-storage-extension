package com.exasol.cloudetl.util

import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.helper.ExasolColumnValueProvider

import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.BeforeAndAfterEach

@SuppressWarnings(Array("org.wartremover.contrib.warts.ExposedTuples"))
class ExasolColumnValueProviderTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

  private[this] val START_INDEX = 3
  private[this] var exasolIterator: ExaIterator = _
  private[this] var columnValueProvider: ExasolColumnValueProvider = _

  override final def beforeEach(): Unit = {
    exasolIterator = mock[ExaIterator]
    columnValueProvider = ExasolColumnValueProvider(exasolIterator)
    ()
  }

  test("getColumnValue returns value with column type") {
    val bd = new BigDecimal(1337)
    val dt = new Date(System.currentTimeMillis())
    val ts = new Timestamp(System.currentTimeMillis())
    when(exasolIterator.getInteger(3)).thenReturn(1)
    when(exasolIterator.getLong(4)).thenReturn(3L)
    when(exasolIterator.getBigDecimal(5)).thenReturn(bd)
    when(exasolIterator.getDouble(6)).thenReturn(3.14)
    when(exasolIterator.getString(7)).thenReturn("xyz")
    when(exasolIterator.getBoolean(8)).thenReturn(true)
    when(exasolIterator.getDate(9)).thenReturn(dt)
    when(exasolIterator.getTimestamp(10)).thenReturn(ts)

    val data = Seq(
      1 -> ExaColumnInfo("c_int", classOf[java.lang.Integer]),
      3L -> ExaColumnInfo("c_long", classOf[java.lang.Long]),
      bd -> ExaColumnInfo("c_decimal", classOf[BigDecimal], precision = 4),
      3.14 -> ExaColumnInfo("c_double", classOf[java.lang.Double]),
      "xyz" -> ExaColumnInfo("c_string", classOf[java.lang.String]),
      true -> ExaColumnInfo("c_boolean", classOf[java.lang.Boolean]),
      dt -> ExaColumnInfo("c_date", classOf[Date]),
      ts -> ExaColumnInfo("c_timestamp", classOf[Timestamp])
    )
    data.zipWithIndex.map { case ((expectedValue, columnInfo), idx) =>
      val nextIndex = START_INDEX + idx
      val value = columnValueProvider.getColumnValue(nextIndex, columnInfo)
      assert(value === expectedValue)
      assert(value.getClass() === columnInfo.`type`)
    }
  }

  test("getColumnValue throws for unsupported type") {
    val thrown = intercept[IllegalArgumentException] {
      columnValueProvider.getColumnValue(0, ExaColumnInfo("c_short", classOf[java.lang.Short]))
    }
    assert(thrown.getMessage().startsWith("E-CSE-23"))
    assert(thrown.getMessage().contains("Cannot obtain Exasol value for column type 'class java.lang.Short'."))
  }

  test("getColumnValue throws for updated big decimal with higher precision") {
    val decimal = BigDecimal.valueOf(13.37)
    val columnInfo = ExaColumnInfo("c_dec", classOf[BigDecimal], precision = 4, scale = 3)
    when(exasolIterator.getBigDecimal(3)).thenReturn(decimal)
    val thrown = intercept[IllegalArgumentException] {
      columnValueProvider.getColumnValue(3, columnInfo)
    }
    assert(thrown.getMessage().startsWith("E-CSE-24"))
    assert(thrown.getMessage().contains("value exceeds configured '4'."))
  }

  test("getColumnValue returns updated big decimal") {
    val decimal = BigDecimal.valueOf(238316.38)
    val columnInfo = ExaColumnInfo("c_decimal", classOf[BigDecimal], precision = 18, scale = 4)
    when(exasolIterator.getBigDecimal(3)).thenReturn(decimal)

    @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf")) // safe since we set the return value
    val columnValue = columnValueProvider.getColumnValue(3, columnInfo).asInstanceOf[BigDecimal]
    assert(columnValue.toPlainString() === "238316.3800")
  }

}
