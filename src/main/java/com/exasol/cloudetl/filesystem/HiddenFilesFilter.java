package com.exasol.cloudetl.filesystem;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/** Filters hidden filesystem entries. */
public final class HiddenFilesFilter implements PathFilter {
    /** Singleton instance. */
    public static final HiddenFilesFilter INSTANCE = new HiddenFilesFilter();

    private HiddenFilesFilter() {
        // Singleton.
    }

    @Override
    public boolean accept(final Path path) {
        final String name = path.getName();
        return !name.startsWith(".") && !name.startsWith("_");
    }
}
