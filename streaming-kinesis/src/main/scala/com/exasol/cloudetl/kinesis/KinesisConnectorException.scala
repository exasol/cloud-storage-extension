package com.exasol.cloudetl.kinesis

class KinesisConnectorException(val message: String, val cause: Throwable)
    extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
