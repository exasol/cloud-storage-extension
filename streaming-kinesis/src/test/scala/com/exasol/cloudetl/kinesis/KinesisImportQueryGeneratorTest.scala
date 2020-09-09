package com.exasol.cloudetl.kinesis

import scala.collection.JavaConverters._

import com.exasol.{ExaImportSpecification, ExaMetadata}
import com.exasol.cloudetl.kinesis.KinesisConstants._

import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class KinesisImportQueryGeneratorTest
    extends AnyFunSuite
    with BeforeAndAfterAll
    with MockitoSugar {
  private[this] var exaMetadata: ExaMetadata = _
  private[this] var importSpecification: ExaImportSpecification = _
  private[this] val kinesisProperties = Map(
    "TABLE_NAME" -> "TEST_TABLE",
    "CONNECTION_NAME" -> "MY_CONNECTION",
    "STREAM_NAME" -> "Test_stream",
    "REGION" -> "eu-west-1"
  )

  override final def beforeAll(): Unit = {
    exaMetadata = mock[ExaMetadata]
    importSpecification = mock[ExaImportSpecification]
    val _ = when(importSpecification.getParameters).thenReturn(kinesisProperties.asJava)
  }

  test("generateSqlForImportSpec returns SQL query") {
    val propertiesAsString =
      """CONNECTION_NAME -> MY_CONNECTION;
        |REGION -> eu-west-1;
        |STREAM_NAME -> Test_stream;
        |TABLE_NAME -> TEST_TABLE""".stripMargin.replace("\n", "")
    val expected =
      s"""SELECT KINESIS_IMPORT(
         |  '$propertiesAsString',
         |  $KINESIS_SHARD_ID_COLUMN_NAME,
         |  $SHARD_SEQUENCE_NUMBER_COLUMN_NAME
         |)
         |FROM (
         |  SELECT KINESIS_METADATA('$propertiesAsString',
         |  $KINESIS_SHARD_ID_COLUMN_NAME, $SHARD_SEQUENCE_NUMBER_COLUMN_NAME)
         |  FROM (SELECT
         |  $KINESIS_SHARD_ID_COLUMN_NAME,
         |  MAX($SHARD_SEQUENCE_NUMBER_COLUMN_NAME) AS $SHARD_SEQUENCE_NUMBER_COLUMN_NAME
         |  FROM TEST_TABLE
         |  GROUP BY $KINESIS_SHARD_ID_COLUMN_NAME
         |  UNION ALL
         |  SELECT TO_CHAR(0), TO_CHAR(0)
         |  FROM DUAL
         |  WHERE NOT EXISTS (SELECT * FROM TEST_TABLE LIMIT 2)
         |  )
         |)
         |GROUP BY $KINESIS_SHARD_ID_COLUMN_NAME;
         |""".stripMargin
    assert(
      KinesisImportQueryGenerator
        .generateSqlForImportSpec(exaMetadata, importSpecification) === expected
    )
  }
}
