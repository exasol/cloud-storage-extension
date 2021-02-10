package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

import com.typesafe.scalalogging.LazyLogging

object ImportMetadata extends LazyLogging {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit =
    throw new ImportScriptClassException(
      "This script class is deprecated. Please use the FilesMetadataReader " +
        "class name. You can check the user guide for updated deployment scripts."
    )
}
