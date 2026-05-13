package com.exasol.cloudetl.parallelism;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Calculates parallel UDF instance counts. */
public interface UdfCountCalculator {
    /** Logger. */
    Logger LOGGER = LoggerFactory.getLogger(UdfCountCalculator.class);

    /** @return total number of nodes */
    int getNumberOfNodes();

    /** @return cores per node limited by memory */
    int getNumberOfCoresLimitedByMemoryPerNode();

    /** Calculate possible UDF instances. */
    default int getUdfCount(final int numberOfCoresPerNode) {
        final int totalNodesInCluster = getNumberOfNodes();
        final int numberOfCoresLimitedByMemory = getNumberOfCoresLimitedByMemoryPerNode();
        final int autoParallelism = totalNodesInCluster * Math.min(numberOfCoresLimitedByMemory, numberOfCoresPerNode);
        LOGGER.info("Calculated maximum UDF parallelism as, {} total nodes * min({} cores per node, {}"
                + " cores limited by memory) = {}.", totalNodesInCluster, numberOfCoresPerNode,
                numberOfCoresLimitedByMemory, autoParallelism);
        return autoParallelism;
    }
}
