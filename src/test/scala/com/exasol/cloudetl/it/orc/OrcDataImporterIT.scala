package com.exasol.cloudetl.orc

import java.nio.charset.StandardCharsets.UTF_8
import java.sql.Timestamp

import com.exasol.cloudetl.BaseDataImporter
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription

class OrcDataImporterIT extends BaseDataImporter {

  override val schemaName = "ORC_SCHEMA"
  override val bucketName = "orc-bucket"
  override val dataFormat = "orc"
  var conf = new Configuration

  test("imports boolean") {
    OrcChecker("struct<f:boolean>", "BOOLEAN", "boolean_table")
      .withInputValues(List(true, false, null))
      .assertResultSet(
        table()
          .row(java.lang.Boolean.TRUE)
          .row(java.lang.Boolean.FALSE)
          .row(null)
          .matches()
      )
  }

  test("imports byte") {
    OrcChecker("struct<f:tinyint>", "DECIMAL(3,0)", "byte_table")
      .withInputValues(List(11, null))
      .assertResultSet(
        table().row(java.lang.Byte.valueOf("11")).row(null).matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports short") {
    OrcChecker("struct<f:smallint>", "DECIMAL(9,0)", "short_table")
      .withInputValues(List(13, null))
      .assertResultSet(
        table().row(java.lang.Short.valueOf("13")).row(null).matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int") {
    OrcChecker("struct<f:int>", "DECIMAL(10,0)", "int_table")
      .withInputValues(List(INT_MIN, 999, null, INT_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Integer.valueOf(INT_MIN))
          .row(java.lang.Integer.valueOf(999))
          .row(null)
          .row(java.lang.Integer.valueOf(INT_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports long") {
    OrcChecker("struct<f:bigint>", "DECIMAL(19,0)", "long_table")
      .withInputValues(List(LONG_MIN, 1234L, null, LONG_MAX))
      .assertResultSet(
        table()
          .row(java.lang.Long.valueOf(LONG_MIN))
          .row(java.lang.Long.valueOf(1234))
          .row(null)
          .row(java.lang.Long.valueOf(LONG_MAX))
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports float") {
    val EPS = java.math.BigDecimal.valueOf(0.0001)
    OrcChecker("struct<f:float>", "FLOAT", "float_table")
      .withInputValues(List(3.14f, null))
      .assertResultSet(
        table()
          .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
          .row(null)
          .matches()
      )
  }

  test("imports double") {
    OrcChecker("struct<f:double>", "DOUBLE", "double_table")
      .withInputValues(List(2.71, null))
      .assertResultSet(table().row(java.lang.Double.valueOf(2.71)).row(null).matches())
  }

  // scalastyle:off nonascii
  test("imports char") {
    OrcChecker("struct<f:char(3)>", "VARCHAR(3)", "char_table")
      .withInputValues(List("a✅a", null))
      .assertResultSet(table().row("a✅a").row(null).matches())
  }
  // scalastyle:on

  test("imports varchar") {
    OrcChecker("struct<f:varchar(5)>", "VARCHAR(5)", "varchar_table")
      .withInputValues(List("hello", "world", null))
      .assertResultSet(table().row("hello").row("world").row(null).matches())
  }

  // scalastyle:off nonascii
  test("imports binary") {
    OrcChecker("struct<f:string>", "VARCHAR(10)", "binary_table")
      .withInputValues(List("中文", null))
      .assertResultSet(table().row("中文").row(null).matches())
  }
  // scalastyle:on

  test("imports string") {
    OrcChecker("struct<f:string>", "VARCHAR(10)", "string_table")
      .withInputValues(List("value", null))
      .assertResultSet(table().row("value").row(null).matches())
  }

  test("imports decimal") {
    OrcChecker("struct<f:decimal(6,3)>", "DECIMAL(6,3)", "decimal_table")
      .withInputValues(List("333.333", "0.666", null))
      .assertResultSet(
        table()
          .row(java.lang.Double.valueOf(333.333))
          .row(java.lang.Double.valueOf(0.666))
          .row(null)
          .matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports date") {
    OrcChecker("struct<f:date>", "DATE", "date_table")
      .withInputValues(List(0, 1, null))
      .assertResultSet(
        table()
          .row(java.sql.Date.valueOf("1970-01-01"))
          .row(java.sql.Date.valueOf("1970-01-02"))
          .row(null)
          .matches()
      )
  }

  test("imports timestamp") {
    val timestamp1 = Timestamp.from(java.time.Instant.EPOCH)
    val timestamp2 = new Timestamp(System.currentTimeMillis())
    OrcChecker("struct<f:timestamp>", "TIMESTAMP", "timestamp_table")
      .withInputValues(List(timestamp1, timestamp2, null))
      .assertResultSet(
        table()
          .row(timestamp1)
          .row(timestamp2)
          .row(null)
          .matches()
      )
  }

  test("imports list of strings") {
    OrcChecker("struct<f:array<string>>", "VARCHAR(30)", "list_strings")
      .withInputValues(List(Seq("Brandon", "Eddard", null)))
      .assertResultSet(table().row("""["Brandon","Eddard",null]""").matches())
  }

  test("imports list of doubles") {
    OrcChecker("struct<f:array<double>>", "VARCHAR(20)", "list_doubles")
      .withInputValues(List(Seq(1.11, 2.22, null)))
      .assertResultSet(table().row("[1.11,2.22,null]").matches())
  }

  test("imports list of ints") {
    OrcChecker("struct<f:array<int>>", "VARCHAR(10)", "list_ints")
      .withInputValues(List(Seq(3, 7, 1)))
      .assertResultSet(table().row("[3,7,1]").matches())
  }

  test("imports empty list") {
    OrcChecker("struct<f:array<string>>", "VARCHAR(10)", "empty_list")
      .withInputValues(List(null))
      .assertResultSet(table().row("[]").matches())
  }

  test("imports list of lists") {
    OrcChecker("struct<f:array<array<int>>>", "VARCHAR(30)", "list_lists")
      .withInputValues(List(Seq(List(314, 271), List(1, 2, 4))))
      .assertResultSet(table().row("[[314,271],[1,2,4]]").matches())
  }

  test("imports list of maps") {
    OrcChecker("struct<f:array<map<string,int>>>", "VARCHAR(30)", "list_map")
      .withInputValues(List(Seq(Map("p" -> 314), Map("a" -> 1, "b" -> 2))))
      .assertResultSet(table().row("""[{"p":314},{"a":1,"b":2}]""").matches())
  }

  test("imports map") {
    OrcChecker("struct<f:map<string,int>>", "VARCHAR(30)", "map_table")
      .withInputValues(List(Map("a" -> 3, "b" -> 5, "c" -> 7)))
      .assertResultSet(table().row("""{"a":3,"b":5,"c":7}""").matches())
  }

  test("imports map with list values") {
    val input = Map("consts" -> List(3.14, 2.71), "nums" -> List(1.0, 0.5))
    OrcChecker("struct<f:map<string,array<double>>>", "VARCHAR(40)", "map_list_values")
      .withInputValues(List(input))
      .assertResultSet(table().row("""{"nums":[1.0,0.5],"consts":[3.14,2.71]}""").matches())
  }

  test("imports nested struct") {
    val input: Map[String, Any] = Map("name" -> "Jon", "phoneNumber" -> 1337, "pets" -> Seq("cat", "direwolf"))
    OrcChecker(
      "struct<f:struct<name:string,phoneNumber:int,pets:array<string>>>",
      "VARCHAR(60)",
      "nested_struct_table"
    ).withInputValues(List(input))
      .assertResultSet(
        table()
          .row("""{"pets":["cat","direwolf"],"phoneNumber":1337,"name":"Jon"}""")
          .matches()
      )
  }

  test("imports union") {
    val orcType = "struct<f:uniontype<int,string>>"
    val orcSchema = TypeDescription.fromString(orcType)
    val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(orcSchema))
    val batch = orcSchema.createRowBatch()
    batch.size = 3
    val unionVector = batch.cols(0).asInstanceOf[UnionColumnVector]
    unionVector.noNulls = false
    // Set string type for the first row
    unionVector.tags(1) = 0
    unionVector.fields(1).asInstanceOf[BytesColumnVector].setVal(0, "str".getBytes(UTF_8))
    // Set int type for the second row
    unionVector.tags(0) = 1
    unionVector.fields(0).asInstanceOf[LongColumnVector].vector(1) = 23
    // Set null for the third row
    unionVector.isNull(2) = true
    writer.addRowBatch(batch)
    writer.close()

    OrcChecker(orcType, "VARCHAR(30)", "union_table")
      .assertResultSet(
        table()
          .row("""{"STRING":"str","INT":null}""")
          .row("""{"STRING":null,"INT":23}""")
          .row("""{"STRING":null,"INT":null}""")
          .matches()
      )
  }

  case class OrcChecker(orcColumn: String, exaColumn: String, tableName: String)
      extends AbstractChecker(exaColumn, tableName)
      with OrcTestDataWriter {
    val orcSchema = TypeDescription.fromString(orcColumn)

    def withInputValues[T](values: List[T]): OrcChecker = {
      writeDataValues(values, path, orcSchema)
      this
    }
  }

}
