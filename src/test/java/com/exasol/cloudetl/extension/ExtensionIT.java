package com.exasol.cloudetl.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.io.FileNotFoundException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.itest.ExasolVersionCheck;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.base.AbstractScriptExtensionIT;
import com.exasol.extensionmanager.itest.base.ExtensionITConfig;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.matcher.TypeMatchMode;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import junit.framework.AssertionFailedError;

class ExtensionIT extends AbstractScriptExtensionIT {
    private static final Logger LOGGER = Logger.getLogger(ExtensionIT.class.getName());
    private static final String EXTENSION_ID = "cloud-storage-extension.js";
    private static final String PREVIOUS_VERSION = "2.7.8";
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static final String S3_CONNECTION = "S3_CONNECTION";
    private static final Path ADAPTER_JAR = getAdapterJar();
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static S3Setup s3setup;
    private static Connection connection;
    private static ExasolObjectFactory exasolObjectFactory;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException, SQLException {
        if (System.getProperty("com.exasol.dockerdb.image") == null) {
            System.setProperty("com.exasol.dockerdb.image", "8.23.1");
        }
        exasolTestSetup = new ExasolTestSetupFactory(Paths.get("no-cloud-setup")).getTestSetup();
        ExasolVersionCheck.assumeExasolVersion8(exasolTestSetup);
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist").resolve(EXTENSION_ID)));
        exasolTestSetup.getDefaultBucket().uploadFile(ADAPTER_JAR, ADAPTER_JAR.getFileName().toString());

        s3setup = S3Setup.create();
        connection = exasolTestSetup.createConnection();
        exasolObjectFactory = new ExasolObjectFactory(connection, ExasolObjectConfiguration.builder().build());
        exasolObjectFactory.createConnectionDefinition(S3_CONNECTION, "", "dummy_user", s3setup.getSecret());
    }

    private static Path getAdapterJar() {
        final Path jar = Paths.get("target").resolve("exasol-cloud-storage-extension-" + PROJECT_VERSION + ".jar")
                .toAbsolutePath();
        if (Files.exists(jar)) {
            return jar;
        } else {
            throw new AssertionFailedError("Adapter jar " + jar + " does not exist. Run 'mvn package'.");
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
        if (exasolTestSetup != null) {
            exasolTestSetup.getDefaultBucket().deleteFileNonBlocking(ADAPTER_JAR.getFileName().toString());
            exasolTestSetup.close();
        }
        if (s3setup != null) {
            s3setup.close();
        }
    }

    @Override
    protected ExtensionManagerSetup getSetup() {
        return setup;
    }

    @Override
    protected ExtensionITConfig createConfig() {
        return ExtensionITConfig.builder() //
                .projectName("cloud-storage-extension").extensionId(EXTENSION_ID).currentVersion(PROJECT_VERSION)
                .extensionName("Cloud Storage Extension")
                .extensionDescription(
                        "Access data formatted with Avro, Orc and Parquet on public cloud storage systems")
                .previousVersion(PREVIOUS_VERSION)
                .previousVersionJarFile("exasol-cloud-storage-extension-" + PREVIOUS_VERSION + ".jar") //
                .expectedParameterCount(-1) //
                .build();
    }

    @Override
    protected void assertScriptsWork() {
        final ExasolSchema schema = exasolObjectFactory.createSchema("TESTING_SCHEMA_" + System.currentTimeMillis());
        final String bucket = s3setup.createBucket();
        final String s3Path = "data";
        try {
            final Table sourceTable = schema.createTable("SRC_TABLE", "ID", "INTEGER", "NAME", "VARCHAR(10)")
                    .insert(1, "a").insert(2, "b").insert(3, "c");
            exportIntoS3(sourceTable, bucket, s3Path);
            final Table targetTable = schema.createTable("TARGET_TABLE", "ID", "INTEGER", "NAME", "VARCHAR(10)");
            importFromS3IntoExasol(targetTable, bucket, s3Path + "/*");
            try (var statement = connection
                    .prepareStatement("select * from " + targetTable.getFullyQualifiedName() + " order by 1")) {
                assertThat(statement.executeQuery(),
                        table().row(1, "a").row(2, "b").row(3, "c").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
            }
        } catch (final SQLException exception) {
            throw new AssertionError("Failed to assert scripts execution: " + exception.getMessage(), exception);
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
                + "PARALLELISM              = 'nproc()'");
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

    @Override
    protected void assertScriptsExist() {
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
        final String comment = "Created by Extension Manager for Cloud Storage Extension " + PROJECT_VERSION;
        final String jarPath = "/buckets/bfsdefault/default/" + ADAPTER_JAR.getFileName().toString();
        return new Object[] { name, "UDF", inputType, "EMITS", allOf(//
                containsString("%jar " + jarPath + ";"), //
                containsString("%scriptclass " + scriptClass + ";")), //
                comment };
    }
}
