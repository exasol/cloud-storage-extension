package com.exasol.cloudetl.data;

import java.util.Objects;

/** Exasol table column information. */
public final class ExaColumnInfo {
    /** Column name. */
    public final String name;
    /** Java class representing the column type. */
    public final Class<?> type;
    /** Decimal precision. */
    public final int precision;
    /** Decimal scale. */
    public final int scale;
    /** String length. */
    public final int length;
    /** Nullable flag. */
    public final boolean isNullable;

    /** Create column information. */
    public ExaColumnInfo(final String name, final Class<?> type, final int precision, final int scale, final int length,
            final boolean isNullable) {
        this.name = name;
        this.type = type;
        this.precision = precision;
        this.scale = scale;
        this.length = length;
        this.isNullable = isNullable;
    }

    /** Create nullable column information with unspecified precision, scale, and length. */
    public ExaColumnInfo(final String name, final Class<?> type) {
        this(name, type, 0, 0, 0, true);
    }

    public String name() {
        return this.name;
    }

    public Class<?> type() {
        return this.type;
    }

    public int precision() {
        return this.precision;
    }

    public int scale() {
        return this.scale;
    }

    public int length() {
        return this.length;
    }

    public boolean isNullable() {
        return this.isNullable;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ExaColumnInfo)) {
            return false;
        }
        final ExaColumnInfo other = (ExaColumnInfo) obj;
        return this.precision == other.precision && this.scale == other.scale && this.length == other.length
                && this.isNullable == other.isNullable && Objects.equals(this.name, other.name)
                && Objects.equals(this.type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.type, this.precision, this.scale, this.length, this.isNullable);
    }

    @Override
    public String toString() {
        return "ExaColumnInfo(" + this.name + "," + this.type + "," + this.precision + "," + this.scale + ","
                + this.length + "," + this.isNullable + ")";
    }
}
