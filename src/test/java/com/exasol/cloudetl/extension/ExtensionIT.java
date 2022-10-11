package com.exasol.cloudetl.extension;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import com.exasol.bucketfs.BucketAccessException;

class ExtensionIT {
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final int EXPECTED_PARAMETER_COUNT = 10;
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static String s3BucketName;
    private static String projectVersion;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        exasolTestSetup = new ExasolTestSetupFactory(IntegrationTestSetup.CLOUD_SETUP_CONFIG).getTestSetup();
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist/s3-vs-extension.js")));
        s3TestSetup = new AwsS3TestSetup();
        projectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
        exasolTestSetup.getDefaultBucket().uploadFile(IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH,
                IntegrationTestSetup.ADAPTER_JAR);
    }

    @AfterAll
    static void teardown()  {
        if (setup != null) {
            setup.close();
        }
        exasolTestSetup.getDefaultBucket().deleteFileNonBlocking(IntegrationTestSetup.ADAPTER_JAR);
        exasolTestSetup.close();
    }

    @AfterEach
    void cleanup() {
        setup.cleanup();
    }
}
