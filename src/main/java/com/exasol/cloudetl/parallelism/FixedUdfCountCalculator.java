package com.exasol.cloudetl.parallelism;

import java.util.Objects;

import com.exasol.ExaMetadata;

/** Uses the default fixed UDF memory value. */
public final class FixedUdfCountCalculator extends AbstractUdfCountCalculator {
    private final ExaMetadata metadata;

    /** Create a calculator. */
    public FixedUdfCountCalculator(final ExaMetadata metadata) {
        super(metadata, 500L * 1_000_000L);
        this.metadata = metadata;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof FixedUdfCountCalculator
                && Objects.equals(this.metadata, ((FixedUdfCountCalculator) obj).metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.metadata);
    }
}
