package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata

/**
 * A table data exporter class to run inside the Exasol docker
 * container.
 */
object DockerTableDataExporter {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exauser"))
    TableDataExporter.run(metadata, iterator)
  }

}
