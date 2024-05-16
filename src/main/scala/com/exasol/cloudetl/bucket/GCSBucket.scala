package com.exasol.cloudetl.bucket

import java.nio.charset.StandardCharsets
import java.nio.file.Files

import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.CommonConstants.CONNECTION_NAME
import com.exasol.errorreporting.ExaError

import org.apache.hadoop.conf.Configuration

/** A [[Bucket]] implementation for the Google Cloud Storage (GCS) */
final case class GCSBucket(path: String, params: StorageProperties) extends Bucket {

  private[this] val GCS_PROJECT_ID: String = "GCS_PROJECT_ID"
  private[this] val GCS_KEYFILE_PATH: String = "GCS_KEYFILE_PATH"
  private[this] val GCS_KEYFILE_CONTENT: String = "GCS_KEYFILE_CONTENT"

  /** @inheritdoc */
  override val bucketPath: String = path

  /** @inheritdoc */
  override val properties: StorageProperties = params

  override def validate(): Unit = {
    validateRequiredProperties()
    validateKeyfileProperties()
    validateConnectionProperties()
  }

  private def validateKeyfileProperties(): Unit = {
    if (properties.containsKey(GCS_KEYFILE_PATH) && properties.hasNamedConnection()) {
      throw new IllegalArgumentException(
        ExaError
          .messageBuilder("E-CSE-30")
          .message(
            "Both properties {{GCS_KEYFILE_PATH}} and {{CONNECTION_NAME}} are specified.",
            GCS_KEYFILE_PATH,
            CONNECTION_NAME
          )
          .mitigation("Please specify only one of them.")
          .toString()
      )
    }
    if (!properties.containsKey(GCS_KEYFILE_PATH) && !properties.hasNamedConnection()) {
      throw new IllegalArgumentException(
        ExaError
          .messageBuilder("E-CSE-31")
          .message(
            "Neither of properties {{GCS_KEYFILE_PATH}} or {{CONNECTION_NAME}} is specified.",
            GCS_KEYFILE_PATH,
            CONNECTION_NAME
          )
          .mitigation("Please specify exactly one of them.")
          .toString()
      )
    }
  }

  private def validateConnectionProperties(): Unit =
    if (properties.hasNamedConnection()) {
      val connectionName = properties.getString(CONNECTION_NAME)
      val content = getKeyfileContentFromConnection(connectionName)
      if (!content.trim().startsWith("{")) {
        throw new IllegalArgumentException(
          ExaError
            .messageBuilder("E-CSE-33")
            .message(
              "The connection {{connectionName}} does not contain valid JSON in property {{GCS_KEYFILE_CONTENT}}.",
              connectionName,
              GCS_KEYFILE_CONTENT
            )
            .mitigation("Please check the connection properties.")
            .toString()
        )
      }
    }

  /**
   * Returns the list of required property keys for Google Cloud Storage.
   */
  override def getRequiredProperties(): Seq[String] =
    Seq(GCS_PROJECT_ID)

  /**
   * @inheritdoc
   *
   * Additionally validates that all required parameters are available in order to create a configuration.
   */
  override def getConfiguration(): Configuration = {
    validate()

    val conf = new Configuration()
    conf.set("fs.gs.impl", classOf[com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem].getName)
    conf.setBoolean("fs.gs.auth.service.account.enable", true)
    conf.set("fs.gs.project.id", properties.getString(GCS_PROJECT_ID))
    conf.set("fs.gs.auth.type", "SERVICE_ACCOUNT_JSON_KEYFILE")
    conf.set("fs.gs.auth.service.account.json.keyfile", getKeyFilePath())

    properties.getProxyHost().foreach { proxyHost =>
      properties.getProxyPort().foreach(proxyPort => conf.set("fs.gs.proxy.address", s"$proxyHost:$proxyPort"))
      properties.getProxyUsername().foreach(conf.set("fs.gs.proxy.username", _))
      properties.getProxyPassword().foreach(conf.set("fs.gs.proxy.password", _))
    }

    conf
  }

  private def getKeyFilePath(): String =
    if (properties.containsKey(GCS_KEYFILE_PATH)) {
      properties.getString(GCS_KEYFILE_PATH)
    } else {
      val connectionName = properties.getString(CONNECTION_NAME)
      val jsonContent = getKeyfileContentFromConnection(connectionName)
      writeToTempFile(jsonContent)
    }

  private def getKeyfileContentFromConnection(connectionName: String): String = {
    def map = properties.getConnectionProperties(null)
    map
      .get(GCS_KEYFILE_CONTENT)
      .getOrElse {
        throw new IllegalArgumentException(
          ExaError
            .messageBuilder("E-CSE-32")
            .message(
              "The connection {{connectionName}} does not contain {{GCS_KEYFILE_CONTENT}} property.",
              connectionName,
              GCS_KEYFILE_CONTENT
            )
            .mitigation("Please check the connection properties.")
            .toString()
        )
      }
  }

  private def writeToTempFile(jsonContent: String): String = {
    val tempPath = Files.createTempFile("gcs-credentials", ".json")
    Files.writeString(tempPath, jsonContent, StandardCharsets.UTF_8)
    tempPath.toString
  }
}
