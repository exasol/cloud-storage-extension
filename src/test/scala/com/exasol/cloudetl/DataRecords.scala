package com.exasol.cloudetl

import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

/**
 * A mixin trait with predefined helper data types.
 */
trait DataRecords {

  val BIG_DECIMAL_VALUE1: BigDecimal = new BigDecimal("5555555555555555555555555555555.55555")
  val BIG_DECIMAL_VALUE2: BigDecimal = new BigDecimal("5555555555555555555555555555555.55555")
  val DATE_VALUE1: Date = new Date(System.currentTimeMillis())
  val DATE_VALUE2: Date = new Date(System.currentTimeMillis())
  val TIMESTAMP_VALUE1: Timestamp = new Timestamp(System.currentTimeMillis())
  val TIMESTAMP_VALUE2: Timestamp = new Timestamp(System.currentTimeMillis())

  val rawRecords: Seq[Seq[Object]] = Seq(
    Seq[Any](1, 3L, BIG_DECIMAL_VALUE1, 3.14d, "xyz", true, DATE_VALUE1, TIMESTAMP_VALUE1),
    Seq[Any](2, 4L, BIG_DECIMAL_VALUE2, 0.13d, "abc", false, DATE_VALUE2, TIMESTAMP_VALUE2)
  ).map { seq =>
    seq.map(_.asInstanceOf[AnyRef])
  }

}
