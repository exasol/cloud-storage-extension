package com.exasol.cloudetl.helper

import java.math.BigDecimal
import java.math.RoundingMode

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo
import com.exasol.errorreporting.ExaError

/**
 * A class that returns column value on a given index and type from Exasol iterator.
 */
final case class ExasolColumnValueProvider(iterator: ExaIterator) extends JavaClassTypes {

  /**
   * Returns a value from Exasol [[ExaIterator]] iterator on given index
   * which has [[com.exasol.cloudetl.data.ExaColumnInfo]] column type.
   *
   * @param index index of a column
   * @param columnInfo column information that holds column type
   * @return a corresponding column value
   */
  def getColumnValue(index: Int, columnInfo: ExaColumnInfo): Any = {
    val columnType = columnInfo.`type`
    columnType match {
      case `jInteger`      => iterator.getInteger(index)
      case `jLong`         => iterator.getLong(index)
      case `jBigDecimal`   => updateBigDecimal(iterator.getBigDecimal(index), columnInfo.precision, columnInfo.scale)
      case `jDouble`       => iterator.getDouble(index)
      case `jString`       => iterator.getString(index)
      case `jBoolean`      => iterator.getBoolean(index)
      case `jSqlDate`      => iterator.getDate(index)
      case `jSqlTimestamp` => iterator.getTimestamp(index)
      case _ =>
        throw new IllegalArgumentException(
          ExaError
            .messageBuilder("E-CSE-23")
            .message("Cannot obtain Exasol value for column type {{TYPE}}.")
            .parameter("TYPE", String.valueOf(columnType))
            .toString()
        )
    }
  }

  private[this] def updateBigDecimal(bigDecimal: BigDecimal, precision: Int, scale: Int): BigDecimal =
    if (bigDecimal == null) {
      bigDecimal
    } else {
      val updatedBigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP)
      if (updatedBigDecimal.precision > precision) {
        throw new IllegalArgumentException(
          ExaError
            .messageBuilder("E-CSE-24")
            .message(
              "Actual precision {{ACTUAL_PRECISION}} of big decimal {{VALUE}} value exceeds configured {{PRECISION}}."
            )
            .parameter("ACTUAL_PRECISION", String.valueOf(updatedBigDecimal.precision))
            .parameter("VALUE", updatedBigDecimal.toString())
            .parameter("PRECISION", String.valueOf(precision))
            .toString()
        )
      }
      updatedBigDecimal
    }

}
