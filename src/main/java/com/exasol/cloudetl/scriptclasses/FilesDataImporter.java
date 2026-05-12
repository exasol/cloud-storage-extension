package com.exasol.cloudetl.scriptclasses;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;
import com.exasol.cloudetl.ScalaConverters;
import com.exasol.cloudetl.emitter.FilesDataEmitter;
import com.exasol.cloudetl.storage.StorageProperties;
import com.exasol.parquetio.data.ChunkInterval;
import com.exasol.parquetio.data.ChunkIntervalImpl;

/** Imports file data into Exasol. */
public final class FilesDataImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesDataImporter.class);
    private static final int STORAGE_PROPERTIES_INDEX = 1;
    private static final int FILENAME_STARTING_INDEX = 2;

    private FilesDataImporter() {
        // Script class.
    }

    /** Run the importer. */
    public static void run(final ExaMetadata metadata, final ExaIterator iterator)
            throws com.exasol.ExaIterationException, com.exasol.ExaDataTypeException {
        final StorageProperties storageProperties = StorageProperties.apply(iterator.getString(STORAGE_PROPERTIES_INDEX),
                metadata);
        final scala.collection.immutable.Map<String, List<ChunkInterval>> files = collectFiles(iterator);
        final long nodeId = metadata.getNodeId();
        final String vmId = metadata.getVmId();
        int intervalCount = 0;
        for (final Map.Entry<String, List<ChunkInterval>> entry : ScalaConverters.javaMapCopy(files).entrySet()) {
            LOGGER.info("Importing {} intervals '{}' for file {} on node '{}' and vm '{}'.", entry.getValue().size(),
                    getIntervalString(entry.getValue()), entry.getKey(), nodeId, vmId);
            intervalCount += entry.getValue().size();
        }
        LOGGER.info("Importing {} files with {} intervals", files.size(), intervalCount);
        new FilesDataEmitter(storageProperties, files).emit(iterator);
    }

    /** Collect files and chunk intervals from the iterator. */
    public static scala.collection.immutable.Map<String, List<ChunkInterval>> collectFiles(final ExaIterator iterator)
            throws com.exasol.ExaIterationException, com.exasol.ExaDataTypeException {
        final Map<String, List<ChunkInterval>> files = new LinkedHashMap<>();
        do {
            final String filename = iterator.getString(FILENAME_STARTING_INDEX);
            final long startIndex = iterator.getLong(FILENAME_STARTING_INDEX + 1);
            final long endIndex = iterator.getLong(FILENAME_STARTING_INDEX + 2);
            files.computeIfAbsent(filename, ignored -> new ArrayList<>()).add(new ChunkIntervalImpl(startIndex, endIndex));
        } while (iterator.next());
        return ScalaConverters.mapFromJava(files);
    }

    private static String getIntervalString(final List<ChunkInterval> intervals) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < intervals.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append("[").append(intervals.get(i).getStartPosition()).append(",")
                    .append(intervals.get(i).getEndPosition()).append(")");
        }
        return builder.toString();
    }
}
