package com.exasol.cloudetl.util

import com.exasol.ExaIterator
import com.exasol.cloudetl.data.ExaColumnInfo

/**
 * A class that return column value given index and type from Exasol iterator.
 */
final case class ExasolColumnValueProvider(iterator: ExaIterator) extends JavaClassTypes {

  /**
   * Returns a value from Exasol [[ExaIterator]] iterator on given index
   * which have [[com.exasol.cloudetl.data.ExaColumnInfo]] column type.
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
      case `jBigDecimal`   => iterator.getBigDecimal(index)
      case `jDouble`       => iterator.getDouble(index)
      case `jString`       => iterator.getString(index)
      case `jBoolean`      => iterator.getBoolean(index)
      case `jSqlDate`      => iterator.getDate(index)
      case `jSqlTimestamp` => iterator.getTimestamp(index)
      case _ =>
        throw new IllegalArgumentException(s"Cannot get Exasol value for column type '$columnType'.")
    }
  }

}
