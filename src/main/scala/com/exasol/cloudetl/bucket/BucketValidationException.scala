package com.exasol.cloudetl.bucket

class BucketValidationException(val message: String, val cause: Throwable) extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
