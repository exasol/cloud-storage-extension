package com.exasol.cloudetl.helper

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

/**
 * Helper functions to convert {@code UUID} values.
 */
object UUIDConverter {

  /**
   * Converts UUID value to ByteBuffer.
   *
   * @param uuid a UUID value
   * @return a ByteBuffer value
   */
  def toByteArray(uuid: UUID): Array[Byte] =
    ByteBuffer
      .allocate(16)
      .order(ByteOrder.BIG_ENDIAN)
      .putLong(uuid.getMostSignificantBits())
      .putLong(uuid.getLeastSignificantBits())
      .array()

}
