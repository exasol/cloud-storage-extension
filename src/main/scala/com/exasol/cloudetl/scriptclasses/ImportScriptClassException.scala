package com.exasol.cloudetl.scriptclasses

class ImportScriptClassException(val message: String, val cause: Throwable)
    extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
