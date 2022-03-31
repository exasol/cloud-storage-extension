package com.exasol.cloudetl.scriptclasses

import com.exasol.ExaExportSpecification
import com.exasol.ExaImportSpecification
import com.exasol.ExaMetadata

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

/**
 * A trait with helper variables for import or export path test classes.
 */
trait PathTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

  private[scriptclasses] val schema = "myDBSchema"

  private[scriptclasses] var properties = Map(
    "BUCKET_PATH" -> "s3a://my-bucket/folder1/*",
    "DATA_FORMAT" -> "PARQUET",
    "S3_ENDPOINT" -> "s3.eu-central-1.com",
    "CONNECTION_NAME" -> "S3_CONNECTION"
  )

  private[scriptclasses] var metadata: ExaMetadata = _
  private[scriptclasses] var importSpec: ExaImportSpecification = _
  private[scriptclasses] var exportSpec: ExaExportSpecification = _

  @SuppressWarnings(Array("org.wartremover.contrib.warts.UnsafeInheritance"))
  override def beforeEach(): Unit = {
    metadata = mock[ExaMetadata]
    importSpec = mock[ExaImportSpecification]
    exportSpec = mock[ExaExportSpecification]
    ()
  }

}
