package com.exasol.cloudetl.bucket

class AlluxioBucketTest extends AbstractBucketTest {

  test("apply sets correct configuration") {
    val properties = Map(
      PATH -> "alluxio://alluxio/fs/path",
      FORMAT -> "parquet"
    )
    val exaMetadata = mockConnectionInfo("", "")
    val bucket = getBucket(properties, exaMetadata)
    val conf = bucket.getConfiguration()
    assert(bucket.isInstanceOf[AlluxioBucket])
    assert(conf.get("fs.alluxio.impl") === classOf[alluxio.hadoop.FileSystem].getName())
    assert(
      conf.get("fs.AbstractFileSystem.alluxio.impl") === classOf[alluxio.hadoop.AlluxioFileSystem]
        .getName()
    )
  }

}
