package com.exasol.cloudetl.util

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

import org.scalatest.FunSuite
import org.scalatest.Matchers

@SuppressWarnings(Array("org.wartremover.contrib.warts.OldTime"))
class DateTimeUtilTest extends FunSuite with Matchers {

  final def daysSinceEpochToDate(dt: Date): Unit = {
    val newDT = DateTimeUtil.daysToDate(DateTimeUtil.daysSinceEpoch(dt))
    assert(dt.toString === newDT.toString)
    ()
  }

  test("from java.sql.Date to days (since epoch) and back to java.sql.Date") {
    val df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US)

    val testDate = Seq(
      new Date(100),
      new Date(df1.parse("1776-07-04 10:30:00").getTime),
      new Date(df2.parse("1776-07-04 18:30:00 UTC").getTime),
      Date.valueOf("1912-05-05"),
      Date.valueOf("1969-01-01"),
      new Date(df1.parse("1969-01-01 00:00:00").getTime),
      new Date(df2.parse("1969-01-01 00:00:00 UTC").getTime),
      new Date(df1.parse("1969-01-01 00:00:01").getTime),
      new Date(df2.parse("1969-01-01 00:00:01 UTC").getTime),
      new Date(df1.parse("1969-12-31 23:59:59").getTime),
      new Date(df2.parse("1969-12-31 23:59:59 UTC").getTime),
      Date.valueOf("1970-01-01"),
      new Date(df1.parse("1970-01-01 00:00:00").getTime),
      new Date(df2.parse("1970-01-01 00:00:00 UTC").getTime),
      new Date(df1.parse("1970-01-01 00:00:01").getTime),
      new Date(df2.parse("1970-01-01 00:00:01 UTC").getTime),
      new Date(df1.parse("1989-11-09 11:59:59").getTime),
      new Date(df2.parse("1989-11-09 19:59:59 UTC").getTime),
      Date.valueOf("2019-02-10")
    )

    testDate.foreach { case dt => daysSinceEpochToDate(dt) }
  }

  test("correctly converts date `0001-01-01` to days and back to date") {
    daysSinceEpochToDate(Date.valueOf("0001-01-01"))
  }
}
