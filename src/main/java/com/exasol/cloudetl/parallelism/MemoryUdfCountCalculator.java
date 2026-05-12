package com.exasol.cloudetl.parallelism;

import java.util.Objects;

import com.exasol.ExaMetadata;

/** Uses a user-provided UDF memory value. */
public final class MemoryUdfCountCalculator extends AbstractUdfCountCalculator {
    private final ExaMetadata metadata;
    private final long memory;

    /** Create a calculator. */
    public MemoryUdfCountCalculator(final ExaMetadata metadata, final long memory) {
        super(metadata, memory);
        this.metadata = metadata;
        this.memory = memory;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MemoryUdfCountCalculator)) {
            return false;
        }
        final MemoryUdfCountCalculator other = (MemoryUdfCountCalculator) obj;
        return this.memory == other.memory && Objects.equals(this.metadata, other.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.metadata, this.memory);
    }
}
