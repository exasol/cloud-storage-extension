package com.exasol.cloudetl.emitter;

import com.exasol.ExaIterator;

/** Emits data into an Exasol iterator. */
public interface Emitter {
    /** Emit data. */
    void emit(ExaIterator context);
}
