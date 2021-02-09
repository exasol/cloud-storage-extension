package com.exasol.cloudetl.filesystem

import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.PathFilter

object HiddenFilesFilter extends PathFilter {

  private[this] val PREFIX_DOT = "."
  private[this] val PREFIX_UNDERSCORE = "_"

  override def accept(p: Path): Boolean = {
    val name = p.getName()
    !name.startsWith(PREFIX_DOT) && !name.startsWith(PREFIX_UNDERSCORE)
  }

}
