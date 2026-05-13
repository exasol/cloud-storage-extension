package com.exasol.cloudetl.orc.converter;

import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;

/** Converter for ORC column vectors. */
public interface OrcConverter<T extends ColumnVector> {
    /** Read value at row index. */
    Object readAt(T vector, int index);
}
