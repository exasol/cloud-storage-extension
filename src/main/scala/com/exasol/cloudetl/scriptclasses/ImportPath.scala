package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.errorreporting.ExaError

object ImportPath {

  def generateSqlForImportSpec(meta: ExaMetadata, spec: ExaImportSpecification): String =
    throw new IllegalArgumentException(
      ExaError
        .messageBuilder("W-CSE-8")
        .message("This ImportPath script class is deprecated.")
        .mitigation("Please use the FilesImportQueryGenerator script class name.")
        .mitigation("Please check the user guide for updated deployment scripts.")
        .toString()
    )
}
