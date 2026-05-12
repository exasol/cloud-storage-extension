package com.exasol.cloudetl.bucket;

/**
 * Exception for bucket validation failures.
 */
public class BucketValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with a message.
     *
     * @param message error message
     */
    public BucketValidationException(final String message) {
        super(message);
    }

    /**
     * Create a new exception with a message and cause.
     *
     * @param message error message
     * @param cause root cause
     */
    public BucketValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
