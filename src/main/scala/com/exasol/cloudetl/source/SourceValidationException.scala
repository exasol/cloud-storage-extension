package com.exasol.cloudetl.source

class SourceValidationException(val message: String, val cause: Throwable) extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
