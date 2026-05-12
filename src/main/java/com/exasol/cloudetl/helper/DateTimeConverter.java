package com.exasol.cloudetl.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Helper functions to convert date time values.
 */
public final class DateTimeConverter {
    public static final LocalDateTime UnixEpochDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    public static final long JULIAN_DAY_OF_EPOCH = 2440588L;
    public static final long SECONDS_PER_DAY = 60L * 60L * 24L;
    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_DAY = SECONDS_PER_DAY * MILLIS_PER_SECOND;
    public static final long MICROS_PER_MILLIS = 1000L;
    public static final long MICROS_PER_SECOND = MICROS_PER_MILLIS * MILLIS_PER_SECOND;
    public static final long MICROS_PER_DAY = MICROS_PER_SECOND * SECONDS_PER_DAY;

    private DateTimeConverter() {
        // Prevent instantiation.
    }

    /**
     * Returns a timestamp from number of microseconds since epoch.
     *
     * @param microseconds microseconds since epoch
     * @return timestamp
     */
    public static Timestamp getTimestampFromMicros(final long microseconds) {
        long seconds = microseconds / MICROS_PER_SECOND;
        long micros = microseconds % MICROS_PER_SECOND;
        if (micros < 0) {
            micros += MICROS_PER_SECOND;
            seconds -= 1;
        }
        final Timestamp timestamp = new Timestamp(seconds * 1000L);
        timestamp.setNanos((int) micros * 1000);
        return timestamp;
    }

    /**
     * Returns a timestamp from number of milliseconds since epoch.
     *
     * @param milliseconds milliseconds since epoch
     * @return timestamp
     */
    public static Timestamp getTimestampFromMillis(final long milliseconds) {
        return new Timestamp(milliseconds);
    }

    /**
     * Returns the number of micros since epoch from a timestamp.
     *
     * @param timestamp timestamp
     * @return microseconds since epoch, or {@code 0} for {@code null}
     */
    public static long getMicrosFromTimestamp(final Timestamp timestamp) {
        if (timestamp == null) {
            return 0L;
        }
        return timestamp.getTime() * 1000L + (timestamp.getNanos() / 1000L) % 1000L;
    }

    /**
     * Returns Julian day and nanoseconds in a day from microseconds since epoch.
     *
     * @param microseconds microseconds since epoch
     * @return julian day and nanos in day
     */
    public static scala.Tuple2<Integer, Long> getJulianDayAndNanos(final long microseconds) {
        final long julianMicros = microseconds + JULIAN_DAY_OF_EPOCH * MICROS_PER_DAY;
        final long day = julianMicros / MICROS_PER_DAY;
        final long micros = julianMicros % MICROS_PER_DAY;
        return new scala.Tuple2<>(Integer.valueOf((int) day), Long.valueOf(micros * 1000L));
    }

    /**
     * Returns microseconds since epoch from Julian day and nanoseconds in a day.
     *
     * @param day julian day
     * @param nanos nanoseconds in day
     * @return microseconds since epoch
     */
    public static long getMicrosFromJulianDay(final int day, final long nanos) {
        final long seconds = (long) (day - JULIAN_DAY_OF_EPOCH) * SECONDS_PER_DAY;
        return seconds * MICROS_PER_SECOND + nanos / 1000L;
    }

    /**
     * Returns the number of days since unix epoch.
     *
     * @param date date
     * @return days since epoch
     */
    public static long daysSinceEpoch(final Date date) {
        final long millisUtc = date.getTime();
        final long millis =
                millisUtc + TimeZone.getTimeZone(ZoneId.systemDefault()).getOffset(millisUtc);
        return (long) Math.floor((double) millis / MILLIS_PER_DAY);
    }

    /**
     * Returns a date given the days since epoch.
     *
     * @param days days since epoch
     * @return date
     */
    public static Date daysToDate(final long days) {
        final LocalDateTime date = UnixEpochDateTime.plusDays(days);
        final long millis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new Date(millis);
    }
}
