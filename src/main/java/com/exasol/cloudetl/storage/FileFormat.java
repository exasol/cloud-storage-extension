package com.exasol.cloudetl.storage;

import static java.util.Locale.ENGLISH;

import com.exasol.cloudetl.constants.Constants;
import com.exasol.errorreporting.ExaError;

/** Supported file formats. */
public enum FileFormat {
    /** Avro files. */
    AVRO,
    /** Delta Lake tables. */
    DELTA,
    /** Plain files. */
    FILE,
    /** ORC files. */
    ORC,
    /** Parquet files. */
    PARQUET;

    /**
     * Create a file format from a string.
     *
     * @param fileFormat file format name
     * @return matching file format
     */
    public static FileFormat apply(final String fileFormat) {
        return fromString(fileFormat);
    }

    /**
     * Create a file format from a string.
     *
     * @param fileFormat file format name
     * @return matching file format
     */
    public static FileFormat fromString(final String fileFormat) {
        switch (fileFormat.toUpperCase(ENGLISH)) {
        case "AVRO":
            return AVRO;
        case "DELTA":
            return DELTA;
        case "FILE":
            return FILE;
        case "ORC":
            return ORC;
        case "PARQUET":
            return PARQUET;
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-17")
                    .message("Provided file format {{FORMAT}} is not supported.", fileFormat)
                    .mitigation("Please use one of supported formats.")
                    .mitigation("You can check user guide at {{LINK}} for supported list of formats.",
                            Constants.USER_GUIDE_LINK)
                    .toString());
        }
    }
}
