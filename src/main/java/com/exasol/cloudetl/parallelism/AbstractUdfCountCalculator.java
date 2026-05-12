package com.exasol.cloudetl.parallelism;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaMetadata;

/** Common implementation for UDF count calculators. */
public abstract class AbstractUdfCountCalculator implements UdfCountCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUdfCountCalculator.class);

    private final ExaMetadata metadata;
    private final long memory;

    /** Create a calculator. */
    protected AbstractUdfCountCalculator(final ExaMetadata metadata, final long memory) {
        this.metadata = metadata;
        this.memory = memory;
    }

    @Override
    public int getNumberOfNodes() {
        return (int) this.metadata.getNodeCount();
    }

    @Override
    public int getNumberOfCoresLimitedByMemoryPerNode() {
        final BigInteger memoryLimit = this.metadata.getMemoryLimit();
        final int coresPerNode = memoryLimit.divide(BigInteger.valueOf(this.memory)).intValue();
        LOGGER.info("Calculated cores limited by memory ({} MB memory per node / {} MB UDF memory = {}).", memoryLimit,
                this.memory, coresPerNode);
        return coresPerNode;
    }
}
