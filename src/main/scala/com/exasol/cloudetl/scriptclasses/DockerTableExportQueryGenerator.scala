package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaExportSpecification
import com.exasol.ExaMetadata

/**
 * An export query generator class to run inside the Exasol docker container.
 */
object DockerTableExportQueryGenerator {

  def generateSqlForExportSpec(
    metadata: ExaMetadata,
    exportSpecification: ExaExportSpecification
  ): String = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    TableExportQueryGenerator.generateSqlForExportSpec(metadata, exportSpecification)
  }

}
