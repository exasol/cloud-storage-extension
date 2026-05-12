package com.exasol.cloudetl

package object data {
  val ExaColumnInfo: ExaColumnInfoFactory.type = ExaColumnInfoFactory
}

object ExaColumnInfoFactory {
  def apply(
    name: String,
    `type`: Class[_],
    precision: Int = 0,
    scale: Int = 0,
    length: Int = 0,
    isNullable: Boolean = true
  ): _root_.com.exasol.cloudetl.data.ExaColumnInfo =
    new _root_.com.exasol.cloudetl.data.ExaColumnInfo(name, `type`, precision, scale, length, isNullable)
}
