package com.exasol.cloudetl.orc.converter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DecimalColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DoubleColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ListColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.MapColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;

import com.exasol.cloudetl.helper.DateTimeConverter;

final class OrcConverters {
    private OrcConverters() {
        // Utility holder.
    }

    static final OrcConverter<LongColumnVector> BOOLEAN = (vector, index) -> vector.isNull[index] ? null
            : vector.vector[index] == 1L;
    static final OrcConverter<LongColumnVector> BYTE = (vector, index) -> vector.isNull[index] ? null
            : Byte.valueOf((byte) vector.vector[index]);
    static final OrcConverter<LongColumnVector> SHORT = (vector, index) -> vector.isNull[index] ? null
            : Short.valueOf((short) vector.vector[index]);
    static final OrcConverter<LongColumnVector> INT = (vector, index) -> vector.isNull[index] ? null
            : Integer.valueOf((int) vector.vector[index]);
    static final OrcConverter<LongColumnVector> LONG = (vector, index) -> vector.isNull[index] ? null
            : Long.valueOf(vector.vector[index]);
    static final OrcConverter<DoubleColumnVector> DOUBLE = (vector, index) -> vector.isNull[index] ? null
            : Double.valueOf(vector.vector[index]);
    static final OrcConverter<DoubleColumnVector> FLOAT = (vector, index) -> vector.isNull[index] ? null
            : Float.valueOf((float) vector.vector[index]);
    static final OrcConverter<LongColumnVector> DATE = (vector, index) -> vector.isNull[index] ? null
            : DateTimeConverter.daysToDate(vector.vector[index]);
    static final OrcConverter<TimestampColumnVector> TIMESTAMP = (vector, index) -> {
        if (vector.isNull[index]) {
            return null;
        }
        final java.sql.Timestamp timestamp = new java.sql.Timestamp(vector.getTime(index));
        timestamp.setNanos(vector.getNanos(index));
        return timestamp;
    };
    static final OrcConverter<DecimalColumnVector> DECIMAL = (vector, index) -> vector.isNull[index] ? null
            : vector.vector[index].getHiveDecimal().bigDecimalValue();
    static final OrcConverter<BytesColumnVector> STRING = (vector, index) -> vector.isNull[index] ? null
            : vector.toString(index);
    static final OrcConverter<BytesColumnVector> BINARY = (vector, index) -> {
        if (vector.isNull[index]) {
            return null;
        }
        final int startOffset = vector.start[index];
        final int length = vector.length[index];
        return new String(Arrays.copyOfRange(vector.vector[index], startOffset, startOffset + length),
                StandardCharsets.UTF_8);
    };

    static final class ListConverter<T extends ColumnVector> implements OrcConverter<ListColumnVector> {
        private final OrcConverter<T> elementConverter;

        ListConverter(final OrcConverter<T> elementConverter) {
            this.elementConverter = elementConverter;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object readAt(final ListColumnVector list, final int rowIndex) {
            final int offset = (int) list.offsets[rowIndex];
            final int length = (int) list.lengths[rowIndex];
            final java.util.List<Object> values = new java.util.ArrayList<>(length);
            for (int pointer = offset; pointer < offset + length; pointer++) {
                if (!list.noNulls && list.isNull[pointer]) {
                    values.add(null);
                } else if (!list.noNulls && list.isRepeating && list.isNull[0]) {
                    values.add(null);
                } else {
                    values.add(this.elementConverter.readAt((T) list.child, pointer));
                }
            }
            return values;
        }
    }

    static final class MapConverter<T extends ColumnVector, U extends ColumnVector> implements OrcConverter<MapColumnVector> {
        private final OrcConverter<T> keyConverter;
        private final OrcConverter<U> valueConverter;

        MapConverter(final OrcConverter<T> keyConverter, final OrcConverter<U> valueConverter) {
            this.keyConverter = keyConverter;
            this.valueConverter = valueConverter;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<Object, Object> readAt(final MapColumnVector vector, final int rowIndex) {
            final int offset = (int) vector.offsets[rowIndex];
            final int length = (int) vector.lengths[rowIndex];
            final Map<Object, Object> values = new HashMap<>();
            for (int pointer = offset; pointer < offset + length; pointer++) {
                values.put(this.keyConverter.readAt((T) vector.keys, pointer),
                        this.valueConverter.readAt((U) vector.values, pointer));
            }
            return values;
        }
    }
}
