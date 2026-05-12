package com.exasol.cloudetl.orc.converter;

import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.orc.TypeDescription;
import org.apache.orc.TypeDescription.Category;

import com.exasol.errorreporting.ExaError;

/** Factory for ORC converters. */
public final class OrcConverterFactory {
    private OrcConverterFactory() {
        // Utility class.
    }

    /** Create a converter for an ORC type. */
    public static OrcConverter<? extends ColumnVector> create(final TypeDescription orcType) {
        if (orcType.getCategory().isPrimitive()) {
            return createPrimitiveConverter(orcType);
        }
        return createComplexConverter(orcType);
    }

    /** Factory alias for Java callers. */
    public static OrcConverter<? extends ColumnVector> apply(final TypeDescription orcType) {
        return create(orcType);
    }

    private static OrcConverter<? extends ColumnVector> createPrimitiveConverter(final TypeDescription orcType) {
        switch (orcType.getCategory()) {
        case BOOLEAN:
            return OrcConverters.BOOLEAN;
        case BYTE:
            return OrcConverters.BYTE;
        case SHORT:
            return OrcConverters.SHORT;
        case INT:
            return OrcConverters.INT;
        case LONG:
            return OrcConverters.LONG;
        case FLOAT:
            return OrcConverters.FLOAT;
        case DOUBLE:
            return OrcConverters.DOUBLE;
        case DECIMAL:
            return OrcConverters.DECIMAL;
        case DATE:
            return OrcConverters.DATE;
        case TIMESTAMP:
            return OrcConverters.TIMESTAMP;
        case BINARY:
            return OrcConverters.BINARY;
        case CHAR:
        case STRING:
        case VARCHAR:
            return OrcConverters.STRING;
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("F-CSE-10")
                    .message("Orc primitive type {{PRIMITIVE_TYPE}} is not supported.")
                    .parameter("PRIMITIVE_TYPE", String.valueOf(orcType.getCategory())).ticketMitigation().toString());
        }
    }

    private static OrcConverter<? extends ColumnVector> createComplexConverter(final TypeDescription orcType) {
        final Category category = orcType.getCategory();
        switch (category) {
        case LIST:
            return new OrcConverters.ListConverter<>(create(orcType.getChildren().get(0)));
        case MAP:
            return new OrcConverters.MapConverter<>(create(orcType.getChildren().get(0)),
                    create(orcType.getChildren().get(1)));
        case STRUCT:
            return new StructConverter(orcType);
        case UNION:
            return new UnionConverter(orcType);
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("F-CSE-11")
                    .message("Orc complex type {{COMPLEX_TYPE}} is not supported.")
                    .parameter("COMPLEX_TYPE", String.valueOf(orcType.getCategory())).ticketMitigation().toString());
        }
    }
}
