package com.exasol.cloudetl.source;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.schema.MessageTypeParser;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.filesystem.FileSystemManager;
import com.exasol.parquetio.reader.RowParquetReader;

class ParquetSourceTestTest extends AbstractSourceTest {
    @Override
    protected String getFormat() {
        return "parquet";
    }

    @Test
    void getSchemaReturnsParquetSchema() throws Exception {
        final var expectedMessageType = MessageTypeParser.parseMessageType(("message spark_schema {\n" +
"  optional int64 sales_id;\n" +
"  optional int32 position_id;\n" +
"  optional int32 article_id;\n" +
"  optional int32 amount;\n" +
"  optional double price;\n" +
"  optional int32 voucher_id;\n" +
"  optional boolean canceled;\n" +
"}\n" +
"\n"));

        final java.nio.file.Path filePattern = Paths.get(this.resourceDir + "/sales_pos*.parquet");
        for (final Path file : ScalaConverters
                .asJavaList(new FileSystemManager(getFileSystem()).getLocalFiles(filePattern))) {
            assertEquals(expectedMessageType, RowParquetReader.getSchema(getInputFile(file)));
        }
    }

    @Test
    void streamReturnsCountOfRecordsFromSingleParquetFile() throws java.io.IOException {
        assertEquals(500, getRecordsCount(Paths.get(this.resourceDir + "/sales_positions1.snappy.parquet")));
    }

    @Test
    void streamReturnsCountOfRecordsFromParquetFiles() throws java.io.IOException {
        assertEquals(1005, getRecordsCount(Paths.get(this.resourceDir + "/sales_positions*.parquet")));
    }

    @Test
    void streamReturnsCountOfRecordsFromParquetFilesWithRicherTypes() throws java.io.IOException {
        assertEquals(999, getRecordsCount(Paths.get(this.resourceDir + "/sales1.snappy.parquet")));
    }

    @Test
    void streamThrowsIfItCannotCreateParquetReader() {
        final Path nonPath = new Path(this.resourceDir + "/notFile.parquet");
        final IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> getSource(nonPath).stream().size());
        assertInstanceOf(java.io.FileNotFoundException.class, thrown.getCause());
        assertEquals("File " + nonPath + " does not exist", thrown.getCause().getMessage());
    }

    private InputFile getInputFile(final Path path) throws java.io.IOException {
        return HadoopInputFile.fromPath(path, getConf());
    }
}
