package com.exasol.cloudetl.parallelism;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.ExaMetadata;

class AbstractUdfCountCalculatorTest {
    private static final int MB = 1_000_000;
    private static final BigInteger GB = new BigInteger("1000000000");
    private static final int TOTAL_NUMBER_OF_NODES = 2;
    private final ExaMetadata metadata = mock(ExaMetadata.class);

    @BeforeEach
    void beforeEach() {
        when(this.metadata.getNodeCount()).thenReturn((long) TOTAL_NUMBER_OF_NODES);
    }

    @Test
    void calculatesFixedBasedNumberOfUdfInstances() {
        final long[][] testData = { { 2L, 20, 8 }, { 4L, 20, 16 }, { 20L, 10, 20 } };
        for (final long[] row : testData) {
            when(this.metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(row[0])));
            assertThat(new FixedUdfCountCalculator(this.metadata).getUdfCount((int) row[1]), equalTo((int) row[2]));
        }
    }

    @Test
    void calculatesMemoryBasedNumberOfUdfInstances() {
        final long[][] testData = { { 2L, 20, 500L, 8 }, { 4L, 20, 150L, 40 }, { 20L, 10, 1000L, 20 },
                { 10L, 25, 700L, 28 } };
        for (final long[] row : testData) {
            when(this.metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(row[0])));
            assertThat(new MemoryUdfCountCalculator(this.metadata, row[2] * MB).getUdfCount((int) row[1]),
                    equalTo((int) row[3]));
        }
    }
}
