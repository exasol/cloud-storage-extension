package com.exasol.cloudetl.parquet;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.LogicalTypeAnnotation.DecimalLogicalTypeAnnotation;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName;

import com.exasol.cloudetl.helper.DateTimeConverter;
import com.exasol.cloudetl.helper.ParquetSchemaConverter;
import com.exasol.common.data.Row;
import com.exasol.errorreporting.ExaError;

/** Parquet WriteSupport for common Row values. */
@SuppressWarnings("deprecation")
public class RowWriteSupport extends WriteSupport<Row> {
    private static final int TIMESTAMP_MAX_BYTE_SIZE = 12;

    private final MessageType schema;
    private RowValueWriter[] rootFieldWriters;
    private RecordConsumer recordConsumer;
    private final byte[] timestampBuffer = new byte[TIMESTAMP_MAX_BYTE_SIZE];
    private final byte[] decimalBuffer = new byte[ParquetSchemaConverter.PRECISION_TO_BYTE_SIZE
            .get(ParquetSchemaConverter.DECIMAL_MAX_PRECISION - 1)];

    /** Create write support. */
    public RowWriteSupport(final MessageType schema) {
        this.schema = schema;
    }

    @Override
    public WriteContext init(final Configuration configuration) {
        this.rootFieldWriters = new RowValueWriter[this.schema.getFieldCount()];
        for (int index = 0; index < this.schema.getFieldCount(); index++) {
            this.rootFieldWriters[index] = makeWriter(this.schema.getType(index).asPrimitiveType());
        }
        return new WriteContext(this.schema, new java.util.HashMap<>());
    }

    @Override
    public void prepareForWrite(final RecordConsumer record) {
        this.recordConsumer = record;
    }

    @Override
    public void write(final Row row) {
        this.recordConsumer.startMessage();
        writeFields(row, this.schema, this.rootFieldWriters);
        this.recordConsumer.endMessage();
    }

    @Override
    public FinalizedWriteContext finalizeWrite() {
        return new FinalizedWriteContext(new java.util.HashMap<>());
    }

    private void writeFields(final Row row, final MessageType schema, final RowValueWriter[] writers) {
        for (int index = 0; index < schema.getFieldCount(); index++) {
            final org.apache.parquet.schema.Type fieldType = schema.getType(index);
            if (!row.isNullAt(index)) {
                this.recordConsumer.startField(fieldType.getName(), index);
                writers[index].write(row, index);
                this.recordConsumer.endField(fieldType.getName(), index);
            }
        }
    }

    private RowValueWriter makeWriter(final PrimitiveType primitiveType) {
        final PrimitiveTypeName typeName = primitiveType.getPrimitiveTypeName();
        final OriginalType originalType = primitiveType.getOriginalType();
        switch (typeName) {
        case BOOLEAN:
            return (row, index) -> this.recordConsumer.addBoolean((Boolean) row.get(index));
        case INT32:
            if (originalType == OriginalType.DATE) {
                return makeDateWriter();
            }
            return (row, index) -> this.recordConsumer.addInteger((Integer) row.get(index));
        case INT64:
            return (row, index) -> this.recordConsumer.addLong((Long) row.get(index));
        case FLOAT:
            return (row, index) -> this.recordConsumer.addFloat(((Double) row.get(index)).floatValue());
        case DOUBLE:
            return (row, index) -> this.recordConsumer.addDouble((Double) row.get(index));
        case BINARY:
            return (row, index) -> this.recordConsumer
                    .addBinary(Binary.fromReusedByteArray(((String) row.get(index)).getBytes(UTF_8)));
        case INT96:
            return makeTimestampWriter();
        case FIXED_LEN_BYTE_ARRAY:
            if (originalType == OriginalType.DECIMAL) {
                final DecimalLogicalTypeAnnotation decimal = (DecimalLogicalTypeAnnotation) primitiveType
                        .getLogicalTypeAnnotation();
                return makeDecimalWriter(decimal.getPrecision());
            }
            break;
        default:
            break;
        }
        throw new UnsupportedOperationException(ExaError.messageBuilder("E-CSE-18")
                .message("Parquet type {{PARQUET_TYPE}} is not supported.").parameter("PARQUET_TYPE", typeName.toString())
                .toString());
    }

    private RowValueWriter makeDateWriter() {
        return (row, index) -> {
            final java.sql.Date date = (java.sql.Date) row.get(index);
            this.recordConsumer.addInteger((int) DateTimeConverter.daysSinceEpoch(date));
        };
    }

    private RowValueWriter makeTimestampWriter() {
        return (row, index) -> {
            final java.sql.Timestamp timestamp = (java.sql.Timestamp) row.get(index);
            final long micros = DateTimeConverter.getMicrosFromTimestamp(timestamp);
            final scala.Tuple2<Integer, Long> daysAndNanos = DateTimeConverter.getJulianDayAndNanos(micros);
            ByteBuffer.wrap(this.timestampBuffer).order(ByteOrder.LITTLE_ENDIAN).putLong(daysAndNanos._2())
                    .putInt(daysAndNanos._1());
            this.recordConsumer.addBinary(Binary.fromReusedByteArray(this.timestampBuffer));
        };
    }

    private RowValueWriter makeDecimalWriter(final int precision) {
        require(precision >= 1, "Decimal precision " + precision + " should not be less than minimum precision 1");
        require(precision <= ParquetSchemaConverter.DECIMAL_MAX_PRECISION,
                "Decimal precision " + precision + " should not exceed max precision "
                        + ParquetSchemaConverter.DECIMAL_MAX_PRECISION);
        final int numBytes = ParquetSchemaConverter.PRECISION_TO_BYTE_SIZE.get(precision - 1);
        return (row, index) -> {
            final java.math.BigDecimal decimal = (java.math.BigDecimal) row.get(index);
            final java.math.BigInteger unscaled = decimal.unscaledValue();
            final byte[] bytes = unscaled.toByteArray();
            final byte[] fixedLenBytesArray;
            if (bytes.length == numBytes) {
                fixedLenBytesArray = bytes;
            } else if (bytes.length < numBytes) {
                final byte signByte = (byte) (unscaled.signum() < 0 ? -1 : 0);
                Arrays.fill(this.decimalBuffer, 0, numBytes - bytes.length, signByte);
                System.arraycopy(bytes, 0, this.decimalBuffer, numBytes - bytes.length, bytes.length);
                fixedLenBytesArray = this.decimalBuffer;
            } else {
                throw new IllegalStateException(ExaError.messageBuilder("E-CSE-9")
                        .message("The precision {{PRECISION}} is too small for decimal value.")
                        .parameter("PRECISION", String.valueOf(precision))
                        .mitigation("The precision should be at least as {{LEN}}.", String.valueOf(bytes.length)).toString());
            }
            this.recordConsumer.addBinary(Binary.fromReusedByteArray(fixedLenBytesArray, 0, numBytes));
        };
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException("requirement failed: " + message);
        }
    }

    private interface RowValueWriter {
        void write(Row row, int index);
    }
}
