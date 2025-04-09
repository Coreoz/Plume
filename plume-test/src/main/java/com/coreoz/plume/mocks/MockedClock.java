package com.coreoz.plume.mocks;

import jakarta.inject.Singleton;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A mocked {@link Clock} object for unit tests on code that uses a {@link Clock}
 */
@Singleton
public class MockedClock extends Clock {
    private Clock baseClock = Clock.systemDefaultZone();

    /**
     * Changes the current clock used. This is generally a temporary measure that should be reverted.
     * See {@link #executeWithClock(Clock, Runnable)} for usage
     * @param newClock The new clock to use
     */
    public void changeClock(Clock newClock) {
        this.baseClock = newClock;
    }

    /**
     * Execute a function with a custom clock. If unsure, use {@link #executeWithInstant(Instant, Runnable)} or {@link #executeWithConstantTime(Runnable)} instead
     * @param newClock The custom clock
     * @param toExecuteWithClock The function to execute
     */
    public void executeWithClock(Clock newClock, Runnable toExecuteWithClock) {
        Clock oldClock = this.baseClock;
        changeClock(newClock);
        toExecuteWithClock.run();
        changeClock(oldClock);
    }

    /**
     * Execute a function for which for time does not change
     * @param fixedInstantForExecution The instant that will be used to execute the function
     * @param toExecuteWithInstant The function to execute
     */
    public void executeWithInstant(Instant fixedInstantForExecution, Runnable toExecuteWithInstant) {
        executeWithClock(
            Clock.fixed(fixedInstantForExecution, ZoneId.systemDefault()),
            toExecuteWithInstant
        );
    }

    public MockedClock setConstantInstant(Instant instant) {
        this.baseClock = Clock.fixed(instant, ZoneId.systemDefault());
        return this;
    }

    /**
     * Advance the current clock to a duration and set it as a constant time, see {@link #setConstantInstant(Instant)}
     * @param timeToAdvance The time to advance
     */
    public MockedClock advanceTime(Duration timeToAdvance) {
        return setConstantInstant(instant().plus(timeToAdvance));
    }

    /**
     * Advance the current clock to a duration in millis and set it as a constant time, see {@link #setConstantInstant(Instant)}
     * @param timeInMillis The time to advance in milliseconds
     */
    public MockedClock advanceTime(long timeInMillis) {
        return setConstantInstant(instant().plusMillis(timeInMillis));
    }

    /**
     * Execute a function for which for time does not change
     * @param toExecuteWithConstantTime The function to execute
     */
    public void executeWithConstantTime(Runnable toExecuteWithConstantTime) {
        executeWithInstant(Instant.now(), toExecuteWithConstantTime);
    }

    public MockedClock setConstantTime(LocalTime localTime) {
        return setConstantInstant(localTimeToInstant(localTime));
    }

    public MockedClock setTickingClockFromInstant(Instant instant) {
        this.baseClock = Clock.offset(Clock.systemDefaultZone(), Duration.ofMillis(instant.toEpochMilli() - System.currentTimeMillis()));
        return this;
    }

    public static Instant localTimeToInstant(LocalTime localTime) {
        return ZonedDateTime
            .ofInstant(Instant.now(), ZoneId.systemDefault())
            .withHour(localTime.getHour())
            .withMinute(localTime.getMinute())
            .withSecond(localTime.getSecond())
            .withNano(localTime.getNano())
            .toInstant();
    }

    // Overrides

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.system(zone);
    }

    @Override
    public Instant instant() {
        return baseClock.instant();
    }
}
