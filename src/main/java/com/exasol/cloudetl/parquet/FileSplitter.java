package com.exasol.cloudetl.parquet;

import java.io.IOException;
import java.util.List;

/**
 * An interface for splitting files into chunks.
 */
public interface FileSplitter {

  /**
   * Gets file splits in the form of {@code start} and {@code end} intervals.
   *
   * @return an array of intervals
   */
  List<Interval> getSplits() throws IOException;

}
