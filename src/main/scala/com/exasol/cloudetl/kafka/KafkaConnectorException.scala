package com.exasol.cloudetl.kafka

class KafkaConnectorException(val message: String, val cause: Throwable)
    extends RuntimeException(message, cause) {
  def this(message: String) = this(message, null)
}
