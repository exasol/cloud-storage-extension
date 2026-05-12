package com.exasol.cloudetl.helper;

import com.exasol.ExaMetadata;
import com.exasol.cloudetl.parallelism.FixedUdfCountCalculator;
import com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator;
import com.exasol.cloudetl.storage.StorageProperties;

/** Generates parallelism strings for exports. */
public final class ExportParallelismCalculator {
    private final ExaMetadata metadata;
    private final StorageProperties properties;

    /** Create a calculator. */
    public ExportParallelismCalculator(final ExaMetadata metadata, final StorageProperties properties) {
        this.metadata = metadata;
        this.properties = properties;
    }

    /** Factory for Java callers. */
    public static ExportParallelismCalculator apply(final ExaMetadata metadata, final StorageProperties properties) {
        return new ExportParallelismCalculator(metadata, properties);
    }

    /** @return export parallelism SQL fragment */
    public String getParallelism() {
        if (this.properties.getParallelism().isDefined()) {
            return this.properties.getParallelism().get();
        }
        final int multiplier = getCoresPerNode();
        return String.format("iproc(), mod(rownum,%d)", multiplier);
    }

    private int getCoresPerNode() {
        final int coresPerNode = Runtime.getRuntime().availableProcessors();
        return Math.min(coresPerNode, getCoresLimitedByMemory());
    }

    private int getCoresLimitedByMemory() {
        if (this.properties.hasUdfMemory()) {
            return new MemoryUdfCountCalculator(this.metadata, this.properties.getUdfMemory())
                    .getNumberOfCoresLimitedByMemoryPerNode();
        }
        return new FixedUdfCountCalculator(this.metadata).getNumberOfCoresLimitedByMemoryPerNode();
    }
}
