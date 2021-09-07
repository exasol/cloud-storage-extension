package com.exasol.cloudetl.parquet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowReadSupport;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.filter2.compat.FilterCompat;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.api.ReadSupport;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.io.api.RecordMaterializer;
import org.apache.parquet.schema.MessageType;

public class RowParquetChunkReader {

  private final InputFile file;
  private final List<Interval> chunks;
  private final RowReadSupport readSupport;

  private MessageColumnIO messageIO;
  private RecordMaterializer<Row> recordMaterializer;


  public RowParquetChunkReader(final InputFile file, final long start, final long end) {
    this(file, List.of(new ChunkInterval(start, end)));
  }

  public RowParquetChunkReader(final InputFile file, final List<Interval> chunks) {
    this.file = file;
    this.chunks = chunks;
    this.readSupport = new RowReadSupport();
  }

  public void read(final Consumer<Row> rowConsumer) throws IOException {
    // sort and merge chunk intervals
    initialize();
    try (final ParquetFileReader reader = ParquetFileReader.open(file)) {
      long currentRowGroup = 0;
      for (final Interval chunk : chunks) {
        currentRowGroup = moveToPosition(reader, currentRowGroup, chunk.getStartPosition());
        final long endPosition = chunk.getEndPosition();
        while (currentRowGroup < endPosition) {
            final PageReadStore pageStore = reader.readNextRowGroup();
            currentRowGroup += 1;
            consumeRows(pageStore, rowConsumer);
        }
      }
    }
  }

  private long moveToPosition(final ParquetFileReader reader, final long currentPosition, final long startPosition) {
    long position = currentPosition;
    while (position < startPosition) {
      reader.skipNextRowGroup();
      position += 1;
    }
    return position;
  }

  private void consumeRows(final PageReadStore pageStore, final Consumer<Row> rowConsumer) {
    final RecordReader<Row> recordReader = messageIO.getRecordReader(pageStore, recordMaterializer, FilterCompat.NOOP);
    long currentRow = 0;
    long totalRows = pageStore.getRowCount();
    while (currentRow < totalRows) {
      Row row;
      currentRow += 1;
      try {
        row = recordReader.read();
      } catch (RecordMaterializer.RecordMaterializationException e) {
        //
        continue;
      }
      if (row == null) { // end of block
        break;
      }
      if (recordReader.shouldSkipCurrentRecord()) { // filtered
        continue;
      }
      rowConsumer.accept(row);
    }
  }

  private void initialize() throws IOException {
    try (final ParquetFileReader reader = ParquetFileReader.open(file)) {
      final Configuration conf = new Configuration();
      final MessageType schema = reader.getFooter().getFileMetaData().getSchema();
      final ReadSupport.ReadContext readContext = readSupport.init(conf, Collections.emptyMap(), schema);
      this.recordMaterializer = readSupport.prepareForRead(conf, Collections.emptyMap(), schema, readContext);
      this.messageIO = new ColumnIOFactory(reader.getFooter().getFileMetaData().getCreatedBy()).getColumnIO(readContext.getRequestedSchema(), schema, true);
    }
  }

}
