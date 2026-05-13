package com.exasol.cloudetl.parallelism;

import com.exasol.ExaMetadata;

/** Uses the default fixed UDF memory value. */
public final class FixedUdfCountCalculator extends AbstractUdfCountCalculator {
    /** Create a calculator. */
    public FixedUdfCountCalculator(final ExaMetadata metadata) {
        super(metadata, 500L * 1_000_000L);
    }
}
