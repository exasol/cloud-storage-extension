package com.exasol.cloudetl.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DateTimeConverterTest {
    private void daysSinceEpochToDate(final Date date) {
        final Date converted = DateTimeConverter.daysToDate(DateTimeConverter.daysSinceEpoch(date));
        assertEquals(date.toString(), converted.toString());
    }

    @ParameterizedTest
    @MethodSource("dates")
    void fromJavaSqlDateToDaysSinceEpochAndBackToJavaSqlDate(final Date date) {
        daysSinceEpochToDate(date);
    }

    private static Stream<Date> dates() throws ParseException {
        final SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);

        return Stream.of(//
                new Date(100), //
                new Date(localFormat.parse("1776-07-04 10:30:00").getTime()), //
                new Date(utcFormat.parse("1776-07-04 18:30:00 UTC").getTime()), //
                Date.valueOf("1912-05-05"), //
                Date.valueOf("1969-01-01"), //
                new Date(localFormat.parse("1969-01-01 00:00:00").getTime()), //
                new Date(utcFormat.parse("1969-01-01 00:00:00 UTC").getTime()), //
                new Date(localFormat.parse("1969-01-01 00:00:01").getTime()), //
                new Date(utcFormat.parse("1969-01-01 00:00:01 UTC").getTime()), //
                new Date(localFormat.parse("1969-12-31 23:59:59").getTime()), //
                new Date(utcFormat.parse("1969-12-31 23:59:59 UTC").getTime()), //
                Date.valueOf("1970-01-01"), //
                new Date(localFormat.parse("1970-01-01 00:00:00").getTime()), //
                new Date(utcFormat.parse("1970-01-01 00:00:00 UTC").getTime()), //
                new Date(localFormat.parse("1970-01-01 00:00:01").getTime()), //
                new Date(utcFormat.parse("1970-01-01 00:00:01 UTC").getTime()), //
                new Date(localFormat.parse("1989-11-09 11:59:59").getTime()), //
                new Date(utcFormat.parse("1989-11-09 19:59:59 UTC").getTime()), //
                Date.valueOf("2019-02-10"));
    }

    @Test
    void correctlyConvertsDate00010101ToDaysAndBackToDate() {
        daysSinceEpochToDate(Date.valueOf("0001-01-01"));
    }
}
