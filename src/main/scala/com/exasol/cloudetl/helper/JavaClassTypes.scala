package com.exasol.cloudetl.helper

/**
 * An extendable trait that holds Java class types.
 *
 * These class values are used in pattern match to obtain the
 * corresponding values from Exasol iterator.
 */
trait JavaClassTypes {
  val jInteger: Class[java.lang.Integer] = classOf[java.lang.Integer]
  val jLong: Class[java.lang.Long] = classOf[java.lang.Long]
  val jBigDecimal: Class[java.math.BigDecimal] = classOf[java.math.BigDecimal]
  val jDouble: Class[java.lang.Double] = classOf[java.lang.Double]
  val jBoolean: Class[java.lang.Boolean] = classOf[java.lang.Boolean]
  val jString: Class[java.lang.String] = classOf[java.lang.String]
  val jSqlDate: Class[java.sql.Date] = classOf[java.sql.Date]
  val jSqlTimestamp: Class[java.sql.Timestamp] = classOf[java.sql.Timestamp]
}
