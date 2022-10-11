package com.exasol.cloudetl.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.*;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.ExtensionsResponseExtension;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import junit.framework.AssertionFailedError;

class ExtensionIT {
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static final Path ADAPTER_JAR = getAdapterJar();
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        exasolTestSetup = new ExasolTestSetupFactory(Paths.get("no-cloud-setup")).getTestSetup();
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist/cloud-storage-extension.js")));
        exasolTestSetup.getDefaultBucket().uploadFile(ADAPTER_JAR, ADAPTER_JAR.getFileName().toString());
    }

    private static Path getAdapterJar() {
        final Path jar = Paths.get("target").resolve("exasol-cloud-storage-extension-2.5.1.jar")
                .toAbsolutePath();
        if (Files.exists(jar)) {
            return jar;
        } else {
            throw new AssertionFailedError("Adapter jar " + jar + " does not exist. Run mvn package.");
        }
    }

    @AfterAll
    static void teardown() throws Exception {
        if (setup != null) {
            setup.close();
        }
        exasolTestSetup.getDefaultBucket().deleteFileNonBlocking(ADAPTER_JAR.getFileName().toString());
        exasolTestSetup.close();
    }

    @AfterEach
    void cleanup() {
        setup.cleanup();
    }

    @Test
    void listExtensions() {
        final List<ExtensionsResponseExtension> extensions = setup.client().getExtensions();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("Cloud Storage Extension")),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).getName(), equalTo(PROJECT_VERSION)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isLatest(), is(true)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isDeprecated(), is(false)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Access data formatted with Avro, Orc and Parquet on public cloud storage systems")));
    }
}
