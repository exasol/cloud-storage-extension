package com.exasol.cloudetl.timestamp;

import static com.exasol.cloudetl.helper.DateTimeConverter.getJulianDayAndNanos;
import static com.exasol.cloudetl.helper.DateTimeConverter.getMicrosFromTimestamp;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.avro.Schema;
import org.apache.orc.TypeDescription;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.schema.MessageTypeParser;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import com.exasol.cloudetl.*;
import com.exasol.cloudetl.avro.AvroTestDataWriter;
import com.exasol.cloudetl.orc.OrcTestDataWriter;
import com.exasol.cloudetl.parquet.ParquetTestDataWriter;
import com.exasol.dbbuilder.dialects.Table;

class TimestampWithUTCImportExportIT extends BaseS3IntegrationTest {
    private static final String SCHEMA_NAME = "TIMESTAMP_SCHEMA";
    private java.nio.file.Path outputDirectory;
    private org.apache.hadoop.fs.Path path;

    @BeforeEach
    void beforeEach() throws IOException {
        this.outputDirectory = TestFileManager.createTemporaryFolder("timestamp-tests-");
        this.path = new org.apache.hadoop.fs.Path(this.outputDirectory.toUri().toString(), "part-00000");
    }

    @AfterEach
    void afterEach() throws IOException {
        TestFileManager.deletePathFiles(this.outputDirectory);
    }

    @BeforeAll
    void beforeAll() {
        prepareExasolDatabase(SCHEMA_NAME);
        createS3ConnectionObject();
    }

    @Test
    void parquetImportsInt64TimestampMillis() throws IOException, SQLException {
        final long millis1 = Instant.EPOCH.toEpochMilli();
        final long millis2 = System.currentTimeMillis();
        new ParquetTimestampWriter("optional int64 column (TIMESTAMP_MILLIS);")
                .withBucketName("int64-timestamp-millis").withTableColumnType("TIMESTAMP")
                .withInputValues(values(Long.valueOf(millis1), Long.valueOf(millis2), (Object) null))
                .verify(table().row(new Timestamp(millis1)).row(new Timestamp(millis2)).row((Object) null).withUtcCalendar()
                        .matches());
    }

    @Test
    void parquetImportsInt64TimestampMicros() throws IOException, SQLException {
        final Timestamp timestamp = Timestamp.valueOf("2022-01-12 10:28:53.123456");
        final long millis = timestamp.getTime();
        final long micros = millis * 1000L + (timestamp.getNanos() / 1000L) % 1000L;
        new ParquetTimestampWriter("optional int64 column (TIMESTAMP_MICROS);")
                .withBucketName("int64-timestamp-micros").withTableColumnType("TIMESTAMP")
                .withInputValues(values(Long.valueOf(micros), (Object) null))
                .verify(table().row(new Timestamp(millis)).row((Object) null).withUtcCalendar().matches());
    }

    @Test
    void parquetImportsInt96TimestampNanos() throws IOException, SQLException {
        final long millis = System.currentTimeMillis();
        final Timestamp timestamp = new Timestamp(millis);
        final ByteBuffer buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
        final scala.Tuple2<Integer, Long> dayAndNanos = getJulianDayAndNanos(getMicrosFromTimestamp(timestamp));
        buffer.putLong(dayAndNanos._2().longValue()).putInt(dayAndNanos._1().intValue());
        new ParquetTimestampWriter("optional int96 column;").withBucketName("int96-timestamp")
                .withTableColumnType("TIMESTAMP").withInputValues(values(Binary.fromConstantByteArray(buffer.array()), (Object) null))
                .verify(table().row(timestamp).row((Object) null).withUtcCalendar().matches());
    }

    @Test
    void parquetExportTimestamp() throws SQLException {
        final long millis = System.currentTimeMillis();
        final String timestampString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.of("UTC"))
                .format(Instant.ofEpochMilli(millis));
        final String bucket = "export-timestamp-utc";
        final Table importTable = this.schema.createTable("TIMESTAMP_IMPORT_TABLE", "ID", "DECIMAL(18,0)", "COLUMN",
                "TIMESTAMP");
        final Table exportTable = this.schema
                .createTable("TIMESTAMP_EXPORT_TABLE", "ID", "DECIMAL(18,0)", "COLUMN", "TIMESTAMP")
                .insert(Long.valueOf(1), timestampString).insert(Long.valueOf(2), (Object) null);

        createBucket(bucket);
        exportIntoS3Bucket(exportTable, bucket);
        importFromS3Bucket(importTable, bucket, "PARQUET");
        verifyTable("SELECT * FROM " + importTable.getFullyQualifiedName() + " ORDER BY ID ASC",
                table().row(Long.valueOf(1), new Timestamp(millis)).row(Long.valueOf(2), (Object) null).withUtcCalendar()
                        .matches());
    }

    @Test
    void avroImportsLongTimestampMillis() throws IOException, SQLException {
        final long millis1 = System.currentTimeMillis();
        final long millis2 = 0L;
        new AvroTimestampWriter("{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}").withBucketName("avro-timestamp")
                .withTableColumnType("TIMESTAMP").withInputValues(List.of(Long.valueOf(millis1), Long.valueOf(millis2)))
                .verify(table().row(new Timestamp(millis1)).row(new Timestamp(millis2)).withUtcCalendar().matches());
    }

    @Test
    void orcImportsTimestamp() throws IOException, SQLException {
        final Timestamp timestamp1 = new Timestamp(Instant.EPOCH.toEpochMilli());
        final Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        new OrcTimestampWriter("struct<f:timestamp>").withBucketName("orc-timestamp").withTableColumnType("TIMESTAMP")
                .withInputValues(values(timestamp1, timestamp2, (Object) null))
                .verify(table().row(timestamp1).row(timestamp2).row((Object) null).withUtcCalendar().matches());
    }

    private void importFromS3Bucket(final Table table, final String bucket, final String dataFormat) {
        executeStmt(String.format("IMPORT INTO %s%n"
                + "FROM SCRIPT %s.IMPORT_PATH WITH%n"
                + "BUCKET_PATH              = 's3a://%s/*'%n"
                + "DATA_FORMAT              = '%s'%n"
                + "S3_ENDPOINT              = '%s'%n"
                + "S3_CHANGE_DETECTION_MODE = 'none'%n"
                + "CONNECTION_NAME          = 'S3_CONNECTION'%n"
                + "TIMEZONE_UTC             = 'true'%n"
                + "PARALLELISM              = 'nproc()';%n", table.getFullyQualifiedName(), SCHEMA_NAME, bucket,
                dataFormat, this.s3Endpoint));
    }

    private void exportIntoS3Bucket(final Table table, final String bucket) {
        executeStmt(String.format("EXPORT %s%n"
                + "INTO SCRIPT %s.EXPORT_PATH WITH%n"
                + "BUCKET_PATH     = 's3a://%s/'%n"
                + "DATA_FORMAT     = 'PARQUET'%n"
                + "S3_ENDPOINT     = '%s'%n"
                + "CONNECTION_NAME = 'S3_CONNECTION'%n"
                + "TIMEZONE_UTC    = 'true'%n"
                + "PARALLELISM     = 'iproc()';%n", table.getFullyQualifiedName(), SCHEMA_NAME, bucket,
                this.s3Endpoint));
    }

    private void verifyTable(final String query, final Matcher<ResultSet> matcher) throws SQLException {
        try (ResultSet resultSet = executeQuery(query)) {
            assertThat(resultSet, matcher);
        }
    }

    private static List<Object> values(final Object... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    private abstract class BaseTimestampWriter {
        private String bucketName;
        private Table table;

        protected abstract String dataFormat();

        protected abstract org.apache.hadoop.fs.Path filePath();

        BaseTimestampWriter withBucketName(final String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        BaseTimestampWriter withTableColumnType(final String columnType) {
            final String tableName = this.bucketName.replace("-", "_").toUpperCase(java.util.Locale.ENGLISH);
            this.table = TimestampWithUTCImportExportIT.this.schema.createTableBuilder(tableName).column("COLUMN", columnType)
                    .build();
            return this;
        }

        abstract BaseTimestampWriter withInputValues(List<?> values) throws IOException;

        void verify(final Matcher<ResultSet> matcher) throws SQLException {
            uploadFileToS3(this.bucketName, filePath());
            importFromS3Bucket(this.table, this.bucketName, dataFormat());
            verifyTable("SELECT * FROM " + this.table.getFullyQualifiedName(), matcher);
        }
    }

    private final class ParquetTimestampWriter extends BaseTimestampWriter {
        private final org.apache.parquet.schema.MessageType parquetSchema;
        private final ParquetTestDataWriter writer = new ParquetTestDataWriter();

        private ParquetTimestampWriter(final String parquetType) {
            this.parquetSchema = MessageTypeParser.parseMessageType("message test { " + parquetType + " }");
        }

        @Override
        protected String dataFormat() {
            return "PARQUET";
        }

        @Override
        protected org.apache.hadoop.fs.Path filePath() {
            return TimestampWithUTCImportExportIT.this.path;
        }

        @Override
        BaseTimestampWriter withInputValues(final List<?> values) throws IOException {
            this.writer.writeDataValues(values, filePath(), this.parquetSchema);
            return this;
        }
    }

    private final class AvroTimestampWriter extends BaseTimestampWriter {
        private final Schema avroSchema;
        private final AvroTestDataWriter writer = new AvroTestDataWriter();

        private AvroTimestampWriter(final String avroType) {
            this.avroSchema = new Schema.Parser().parse("{\n"
                    + "  \"type\": \"record\",\n"
                    + "  \"namespace\": \"avrorecord\",\n"
                    + "  \"name\": \"basictype\",\n"
                    + "  \"fields\": [{\"name\": \"column\", \"type\": " + avroType + "}]\n"
                    + "}");
        }

        @Override
        protected String dataFormat() {
            return "AVRO";
        }

        @Override
        protected org.apache.hadoop.fs.Path filePath() {
            return TimestampWithUTCImportExportIT.this.path;
        }

        @Override
        BaseTimestampWriter withInputValues(final List<?> values) throws IOException {
            this.writer.writeDataValues(values, filePath(), this.avroSchema);
            return this;
        }
    }

    private final class OrcTimestampWriter extends BaseTimestampWriter {
        private final TypeDescription orcSchema;
        private final OrcTestDataWriter writer = new OrcTestDataWriter();

        private OrcTimestampWriter(final String orcType) {
            this.orcSchema = TypeDescription.fromString(orcType);
        }

        @Override
        protected String dataFormat() {
            return "ORC";
        }

        @Override
        protected org.apache.hadoop.fs.Path filePath() {
            return TimestampWithUTCImportExportIT.this.path;
        }

        @Override
        BaseTimestampWriter withInputValues(final List<?> values) throws IOException {
            this.writer.writeDataValues(values, filePath(), this.orcSchema);
            return this;
        }
    }
}
