package com.exasol.cloudetl.parquet;

/**
 * ChunkInterval
 */
public class ChunkInterval implements Interval {

  private long start;
  private long end;

  public ChunkInterval(final long start, final long end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public long getStartPosition() {
    return start;
  }

  @Override
  public long getEndPosition() {
    return end;
  }

}
