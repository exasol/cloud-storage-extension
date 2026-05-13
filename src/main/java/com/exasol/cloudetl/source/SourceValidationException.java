package com.exasol.cloudetl.source;

/**
 * Exception for source validation failures.
 */
public class SourceValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with a message.
     *
     * @param message error message
     */
    public SourceValidationException(final String message) {
        super(message);
    }

    /**
     * Create a new exception with a message and cause.
     *
     * @param message error message
     * @param cause root cause
     */
    public SourceValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
