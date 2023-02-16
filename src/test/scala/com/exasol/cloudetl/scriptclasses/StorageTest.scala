package com.exasol.cloudetl.scriptclasses

import java.nio.file.Path
import java.nio.file.Paths

import com.exasol.ExaIterator
import com.exasol.cloudetl.storage.StorageProperties

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

/**
 * A trait with helper methods and default values for storage test
 * classes.
 */
trait StorageTest extends AnyFunSuite with MockitoSugar {

  private[scriptclasses] val testResourceDir: String =
    normalize(Paths.get(getClass.getResource("/data").toURI))

  private[scriptclasses] val testResourceParquetPath: String =
    s"$testResourceDir/import/parquet/sales_pos*.parquet"

  final def normalize(path: Path): String =
    path.toUri.toString.replaceAll("/$", "").replaceAll("///", "/")

  final def mockExasolIterator(params: Map[String, String]): ExaIterator = {
    val storageProperties = StorageProperties(params)
    val bucketPath = storageProperties.getStoragePath()

    val mockedIterator = mock[ExaIterator]
    when(mockedIterator.getString(0)).thenReturn(bucketPath)
    when(mockedIterator.getString(1)).thenReturn(storageProperties.mkString())
    mockedIterator
  }

  final def anyObjects(): Array[Object] =
    any(classOf[Array[Object]])

}
