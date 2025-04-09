package com.coreoz.plume.mocks;

import com.coreoz.test.GuiceTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

@GuiceTest({})
class MockedClockTest {
    static final Instant fixedInstant = Instant.parse("2000-01-01T10:00:00Z");
    static final Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

    static Clock clockInstance;

    @Inject
    MockedClock currentClock;

    @BeforeEach
    void setupClockInstance() {
        if (clockInstance == null) {
            clockInstance = currentClock;
        }
    }

    @Test
    void test_same_clock_instance_1() {
        assertThat(currentClock).isSameAs(clockInstance);
    }

    @Test
    void test_same_clock_instance_2() {
        assertThat(currentClock).isSameAs(clockInstance);
    }

    @Test
    void changeClock__when_newClockProvided__should_useNewClock() {
        // Arrange
        MockedClock mockedClock = new MockedClock();

        // Act
        mockedClock.changeClock(fixedClock);

        // Assert
        assertThat(mockedClock.instant()).isEqualTo(fixedClock.instant());
    }

    @Test
    void executeWithClock__when_functionExecuted__should_useNewClockTemporarily() {
        // Arrange
        MockedClock mockedClock = new MockedClock();

        // Act & Assert
        mockedClock.executeWithClock(fixedClock, () -> {
            assertThat(mockedClock.instant()).isEqualTo(fixedClock.instant());
        });

        // Verify clock reverted
        assertThat(mockedClock.instant()).isNotEqualTo(fixedClock.instant());
    }

    @Test
    void executeWithInstant__when_fixedInstantProvided__should_useFixedInstantTemporarily() {
        // Arrange
        MockedClock mockedClock = new MockedClock();

        // Act & Assert
        mockedClock.executeWithInstant(fixedInstant, () -> {
            assertThat(mockedClock.instant()).isEqualTo(fixedInstant);
        });

        // Verify clock reverted
        assertThat(mockedClock.instant()).isNotEqualTo(fixedInstant);
    }

    @Test
    void setConstantInstant__when_fixedInstantProvided__should_useFixedInstant() {
        // Arrange
        MockedClock mockedClock = new MockedClock();

        // Act
        mockedClock.setConstantInstant(fixedInstant);

        // Assert
        assertThat(mockedClock.instant()).isEqualTo(fixedInstant);
    }

    @Test
    void executeWithConstantTime__when_functionExecuted__should_useCurrentInstantTemporarily() {
        // Arrange
        MockedClock mockedClock = new MockedClock();
        Instant beforeExecution = Instant.now();

        // Act & Assert
        mockedClock.executeWithConstantTime(() -> {
            Instant duringExecution = mockedClock.instant();
            assertThat(duringExecution).isAfterOrEqualTo(beforeExecution);
        });
    }

    @Test
    void setTickingClockFromInstant__when_instantProvided__should_setOffsetClock() throws InterruptedException {
        // Arrange
        MockedClock mockedClock = new MockedClock();
        Instant futureInstant = Instant.now().plusSeconds(10);

        // Act
        mockedClock.setTickingClockFromInstant(futureInstant);
        Thread.sleep(1);

        assertThat(mockedClock.instant()).isAfter(futureInstant);
    }
}
