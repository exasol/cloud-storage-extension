package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata

object ImportPath {

  def generateSqlForImportSpec(meta: ExaMetadata, spec: ExaImportSpecification): String =
    throw new ImportScriptClassException(
      "This script class is deprecated. Please use the FilesImportQueryGenerator " +
        "class name. You can check the user guide for updated deployment scripts."
    )
}
