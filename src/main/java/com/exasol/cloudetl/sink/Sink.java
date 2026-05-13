package com.exasol.cloudetl.sink;

import com.exasol.cloudetl.bucket.Bucket;

/** Abstract data sink. */
public abstract class Sink<T> implements AutoCloseable {
    /** @return target bucket */
    public abstract Bucket bucket();

    /** Create a writer for a path. */
    public abstract Writer<T> createWriter(String path);

    /** Write a value. */
    public abstract void write(T value);

    @Override
    public abstract void close();
}
