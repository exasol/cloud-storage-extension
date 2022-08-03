package com.exasol.cloudetl.bucket

import com.exasol.cloudetl.storage.StorageProperties

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.funsuite.AnyFunSuite

class BucketContractTest extends AnyFunSuite {

  test("verify bucket implementation contracts") {
    val storageProperties1 = new StorageProperties(Map("a" -> "1"), None)
    val storageProperties2 = new StorageProperties(Map("c" -> "2"), None)
    val localFilesystem = FileSystem.get(new Configuration())
    val s3aFilesystem = FileSystem.get(new Path("s3a://tmp").toUri(), new Configuration())
    Seq(
      classOf[AlluxioBucket],
      classOf[AzureAbfsBucket],
      classOf[AzureAdlsBucket],
      classOf[AzureBlobBucket],
      classOf[GCSBucket],
      classOf[HDFSBucket],
      classOf[LocalBucket],
      classOf[S3Bucket]
    ).foreach { case classType =>
      EqualsVerifier
        .forClass(classType)
        .withPrefabValues(classOf[FileSystem], localFilesystem, s3aFilesystem)
        .withPrefabValues(classOf[StorageProperties], storageProperties1, storageProperties2)
        .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
        .verify()
    }
  }

}
