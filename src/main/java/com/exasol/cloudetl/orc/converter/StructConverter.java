package com.exasol.cloudetl.orc.converter;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.orc.TypeDescription;

/** Converter for ORC STRUCT values. */
public final class StructConverter extends AbstractStructLikeConverter implements OrcConverter<StructColumnVector> {
    private final List<String> fieldNames;

    /** Create a converter. */
    public StructConverter(final TypeDescription schema) {
        super(schema);
        this.fieldNames = schema.getFieldNames();
    }

    @Override
    String getFieldName(final int fieldIndex) {
        return this.fieldNames.get(fieldIndex);
    }

    @Override
    public Map<String, Object> readAt(final StructColumnVector struct, final int rowIndex) {
        return readFields(struct.fields, rowIndex);
    }
}
