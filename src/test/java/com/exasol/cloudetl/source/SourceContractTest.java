package com.exasol.cloudetl.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class SourceContractTest {
    private final Configuration configuration1 = new Configuration(false);
    private final Configuration configuration2 = new Configuration();
    private final FileSystem localFilesystem = FileSystem.get(this.configuration1);
    private final FileSystem s3aFilesystem = FileSystem.get(new Path("s3a://tmp").toUri(), this.configuration2);

    SourceContractTest() throws IOException {
        // FileSystem fields are initialized above.
    }

    @Test
    void verifyAvroSourceImplementationContract() throws Exception {
        final String avroSchemaString = ("{\n" +
"  \"type\": \"record\",\n" +
"  \"namespace\": \"avro.types\",\n" +
"  \"name\": \"basic\",\n" +
"  \"fields\": [{\n" +
"    \"name\": \"column\",\n" +
"    \"type\": \"boolean\"\n" +
"  }]\n" +
"}\n" +
"\n");
        final Schema schema = new Schema.Parser().parse(avroSchemaString);
        final GenericDatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
        final java.nio.file.Path path = Paths.get(getClass().getResource("/data/import/avro").toURI());
        try (DataFileReader<GenericRecord> dataReader1 = new DataFileReader<>(new File(path + "/sales10.avro"), reader);
                DataFileReader<GenericRecord> dataReader2 = new DataFileReader<>(new File(path + "/sales11.avro"),
                        reader)) {
            EqualsVerifier.forClass(AvroSource.class).withPrefabValues(Configuration.class, this.configuration1,
                    this.configuration2).withPrefabValues(FileSystem.class, this.localFilesystem, this.s3aFilesystem)
                    .withPrefabValues(DataFileReader.class, dataReader1, dataReader2)
                    .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
        }
    }

    @Test
    void verifyOrcSourceImplementationContract() {
        EqualsVerifier.forClass(OrcSource.class).withPrefabValues(Configuration.class, this.configuration1,
                this.configuration2).withPrefabValues(FileSystem.class, this.localFilesystem, this.s3aFilesystem)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}
