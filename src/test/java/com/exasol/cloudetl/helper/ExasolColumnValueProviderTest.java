package com.exasol.cloudetl.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.ExaIterator;
import com.exasol.cloudetl.data.ExaColumnInfo;

class ExasolColumnValueProviderTest {
    private static final int START_INDEX = 3;
    private ExaIterator exasolIterator;
    private ExasolColumnValueProvider columnValueProvider;

    @BeforeEach
    void beforeEach() {
        this.exasolIterator = mock(ExaIterator.class);
        this.columnValueProvider = new ExasolColumnValueProvider(this.exasolIterator);
    }

    @ParameterizedTest
    @MethodSource("columnValues")
    void getColumnValueReturnsValueWithColumnType(final int index, final Object expectedValue,
            final ExaColumnInfo columnInfo) throws Exception {
        mockIteratorValue(index, expectedValue, columnInfo.type);

        final Object value = this.columnValueProvider.getColumnValue(index, columnInfo);

        assertEquals(expectedValue, value);
        assertEquals(columnInfo.type, value.getClass());
    }

    @Test
    void getColumnValueThrowsForUnsupportedType() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> this.columnValueProvider.getColumnValue(0, new ExaColumnInfo("c_short", Short.class)));
        assertTrue(thrown.getMessage().startsWith("E-CSE-23"));
        assertTrue(thrown.getMessage().contains("Cannot obtain Exasol value for column type 'class java.lang.Short'."));
    }

    @Test
    void getColumnValueThrowsForUpdatedBigDecimalWithHigherPrecision() throws Exception {
        final BigDecimal decimal = BigDecimal.valueOf(13.37);
        final ExaColumnInfo columnInfo = new ExaColumnInfo("c_dec", BigDecimal.class, 4, 3, 0, true);
        when(this.exasolIterator.getBigDecimal(3)).thenReturn(decimal);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> this.columnValueProvider.getColumnValue(3, columnInfo));
        assertTrue(thrown.getMessage().startsWith("E-CSE-24"));
        assertTrue(thrown.getMessage().contains("value exceeds configured '4'."));
    }

    @Test
    void getColumnValueReturnsUpdatedBigDecimal() throws Exception {
        final BigDecimal decimal = BigDecimal.valueOf(238316.38);
        final ExaColumnInfo columnInfo = new ExaColumnInfo("c_decimal", BigDecimal.class, 18, 4, 0, true);
        when(this.exasolIterator.getBigDecimal(3)).thenReturn(decimal);
        final BigDecimal columnValue = (BigDecimal) this.columnValueProvider.getColumnValue(3, columnInfo);
        assertEquals("238316.3800", columnValue.toPlainString());
    }

    private static Stream<Arguments> columnValues() {
        final BigDecimal decimal = new BigDecimal(1337);
        final Date date = Date.valueOf("2026-05-13");
        final Timestamp timestamp = Timestamp.valueOf("2026-05-13 12:34:56");
        return Stream.of(//
                Arguments.of(START_INDEX, 1, new ExaColumnInfo("c_int", Integer.class)), //
                Arguments.of(START_INDEX + 1, 3L, new ExaColumnInfo("c_long", Long.class)), //
                Arguments.of(START_INDEX + 2, decimal,
                        new ExaColumnInfo("c_decimal", BigDecimal.class, 4, 0, 0, true)), //
                Arguments.of(START_INDEX + 3, 3.14, new ExaColumnInfo("c_double", Double.class)), //
                Arguments.of(START_INDEX + 4, "xyz", new ExaColumnInfo("c_string", String.class)), //
                Arguments.of(START_INDEX + 5, true, new ExaColumnInfo("c_boolean", Boolean.class)), //
                Arguments.of(START_INDEX + 6, date, new ExaColumnInfo("c_date", Date.class)), //
                Arguments.of(START_INDEX + 7, timestamp, new ExaColumnInfo("c_timestamp", Timestamp.class)));
    }

    private void mockIteratorValue(final int index, final Object value, final Class<?> type) throws Exception {
        if (type.equals(Integer.class)) {
            when(this.exasolIterator.getInteger(index)).thenReturn((Integer) value);
        } else if (type.equals(Long.class)) {
            when(this.exasolIterator.getLong(index)).thenReturn((Long) value);
        } else if (type.equals(BigDecimal.class)) {
            when(this.exasolIterator.getBigDecimal(index)).thenReturn((BigDecimal) value);
        } else if (type.equals(Double.class)) {
            when(this.exasolIterator.getDouble(index)).thenReturn((Double) value);
        } else if (type.equals(String.class)) {
            when(this.exasolIterator.getString(index)).thenReturn((String) value);
        } else if (type.equals(Boolean.class)) {
            when(this.exasolIterator.getBoolean(index)).thenReturn((Boolean) value);
        } else if (type.equals(Date.class)) {
            when(this.exasolIterator.getDate(index)).thenReturn((Date) value);
        } else if (type.equals(Timestamp.class)) {
            when(this.exasolIterator.getTimestamp(index)).thenReturn((Timestamp) value);
        }
    }
}
