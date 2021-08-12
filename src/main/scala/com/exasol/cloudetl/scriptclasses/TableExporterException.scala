package com.exasol.cloudetl.scriptclasses

class TableExporterException(val message: String, val cause: Throwable) extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
