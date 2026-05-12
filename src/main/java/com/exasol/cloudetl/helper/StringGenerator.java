package com.exasol.cloudetl.helper;

import java.util.Random;

/**
 * Helper class that generates string values.
 */
public final class StringGenerator {
    private static final int FIRST_PRINTABLE_ASCII = 32;
    private static final int PRINTABLE_ASCII_RANGE = 95;
    private static final Random RNG = new Random();

    private StringGenerator() {
        // Prevent instantiation.
    }

    /**
     * Generates a random string with a given length.
     *
     * @param length length of the final string
     * @return generated string
     */
    public static String getRandomString(final int length) {
        final StringBuilder builder = new StringBuilder(length);
        int index = 0;
        while (index < length) {
            builder.append(nextPrintableChar());
            index++;
        }
        return builder.toString();
    }

    private static char nextPrintableChar() {
        return (char) (FIRST_PRINTABLE_ASCII + RNG.nextInt(PRINTABLE_ASCII_RANGE));
    }
}
