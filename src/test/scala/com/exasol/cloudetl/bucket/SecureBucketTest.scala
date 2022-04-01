package com.exasol.cloudetl.bucket

import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

class SecureBucketTest extends AbstractBucketTest {

  test("validate throws if no connection name is provided") {
    assertNoConnectionName(BaseSecureBucket(properties).validate())
  }

  test("validate throws if access properties are provided") {
    properties = Map(
      "accountNameProperty" -> "user",
      "accountSecretProperty" -> "secret"
    )
    assertForbiddenProperty(BaseSecureBucket(properties).validate())
  }

  private[this] case class BaseSecureBucket(
    val params: Map[String, String],
    val metadata: ExaMetadata = null
  ) extends Bucket
      with SecureBucket {
    override val properties = StorageProperties(params, metadata)

    override val bucketPath = "local_path"
    override def getRequiredProperties(): Seq[String] = Seq.empty[String]
    override def getSecureProperties(): Seq[String] = Seq("accountSecretProperty")
    override def getConfiguration(): Configuration = new Configuration()
    override def validate(): Unit = {
      validateRequiredProperties()
      validateConnectionProperties()
    }
  }
}
