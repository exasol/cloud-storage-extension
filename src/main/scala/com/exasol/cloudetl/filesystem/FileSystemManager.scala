package com.exasol.cloudetl.filesystem

import java.io.FileNotFoundException
import java.nio.file.Path

import com.exasol.errorreporting.ExaError

import org.apache.hadoop.fs.{Path => HadoopPath}
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.fs.FileSystem

/**
 * A filesystem helper class.
 */
final case class FileSystemManager(fileSystem: FileSystem) {

  /**
   * Returns list of files from path with or without globbing.
   *
   * It also adds paths from the sub-directories to the returned list.
   *
   * @param path input path
   * @return list of paths
   */
  def getFiles(path: String): Seq[HadoopPath] = globStatus(path)

  /**
   * Returns list of files from local path.
   */
  def getLocalFiles(path: Path): Seq[HadoopPath] =
    getFiles(path.toAbsolutePath().toUri().getRawPath())

  private[this] def globStatus(path: String): Seq[HadoopPath] = {
    val hadoopPath = new HadoopPath(path)
    val statuses = fileSystem.globStatus(hadoopPath, HiddenFilesFilter)
    if (statuses == null) {
      throw new FileNotFoundException(
        ExaError
          .messageBuilder("E-CSE-1")
          .message("Provided file path {{PATH}} does not exist.")
          .parameter("PATH", path)
          .mitigation("Please use valid file path.")
          .toString()
      )
    } else {
      if (isSingleDirectory(statuses)) {
        // User path is a single directory without any glob, only then list the files.
        listFiles(fileSystem.listStatus(statuses(0).getPath(), HiddenFilesFilter))
      } else {
        listFiles(statuses)
      }
    }
  }

  // Check if user provided path returns a single directory
  private[this] def isSingleDirectory(paths: Array[FileStatus]): Boolean =
    paths.length == 1 && fileSystem.getFileStatus(paths(0).getPath()).isDirectory()

  private[this] def listFiles(paths: Array[FileStatus]): Seq[HadoopPath] =
    paths.toSeq.filter(_.isFile()).map(_.getPath())

}
