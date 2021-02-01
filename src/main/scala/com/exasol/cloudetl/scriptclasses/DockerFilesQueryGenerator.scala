package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata

/**
 * A query generator class to run inside the Exasol docker container.
 */
object DockerFilesQueryGenerator {

  def generateSqlForImportSpec(
    metadata: ExaMetadata,
    importSpecification: ExaImportSpecification
  ): String = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exauser"))
    ImportPath.generateSqlForImportSpec(metadata, importSpecification)
  }

}
