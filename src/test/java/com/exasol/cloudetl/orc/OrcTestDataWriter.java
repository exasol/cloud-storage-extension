package com.exasol.cloudetl.orc;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.TypeDescription.Category;
import org.apache.orc.Writer;

import com.exasol.errorreporting.ExaError;

public class OrcTestDataWriter {
    private static final long ORC_STRIPE_SIZE = 32L * 1024L * 1024L;
    private static final long ORC_BLOCK_SIZE = 64L * 1024L * 1024L;

    protected final Writer getOrcWriter(final Path path, final TypeDescription schema) throws IOException {
        final Configuration conf = new Configuration();
        conf.set("orc.stripe.size", Long.toString(ORC_STRIPE_SIZE));
        conf.set("orc.block.size", Long.toString(ORC_BLOCK_SIZE));
        return OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(schema).useUTCTimestamp(true));
    }

    protected final <T> void writeDataValues(final List<T> values, final Path path, final TypeDescription schema)
            throws IOException {
        try (Writer writer = getOrcWriter(path, schema)) {
            final List<TypeDescription> schemaChildren = schema.getChildren();
            final VectorizedRowBatch batch = schema.createRowBatch();
            final ColumnSetter[] columnWriters = new ColumnSetter[schemaChildren.size()];
            for (int index = 0; index < schemaChildren.size(); index++) {
                columnWriters[index] = getColumnSetter(schemaChildren.get(index), batch.cols[index]);
            }
            batch.size = 0;
            for (final T value : values) {
                for (final ColumnSetter columnWriter : columnWriters) {
                    columnWriter.set(value, batch.size);
                }
                batch.size++;
            }
            writer.addRowBatch(batch);
        }
    }

    private ColumnSetter getColumnSetter(final TypeDescription orcType, final ColumnVector column) {
        switch (orcType.getCategory()) {
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case DATE:
            return longWriter((LongColumnVector) column);
        case FLOAT:
        case DOUBLE:
            return doubleWriter((DoubleColumnVector) column);
        case DECIMAL:
            return decimalWriter((DecimalColumnVector) column);
        case CHAR:
        case VARCHAR:
        case BINARY:
        case STRING:
            return stringWriter((BytesColumnVector) column);
        case TIMESTAMP:
            return timestampWriter((TimestampColumnVector) column);
        case LIST:
            return listWriter((ListColumnVector) column, orcType);
        case MAP:
            return mapWriter((MapColumnVector) column, orcType);
        case STRUCT:
            return structWriter((StructColumnVector) column, orcType);
        default:
            throw new UnsupportedOperationException(ExaError.messageBuilder("E-CSE-15")
                    .message("Unknown Orc type {{ORC_TYPE}} for writer.")
                    .parameter("ORC_TYPE", String.valueOf(orcType)).toString());
        }
    }

    private ColumnSetter longWriter(final LongColumnVector column) {
        return (value, index) -> {
            if (value == null) {
                setNull(column, index);
            } else if (value instanceof Boolean) {
                column.vector[index] = ((Boolean) value).booleanValue() ? 1L : 0L;
            } else if (value instanceof Byte) {
                column.vector[index] = ((Byte) value).longValue();
            } else if (value instanceof Short) {
                column.vector[index] = ((Short) value).longValue();
            } else if (value instanceof Integer) {
                column.vector[index] = ((Integer) value).longValue();
            } else if (value instanceof Long) {
                column.vector[index] = ((Long) value).longValue();
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter doubleWriter(final DoubleColumnVector column) {
        return (value, index) -> {
            if (value instanceof Double) {
                column.vector[index] = ((Double) value).doubleValue();
            } else if (value instanceof Float) {
                column.vector[index] = ((Float) value).doubleValue();
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter decimalWriter(final DecimalColumnVector column) {
        return (value, index) -> {
            if (value instanceof String) {
                column.set(index, HiveDecimal.create((String) value));
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter stringWriter(final BytesColumnVector column) {
        return (value, index) -> {
            if (value instanceof String) {
                column.setVal(index, ((String) value).getBytes(UTF_8));
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter timestampWriter(final TimestampColumnVector column) {
        return (value, index) -> {
            if (value instanceof Timestamp) {
                column.set(index, (Timestamp) value);
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter listWriter(final ListColumnVector column, final TypeDescription orcType) {
        final ColumnSetter innerSetter = getColumnSetter(orcType.getChildren().get(0), column.child);
        return (value, index) -> {
            if (value instanceof Iterable<?>) {
                final List<?> values = toList((Iterable<?>) value);
                if (!values.isEmpty()) {
                    final int length = values.size();
                    column.offsets[index] = column.childCount;
                    column.lengths[index] = length;
                    column.childCount += length;
                    column.child.ensureSize(column.childCount, column.offsets[index] != 0);
                    for (int offset = 0; offset < length; offset++) {
                        innerSetter.set(values.get(offset), (int) column.offsets[index] + offset);
                    }
                    return;
                }
            }
            setNull(column, index);
        };
    }

    private static List<?> toList(final Iterable<?> iterable) {
        if (iterable instanceof List<?>) {
            return (List<?>) iterable;
        }
        final java.util.ArrayList<Object> list = new java.util.ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    private ColumnSetter mapWriter(final MapColumnVector column, final TypeDescription orcType) {
        final ColumnSetter keySetter = getColumnSetter(orcType.getChildren().get(0), column.keys);
        final ColumnSetter valueSetter = getColumnSetter(orcType.getChildren().get(1), column.values);
        return (value, index) -> {
            if (value instanceof Map<?, ?> && !((Map<?, ?>) value).isEmpty()) {
                final Map<?, ?> map = (Map<?, ?>) value;
                final int length = map.size();
                column.offsets[index] = column.childCount;
                column.lengths[index] = length;
                column.childCount += length;
                column.keys.ensureSize(column.childCount, column.offsets[index] != 0);
                column.values.ensureSize(column.childCount, column.offsets[index] != 0);
                int offset = 0;
                for (final Map.Entry<?, ?> entry : map.entrySet()) {
                    keySetter.set(entry.getKey(), (int) column.offsets[index] + offset);
                    valueSetter.set(entry.getValue(), (int) column.offsets[index] + offset);
                    offset++;
                }
            } else {
                setNull(column, index);
            }
        };
    }

    private ColumnSetter structWriter(final StructColumnVector column, final TypeDescription orcType) {
        final List<TypeDescription> columns = orcType.getChildren();
        final List<String> fieldNames = orcType.getFieldNames();
        final java.util.Map<String, ColumnSetter> fieldSetters = new java.util.LinkedHashMap<>();
        for (int index = 0; index < columns.size(); index++) {
            fieldSetters.put(fieldNames.get(index), getColumnSetter(columns.get(index), column.fields[index]));
        }
        return (value, index) -> {
            if (value instanceof Map<?, ?>) {
                final Map<?, ?> map = (Map<?, ?>) value;
                for (final Map.Entry<String, ColumnSetter> setter : fieldSetters.entrySet()) {
                    setter.getValue().set(map.get(setter.getKey()), index);
                }
            } else {
                setNull(column, index);
            }
        };
    }

    private void setNull(final ColumnVector column, final int index) {
        if (column instanceof ListColumnVector) {
            ((ListColumnVector) column).lengths[index] = 0;
        }
        if (column instanceof MapColumnVector) {
            ((MapColumnVector) column).lengths[index] = 0;
        }
        column.noNulls = false;
        column.isNull[index] = true;
    }

    @FunctionalInterface
    private interface ColumnSetter {
        void set(Object value, int index);
    }
}
