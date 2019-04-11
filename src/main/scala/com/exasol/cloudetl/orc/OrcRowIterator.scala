package com.exasol.cloudetl.orc

import scala.collection.JavaConverters._

import com.exasol.cloudetl.data.Row

import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector
import org.apache.orc.Reader

/**
 * An object class that creates an Orc [[com.exasol.cloudetl.data.Row]]
 * iterator from a provided Orc file [[org.apache.orc.Reader]].
 */
object OrcRowIterator {

  def apply(reader: Reader): Iterator[Seq[Row]] = new Iterator[Seq[Row]] {
    val readerBatch = reader.getSchema().createRowBatch()
    val readerRows = reader.rows(new Reader.Options())
    val structColumnVector = new StructColumnVector(readerBatch.numCols, readerBatch.cols: _*)
    val structDeserializer = new StructDeserializer(reader.getSchema.getChildren.asScala)

    override def hasNext: Boolean =
      readerRows.nextBatch(readerBatch) && !readerBatch.endOfFile && readerBatch.size > 0

    override def next(): Seq[Row] = {
      val size = readerBatch.size
      val rows = Vector.newBuilder[Row]
      for { rowIdx <- 0 until size } {
        val values = structDeserializer.readAt(structColumnVector, rowIdx)
        rows += Row(values.toSeq)
      }
      readerBatch.reset()
      rows.result()
    }
  }

}
