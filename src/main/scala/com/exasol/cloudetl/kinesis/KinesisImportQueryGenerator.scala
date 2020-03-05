package com.exasol.cloudetl.kinesis

import scala.collection.JavaConverters._

import com.exasol.{ExaImportSpecification, ExaMetadata}
import com.exasol.cloudetl.kinesis.KinesisConstants._

/**
 * This object returns an SQL query for an EXASOL IMPORT FROM SCRIPT statement.
 */
object KinesisImportQueryGenerator {

  /** Provides a SELECT query for IMPORT.
   *
   * This is a function which is called inside Exasol when an IMPORT query refers to a script
   * based on this object.
   */
  def generateSqlForImportSpec(
    exaMetadata: ExaMetadata,
    importSpecification: ExaImportSpecification
  ): String = {
    val kinesisUserProperties = new KinesisUserProperties(
      importSpecification.getParameters.asScala.toMap
    )
    val tableName = kinesisUserProperties.getTableName
    val innerUdfProperties =
      kinesisUserProperties.createSelectedPropertiesMap(
        KinesisUserProperties.AWS_ACCESS_KEY_PROPERTY,
        KinesisUserProperties.AWS_SECRET_KEY_PROPERTY,
        KinesisUserProperties.AWS_SESSION_TOKEN_PROPERTY,
        KinesisUserProperties.REGION_PROPERTY,
        KinesisUserProperties.STREAM_NAME_PROPERTY
      )
    val innerUdfPropertiesString = kinesisUserProperties.mkString(innerUdfProperties)
    s"""SELECT KINESIS_IMPORT(
       |  '$innerUdfPropertiesString',
       |  $KINESIS_SHARD_ID_COLUMN_NAME,
       |  $SHARD_SEQUENCE_NUMBER_COLUMN_NAME
       |)
       |FROM (
       |  SELECT KINESIS_METADATA('$innerUdfPropertiesString',
       |  $KINESIS_SHARD_ID_COLUMN_NAME, $SHARD_SEQUENCE_NUMBER_COLUMN_NAME)
       |  FROM (SELECT
       |  $KINESIS_SHARD_ID_COLUMN_NAME,
       |  MAX($SHARD_SEQUENCE_NUMBER_COLUMN_NAME) AS $SHARD_SEQUENCE_NUMBER_COLUMN_NAME
       |  FROM $tableName
       |  GROUP BY $KINESIS_SHARD_ID_COLUMN_NAME
       |  UNION ALL
       |  SELECT TO_CHAR(0), TO_CHAR(0)
       |  FROM DUAL
       |  WHERE NOT EXISTS (SELECT * FROM $tableName LIMIT 2)
       |  )
       |)
       |GROUP BY $KINESIS_SHARD_ID_COLUMN_NAME;
       |""".stripMargin
  }
}
