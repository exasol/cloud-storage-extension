package com.exasol.cloudetl.alluxio

import java.sql.ResultSet

import com.exasol.cloudetl.BaseIntegrationTest
import com.exasol.dbbuilder.dialects.Table
import com.exasol.matcher.ResultSetStructureMatcher.table

import com.dimafeng.testcontainers.GenericContainer
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

class AlluxioExportImportIT extends BaseIntegrationTest {

  val ALLUXIO_IMAGE = "alluxio/alluxio:2.9.2"
  val SCHEMA_NAME = "ALLUXIO_SCHEMA"

  val alluxioMainContainer =
    GenericContainer(ALLUXIO_IMAGE, exposedPorts = Seq(19998, 19999), command = Seq("master"))
      .configure { c =>
        c.withNetwork(network)
        c.withNetworkAliases("alluxio-main")
        c.withEnv("ALLUXIO_JAVA_OPTS", "-Dalluxio.master.hostname=alluxio-main")
        c.withReuse(true)
        ()
      }
  val alluxioWorkerContainer =
    GenericContainer(ALLUXIO_IMAGE, exposedPorts = Seq(29999, 30000), command = Seq("worker"))
      .configure { c =>
        c.withNetwork(network)
        c.withNetworkAliases("alluxio-worker")
        c.withEnv(
          "ALLUXIO_JAVA_OPTS",
          "-Dalluxio.master.hostname=alluxio-main " +
            "-Dalluxio.worker.container.hostname=alluxio-worker -Dalluxio.worker.ramdisk.size=64MB"
        )
        c.withSharedMemorySize(1024 * 1024 * 1024)
        c.dependsOn(alluxioMainContainer)
        c.withReuse(true)
        ()
      }

  override final def beforeAll(): Unit = {
    super.beforeAll()
    alluxioMainContainer.start()
    alluxioWorkerContainer.start()
    prepareExasolDatabase(SCHEMA_NAME)
  }

  override final def afterAll(): Unit = {
    alluxioMainContainer.stop()
    alluxioWorkerContainer.stop()
    super.afterAll()
  }

  test("alluxio filesystem export and import") {
    val exportedTable = schema
      .createTable("EXPORTED_ITEMS", "PRODUCT_ID", "DECIMAL(18,0)", "NAME", "VARCHAR(40)")
      .insert("1", "Cat food")
      .insert("2", "Toy mouse")
    val importedTable = schema
      .createTable("IMPORTED_ITEMS", "PRODUCT_ID", "DECIMAL(18,0)", "NAME", "VARCHAR(40)")
    prepareContainers("data")
    exportIntoAlluxio(exportedTable, "data")
    importIntoExasol(importedTable, "data")
    assertResultSet(
      importedTable,
      table()
        .row(java.lang.Long.valueOf(1), "Cat food")
        .row(java.lang.Long.valueOf(2), "Toy mouse")
        .matches()
    )
  }

  def assertResultSet(table: Table, matcher: Matcher[ResultSet]): Unit = {
    val resultSet = executeQuery(
      s"SELECT * FROM ${table.getFullyQualifiedName()} ORDER BY PRODUCT_ID ASC"
    )
    assertThat(resultSet, matcher)
    resultSet.close()
  }

  def prepareContainers(bucket: String): Unit = {
    val alluxioFsCmd = "/opt/alluxio/bin/alluxio fs"
    var exitCode = alluxioMainContainer.execInContainer("/bin/sh", "-c", s"$alluxioFsCmd mkdir /$bucket")
    if (exitCode.getExitCode() != 0) {
      throw new RuntimeException(s"Could not create '$bucket' folder in Alluxio container.")
    }
    exitCode = alluxioMainContainer.execInContainer("/bin/sh", "-c", s"$alluxioFsCmd chmod 777 /$bucket/")
    if (exitCode.getExitCode() != 0) {
      throw new RuntimeException(
        s"Could not change '$bucket' folder permissions in Alluxio container."
      )
    }
    val workerIPv4Address = getContainerIPv4Address(alluxioWorkerContainer)
    exitCode = exasolContainer.execInContainer(
      "/bin/sh",
      "-c",
      s"echo '$workerIPv4Address alluxio-worker' >> /etc/hosts"
    )
    if (exitCode.getExitCode() != 0) {
      throw new RuntimeException("Could not update `/etc/hosts` file in Exasol container.")
    }
  }

  def exportIntoAlluxio(table: Table, bucket: String): Unit =
    executeStmt(
      s"""|EXPORT ${table.getFullyQualifiedName()}
          |INTO SCRIPT $SCHEMA_NAME.EXPORT_PATH WITH
          |BUCKET_PATH = 'alluxio://${getContainerIPv4Address(alluxioMainContainer)}:19998/$bucket/'
          |DATA_FORMAT = 'PARQUET'
          |PARQUET_BLOCK_SIZE = '67108864'
          |PARALLELISM = 'iproc()';
      """.stripMargin
    )

  def importIntoExasol(table: Table, bucket: String): Unit =
    executeStmt(
      s"""|IMPORT INTO ${table.getFullyQualifiedName()}
          |FROM SCRIPT $SCHEMA_NAME.IMPORT_PATH WITH
          |BUCKET_PATH = 'alluxio://${getContainerIPv4Address(alluxioMainContainer)}:19998/$bucket/'
          |DATA_FORMAT = 'PARQUET'
          |PARALLELISM = 'nproc()';
        """.stripMargin
    )

  private[this] def getContainerIPv4Address(container: GenericContainer): String =
    container.containerInfo
      .getNetworkSettings()
      .getNetworks()
      .values()
      .iterator()
      .next()
      .getIpAddress()

}
