package com.exasol.cloudetl.orc.converter;

import java.util.Map;

import org.apache.hadoop.hive.ql.exec.vector.UnionColumnVector;
import org.apache.orc.TypeDescription;

final class UnionConverter extends AbstractStructLikeConverter implements OrcConverter<UnionColumnVector> {
    UnionConverter(final TypeDescription schema) {
        super(schema);
    }

    @Override
    String getFieldName(final int fieldIndex) {
        return this.fields.get(fieldIndex).getCategory().name();
    }

    @Override
    public Map<String, Object> readAt(final UnionColumnVector union, final int rowIndex) {
        return readFields(union.fields, rowIndex);
    }
}
