package com.exasol.cloudetl.bucket

import com.exasol.ExaMetadata
import com.exasol.cloudetl.storage.StorageProperties

import org.apache.hadoop.conf.Configuration

class SecureBucketTest extends AbstractBucketTest {

  test("validate throws if no access credentials are provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseSecureBucket(properties).validate()
    }
    assert(thrown.getMessage.contains("Please provide either CONNECTION_NAME property or"))
  }

  test("validate throws if both connection name and access properties are provided") {
    properties = Map(
      "CONNECTION_NAME" -> "named_connection",
      "accountNameProperty" -> "user",
      "accountSecretProperty" -> "secret"
    )
    val thrown = intercept[IllegalArgumentException] {
      BaseSecureBucket(properties).validate()
    }
    assert(thrown.getMessage.contains("Please provide either CONNECTION_NAME property or"))
    assert(thrown.getMessage.contains("secure access credentials parameters, but not the both!"))
  }

  @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
  private[this] case class BaseSecureBucket(
    val params: Map[String, String],
    val metadata: ExaMetadata = null
  ) extends Bucket
      with SecureBucket {
    override val properties = StorageProperties(params, metadata)

    override val bucketPath = "local_path"
    override def getRequiredProperties: Seq[String] = Seq.empty[String]
    override def getSecureProperties: Seq[String] = Seq("accountSecretProperty")
    override def getConfiguration: Configuration = new Configuration()
    override def validate(): Unit = {
      validateRequiredProperties()
      validateConnectionProperties()
    }
  }
}
