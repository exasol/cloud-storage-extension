package com.exasol.cloudetl.orc

import java.io.File
import java.nio.file.Path
import java.sql.ResultSet
import java.sql.Timestamp

import com.exasol.cloudetl.BaseIntegrationTest
import com.exasol.cloudetl.TestFileManager
import com.exasol.matcher.CellMatcherFactory
import com.exasol.matcher.ResultSetStructureMatcher.table
import com.exasol.matcher.TypeMatchMode._

import com.amazonaws.services.s3.model._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.hadoop.fs.{Path => HPath}
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.scalatest.BeforeAndAfterEach

class OrcDataImporterIT extends BaseIntegrationTest with BeforeAndAfterEach with TestFileManager {

  val schemaName = "ORC_SCHEMA"
  val tableName = "ORC_TABLE"
  val bucketName: String = "orcbucket"
  var outputDirectory: Path = _
  var conf: Configuration = _
  var path: HPath = _
  var s3: AmazonS3 = _

  override final def beforeAll(): Unit = {
    startContainers()
    prepareExasolDatabase(schemaName)
    s3 = AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(s3Container.getEndpointConfiguration(S3))
      .withCredentials(s3Container.getDefaultCredentialsProvider())
      .disableChunkedEncoding()
      .build()
    conf = new Configuration
  }

  override final def beforeEach(): Unit = {
    outputDirectory = createTemporaryFolder("orc-tests-")
    path = new HPath(outputDirectory.toUri.toString, "orc-00000.orc")
    ()
  }

  override final def afterEach(): Unit = {
    deletePathFiles(outputDirectory)
    execute(s"DROP TABLE IF EXISTS ${getTableName()}")
  }

  def getTableName(): String = s""""$schemaName"."$tableName""""

  test("imports boolean") {
    OrcChecker("struct<f:boolean>", "BOOLEAN")
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
    OrcChecker("struct<f:tinyint>", "DECIMAL(9,0)")
      .withInputValues(List(11, null))
      .assertResultSet(
        table().row(java.lang.Byte.valueOf("11")).row(null).matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports short") {
    OrcChecker("struct<f:smallint>", "DECIMAL(9,0)")
      .withInputValues(List(13, null))
      .assertResultSet(
        table().row(java.lang.Short.valueOf("13")).row(null).matches(NO_JAVA_TYPE_CHECK)
      )
  }

  test("imports int") {
    OrcChecker("struct<f:int>", "DECIMAL(9,0)")
      .withInputValues(List(999, null))
      .assertResultSet(table().row(java.lang.Integer.valueOf(999)).row(null).matches())
  }

  test("imports long") {
    OrcChecker("struct<f:bigint>", "DECIMAL(18,0)")
      .withInputValues(List(1234L, null))
      .assertResultSet(table().row(java.lang.Long.valueOf(1234)).row(null).matches())
  }

  test("imports float") {
    val EPS = java.math.BigDecimal.valueOf(0.0001)
    OrcChecker("struct<f:float>", "FLOAT")
      .withInputValues(List(3.14F, null))
      .assertResultSet(
        table()
          .row(CellMatcherFactory.cellMatcher(3.14, STRICT, EPS))
          .row(null)
          .matches()
      )
  }

  test("imports double") {
    OrcChecker("struct<f:double>", "DOUBLE")
      .withInputValues(List(2.71, null))
      .assertResultSet(table().row(java.lang.Double.valueOf(2.71)).row(null).matches())
  }

  test("imports char") {
    OrcChecker("struct<f:char(3)>", "VARCHAR(3)")
      .withInputValues(List("a✅a", null))
      .assertResultSet(table().row("a✅a").row(null).matches())
  }

  test("imports varchar") {
    OrcChecker("struct<f:varchar(5)>", "VARCHAR(5)")
      .withInputValues(List("hello", "world", null))
      .assertResultSet(table().row("hello").row("world").row(null).matches())
  }

  test("imports binary") {
    OrcChecker("struct<f:string>", "VARCHAR(10)")
      .withInputValues(List("中文", null))
      .assertResultSet(table().row("中文").row(null).matches())
  }

  test("imports string") {
    OrcChecker("struct<f:string>", "VARCHAR(10)")
      .withInputValues(List("value", null))
      .assertResultSet(table().row("value").row(null).matches())
  }

  test("imports decimal") {
    OrcChecker("struct<f:decimal(6,3)>", "DECIMAL(6,3)")
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
    OrcChecker("struct<f:date>", "DATE")
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
    OrcChecker("struct<f:timestamp>", "TIMESTAMP")
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
    OrcChecker("struct<f:array<string>>", "VARCHAR(30)")
      .withInputValues(List(Seq("Brandon", "Eddard", null)))
      .assertResultSet(table().row("""["Brandon","Eddard",null]""").matches())
  }

  test("imports list of doubles") {
    OrcChecker("struct<f:array<double>>", "VARCHAR(20)")
      .withInputValues(List(Seq(1.11, 2.22, null)))
      .assertResultSet(table().row("[1.11,2.22,null]").matches())
  }

  test("imports list of ints") {
    OrcChecker("struct<f:array<int>>", "VARCHAR(10)")
      .withInputValues(List(Seq(3, 7, 1)))
      .assertResultSet(table().row("[3,7,1]").matches())
  }

  test("imports empty list") {
    OrcChecker("struct<f:array<string>>", "VARCHAR(10)")
      .withInputValues(List(null))
      .assertResultSet(table().row("[]").matches())
  }

  test("imports list of lists") {
    OrcChecker("struct<f:array<array<int>>>", "VARCHAR(30)")
      .withInputValues(List(Seq(List(314, 271), List(1, 2, 4))))
      .assertResultSet(table().row("[[314,271],[1,2,4]]").matches())
  }

  test("imports list of maps") {
    OrcChecker("struct<f:array<map<string,int>>>", "VARCHAR(30)")
      .withInputValues(List(Seq(Map("p" -> 314), Map("a" -> 1, "b" -> 2))))
      .assertResultSet(table().row("""[{"p":314},{"b":2,"a":1}]""").matches())
  }

  test("imports map") {
    OrcChecker("struct<f:map<string,int>>", "VARCHAR(30)")
      .withInputValues(List(Map("a" -> 3, "b" -> 5, "c" -> 7)))
      .assertResultSet(table().row("""{"b":5,"a":3,"c":7}""").matches())
  }

  test("imports map with list values") {
    val input = Map("consts" -> List(3.14, 2.71), "nums" -> List(1.0, 0.5))
    OrcChecker("struct<f:map<string,array<double>>>", "VARCHAR(40)")
      .withInputValues(List(input))
      .assertResultSet(table().row("""{"consts":[3.14,2.71],"nums":[1.0,0.5]}""").matches())
  }

  test("imports nested struct") {
    val input: Map[String, Any] =
      Map("name" -> "Jon", "phoneNumber" -> 1337, "pets" -> Seq("cat", "direwolf"))
    OrcChecker("struct<f:struct<name:string,phoneNumber:int,pets:array<string>>>", "VARCHAR(60)")
      .withInputValues(List(input))
      .assertResultSet(
        table()
          .row("""{"phoneNumber":1337,"name":"Jon","pets":["cat","direwolf"]}""")
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
    unionVector.fields(1).asInstanceOf[BytesColumnVector].setVal(0, "str".getBytes("UTF-8"))
    // Set int type for the second row
    unionVector.tags(0) = 1
    unionVector.fields(0).asInstanceOf[LongColumnVector].vector(1) = 23
    // Set null for the third row
    unionVector.isNull(2) = true
    writer.addRowBatch(batch)
    writer.close()

    OrcChecker(orcType, "VARCHAR(30)")
      .assertResultSet(
        table()
          .row("""{"INT":null,"STRING":"str"}""")
          .row("""{"INT":23,"STRING":null}""")
          .row("""{"INT":null,"STRING":null}""")
          .matches()
      )
  }

  case class OrcChecker(orcColumnType: String, exasolColumnType: String) {

    def withOrcWriter(block: OrcTestDataWriter => Unit): OrcChecker = {
      val writer = new OrcTestDataWriter(path, conf)
      block(writer)
      this
    }

    def withInputValues[T](values: List[T]): OrcChecker = withOrcWriter { writer =>
      val schema = TypeDescription.fromString(orcColumnType)
      writer.write(schema, values)
    }

    def withResultSet(block: ResultSet => Unit): OrcChecker = {
      uploadDataFile()
      importIntoExasol()
      val rs = query(s"SELECT * FROM ${getTableName()}")
      block(rs)
      rs.close()
      this
    }

    def assertResultSet(matcher: Matcher[ResultSet]): Unit = {
      withResultSet(assertThat(_, matcher))
      ()
    }

    private[this] def uploadDataFile(): Unit = {
      s3.createBucket(new CreateBucketRequest(bucketName))
      val request = new PutObjectRequest(bucketName, path.getName(), new File(path.toUri()))
      s3.putObject(request)
      ()
    }

    private[this] def importIntoExasol(): Unit = {
      val table = schema.createTableBuilder(tableName).column("COLUMN", exasolColumnType).build()
      Thread.sleep(3 * 1000)
      val s3Endpoint = s3Container
        .getEndpointConfiguration(S3)
        .getServiceEndpoint()
        .replaceAll("127.0.0.1", "172.17.0.1")
      execute(
        s"""|IMPORT INTO ${table.getFullyQualifiedName()}
            |FROM SCRIPT $schemaName.IMPORT_PATH WITH
            |BUCKET_PATH     = 's3a://$bucketName/${path.getName()}'
            |DATA_FORMAT     = 'ORC'
            |S3_ENDPOINT     = '$s3Endpoint'
            |CONNECTION_NAME = 'S3_CONNECTION'
            |PARALLELISM     = 'nproc()';
        """.stripMargin
      )
    }

  }

}
