package com.exasol.cloudetl.helper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Helper functions to convert {@code UUID} values.
 */
public final class UUIDConverter {
    private static final int UUID_BYTE_LENGTH = 16;

    private UUIDConverter() {
        // Prevent instantiation.
    }

    /**
     * Converts UUID value to byte array.
     *
     * @param uuid UUID value
     * @return byte array value
     */
    public static byte[] toByteArray(final UUID uuid) {
        return ByteBuffer.allocate(UUID_BYTE_LENGTH)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }
}
