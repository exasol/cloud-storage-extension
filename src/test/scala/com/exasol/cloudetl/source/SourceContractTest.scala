package com.exasol.cloudetl.source

import java.io.File
import java.nio.file.Paths

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.scalatest.funsuite.AnyFunSuite

class SourceContractTest extends AnyFunSuite {

  private[this] val configuration1 = new Configuration(false)
  private[this] val configuration2 = new Configuration()
  private[this] val localFilesystem = FileSystem.get(configuration1)
  private[this] val s3aFilesystem = FileSystem.get(new Path("s3a://tmp").toUri(), configuration2)

  test("verify avro source implementation contract") {
    val avroSchemaString =
      s"""|{
          |  "type": "record",
          |  "namespace": "avro.types",
          |  "name": "basic",
          |  "fields": [{
          |    "name": "column",
          |    "type": "boolean"
          |  }]
          |}
        """.stripMargin
    val schema = new Schema.Parser().parse(avroSchemaString)
    val reader = new GenericDatumReader[GenericRecord](schema)
    val path = Paths.get(getClass().getResource("/data/import/avro").toURI())
    val dataReader1 = new DataFileReader[GenericRecord](new File(s"$path/sales10.avro"), reader)
    val dataReader2 = new DataFileReader[GenericRecord](new File(s"$path/sales11.avro"), reader)
    EqualsVerifier
      .forClass(classOf[AvroSource])
      .withPrefabValues(classOf[Configuration], configuration1, configuration2)
      .withPrefabValues(classOf[FileSystem], localFilesystem, s3aFilesystem)
      .withPrefabValues(classOf[DataFileReader[GenericRecord]], dataReader1, dataReader2)
      .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
      .verify()
  }

  test("verify orc source implementation contract") {
    EqualsVerifier
      .forClass(classOf[OrcSource])
      .withPrefabValues(classOf[Configuration], configuration1, configuration2)
      .withPrefabValues(classOf[FileSystem], localFilesystem, s3aFilesystem)
      .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
      .verify()
  }

}
