package com.exasol.cloudetl;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.Column;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(BaseIntegrationTest.class.getName());
    private static final String JAR_NAME_PATTERN = "exasol-cloud-storage-extension-";
    private static final String SPARK_JAVA_MODULE_OPTION = "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED";
    protected final String defaultExasolDockerImage = "2025.2.1";
    protected final DockerNamedNetwork network = DockerNamedNetwork.create("it-tests", true);
    protected final ExasolContainer<?> exasolContainer = createExasolContainer();
    protected ExasolObjectFactory factory;
    protected ExasolSchema schema;
    protected Connection connection;
    protected final String assembledJarName = getAssembledJarName();

    @BeforeAll
    void baseBeforeAll() {
        this.exasolContainer.start();
    }

    @AfterAll
    void baseAfterAll() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
        this.exasolContainer.stop();
    }

    protected void prepareExasolDatabase(final String schemaName) {
        executeStmt("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
        this.factory = new ExasolObjectFactory(getConnection(),
                ExasolObjectConfiguration.builder().withJvmOptions(SPARK_JAVA_MODULE_OPTION).build());
        LOGGER.info(() -> "Creating schema " + schemaName);
        this.schema = this.factory.createSchema(schemaName);
        createImportDeploymentScripts();
        createExportDeploymentScripts();
        uploadJarToBucket();
    }

    protected void executeStmt(final String sql) {
        LOGGER.info(() -> "Executing statement " + sql + "...");
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(sql);
        } catch (final Exception exception) {
            throw new IllegalStateException("Failed executing SQL '" + sql + "': " + exception.getMessage(), exception);
        }
    }

    protected ResultSet executeQuery(final String sql) throws SQLException {
        return getConnection().createStatement().executeQuery(sql);
    }

    private Connection getConnection() {
        if (this.connection == null) {
            try {
                this.connection = this.exasolContainer.createConnection("");
            } catch (final SQLException exception) {
                throw new IllegalStateException(exception);
            }
        }
        return this.connection;
    }

    private ExasolContainer<?> createExasolContainer() {
        final ExasolContainer<?> container = new ExasolContainer<>(getExasolDockerImageVersion());
        container.withNetwork(this.network);
        container.withReuse(true);
        return container;
    }

    private void createImportDeploymentScripts() {
        final String jarPath = "/buckets/bfsdefault/default/" + this.assembledJarName;
        this.schema.createUdfBuilder("IMPORT_PATH").language(UdfScript.Language.JAVA).inputType(UdfScript.InputType.SET)
                .emits().bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator", jarPath).build();
        this.schema.createUdfBuilder("IMPORT_METADATA").language(UdfScript.Language.JAVA)
                .inputType(UdfScript.InputType.SCALAR)
                .emits(new Column("filename", "VARCHAR(2000)"), new Column("partition_index", "VARCHAR(100)"),
                        new Column("start_index", "DECIMAL(36, 0)"), new Column("end_index", "DECIMAL(36, 0)"))
                .bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesMetadataReader", jarPath).build();
        this.schema.createUdfBuilder("IMPORT_FILES").language(UdfScript.Language.JAVA).inputType(UdfScript.InputType.SET)
                .emits().bucketFsContent("com.exasol.cloudetl.scriptclasses.FilesDataImporter", jarPath).build();
    }

    private void createExportDeploymentScripts() {
        final String jarPath = "/buckets/bfsdefault/default/" + this.assembledJarName;
        this.schema.createUdfBuilder("EXPORT_PATH").language(UdfScript.Language.JAVA).inputType(UdfScript.InputType.SET)
                .emits().bucketFsContent("com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator", jarPath).build();
        this.schema.createUdfBuilder("EXPORT_TABLE").language(UdfScript.Language.JAVA).inputType(UdfScript.InputType.SET)
                .emits(new Column("rows_affected", "INT"))
                .bucketFsContent("com.exasol.cloudetl.scriptclasses.TableDataExporter", jarPath).build();
    }

    private void uploadJarToBucket() {
        final java.nio.file.Path jarPath = Paths.get("target", this.assembledJarName).toAbsolutePath();
        LOGGER.info(() -> "Uploading JAR " + jarPath + " to bucket at " + this.assembledJarName + "...");
        try {
            this.exasolContainer.getDefaultBucket().uploadFile(jarPath, this.assembledJarName);
        } catch (final com.exasol.bucketfs.BucketAccessException | java.util.concurrent.TimeoutException
                | java.io.FileNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String getAssembledJarName() {
        return findFileOrDirectory("target/", JAR_NAME_PATTERN);
    }

    private String findFileOrDirectory(final String searchDirectory, final String name) {
        return listDirectoryFiles(searchDirectory).stream().filter(file -> file.getName().contains(name)).findFirst()
                .map(File::getName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot find a file or a directory with pattern '" + name + "' in '" + searchDirectory + "'"));
    }

    private List<File> listDirectoryFiles(final String directoryName) {
        final File directory = new File(directoryName);
        final File[] files = directory.exists() && directory.isDirectory() ? directory.listFiles() : new File[0];
        return files == null ? List.of() : Arrays.asList(files);
    }

    private String getExasolDockerImageVersion() {
        final String dockerVersion = System.getenv("EXASOL_DB_VERSION");
        return dockerVersion == null ? this.defaultExasolDockerImage : dockerVersion;
    }
}
