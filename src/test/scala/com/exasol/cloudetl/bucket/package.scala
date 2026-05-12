package com.exasol.cloudetl

package object bucket {
  val Bucket: BucketFactory.type = BucketFactory
  val AlluxioBucket: AlluxioBucketFactory.type = AlluxioBucketFactory
  val AzureAbfsBucket: AzureAbfsBucketFactory.type = AzureAbfsBucketFactory
  val AzureAdlsBucket: AzureAdlsBucketFactory.type = AzureAdlsBucketFactory
  val AzureBlobBucket: AzureBlobBucketFactory.type = AzureBlobBucketFactory
  val GCSBucket: GCSBucketFactory.type = GCSBucketFactory
  val HDFSBucket: HDFSBucketFactory.type = HDFSBucketFactory
  val LocalBucket: LocalBucketFactory.type = LocalBucketFactory
  val S3Bucket: S3BucketFactory.type = S3BucketFactory
}

object BucketFactory {
  def apply(
    storageProperties: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.Bucket = {
    val path = storageProperties.getStoragePath()
    storageProperties.getStoragePathScheme() match {
      case "s3a"            => new _root_.com.exasol.cloudetl.bucket.S3Bucket(path, storageProperties)
      case "gs"             => new _root_.com.exasol.cloudetl.bucket.GCSBucket(path, storageProperties)
      case "abfs" | "abfss" => new _root_.com.exasol.cloudetl.bucket.AzureAbfsBucket(path, storageProperties)
      case "adl"            => new _root_.com.exasol.cloudetl.bucket.AzureAdlsBucket(path, storageProperties)
      case "alluxio"        => new _root_.com.exasol.cloudetl.bucket.AlluxioBucket(path, storageProperties)
      case "wasb" | "wasbs" => new _root_.com.exasol.cloudetl.bucket.AzureBlobBucket(path, storageProperties)
      case "hdfs"           => new _root_.com.exasol.cloudetl.bucket.HDFSBucket(path, storageProperties)
      case "file"           => new _root_.com.exasol.cloudetl.bucket.LocalBucket(path, storageProperties)
      case other            => throw new IllegalArgumentException(s"Unsupported scheme: $other")
    }
  }
}

object AlluxioBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.AlluxioBucket =
    new _root_.com.exasol.cloudetl.bucket.AlluxioBucket(path, params)
}

object AzureAbfsBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.AzureAbfsBucket =
    new _root_.com.exasol.cloudetl.bucket.AzureAbfsBucket(path, params)
}

object AzureAdlsBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.AzureAdlsBucket =
    new _root_.com.exasol.cloudetl.bucket.AzureAdlsBucket(path, params)
}

object AzureBlobBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.AzureBlobBucket =
    new _root_.com.exasol.cloudetl.bucket.AzureBlobBucket(path, params)
}

object GCSBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.GCSBucket =
    new _root_.com.exasol.cloudetl.bucket.GCSBucket(path, params)
}

object HDFSBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.HDFSBucket =
    new _root_.com.exasol.cloudetl.bucket.HDFSBucket(path, params)
}

object LocalBucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.LocalBucket =
    new _root_.com.exasol.cloudetl.bucket.LocalBucket(path, params)
}

object S3BucketFactory {
  def apply(
    path: String,
    params: _root_.com.exasol.cloudetl.storage.StorageProperties
  ): _root_.com.exasol.cloudetl.bucket.S3Bucket =
    new _root_.com.exasol.cloudetl.bucket.S3Bucket(path, params)
}
