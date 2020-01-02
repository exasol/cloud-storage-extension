package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

import com.typesafe.scalalogging.LazyLogging

object ImportMetadata extends LazyLogging {

  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    import org.apache.hadoop.security.UserGroupInformation
    UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("exadefusr"))
    val bucketPath = iterator.getString(0)
    val parallelism = iterator.getInteger(2)

    logger.info(
      s"Reading metadata from bucket path: $bucketPath "
        + s"with parallelism: ${parallelism.toString}"
    )

    val storageProperties = StorageProperties(iterator.getString(1), metadata)
    val bucket = Bucket(storageProperties)
    val paths = bucket.getPaths().filter(p => !p.getName().startsWith("_"))
    logger.info(s"Total number of files: ${paths.size} in bucket path: $bucketPath")

    paths.zipWithIndex.foreach {
      case (filename, idx) =>
        iterator.emit(filename.toString, s"${idx % parallelism}")
    }
  }

}
