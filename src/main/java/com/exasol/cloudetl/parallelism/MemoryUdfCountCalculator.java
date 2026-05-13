package com.exasol.cloudetl.parallelism;

import com.exasol.ExaMetadata;

/** Uses a user-provided UDF memory value. */
public final class MemoryUdfCountCalculator extends AbstractUdfCountCalculator {
    /** Create a calculator. */
    public MemoryUdfCountCalculator(final ExaMetadata metadata, final long memory) {
        super(metadata, memory);
    }
}
