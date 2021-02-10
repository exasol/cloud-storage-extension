package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

/**
 * A files metadata reader class to run inside the Exasol docker container.
 */
object DockerFilesMetadataReader {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exauser"))
    FilesMetadataReader.run(metadata, iterator)
  }

}
