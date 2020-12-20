package com.exasol.cloudetl.orc

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hive.ql.exec.vector._
import org.apache.orc.OrcFile
import org.apache.orc.TypeDescription
import org.apache.orc.TypeDescription.Category

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
class OrcWriter(path: Path, conf: Configuration) {

  final def write[T <: Any](
    schema: TypeDescription,
    values: Map[String, T]
  ): Unit = {
    val writer = OrcFile.createWriter(path, OrcFile.writerOptions(conf).setSchema(schema))
    val schemaChildren = schema.getChildren()
    val batch = schema.createRowBatch()
    val columnWriters = Array.ofDim[(Any, Int) => Unit](schemaChildren.size())
    for { i <- 0 until schemaChildren.size() } {
      columnWriters(i) = getColumnSetter(schemaChildren.get(i), batch.cols(i))
    }

    batch.size = 0
    values.foreach {
      case (_, value) =>
        columnWriters.foreach(writer => writer(value, batch.size))
        batch.size += 1
    }
    writer.addRowBatch(batch)
    writer.close()
  }

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Equals",
      "org.wartremover.warts.Throw",
      "org.wartremover.warts.Recursion"
    )
  )
  private[this] def getColumnSetter(
    orcType: TypeDescription,
    column: ColumnVector
  ): (Any, Int) => Unit =
    orcType.getCategory() match {
      case Category.INT | Category.LONG =>
        val valueColumn = column.asInstanceOf[LongColumnVector]
        (value: Any, rowIndex: Int) =>
          value match {
            case x: Int   => valueColumn.vector(rowIndex) = x.toLong
            case x: Long  => valueColumn.vector(rowIndex) = x
            case x: Short => valueColumn.vector(rowIndex) = x.toLong
            case x: Byte  => valueColumn.vector(rowIndex) = x.toLong
            case _ =>
              valueColumn.noNulls = false
              valueColumn.isNull(rowIndex) = true
          }
      case Category.DOUBLE =>
        val valueColumn = column.asInstanceOf[DoubleColumnVector]
        (value: Any, rowIndex: Int) =>
          value match {
            case d: Double => valueColumn.vector(rowIndex) = d
            case f: Float  => valueColumn.vector(rowIndex) = f.toDouble
            case _ =>
              valueColumn.noNulls = false
              valueColumn.isNull(rowIndex) = true
          }
      case Category.STRING =>
        val valueColumn = column.asInstanceOf[BytesColumnVector]
        (value: Any, rowIndex: Int) =>
          value match {
            case str: String =>
              valueColumn.setVal(rowIndex, str.getBytes("UTF-8"))
            case _ =>
              valueColumn.noNulls = false
              valueColumn.isNull(rowIndex) = true
          }
      case Category.LIST =>
        val valueColumn = column.asInstanceOf[ListColumnVector]
        val innerSetter = getColumnSetter(orcType.getChildren().get(0), valueColumn.child)
        val setListNull = (index: Int) => {
          valueColumn.lengths(index) = 0
          valueColumn.isNull(index) = true
          valueColumn.noNulls = false
        }
        (value: Any, rowIndex: Int) =>
          value match {
            case seq: Iterable[_] if seq.nonEmpty =>
              val len = seq.size
              valueColumn.offsets(rowIndex) = valueColumn.childCount.toLong
              valueColumn.lengths(rowIndex) = len.toLong
              valueColumn.childCount += len
              valueColumn.child
                .ensureSize(valueColumn.childCount, valueColumn.offsets(rowIndex) != 0)
              var offset = 0
              seq.foreach { v =>
                innerSetter(v, valueColumn.offsets(rowIndex).toInt + offset)
                offset += 1
              }
            case _ =>
              setListNull(rowIndex)
          }
      case Category.MAP =>
        val valueColumn = column.asInstanceOf[MapColumnVector]
        val keySetter = getColumnSetter(orcType.getChildren.get(0), valueColumn.keys)
        val valueSetter = getColumnSetter(orcType.getChildren.get(1), valueColumn.values)
        val setMapNull = (index: Int) => {
          valueColumn.lengths(index) = 0
          valueColumn.isNull(index) = true
          valueColumn.noNulls = false
        }
        (value: Any, rowIndex: Int) =>
          value match {
            case map: Map[_, _] if map != null && map.nonEmpty =>
              val len = map.size
              valueColumn.offsets(rowIndex) = valueColumn.childCount.toLong
              valueColumn.lengths(rowIndex) = len.toLong
              valueColumn.childCount += len
              valueColumn.keys
                .ensureSize(valueColumn.childCount, valueColumn.offsets(rowIndex) != 0)
              valueColumn.values
                .ensureSize(valueColumn.childCount, valueColumn.offsets(rowIndex) != 0)
              var offset = 0
              map.foreach {
                case (key, value) =>
                  keySetter(key, valueColumn.offsets(rowIndex).toInt + offset)
                  valueSetter(value, valueColumn.offsets(rowIndex).toInt + offset)
                  offset += 1
              }
            case _ => setMapNull(rowIndex)
          }
      case Category.STRUCT =>
        val columns = orcType.getChildren()
        val valueColumn = column.asInstanceOf[StructColumnVector]
        val fieldNames = orcType.getFieldNames()
        val fieldSetters = (0 until columns.size()).map {
          case idx =>
            fieldNames.get(idx) -> getColumnSetter(columns.get(idx), valueColumn.fields(idx))
        }.toMap
        val setStructNull = (index: Int) => {
          valueColumn.isNull(index) = true
          valueColumn.noNulls = false
        }
        (value: Any, rowIndex: Int) =>
          value match {
            case m: Map[_, _] =>
              val map = m.asInstanceOf[Map[String, Any]]
              fieldSetters.foreach {
                case (key, setter) =>
                  val mapValue = map.getOrElse(key, null)
                  setter(mapValue, rowIndex)
              }
            case _ => setStructNull(rowIndex)
          }
      case _ => throw new UnsupportedOperationException(s"Unknown writer type '$orcType'")
    }
}
