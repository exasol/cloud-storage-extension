package com.exasol.cloudetl.scriptclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaExportSpecification;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.constants.Constants;
import com.exasol.errorreporting.ExaError;

/** Deprecated export path script class. */
public final class ExportPath {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPath.class);

    private ExportPath() {
        // Script class.
    }

    /** Generate SQL for an export spec. */
    public static String generateSqlForExportSpec(final ExaMetadata meta, final ExaExportSpecification spec) {
        LOGGER.info("Script '{}' with parameters '{}'.", meta.getScriptName(), spec.getParameters());
        throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-7")
                .message("The ExportPath script class is deprecated.")
                .mitigation("Please use the TableExportQueryGenerator script class name.")
                .mitigation("Please check the user guide at {{LINK}} for updated deployment scripts.",
                        Constants.USER_GUIDE_LINK)
                .toString());
    }
}
