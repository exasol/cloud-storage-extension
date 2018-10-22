package com.exasol.s3etl.util

import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

object FsUtil {

  def globWithLocal(path: java.nio.file.Path, fs: FileSystem): Seq[Path] =
    globWithPattern(path.toAbsolutePath.toUri.getRawPath, fs)

  def globWithPattern(pattern: String, fs: FileSystem): Seq[Path] =
    glob(new Path(pattern), fs)

  def glob(path: Path, fs: FileSystem): Seq[Path] = {
    val opt = Option(fs.globStatus(path))
    opt.fold(Seq.empty[Path])(_.toSeq.map(_.getPath))
  }

}
