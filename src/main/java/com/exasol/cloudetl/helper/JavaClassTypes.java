package com.exasol.cloudetl.helper;

/** Java class constants used for type dispatch. */
public interface JavaClassTypes {
    /** Integer class. */
    Class<Integer> J_INTEGER = Integer.class;
    /** Long class. */
    Class<Long> J_LONG = Long.class;
    /** BigDecimal class. */
    Class<java.math.BigDecimal> J_BIG_DECIMAL = java.math.BigDecimal.class;
    /** Double class. */
    Class<Double> J_DOUBLE = Double.class;
    /** Boolean class. */
    Class<Boolean> J_BOOLEAN = Boolean.class;
    /** String class. */
    Class<String> J_STRING = String.class;
    /** SQL date class. */
    Class<java.sql.Date> J_SQL_DATE = java.sql.Date.class;
    /** SQL timestamp class. */
    Class<java.sql.Timestamp> J_SQL_TIMESTAMP = java.sql.Timestamp.class;
}
