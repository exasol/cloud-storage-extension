package com.exasol.cloudetl.helper;

/** Java class constants used for type dispatch. */
class JavaClassTypes {
    private JavaClassTypes() {
        // not instantiable
    }

    /** Integer class. */
    static final Class<Integer> J_INTEGER = Integer.class;
    /** Long class. */
    static final Class<Long> J_LONG = Long.class;
    /** BigDecimal class. */
    static final Class<java.math.BigDecimal> J_BIG_DECIMAL = java.math.BigDecimal.class;
    /** Double class. */
    static final Class<Double> J_DOUBLE = Double.class;
    /** Boolean class. */
    static final Class<Boolean> J_BOOLEAN = Boolean.class;
    /** String class. */
    static final Class<String> J_STRING = String.class;
    /** SQL date class. */
    static final Class<java.sql.Date> J_SQL_DATE = java.sql.Date.class;
    /** SQL timestamp class. */
    static final Class<java.sql.Timestamp> J_SQL_TIMESTAMP = java.sql.Timestamp.class;
}
