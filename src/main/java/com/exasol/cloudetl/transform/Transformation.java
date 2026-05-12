package com.exasol.cloudetl.transform;

/**
 * An interface for implementing data transformations before importing into a
 * database or exporting to storage.
 */
public interface Transformation {
    /**
     * Applies transformations to the list of objects.
     *
     * @param values row of objects
     * @return transformed list of objects
     */
    Object[] transform(Object[] values);
}
