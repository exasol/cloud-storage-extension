package com.exasol.cloudetl.helper

import java.math.BigDecimal
import java.math.RoundingMode

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo

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
        throw new IllegalArgumentException(s"Cannot get Exasol value for column type '$columnType'.")
    }
  }

  private[this] def updateBigDecimal(bigDecimal: BigDecimal, precision: Int, scale: Int): BigDecimal =
    if (bigDecimal == null) {
      bigDecimal
    } else {
      val updatedBigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP)
      if (updatedBigDecimal.precision > precision) {
        throw new IllegalArgumentException(
          s"Actual precision of big decimal value exceeds configured '$precision'."
        )
      }
      updatedBigDecimal
    }

}
