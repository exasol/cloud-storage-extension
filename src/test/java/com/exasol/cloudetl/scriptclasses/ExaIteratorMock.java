package com.exasol.cloudetl.scriptclasses;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.exasol.ExaIterator;

public class ExaIteratorMock implements ExaIterator {

    private int currentRow = 0;
    private final Object[][] values;

    public static ExaIteratorMock empty() {
        return new ExaIteratorMock(new Object[0][0]);
    }

    public ExaIteratorMock(final Object[][] values) {
        this.values = values;
    }

    @Override
    public void emit(final Object... args) {
        throw new UnsupportedOperationException("Unimplemented method 'emit'");
    }

    @Override
    public BigDecimal getBigDecimal(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public BigDecimal getBigDecimal(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public Boolean getBoolean(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getBoolean'");
    }

    @Override
    public Boolean getBoolean(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getBoolean'");
    }

    @Override
    public Date getDate(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Date getDate(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Double getDouble(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Override
    public Double getDouble(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Override
    public Integer getInteger(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getInteger'");
    }

    @Override
    public Integer getInteger(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getInteger'");
    }

    @Override
    public Long getLong(final int col) {
        return get(Long.class, col);
    }

    @Override
    public Long getLong(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getLong'");
    }

    @Override
    public Object getObject(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public Object getObject(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public String getString(final int col) {
        return get(String.class, col);
    }

    private <T> T get(final Class<T> type, final int col) {
        if (values.length > currentRow) {
            return type.cast(values[currentRow][col]);
        } else {
            return null;
        }
    }

    @Override
    public String getString(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Override
    public Timestamp getTimestamp(final String col) {
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public Timestamp getTimestamp(final int col) {
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public boolean next() {
        if (currentRow + 1 >= values.length) {
            return false;
        }
        currentRow++;
        return true;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    @Override
    public long size() {
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }
}
