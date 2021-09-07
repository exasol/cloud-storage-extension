package com.exasol.cloudetl.parquet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.io.InputFile;

public class ParquetFileSplitter implements FileSplitter {

  private static final long DEFAULT_CHUNK_SIZE_IN_MB = 64 * 1024 * 1024;
  private final InputFile file;
  private final long chunkSize;

  public ParquetFileSplitter(final InputFile file) {
    this(file, DEFAULT_CHUNK_SIZE_IN_MB);
  }

  public ParquetFileSplitter(final InputFile file, final long chunkSize) {
    this.file = file;
    this.chunkSize = chunkSize;
  }

  @Override
  public List<Interval> getSplits() throws IOException {
    try (final ParquetFileReader reader = ParquetFileReader.open(file)) {
      return getSplits(reader.getRowGroups());
    } catch (final IOException exception) {
      //
    }
    return Collections.emptyList();
  }

  private List<Interval> getSplits(final List<BlockMetaData> rowGroups) {
    final List<Interval> chunks = new ArrayList<>();
    final long end = rowGroups.size();
    long startPosition = 0;
    long currentSize = 0;
    for (int endPosition = 0; endPosition < end; endPosition++) {
      currentSize += rowGroups.get(endPosition).getTotalByteSize();
      if (currentSize >= this.chunkSize) {
        chunks.add(new ChunkInterval(startPosition, endPosition + 1));
        startPosition = endPosition + 1;
        currentSize = 0;
      }
    }
    if (currentSize != 0) {
      chunks.add(new ChunkInterval(startPosition, end));
    }
    return null;
  }

}
