package com.exasol.cloudetl.filesystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.ScalaConverters;

class FileSystemManagerTest {
    private List<java.nio.file.Path> hidden;
    private List<java.nio.file.Path> files;
    private FileSystem fileSystem;
    private java.nio.file.Path temporaryDirectory;

    @BeforeEach
    void beforeEach() throws IOException {
        this.fileSystem = FileSystem.get(new Configuration());
        this.temporaryDirectory = Files.createTempDirectory("tempdir");
        this.files = List.of("a", "b", "c", "a.parquet", "b.parquet").stream().map(this.temporaryDirectory::resolve)
                .collect(Collectors.toList());
        this.hidden = List.of("_SUCCESS", ".hidden").stream().map(this.temporaryDirectory::resolve)
                .collect(Collectors.toList());
        for (final java.nio.file.Path file : concat(this.files, this.hidden)) {
            Files.createFile(file);
        }
    }

    @AfterEach
    void afterEach() throws IOException {
        for (final java.nio.file.Path file : concat(this.files, this.hidden)) {
            Files.deleteIfExists(file);
        }
        Files.deleteIfExists(this.temporaryDirectory);
    }

    @Test
    void getFilesReturnsPathsFromAPattern() throws IOException {
        assertEquals(getDefaultExpectedPaths(), getFiles(getDirPath() + "/*"));
    }

    @Test
    void getFilesReturnsPathsFromARegularPathWithoutGlob() throws IOException {
        assertEquals(getDefaultExpectedPaths(), getFiles(getDirPath()));
    }

    @Test
    void getFilesReturnsPathsFromTopDirectoryAndIgnoresPathsInSubDirectories() throws IOException {
        final java.nio.file.Path subDirectory = Files.createDirectories(Paths.get(getDirPath() + "/subDir/"));
        final java.nio.file.Path subDirectoryFile = Files.createFile(subDirectory.resolve("aa.parquet"));
        assertEquals(getDefaultExpectedPaths(), getFiles(getDirPath()));
        Files.deleteIfExists(subDirectoryFile);
        Files.deleteIfExists(subDirectory);
    }

    @Test
    void getFilesReturnsPathsFromAsteriskGlobbedDirectories() throws IOException {
        final java.nio.file.Path subDirectory = Files.createDirectories(Paths.get(getDirPath() + "/subDir/"));
        final java.nio.file.Path subDirectoryFileParquet = Files.createFile(subDirectory.resolve("aa.parquet"));
        final java.nio.file.Path subDirectoryFileRegular = Files.createFile(subDirectory.resolve("summary.txt"));
        final Set<Path> expectedPaths = new HashSet<>(getDefaultExpectedPaths());
        expectedPaths.add(new Path("file:" + subDirectoryFileParquet.toUri().getRawPath()));
        assertEquals(expectedPaths, getFiles(getDirPath() + "/{*,subDir/*.parquet}"));
        Files.deleteIfExists(subDirectoryFileParquet);
        Files.deleteIfExists(subDirectoryFileRegular);
        Files.deleteIfExists(subDirectory);
    }

    @Test
    void getFilesReturnsPathsFromDirectGlobbedFiles() throws IOException {
        final java.nio.file.Path subDirectory = Files.createDirectories(Paths.get(getDirPath() + "/subDir/"));
        final java.nio.file.Path subDirectoryFileParquet = Files.createFile(subDirectory.resolve("aa.parquet"));
        final Set<Path> expectedPaths = Set.of(new Path("file:" + getDirPath() + "/a.parquet"),
                new Path("file:" + subDirectoryFileParquet.toUri().getRawPath()));
        assertEquals(expectedPaths, getFiles(getDirPath() + "/{a.parquet,subDir/aa.parquet}"));
        Files.deleteIfExists(subDirectoryFileParquet);
        Files.deleteIfExists(subDirectory);
    }

    @Test
    void getFilesReturnsDirectFilePath() throws IOException {
        assertEquals(Set.of(new Path("file:" + getDirPath() + "/a.parquet")), getFiles(getDirPath() + "/a.parquet"));
    }

    @Test
    void getFilesReturnsPathsFromAPatternWithFileExtensions() throws IOException {
        final Set<Path> pathsWithExtensions = getFiles(getDirPath() + "/*.parquet");
        assertTrue(pathsWithExtensions.stream().map(path -> path.toUri().getRawPath()).allMatch(path -> path.contains("parquet")));
    }

    @Test
    void getFilesThrowsIfNoPathExists() {
        final FileNotFoundException thrown = assertThrows(FileNotFoundException.class,
                () -> new FileSystemManager(this.fileSystem).getFiles("emptyPath"));
        assertTrue(thrown.getMessage().contains("Provided file path 'emptyPath' does not exist."));
    }

    private String getDirPath() {
        return this.temporaryDirectory.toUri().getRawPath();
    }

    private Set<Path> getFiles(final String pattern) throws IOException {
        return new HashSet<>(ScalaConverters.asJavaList(new FileSystemManager(this.fileSystem).getFiles(pattern)));
    }

    private Set<Path> getDefaultExpectedPaths() {
        return this.files.stream().map(file -> new Path("file:" + file.toUri().getRawPath())).collect(Collectors.toSet());
    }

    private static <T> List<T> concat(final List<T> first, final List<T> second) {
        final List<T> result = new ArrayList<>(first);
        result.addAll(second);
        return result;
    }
}
