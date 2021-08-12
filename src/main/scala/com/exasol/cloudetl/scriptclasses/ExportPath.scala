package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata
import com.exasol.errorreporting.ExaError

object ExportPath {

  def generateSqlForExportSpec(meta: ExaMetadata, spec: ExaExportSpecification): String =
    throw new IllegalArgumentException(
      ExaError
        .messageBuilder("W-CSE-7")
        .message("The ExportPath script class is deprecated.")
        .mitigation("Please use the TableExportQueryGenerator script class name.")
        .mitigation("Please check the user guide for updated deployment scripts.")
        .toString()
    )
}
