package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket.Bucket
import com.exasol.cloudetl.storage.StorageProperties

import com.typesafe.scalalogging.LazyLogging

/**
 * A metadata reader for the storage filesystems.
 */
object FilesMetadataReader extends LazyLogging {

  private[this] val BUCKET_PATH_INDEX = 0
  private[this] val STORAGE_PROPERTIES_INDEX = 1
  private[this] val PARALLELISM_INDEX = 2

  /**
   * Emits the number of files available in the storage path into Exasol iterator.
   *
   * @param metadata an Exasol metadata object
   * @param iterator an Exasol iterator object
   */
  def run(metadata: ExaMetadata, iterator: ExaIterator): Unit = {
    val bucketPath = iterator.getString(BUCKET_PATH_INDEX)
    val parallelism = iterator.getInteger(PARALLELISM_INDEX)
    logger.info(s"Reading metadata from bucket path '$bucketPath' with parallelism '$parallelism'.")

    val storageProperties = StorageProperties(iterator.getString(STORAGE_PROPERTIES_INDEX), metadata)
    val paths = Bucket(storageProperties).getPaths()
    logger.info(s"Total number of files '${paths.size}' in bucket path '$bucketPath'. ")

    paths.zipWithIndex.foreach { case (filename, idx) =>
      iterator.emit(filename.toString(), s"${idx % parallelism}")
    }
  }

}
