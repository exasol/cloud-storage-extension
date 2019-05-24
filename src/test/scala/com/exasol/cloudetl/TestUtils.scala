package com.exasol.cloudetl

import java.io.IOException
import java.math.BigDecimal
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.sql.Date
import java.sql.Timestamp

trait TestUtils {

  val BIG_DECIMAL_VALUE1: BigDecimal = new BigDecimal("5555555555555555555555555555555.55555")
  val BIG_DECIMAL_VALUE2: BigDecimal = new BigDecimal("5555555555555555555555555555555.55555")
  val DATE_VALUE1: Date = new Date(System.currentTimeMillis())
  val DATE_VALUE2: Date = new Date(System.currentTimeMillis())
  val TIMESTAMP_VALUE1: Timestamp = new Timestamp(System.currentTimeMillis())
  val TIMESTAMP_VALUE2: Timestamp = new Timestamp(System.currentTimeMillis())

  val rawRecords: Seq[Seq[Object]] = Seq(
    Seq(1, 3L, BIG_DECIMAL_VALUE1, 3.14d, "xyz", true, DATE_VALUE1, TIMESTAMP_VALUE1),
    Seq(2, 4L, BIG_DECIMAL_VALUE2, 0.13d, "abc", false, DATE_VALUE2, TIMESTAMP_VALUE2)
  ).map { seq =>
    seq.map(_.asInstanceOf[AnyRef])
  }

  final def createTemporaryFolder(name: String): Path =
    Files.createTempDirectory(name)

  final def deleteFiles(dir: Path): Unit = {
    Files.walkFileTree(
      dir,
      new SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          Files.delete(file)
          FileVisitResult.CONTINUE
        }
        override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
          Files.delete(dir)
          FileVisitResult.CONTINUE
        }
      }
    )
    ()
  }

}
