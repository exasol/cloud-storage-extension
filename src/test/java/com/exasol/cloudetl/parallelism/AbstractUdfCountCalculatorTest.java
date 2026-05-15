package com.exasol.cloudetl.parallelism;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({ "2, 20, 8", "4, 20, 16", "20, 10, 20" })
    void calculatesFixedBasedNumberOfUdfInstances(final long memoryLimitInGb, final int maxInstances,
            final int expectedUdfCount) {
        when(this.metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(memoryLimitInGb)));
        assertThat(new FixedUdfCountCalculator(this.metadata).getUdfCount(maxInstances), equalTo(expectedUdfCount));
    }

    @ParameterizedTest
    @CsvSource({ "2, 20, 500, 8", "4, 20, 150, 40", "20, 10, 1000, 20", "10, 25, 700, 28" })
    void calculatesMemoryBasedNumberOfUdfInstances(final long memoryLimitInGb, final int maxInstances,
            final long memoryPerInstanceInMb, final int expectedUdfCount) {
        when(this.metadata.getMemoryLimit()).thenReturn(GB.multiply(BigInteger.valueOf(memoryLimitInGb)));
        assertThat(new MemoryUdfCountCalculator(this.metadata, memoryPerInstanceInMb * MB).getUdfCount(maxInstances),
                equalTo(expectedUdfCount));
    }
}
