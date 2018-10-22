package com.exasol.s3etl.row

import org.apache.hadoop.conf.Configuration
import org.apache.parquet.hadoop.api.ReadSupport
import org.apache.parquet.hadoop.api.ReadSupport.ReadContext
import org.apache.parquet.io.api.Binary
import org.apache.parquet.io.api.Converter
import org.apache.parquet.io.api.GroupConverter
import org.apache.parquet.io.api.PrimitiveConverter
import org.apache.parquet.io.api.RecordMaterializer
import org.apache.parquet.schema.GroupType
import org.apache.parquet.schema.MessageType
import org.apache.parquet.schema.Type

final case class Row(val values: Seq[Any])

class RowReadSupport extends ReadSupport[Row] {

  override def prepareForRead(
    conf: Configuration,
    metadata: java.util.Map[String, String],
    messageType: MessageType,
    readContext: ReadContext
  ): RecordMaterializer[Row] =
    new RowRecordMaterializer(messageType, readContext)

  override def init(
    conf: Configuration,
    metadata: java.util.Map[String, String],
    messageType: MessageType
  ): ReadSupport.ReadContext = {
    val projection = conf.get(ReadSupport.PARQUET_READ_SCHEMA)
    val requestedSchema = ReadSupport.getSchemaForRead(messageType, projection)
    new ReadSupport.ReadContext(requestedSchema)
  }
}

class RowRecordMaterializer(messageType: MessageType, readContext: ReadContext)
    extends RecordMaterializer[Row] {

  override val getRootConverter: RowRootConverter = new RowRootConverter(messageType)
  override def skipCurrentRecord(): Unit = getRootConverter.start()
  override def getCurrentRecord: Row = Row(getRootConverter.currentResult.toSeq)
}

@SuppressWarnings(Array("org.wartremover.warts.Var"))
class RowRootConverter(schema: GroupType) extends GroupConverter {
  private val size = schema.getFieldCount
  private var values: Array[Any] = Array.ofDim[Any](size)
  private final val converters: Array[Converter] = {
    var arr = Array.ofDim[Converter](size)
    for { i <- 0 until size } {
      arr(i) = createNewConverter(schema.getType(i), i)
    }
    arr
  }

  private def createNewConverter(tpe: Type, idx: Int): Converter = {
    if (!tpe.isPrimitive()) {
      throw new IllegalArgumentException("Currently only primitive types are supported")
    }
    new RowPrimitiveConverter(this, idx)
  }

  def currentResult(): Array[Any] =
    values

  override def getConverter(idx: Int): Converter =
    converters(idx)

  override def start(): Unit =
    values = Array.ofDim(converters.size)

  override def end(): Unit = {}

}

final class RowPrimitiveConverter(val parent: RowRootConverter, val index: Int)
    extends PrimitiveConverter {

  override def addBinary(value: Binary): Unit =
    parent.currentResult.update(index, value.toStringUsingUTF8())

  override def addBoolean(value: Boolean): Unit =
    parent.currentResult.update(index, value)

  override def addDouble(value: Double): Unit =
    parent.currentResult.update(index, value)

  override def addFloat(value: Float): Unit =
    parent.currentResult.update(index, value)

  override def addInt(value: Int): Unit =
    parent.currentResult.update(index, value)

  override def addLong(value: Long): Unit =
    parent.currentResult.update(index, value)
}
