package com.exasol.cloudetl.scriptclasses;

import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import com.exasol.*;

abstract class PathTest {
    protected final String schema = "myDBSchema";
    protected Map<String, String> properties = Map.of("BUCKET_PATH", "s3a://my-bucket/folder1/*", "DATA_FORMAT",
            "PARQUET", "S3_ENDPOINT", "s3.eu-central-1.com", "CONNECTION_NAME", "S3_CONNECTION");
    protected ExaMetadata metadata;
    protected ExaImportSpecification importSpec;
    protected ExaExportSpecification exportSpec;

    @BeforeEach
    void pathBeforeEach() {
        this.metadata = mock(ExaMetadata.class);
        this.importSpec = mock(ExaImportSpecification.class);
        this.exportSpec = mock(ExaExportSpecification.class);
    }
}
