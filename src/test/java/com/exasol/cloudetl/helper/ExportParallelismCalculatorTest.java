package com.exasol.cloudetl.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.ExaMetadata;
import com.exasol.cloudetl.storage.StorageProperties;

class ExportParallelismCalculatorTest {
    private final ExaMetadata metadata = mock(ExaMetadata.class);

    @BeforeEach
    void beforeEach() {
        when(this.metadata.getNodeCount()).thenReturn(1L);
        when(this.metadata.getMemoryLimit()).thenReturn(new BigInteger("2000000000"));
    }

    @Test
    void getParallelismReturnsUserProvidedValue() {
        final StorageProperties properties = new StorageProperties(Map.of("PARALLELISM", "iproc()"));
        assertEquals("iproc()", new ExportParallelismCalculator(this.metadata, properties).getParallelism());
    }

    @Test
    void getParallelismReturnsCalculatedValue() {
        assertTrue(new ExportParallelismCalculator(this.metadata, new StorageProperties(Map.of())).getParallelism()
                .contains("iproc(), mod(rownum,"));
    }
}
