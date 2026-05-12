package com.exasol.cloudetl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import scala.Option;
import scala.collection.immutable.Seq;
import scala.jdk.javaapi.CollectionConverters;

/**
 * Conversion helpers for the Scala APIs that remain in dependencies and tests.
 */
public final class ScalaConverters {
    private ScalaConverters() {
        // Utility class.
    }

    /** Convert a Java map to an immutable Scala map. */
    public static <K, V> scala.collection.immutable.Map<K, V> mapFromJava(final Map<K, V> map) {
        return scala.collection.immutable.Map$.MODULE$.from(CollectionConverters.asScala(map));
    }

    /** Convert an immutable Scala map to a mutable Java copy. */
    public static <K, V> Map<K, V> javaMapCopy(final scala.collection.immutable.Map<K, V> map) {
        return new LinkedHashMap<>(CollectionConverters.asJava(map));
    }

    /** Convert a Java collection to an immutable Scala sequence. */
    public static <T> Seq<T> seqFromJava(final Collection<T> values) {
        return scala.collection.immutable.Seq$.MODULE$.from(CollectionConverters.asScala(values));
    }

    /** Convert a Scala sequence to a Java list view. */
    public static <T> List<T> asJavaList(final scala.collection.Seq<T> values) {
        return CollectionConverters.asJava(values);
    }

    /** Return the option value or the provided default value. */
    public static <T> T optionOrElse(final Option<T> option, final T defaultValue) {
        return option.isDefined() ? option.get() : defaultValue;
    }
}
