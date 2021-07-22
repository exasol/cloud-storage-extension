package com.exasol.cloudetl.util

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.cloudetl.helper.ExasolColumnValueProvider

import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

@SuppressWarnings(Array("org.wartremover.contrib.warts.ExposedTuples"))
class ExasolColumnValueProviderTest extends AnyFunSuite with MockitoSugar {

  test("getColumnValue returns value with column type") {
    val iter = mock[ExaIterator]
    val columnValueProvider = ExasolColumnValueProvider(iter)
    val startIdx = 3
    val bd = new java.math.BigDecimal(1337)
    val dt = new java.sql.Date(System.currentTimeMillis())
    val ts = new java.sql.Timestamp(System.currentTimeMillis())

    when(iter.getInteger(3)).thenReturn(1)
    when(iter.getLong(4)).thenReturn(3L)
    when(iter.getBigDecimal(5)).thenReturn(bd)
    when(iter.getDouble(6)).thenReturn(3.14)
    when(iter.getString(7)).thenReturn("xyz")
    when(iter.getBoolean(8)).thenReturn(true)
    when(iter.getDate(9)).thenReturn(dt)
    when(iter.getTimestamp(10)).thenReturn(ts)

    val data = Seq(
      1 -> ExaColumnInfo("c_int", classOf[java.lang.Integer]),
      3L -> ExaColumnInfo("c_long", classOf[java.lang.Long]),
      bd -> ExaColumnInfo("c_decimal", classOf[java.math.BigDecimal]),
      3.14 -> ExaColumnInfo("c_double", classOf[java.lang.Double]),
      "xyz" -> ExaColumnInfo("c_string", classOf[java.lang.String]),
      true -> ExaColumnInfo("c_boolean", classOf[java.lang.Boolean]),
      dt -> ExaColumnInfo("c_date", classOf[java.sql.Date]),
      ts -> ExaColumnInfo("c_timestamp", classOf[java.sql.Timestamp])
    )

    data.zipWithIndex.map { case ((expectedValue, columnInfo), idx) =>
      val nextIndex = startIdx + idx
      val value = columnValueProvider.getColumnValue(nextIndex, columnInfo)
      assert(value === expectedValue)
      assert(value.getClass() === columnInfo.`type`)
    }

    val thrown = intercept[IllegalArgumentException] {
      columnValueProvider.getColumnValue(0, ExaColumnInfo("c_short", classOf[java.lang.Short]))
    }
    assert(thrown.getMessage() === "Cannot get Exasol value for column type 'class java.lang.Short'.")
  }

}
