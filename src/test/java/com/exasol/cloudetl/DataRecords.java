package com.exasol.cloudetl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class DataRecords {
    public final BigDecimal bigDecimalValue1 = new BigDecimal("5555555555555555555555555555555.55555");
    public final BigDecimal bigDecimalValue2 = new BigDecimal("5555555555555555555555555555555.55555");
    public final Date dateValue1 = new Date(System.currentTimeMillis());
    public final Date dateValue2 = new Date(System.currentTimeMillis());
    public final Timestamp timestampValue1 = new Timestamp(System.currentTimeMillis());
    public final Timestamp timestampValue2 = new Timestamp(System.currentTimeMillis());

    public final List<List<Object>> rawRecords = List.of(//
            List.of(1, 3L, this.bigDecimalValue1, 3.14d, "xyz", true, this.dateValue1, this.timestampValue1), //
            List.of(2, 4L, this.bigDecimalValue2, 0.13d, "abc", false, this.dateValue2, this.timestampValue2));
}
