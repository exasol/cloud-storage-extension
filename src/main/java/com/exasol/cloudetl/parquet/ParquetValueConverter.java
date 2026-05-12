package com.exasol.cloudetl.parquet;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.parquet.schema.LogicalTypeAnnotation;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName;
import org.apache.parquet.schema.Type.Repetition;

import com.exasol.common.json.JsonMapper;
import com.exasol.parquetio.data.Row;

/** Converts Parquet rows to Exasol-emittable values. */
public final class ParquetValueConverter {
    private final MessageType schema;
    private final int size;
    private final Object[] convertedValues;

    /** Create a converter. */
    public ParquetValueConverter(final MessageType schema) {
        this.schema = schema;
        this.size = schema.getFields().size();
        this.convertedValues = new Object[this.size];
    }

    /** Factory for Java callers. */
    public static ParquetValueConverter apply(final MessageType schema) {
        return new ParquetValueConverter(schema);
    }

    /** Convert a row. */
    public Object[] convert(final Row row) {
        return convertParquetComplexValuesToJson(row.getValues());
    }

    private Object[] convertParquetComplexValuesToJson(final List<Object> values) {
        for (int i = 0; i < this.size; i++) {
            this.convertedValues[i] = convertValue(i, values.get(i));
        }
        return this.convertedValues;
    }

    private Object convertValue(final int index, final Object value) {
        final org.apache.parquet.schema.Type fieldType = this.schema.getType(index);
        if (fieldType.isPrimitive() && !fieldType.isRepetition(Repetition.REPEATED)) {
            return convertPrimitiveValue(fieldType.asPrimitiveType(), value);
        }
        return JsonMapper.toJson(value);
    }

    private Object convertPrimitiveValue(final PrimitiveType primitiveType, final Object value) {
        final LogicalTypeAnnotation logicalType = primitiveType.getLogicalTypeAnnotation();
        if (primitiveType.getPrimitiveTypeName() == PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY
                && Objects.equals(logicalType, LogicalTypeAnnotation.uuidType())) {
            return ((UUID) value).toString();
        }
        return value;
    }
}
