package com.exasol.cloudetl.sink;

/** Data writer. */
public interface Writer<T> extends AutoCloseable {
    /** Write a value. */
    void write(T value);

    @Override
    default void close() {
        // Default no-op.
    }
}
