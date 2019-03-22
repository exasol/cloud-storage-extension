package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaIterator
import com.exasol.ExaMetadata
import com.exasol.cloudetl.bucket._

import com.typesafe.scalalogging.LazyLogging

object ImportMetadata extends LazyLogging {

  def run(meta: ExaMetadata, iter: ExaIterator): Unit = {
    val bucketPath = iter.getString(0)
    val parallelism = iter.getInteger(2)

    logger.info(
      s"Reading metadata from bucket path = $bucketPath with parallelism = ${parallelism.toString}"
    )

    val rest = iter.getString(1)
    val params = Bucket.keyValueStringToMap(rest)

    val bucket = Bucket(params)

    val paths = bucket.getPaths()

    paths.zipWithIndex.foreach {
      case (filename, idx) =>
        iter.emit(filename.toString, s"${idx % parallelism}")
    }
  }

}
