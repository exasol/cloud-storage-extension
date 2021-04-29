package com.exasol.cloudetl.filesystem

import java.io.FileNotFoundException
import java.nio.file.Path

import scala.collection.mutable.ListBuffer

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
        s"Provided file path '$path' does not exist. Please use valid path."
      )
    } else {
      listStatus(statuses)
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Recursion"))
  private[this] def listStatus(paths: Array[FileStatus]): Seq[HadoopPath] = {
    val files = new ListBuffer[HadoopPath]
    def addFilesRecursively(fileStatuses: Array[FileStatus]): Unit =
      fileStatuses.foreach {
        case fileStatus =>
          if (fileStatus.isDirectory()) {
            addFilesRecursively(fileSystem.listStatus(fileStatus.getPath(), HiddenFilesFilter))
          } else {
            files += fileStatus.getPath()
          }
      }
    addFilesRecursively(paths)
    files.toSeq
  }

}
