package com.exasol.cloudetl.scriptclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.bucket.Bucket;
import com.exasol.cloudetl.data.ExaColumnInfo;
import com.exasol.cloudetl.helper.ExasolColumnValueProvider;
import com.exasol.cloudetl.sink.BatchSizedSink;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.common.data.Row;

/** Exports table data to storage. */
public final class TableDataExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableDataExporter.class);
    private static final int STORAGE_PROPERTIES_INDEX = 1;
    private static final int SOURCE_COLUMNS_INDEX = 2;
    private static final int FIRST_COLUMN_INDEX = 3;

    private TableDataExporter() {
        // Script class.
    }

    /** Run the exporter. */
    public static void run(final ExaMetadata metadata, final ExaIterator iterator)
            throws com.exasol.ExaIterationException, com.exasol.ExaDataTypeException {
        final StorageProperties storageProperties = StorageProperties.apply(iterator.getString(STORAGE_PROPERTIES_INDEX),
                metadata);
        checkIfTimezoneUtc(storageProperties);
        runExport(metadata, iterator, Bucket.create(storageProperties));
    }

    private static void checkIfTimezoneUtc(final StorageProperties storageProperties) {
        if (storageProperties.isEnabled("TIMEZONE_UTC")) {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }
    }

    private static void runExport(final ExaMetadata metadata, final ExaIterator iterator, final Bucket bucket)
            throws com.exasol.ExaIterationException, com.exasol.ExaDataTypeException {
        final String[] sourceColumnNames = iterator.getString(SOURCE_COLUMNS_INDEX).split("\\.");
        final scala.collection.immutable.Seq<ExaColumnInfo> columns = getColumns(metadata, sourceColumnNames);
        final long nodeId = metadata.getNodeId();
        final String vmId = metadata.getVmId();
        final BatchSizedSink sink = new BatchSizedSink(nodeId, vmId, iterator.size(), columns, bucket);
        LOGGER.info("Starting export from node: {}, vm: {}.", nodeId, vmId);
        try {
            do {
                sink.write(getRow(iterator, columns));
            } while (iterator.next());
        } finally {
            sink.close();
        }
        final Long totalRecords = sink.getTotalRecords();
        try {
            iterator.emit(totalRecords);
        } catch (final Exception exception) {
            throw new IllegalStateException(exception);
        }
        LOGGER.info("Exported '{}' records from node '{}' and vm '{}'.", totalRecords, nodeId, vmId);
    }

    private static Row getRow(final ExaIterator iterator, final scala.collection.immutable.Seq<ExaColumnInfo> columns) {
        final ExasolColumnValueProvider columnValueProvider = new ExasolColumnValueProvider(iterator);
        final List<Object> values = new ArrayList<>();
        final List<ExaColumnInfo> javaColumns = ScalaConverters.asJavaList(columns);
        for (int index = 0; index < javaColumns.size(); index++) {
            values.add(columnValueProvider.getColumnValue(FIRST_COLUMN_INDEX + index, javaColumns.get(index)));
        }
        return Row.apply(ScalaConverters.seqFromJava(values));
    }

    private static scala.collection.immutable.Seq<ExaColumnInfo> getColumns(final ExaMetadata meta,
            final String[] sourceColumnNames) throws com.exasol.ExaIterationException {
        final int totalColumnCount = (int) meta.getInputColumnCount();
        final List<ExaColumnInfo> columns = new ArrayList<>();
        for (int idx = FIRST_COLUMN_INDEX; idx < totalColumnCount; idx++) {
            columns.add(new ExaColumnInfo(sourceColumnNames[idx - FIRST_COLUMN_INDEX], meta.getInputColumnType(idx),
                    (int) meta.getInputColumnPrecision(idx), (int) meta.getInputColumnScale(idx),
                    (int) meta.getInputColumnLength(idx), true));
        }
        return ScalaConverters.seqFromJava(columns);
    }
}
