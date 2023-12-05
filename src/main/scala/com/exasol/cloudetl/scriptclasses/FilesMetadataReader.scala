package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.emitter.FilesMetadataEmitter
import com.exasol.cloudetl.parallelism.FixedUdfCountCalculator
import com.exasol.cloudetl.parallelism.MemoryUdfCountCalculator
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
    val storageProperties = StorageProperties(iterator.getString(STORAGE_PROPERTIES_INDEX), metadata)
    val parallelism = 8 // calculateParallelism(iterator.getInteger(PARALLELISM_INDEX), metadata, storageProperties)

    logger.info(s"Reading metadata from bucket path '$bucketPath' with parallelism '$parallelism'.")

    logger.info("Emitting 40k files...")
    FilesMetadataEmitter(storageProperties, parallelism).emit(iterator)
    logger.info("Emitting finished.")
  }

  private[this] def calculateParallelism(
    userRequestedParallelism: Int,
    metadata: ExaMetadata,
    storageProperties: StorageProperties
  ): Int = {
    val calculatedParallelism = getUdfCount(metadata, storageProperties)
    math.min(userRequestedParallelism, calculatedParallelism)
  }

  private[this] def getUdfCount(metadata: ExaMetadata, storageProperties: StorageProperties): Int = {
    val coresPerNode = Runtime.getRuntime().availableProcessors()
    if (storageProperties.hasUdfMemory()) {
      MemoryUdfCountCalculator(metadata, storageProperties.getUdfMemory()).getUdfCount(coresPerNode)
    } else {
      FixedUdfCountCalculator(metadata).getUdfCount(coresPerNode)
    }
  }

}
