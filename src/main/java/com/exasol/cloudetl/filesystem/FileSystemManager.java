package com.exasol.cloudetl.filesystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.exasol.cloudetl.ScalaConverters;
import com.exasol.errorreporting.ExaError;

/** Filesystem helper. */
public final class FileSystemManager {
    private final FileSystem fileSystem;

    /** Create a manager. */
    public FileSystemManager(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /** Factory for Java callers. */
    public static FileSystemManager apply(final FileSystem fileSystem) {
        return new FileSystemManager(fileSystem);
    }

    /** Return matching files. */
    public scala.collection.immutable.Seq<Path> getFiles(final String path) throws java.io.IOException {
        return globStatus(path);
    }

    /** Return matching local files. */
    public scala.collection.immutable.Seq<Path> getLocalFiles(final java.nio.file.Path path) throws java.io.IOException {
        return getFiles(path.toAbsolutePath().toUri().getRawPath());
    }

    private scala.collection.immutable.Seq<Path> globStatus(final String path) throws java.io.IOException {
        final Path hadoopPath = new Path(path);
        final FileStatus[] statuses = this.fileSystem.globStatus(hadoopPath, HiddenFilesFilter.INSTANCE);
        if (statuses == null) {
            throw new FileNotFoundException(ExaError.messageBuilder("E-CSE-1")
                    .message("Provided file path {{PATH}} does not exist.", path).mitigation("Please use valid file path.")
                    .toString());
        }
        if (isSingleDirectory(statuses)) {
            return listFiles(this.fileSystem.listStatus(statuses[0].getPath(), HiddenFilesFilter.INSTANCE));
        }
        return listFiles(statuses);
    }

    private boolean isSingleDirectory(final FileStatus[] paths) throws java.io.IOException {
        return paths.length == 1 && this.fileSystem.getFileStatus(paths[0].getPath()).isDirectory();
    }

    private scala.collection.immutable.Seq<Path> listFiles(final FileStatus[] paths) {
        final List<Path> result = new ArrayList<>();
        for (final FileStatus path : paths) {
            if (path.isFile()) {
                result.add(path.getPath());
            }
        }
        return ScalaConverters.seqFromJava(result);
    }
}
