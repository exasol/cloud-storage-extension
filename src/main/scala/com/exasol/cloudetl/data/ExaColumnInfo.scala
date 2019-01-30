package com.exasol.cloudetl.data

/** An Exasol table column information */
final case class ExaColumnInfo(
  name: String,
  `type`: Class[_],
  precision: Int,
  scale: Int,
  length: Int,
  isNullable: Boolean
)
