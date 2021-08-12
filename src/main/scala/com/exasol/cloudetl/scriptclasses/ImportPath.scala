package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata
import com.exasol.cloudetl.constants.Constants.USER_GUIDE_LINK
import com.exasol.errorreporting.ExaError

object ImportPath {

  def generateSqlForImportSpec(meta: ExaMetadata, spec: ExaImportSpecification): String =
    throw new IllegalArgumentException(
      ExaError
        .messageBuilder("E-CSE-8")
        .message("This ImportPath script class is deprecated.")
        .mitigation("Please use the FilesImportQueryGenerator script class name.")
        .mitigation("Please check the user guide at {{LINK}} for updated deployment scripts.", USER_GUIDE_LINK)
        .toString()
    )
}
