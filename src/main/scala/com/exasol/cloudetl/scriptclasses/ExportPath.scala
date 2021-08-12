package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.constants.Constants.USER_GUIDE_LINK
import com.exasol.errorreporting.ExaError

object ExportPath {

  def generateSqlForExportSpec(meta: ExaMetadata, spec: ExaExportSpecification): String =
    throw new IllegalArgumentException(
      ExaError
        .messageBuilder("E-CSE-7")
        .message("The ExportPath script class is deprecated.")
        .mitigation("Please use the TableExportQueryGenerator script class name.")
        .mitigation("Please check the user guide at {{LINK}} for updated deployment scripts.", USER_GUIDE_LINK)
        .toString()
    )
}
