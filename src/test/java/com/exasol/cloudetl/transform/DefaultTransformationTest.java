package com.exasol.cloudetl.transform;

import static com.exasol.cloudetl.constants.Constants.TRUNCATE_STRING;
import static com.exasol.cloudetl.helper.StringGenerator.getRandomString;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.cloudetl.storage.StorageProperties;

class DefaultTransformationTest {
    private final String longString = getRandomString(2_000_010);
    private final Object[] values = { 1, 3.14, "abc", this.longString };

    @Test
    void truncatesStringsLargerThanMaximumVarcharSize() {
        final StorageProperties properties = new StorageProperties(Map.of(TRUNCATE_STRING, "true"));
        final Object[] expected = { 1, 3.14, "abc", this.longString.substring(0, 2_000_000) };
        assertArrayEquals(expected, new DefaultTransformation(properties).transform(this.values.clone()));
    }

    @Test
    void passesIfThereIsNoStringsLargerThanMaximumVarcharSize() {
        final StorageProperties properties = new StorageProperties(Map.of(TRUNCATE_STRING, "true"));
        final Object[] input = { "abc", getRandomString(255), getRandomString(2_000_000) };
        assertArrayEquals(input, new DefaultTransformation(properties).transform(input.clone()));
    }

    @Test
    void throwsStringsLargerThanMaximumVarcharSizeIfNoTruncateStringParameter() {
        final StorageProperties properties = new StorageProperties(Map.of());
        final IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> new DefaultTransformation(properties).transform(this.values.clone()));
        assertTrue(thrown.getMessage().startsWith("E-CSE-29"));
    }

    @Test
    void throwsStringsLargerThanMaximumVarcharSizeIfTruncateStringParameterIsFalse() {
        final StorageProperties properties = new StorageProperties(Map.of(TRUNCATE_STRING, "false"));
        final IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> new DefaultTransformation(properties).transform(this.values.clone()));
        assertTrue(thrown.getMessage().startsWith("E-CSE-29"));
    }
}
