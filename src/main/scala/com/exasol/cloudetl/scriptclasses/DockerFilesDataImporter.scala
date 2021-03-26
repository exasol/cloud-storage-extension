package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

/**
 * A files data importer class to run inside the Exasol docker container.
 */
object DockerFilesDataImporter {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    FilesDataImporter.run(metadata, iterator)
  }

}
