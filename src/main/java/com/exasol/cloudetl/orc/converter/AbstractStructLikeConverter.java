package com.exasol.cloudetl.orc.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.orc.TypeDescription;

abstract class AbstractStructLikeConverter {
    protected final List<TypeDescription> fields;
    protected final int size;

    AbstractStructLikeConverter(final TypeDescription schema) {
        this.fields = schema.getChildren();
        this.size = this.fields.size();
    }

    abstract String getFieldName(int fieldIndex);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    final Object readFromFieldColumn(final ColumnVector fieldVector, final int rowIndex, final int fieldIndex) {
        final OrcConverter converter = OrcConverterFactory.create(this.fields.get(fieldIndex));
        final int newRowIndex = fieldVector.isRepeating ? 0 : rowIndex;
        return converter.readAt(fieldVector, newRowIndex);
    }

    final Map<String, Object> readFields(final ColumnVector[] fieldVectors, final int rowIndex) {
        final Map<String, Object> values = new LinkedHashMap<>();
        for (int fieldIndex = 0; fieldIndex < this.size; fieldIndex++) {
            values.put(getFieldName(fieldIndex), readFromFieldColumn(fieldVectors[fieldIndex], rowIndex, fieldIndex));
        }
        return values;
    }
}
