package com.exasol.cloudetl.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.exasol.ExaIterator;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.errorreporting.ExaError;

/** Provides typed column values from an Exasol iterator. */
public final class ExasolColumnValueProvider {
    private final ExaIterator iterator;

    /** Create a provider. */
    public ExasolColumnValueProvider(final ExaIterator iterator) {
        this.iterator = iterator;
    }

    /** Factory for Java callers. */
    public static ExasolColumnValueProvider apply(final ExaIterator iterator) {
        return new ExasolColumnValueProvider(iterator);
    }

    /** Get the column value at the given index. */
    public Object getColumnValue(final int index, final ExaColumnInfo columnInfo) {
        try {
            final Class<?> columnType = columnInfo.type;
            if (Objects.equals(columnType, JavaClassTypes.J_INTEGER)) {
                return this.iterator.getInteger(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_LONG)) {
                return this.iterator.getLong(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_BIG_DECIMAL)) {
                return scaleBigDecimalAndValidatePrecision(this.iterator.getBigDecimal(index), columnInfo.precision,
                        columnInfo.scale);
            } else if (Objects.equals(columnType, JavaClassTypes.J_DOUBLE)) {
                return this.iterator.getDouble(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_STRING)) {
                return this.iterator.getString(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_BOOLEAN)) {
                return this.iterator.getBoolean(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_SQL_DATE)) {
                return this.iterator.getDate(index);
            } else if (Objects.equals(columnType, JavaClassTypes.J_SQL_TIMESTAMP)) {
                return this.iterator.getTimestamp(index);
            }
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-23")
                    .message("Cannot obtain Exasol value for column type {{TYPE}}.")
                    .parameter("TYPE", String.valueOf(columnType)).toString());
        } catch (final com.exasol.ExaIterationException | com.exasol.ExaDataTypeException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private BigDecimal scaleBigDecimalAndValidatePrecision(final BigDecimal bigDecimal, final int precision,
            final int scale) {
        if (bigDecimal == null) {
            return null;
        }
        final BigDecimal updatedBigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
        if (updatedBigDecimal.precision() > precision) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-24")
                    .message("Actual precision {{ACTUAL_PRECISION}} of big decimal {{VALUE}} value exceeds configured {{PRECISION}}.")
                    .parameter("ACTUAL_PRECISION", String.valueOf(updatedBigDecimal.precision()))
                    .parameter("VALUE", updatedBigDecimal.toString()).parameter("PRECISION", String.valueOf(precision))
                    .toString());
        }
        return updatedBigDecimal;
    }
}
