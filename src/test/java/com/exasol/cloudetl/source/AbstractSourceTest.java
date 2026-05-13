package com.exasol.cloudetl.source;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.jupiter.api.BeforeEach;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.filesystem.FileSystemManager;
import com.exasol.cloudetl.parquet.ParquetSourceTest;
import com.exasol.cloudetl.storage.FileFormat;

abstract class AbstractSourceTest {
    private Configuration conf;
    private FileSystem fileSystem;
    protected Path resourceDir;

    @BeforeEach
    final void abstractSourceBeforeEach() throws Exception {
        this.conf = new Configuration();
        this.fileSystem = FileSystem.get(this.conf);
        this.resourceDir = Paths.get(getClass().getResource("/data/import/" + getFormat()).toURI()).toAbsolutePath();
    }

    final Configuration getConf() {
        return this.conf;
    }

    final FileSystem getFileSystem() {
        return this.fileSystem;
    }

    final Source getSource(final org.apache.hadoop.fs.Path filePath) {
        return getSource(filePath, getFormat());
    }

    protected String getFormat() {
        return "dummy";
    }

    final Source getSource(final org.apache.hadoop.fs.Path filePath, final String fileFormat) {
        final FileFormat format = FileFormat.apply(fileFormat);
        switch (format) {
        case PARQUET:
        case DELTA:
            return new ParquetSourceTest(filePath, this.conf);
        default:
            return Source.create(format, filePath, this.conf, this.fileSystem);
        }
    }

    final int getRecordsCount(final Path filePath) throws java.io.IOException {
        int sum = 0;
        for (final org.apache.hadoop.fs.Path file : ScalaConverters
                .asJavaList(new FileSystemManager(this.fileSystem).getLocalFiles(filePath))) {
            final Source source = getSource(file);
            try {
                sum += source.stream().size();
            } finally {
                source.close();
            }
        }
        return sum;
    }
}
