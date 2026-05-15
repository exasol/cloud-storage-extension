package com.exasol.cloudetl.alluxio;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.*;
import org.testcontainers.utility.DockerImageName;

import com.exasol.cloudetl.BaseIntegrationTest;
import com.exasol.dbbuilder.dialects.Table;

class AlluxioExportImportIT extends BaseIntegrationTest {
    private static final String ALLUXIO_IMAGE = "alluxio/alluxio:300";
    private static final String SCHEMA_NAME = "ALLUXIO_SCHEMA";
    private final GenericContainer<?> alluxioMainContainer = new GenericContainer<>(DockerImageName.parse(ALLUXIO_IMAGE))
            .withExposedPorts(19998, 19999).withCommand("master").withNetwork(this.network)
            .withNetworkAliases("alluxio-main")
            .withEnv("ALLUXIO_JAVA_OPTS", "-Dalluxio.master.hostname=alluxio-main").withReuse(true);
    private final GenericContainer<?> alluxioWorkerContainer = new GenericContainer<>(DockerImageName.parse(ALLUXIO_IMAGE))
            .withExposedPorts(29999, 30000).withCommand("worker").withNetwork(this.network)
            .withNetworkAliases("alluxio-worker")
            .withEnv("ALLUXIO_JAVA_OPTS", "-Dalluxio.master.hostname=alluxio-main "
                    + "-Dalluxio.worker.container.hostname=alluxio-worker -Dalluxio.worker.ramdisk.size=64MB")
            .withSharedMemorySize(1024L * 1024L * 1024L).dependsOn(this.alluxioMainContainer).withReuse(true);

    @BeforeAll
    void beforeAll() {
        this.alluxioMainContainer.start();
        this.alluxioWorkerContainer.start();
        prepareExasolDatabase(SCHEMA_NAME);
    }

    @AfterAll
    void afterAll() {
        this.alluxioMainContainer.stop();
        this.alluxioWorkerContainer.stop();
    }

    @Test
    void alluxioFilesystemExportAndImport() throws SQLException, InterruptedException, java.io.IOException {
        final Table exportedTable = this.schema
                .createTable("EXPORTED_ITEMS", "PRODUCT_ID", "DECIMAL(18,0)", "NAME", "VARCHAR(40)")
                .insert("1", "Cat food").insert("2", "Toy mouse");
        final Table importedTable = this.schema.createTable("IMPORTED_ITEMS", "PRODUCT_ID", "DECIMAL(18,0)", "NAME",
                "VARCHAR(40)");
        prepareContainers("data");
        exportIntoAlluxio(exportedTable, "data");
        importIntoExasol(importedTable, "data");
        assertResultSet(importedTable,
                table().row(Long.valueOf(1), "Cat food").row(Long.valueOf(2), "Toy mouse").matches());
    }

    private void assertResultSet(final Table table, final Matcher<ResultSet> matcher) throws SQLException {
        try (ResultSet resultSet = executeQuery(
                "SELECT * FROM " + table.getFullyQualifiedName() + " ORDER BY PRODUCT_ID ASC")) {
            assertThat(resultSet, matcher);
        }
    }

    private void prepareContainers(final String bucket) throws UnsupportedOperationException, InterruptedException,
            java.io.IOException {
        final String alluxioFsCmd = "/opt/alluxio/bin/alluxio fs";
        Container.ExecResult exitCode = this.alluxioMainContainer.execInContainer("/bin/sh", "-c",
                alluxioFsCmd + " mkdir /" + bucket);
        if (exitCode.getExitCode() != 0) {
            throw new IllegalStateException("Could not create '" + bucket + "' folder in Alluxio container.");
        }
        exitCode = this.alluxioMainContainer.execInContainer("/bin/sh", "-c",
                alluxioFsCmd + " chmod 777 /" + bucket + "/");
        if (exitCode.getExitCode() != 0) {
            throw new IllegalStateException("Could not change '" + bucket + "' folder permissions in Alluxio container.");
        }
        final String workerIPv4Address = getContainerIPv4Address(this.alluxioWorkerContainer);
        exitCode = this.exasolContainer.execInContainer("/bin/sh", "-c",
                "echo '" + workerIPv4Address + " alluxio-worker' >> /etc/hosts");
        if (exitCode.getExitCode() != 0) {
            throw new IllegalStateException("Could not update `/etc/hosts` file in Exasol container.");
        }
    }

    private void exportIntoAlluxio(final Table table, final String bucket) {
        executeStmt(String.format("EXPORT %s%n"
                + "INTO SCRIPT %s.EXPORT_PATH WITH%n"
                + "BUCKET_PATH = 'alluxio://%s:19998/%s/'%n"
                + "DATA_FORMAT = 'PARQUET'%n"
                + "PARQUET_BLOCK_SIZE = '67108864'%n"
                + "PARALLELISM = 'iproc()';%n", table.getFullyQualifiedName(), SCHEMA_NAME,
                getContainerIPv4Address(this.alluxioMainContainer), bucket));
    }

    private void importIntoExasol(final Table table, final String bucket) {
        executeStmt(String.format("IMPORT INTO %s%n"
                + "FROM SCRIPT %s.IMPORT_PATH WITH%n"
                + "BUCKET_PATH = 'alluxio://%s:19998/%s/'%n"
                + "DATA_FORMAT = 'PARQUET'%n"
                + "PARALLELISM = 'nproc()';%n", table.getFullyQualifiedName(), SCHEMA_NAME,
                getContainerIPv4Address(this.alluxioMainContainer), bucket));
    }

    private String getContainerIPv4Address(final GenericContainer<?> container) {
        return container.getContainerInfo().getNetworkSettings().getNetworks().values().iterator().next().getIpAddress();
    }
}
