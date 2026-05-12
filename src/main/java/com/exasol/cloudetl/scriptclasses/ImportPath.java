package com.exasol.cloudetl.scriptclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaImportSpecification;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.constants.Constants;
import com.exasol.errorreporting.ExaError;

/** Deprecated import path script class. */
public final class ImportPath {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportPath.class);

    private ImportPath() {
        // Script class.
    }

    /** Generate SQL for an import spec. */
    public static String generateSqlForImportSpec(final ExaMetadata meta, final ExaImportSpecification spec) {
        LOGGER.info("Script '{}' with parameters '{}'.", meta.getScriptName(), spec.getParameters());
        throw new IllegalArgumentException(ExaError.messageBuilder("E-CSE-8")
                .message("This ImportPath script class is deprecated.")
                .mitigation("Please use the FilesImportQueryGenerator script class name.")
                .mitigation("Please check the user guide at {{LINK}} for updated deployment scripts.",
                        Constants.USER_GUIDE_LINK)
                .toString());
    }
}
