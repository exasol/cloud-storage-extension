package com.exasol.cloudetl.bucket

class HDFSBucketTest extends AbstractBucketTest {

  test("apply sets correct configuration") {
    val properties = Map(
      PATH -> "hdfs://dir/path",
      FORMAT -> "orc"
    )
    val exaMetadata = mockConnectionInfo("", "")
    val bucket = getBucket(properties, exaMetadata)
    val expectedFileSystemName = classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName()
    assert(bucket.isInstanceOf[HDFSBucket])
    assert(bucket.getConfiguration().get("fs.hdfs.impl") === expectedFileSystemName)
  }

}
