package com.exasol.cloudetl.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void getColumnValueReturnsValueWithColumnType() throws Exception {
        final BigDecimal decimal = new BigDecimal(1337);
        final Date date = new Date(System.currentTimeMillis());
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(this.exasolIterator.getInteger(3)).thenReturn(1);
        when(this.exasolIterator.getLong(4)).thenReturn(3L);
        when(this.exasolIterator.getBigDecimal(5)).thenReturn(decimal);
        when(this.exasolIterator.getDouble(6)).thenReturn(3.14);
        when(this.exasolIterator.getString(7)).thenReturn("xyz");
        when(this.exasolIterator.getBoolean(8)).thenReturn(true);
        when(this.exasolIterator.getDate(9)).thenReturn(date);
        when(this.exasolIterator.getTimestamp(10)).thenReturn(timestamp);

        final List<Object[]> data = List.of(//
                new Object[] { 1, new ExaColumnInfo("c_int", Integer.class) }, //
                new Object[] { 3L, new ExaColumnInfo("c_long", Long.class) }, //
                new Object[] { decimal, new ExaColumnInfo("c_decimal", BigDecimal.class, 4, 0, 0, true) }, //
                new Object[] { 3.14, new ExaColumnInfo("c_double", Double.class) }, //
                new Object[] { "xyz", new ExaColumnInfo("c_string", String.class) }, //
                new Object[] { true, new ExaColumnInfo("c_boolean", Boolean.class) }, //
                new Object[] { date, new ExaColumnInfo("c_date", Date.class) }, //
                new Object[] { timestamp, new ExaColumnInfo("c_timestamp", Timestamp.class) });

        for (int index = 0; index < data.size(); index++) {
            final Object expectedValue = data.get(index)[0];
            final ExaColumnInfo columnInfo = (ExaColumnInfo) data.get(index)[1];
            final Object value = this.columnValueProvider.getColumnValue(START_INDEX + index, columnInfo);
            assertEquals(expectedValue, value);
            assertEquals(columnInfo.type, value.getClass());
        }
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
}
