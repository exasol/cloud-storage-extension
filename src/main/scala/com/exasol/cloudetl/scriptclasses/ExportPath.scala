package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata

object ExportPath {

  def generateSqlForExportSpec(meta: ExaMetadata, spec: ExaExportSpecification): String =
    throw new IllegalArgumentException(
      "This script class is deprecated. Please use the TableExportQueryGenerator " +
        "class name. You can check the user guide for updated deployment scripts."
    )
}
