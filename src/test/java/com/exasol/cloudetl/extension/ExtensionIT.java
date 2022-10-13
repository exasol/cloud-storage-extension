package com.exasol.cloudetl.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.FileNotFoundException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.ExtensionsResponseExtension;
import com.exasol.extensionmanager.client.model.InstallationsResponseInstallation;
import com.exasol.extensionmanager.itest.ExtensionManagerClient;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.matcher.TypeMatchMode;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import junit.framework.AssertionFailedError;

class ExtensionIT {
    private static final Logger LOGGER = Logger.getLogger(ExtensionIT.class.getName());
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static final String S3_CONNECTION = "S3_CONNECTION";
    private static final Path ADAPTER_JAR = getAdapterJar();

    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static ExtensionManagerClient client;
    private static S3Setup s3setup;
    private static Connection connection;
    private static ExasolObjectFactory exasolObjectFactory;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException, SQLException {
        exasolTestSetup = new ExasolTestSetupFactory(Paths.get("no-cloud-setup")).getTestSetup();
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist/cloud-storage-extension.js")));
        exasolTestSetup.getDefaultBucket().uploadFile(ADAPTER_JAR, ADAPTER_JAR.getFileName().toString());
        client = setup.client();
        s3setup = S3Setup.create();
        connection = exasolTestSetup.createConnection();
        exasolObjectFactory = new ExasolObjectFactory(connection, ExasolObjectConfiguration.builder().build());
        exasolObjectFactory.createConnectionDefinition(S3_CONNECTION, "", "dummy_user", s3setup.getSecret());
    }

    private static Path getAdapterJar() {
        final Path jar = Paths.get("target").resolve("exasol-cloud-storage-extension-2.5.1.jar").toAbsolutePath();
        if (Files.exists(jar)) {
            return jar;
        } else {
            throw new AssertionFailedError("Adapter jar " + jar + " does not exist. Run mvn package.");
        }
    }

    @AfterAll
    static void teardown() throws Exception {
        if (connection != null) {
            connection.close();
        }
        if (setup != null) {
            setup.close();
        }
        exasolTestSetup.getDefaultBucket().deleteFileNonBlocking(ADAPTER_JAR.getFileName().toString());
        exasolTestSetup.close();
        s3setup.close();
    }

    @AfterEach
    void cleanup() throws SQLException {
        setup.cleanup();
    }

    @Test
    void listExtensions() {
        final List<ExtensionsResponseExtension> extensions = client.getExtensions();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("Cloud Storage Extension")),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).getName(), equalTo(PROJECT_VERSION)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isLatest(), is(true)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isDeprecated(), is(false)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Access data formatted with Avro, Orc and Parquet on public cloud storage systems")));
    }

    @Test
    void getInstallationsReturnsEmptyList() {
        assertThat(client.getInstallations(), hasSize(0));
    }

    @Test
    void getInstallationsReturnsResult() {
        client.install();
        assertThat(client.getInstallations(), contains(
                new InstallationsResponseInstallation().name("Cloud Storage Extension").version(PROJECT_VERSION)));
    }

    @Test
    void installingWrongVersionFails() {
        client.assertRequestFails(() -> client.install("wrongVersion"),
                equalTo("Installing version 'wrongVersion' not supported, try '" + PROJECT_VERSION + "'."),
                equalTo(400));
        setup.exasolMetadata().assertNoScripts();
    }

    @Test
    void installCreatesScripts() {
        setup.client().install();
        assertScriptsInstalled();
    }

    @Test
    void installingTwiceCreatesScripts() {
        setup.client().install();
        setup.client().install();
        assertScriptsInstalled();
    }

    @Test
    void exportImportWorksAfterInstallation() throws SQLException {
        setup.client().install();
        verifyExportImportWorks();
    }

    @Test
    void listingInstancesNotSupported() {
        client.assertRequestFails(() -> client.listInstances(), equalTo("Finding instances not supported"),
                equalTo(404));
    }

    private void verifyExportImportWorks() throws SQLException {
        final ExasolSchema schema = exasolObjectFactory.createSchema("TESTING_SCHEMA_" + System.currentTimeMillis());
        final String bucket = s3setup.createBucket();
        try {
            final Table sourceTable = schema.createTable("SRC_TABLE", "ID", "INTEGER", "NAME", "VARCHAR(10)");
            final Table targetTable = schema.createTable("TARGET_TABLE", "ID", "INTEGER", "NAME", "VARCHAR(10)");
            sourceTable.insert(1, "a").insert(2, "b").insert(3, "c");
            exportIntoS3(sourceTable, bucket, "data");
            importFromS3IntoExasol(targetTable, bucket, "data/*");
            try (var statement = connection
                    .prepareStatement("select * from " + targetTable.getFullyQualifiedName() + " order by 1")) {
                assertThat(statement.executeQuery(),
                        table().row(1, "a").row(2, "b").row(3, "c").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
            }
        } finally {
            schema.drop();
            s3setup.deleteBucket(bucket);
        }
    }

    private void importFromS3IntoExasol(final Table table, final String bucket, final String file) throws SQLException {
        executeStatement("IMPORT INTO " + table.getFullyQualifiedName() + "\n" //
                + "FROM SCRIPT " + ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".IMPORT_PATH WITH\n" //
                + "BUCKET_PATH              = 's3a://" + bucket + "/" + file + "'\n" //
                + "DATA_FORMAT              = 'PARQUET'\n" //
                + "S3_ENDPOINT              = '" + s3setup.getS3Endpoint() + "'\n" //
                + "S3_CHANGE_DETECTION_MODE = 'none'\n" //
                + "TRUNCATE_STRING          = 'true'\n" //
                + "CONNECTION_NAME          = '" + S3_CONNECTION + "'\n" //
                + "PARALLELISM              = 'nproc()'\n");
    }

    private void exportIntoS3(final Table table, final String bucket, final String s3Path) {
        executeStatement("EXPORT " + table.getFullyQualifiedName() + "\n" //
                + "INTO SCRIPT " + ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".EXPORT_PATH WITH\n" //
                + "BUCKET_PATH     = 's3a://" + bucket + "/" + s3Path + "'\n" //
                + "DATA_FORMAT     = 'PARQUET'\n" //
                + "S3_ENDPOINT     = '" + s3setup.getS3Endpoint() + "'\n" //
                + "CONNECTION_NAME = '" + S3_CONNECTION + "'\n" //
                + "PARALLELISM     = 'iproc()'");
    }

    private void executeStatement(final String sql) {
        LOGGER.info(() -> "Running statement '" + sql + "'...");
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to execute statement '" + sql + "': " + exception.getMessage(),
                    exception);
        }
    }

    private void assertScriptsInstalled() {
        setup.exasolMetadata().assertScript(table() //
                .row(setScript("EXPORT_PATH", "com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator")) //
                .row(setScript("EXPORT_TABLE", "com.exasol.cloudetl.scriptclasses.TableDataExporter")) //
                .row(setScript("IMPORT_FILES", "com.exasol.cloudetl.scriptclasses.FilesDataImporter")) //
                .row(scalarScript("IMPORT_METADATA", "com.exasol.cloudetl.scriptclasses.FilesMetadataReader")) //
                .row(setScript("IMPORT_PATH", "com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator")) //
                .matches());
    }

    private Object[] setScript(final String name, final String scriptClass) {
        return script(name, "SET", scriptClass);
    }

    private Object[] scalarScript(final String name, final String scriptClass) {
        return script(name, "SCALAR", scriptClass);
    }

    private Object[] script(final String name, final String inputType, final String scriptClass) {
        final String comment = "Created by extension manager for Cloud Storage Extension " + PROJECT_VERSION;
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + ADAPTER_JAR.getFileName().toString() + ";";
        return new Object[] { name, "UDF", inputType, "EMITS",
                allOf(containsString(jarDirective), containsString("%scriptclass " + scriptClass + ";")), comment };
    }
}
