package com.exasol.cloudetl.helper;

import static java.util.Locale.ENGLISH;
import static org.apache.parquet.schema.LogicalTypeAnnotation.*;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;

import java.util.*;

import org.apache.parquet.schema.*;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.errorreporting.ExaError;

/** Converts Exasol column metadata to a Parquet schema. */
public final class ParquetSchemaConverter {
    /** Decimal maximum precision. */
    public static final int DECIMAL_MAX_PRECISION = 38;
    /** Maximum decimal precision for int32. */
    public static final int DECIMAL_MAX_INT_DIGITS = 9;
    /** Maximum decimal precision for int64. */
    public static final int DECIMAL_MAX_LONG_DIGITS = 18;
    /** Precision-to-byte-size mapping. */
    public static final List<Integer> PRECISION_TO_BYTE_SIZE = createPrecisionToByteSize();

    private final boolean lowercaseSchemaEnabled;

    /** Create a converter. */
    public ParquetSchemaConverter(final boolean lowercaseSchemaEnabled) {
        this.lowercaseSchemaEnabled = lowercaseSchemaEnabled;
    }

    /** Factory for Java callers. */
    public static ParquetSchemaConverter apply(final boolean lowercaseSchemaEnabled) {
        return new ParquetSchemaConverter(lowercaseSchemaEnabled);
    }

    /** Create a Parquet message type. */
    public MessageType createParquetMessageType(final scala.collection.immutable.Seq<ExaColumnInfo> columns,
            final String schemaName) {
        return createParquetMessageType(ScalaConverters.asJavaList(columns), schemaName);
    }

    /** Create a Parquet message type. */
    public MessageType createParquetMessageType(final List<ExaColumnInfo> columns, final String schemaName) {
        final List<Type> types = new ArrayList<>(columns.size());
        for (final ExaColumnInfo column : columns) {
            types.add(columnToParquetField(column));
        }
        return new MessageType(schemaName, types);
    }

    private Type columnToParquetField(final ExaColumnInfo columnInfo) {
        return getParquetType(convertIdentifier(columnInfo.name), columnInfo);
    }

    private String convertIdentifier(final String columnName) {
        final String convertedName = columnName.replace("\"", "");
        return this.lowercaseSchemaEnabled ? convertedName.toLowerCase(ENGLISH) : convertedName;
    }

    private Type getParquetType(final String columnName, final ExaColumnInfo columnInfo) {
        final Class<?> columnType = columnInfo.type;
        final Type.Repetition repetition = columnInfo.isNullable ? Type.Repetition.OPTIONAL : Type.Repetition.REQUIRED;
        if (Objects.equals(columnType, JavaClassTypes.J_INTEGER)) {
            return getIntegerType(columnName, columnInfo, repetition);
        } else if (Objects.equals(columnType, JavaClassTypes.J_LONG)) {
            return getLongType(columnName, columnInfo, repetition);
        } else if (Objects.equals(columnType, JavaClassTypes.J_BIG_DECIMAL)) {
            return getPrimitiveType(columnName, FIXED_LEN_BYTE_ARRAY, repetition,
                    PRECISION_TO_BYTE_SIZE.get(columnInfo.precision - 1), decimalType(columnInfo.scale, columnInfo.precision));
        } else if (Objects.equals(columnType, JavaClassTypes.J_DOUBLE)) {
            return getPrimitiveType(columnName, DOUBLE, repetition, null, null);
        } else if (Objects.equals(columnType, JavaClassTypes.J_STRING)) {
            return getPrimitiveType(columnName, BINARY, repetition, columnInfo.length > 0 ? columnInfo.length : null,
                    stringType());
        } else if (Objects.equals(columnType, JavaClassTypes.J_BOOLEAN)) {
            return getPrimitiveType(columnName, BOOLEAN, repetition, null, null);
        } else if (Objects.equals(columnType, JavaClassTypes.J_SQL_DATE)) {
            return getPrimitiveType(columnName, INT32, repetition, null, dateType());
        } else if (Objects.equals(columnType, JavaClassTypes.J_SQL_TIMESTAMP)) {
            return getPrimitiveType(columnName, INT96, repetition, null, null);
        }
        throw new IllegalArgumentException(ExaError.messageBuilder("F-CSE-22")
                .message("Cannot convert Exasol type {{TYPE}} to Parquet type.").parameter("TYPE", String.valueOf(columnType))
                .ticketMitigation().toString());
    }

    private Type getIntegerType(final String columnName, final ExaColumnInfo columnInfo,
            final Type.Repetition repetition) {
        if (columnInfo.precision == 0) {
            return getPrimitiveType(columnName, INT32, repetition, null, null);
        }
        require(columnInfo.precision <= DECIMAL_MAX_INT_DIGITS,
                "Got an 'Integer' type with more than '" + DECIMAL_MAX_INT_DIGITS + "' precision.");
        return getPrimitiveType(columnName, INT32, repetition, null, decimalType(columnInfo.scale, columnInfo.precision));
    }

    private Type getLongType(final String columnName, final ExaColumnInfo columnInfo, final Type.Repetition repetition) {
        if (columnInfo.precision == 0) {
            return getPrimitiveType(columnName, INT64, repetition, null, null);
        }
        require(columnInfo.precision <= DECIMAL_MAX_LONG_DIGITS,
                "Got a 'Long' type with more than '" + DECIMAL_MAX_LONG_DIGITS + "' precision.");
        return getPrimitiveType(columnName, INT64, repetition, null, decimalType(columnInfo.scale, columnInfo.precision));
    }

    private Type getPrimitiveType(final String name, final PrimitiveType.PrimitiveTypeName primitiveType,
            final Type.Repetition repetition, final Integer length, final LogicalTypeAnnotation logicalType) {
        Types.PrimitiveBuilder<PrimitiveType> resultType = Types.primitive(primitiveType, repetition);
        if (length != null) {
            resultType = resultType.length(length);
        }
        if (logicalType != null) {
            resultType = resultType.as(logicalType);
        }
        return resultType.named(name);
    }

    private static List<Integer> createPrecisionToByteSize() {
        final List<Integer> result = new ArrayList<>(DECIMAL_MAX_PRECISION);
        for (int precision = 1; precision <= DECIMAL_MAX_PRECISION; precision++) {
            final double power = Math.pow(10, precision);
            final double size = Math.ceil((Math.log(power - 1) / Math.log(2) + 1) / 8);
            result.add((int) size);
        }
        return List.copyOf(result);
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException("requirement failed: " + message);
        }
    }
}
