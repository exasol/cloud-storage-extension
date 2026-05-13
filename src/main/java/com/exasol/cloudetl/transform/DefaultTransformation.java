package com.exasol.cloudetl.transform;

import static com.exasol.cloudetl.constants.Constants.TRUNCATE_STRING;

import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.errorreporting.ExaError;

/**
 * Base default transformation class.
 */
public final class DefaultTransformation implements Transformation {
    private static final int MAX_VARCHAR_SIZE = 2_000_000;

    private final boolean hasTruncateString;

    /**
     * Create a new transformation.
     *
     * @param properties storage properties
     */
    public DefaultTransformation(final StorageProperties properties) {
        this.hasTruncateString = properties.isEnabled(TRUNCATE_STRING);
    }

    @Override
    public Object[] transform(final Object[] values) {
        for (int index = 0; index < values.length; index++) {
            final Object value = values[index];
            if (value instanceof String) {
                values[index] = transformString((String) value);
            }
        }
        return values;
    }

    private String transformString(final String value) {
        if (value.length() <= MAX_VARCHAR_SIZE) {
            return value;
        }
        if (!this.hasTruncateString) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-CSE-29")
                            .message(
                                    "Length of a string value exceeds Exasol maximum allowed '2000000' VARCHAR length.")
                            .mitigation(
                                    "Please make sure that the string is shorter or equal to maximum allowed length")
                            .mitigation(
                                    "Please set 'TRUNCATE_STRING' parameter to 'true' to enable truncating longer strings.")
                            .toString());
        }
        return value.substring(0, MAX_VARCHAR_SIZE);
    }
}
