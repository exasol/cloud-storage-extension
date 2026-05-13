package com.exasol.cloudetl.scriptclasses;

/**
 * Exception for table export failures.
 */
public class TableExporterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with a message.
     *
     * @param message error message
     */
    public TableExporterException(final String message) {
        super(message);
    }

    /**
     * Create a new exception with a message and cause.
     *
     * @param message error message
     * @param cause root cause
     */
    public TableExporterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
