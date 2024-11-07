package com.coreoz.plume.services.time;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.time.Clock;

@Singleton
public class ClockProvider implements Provider<Clock> {
    private final Clock clock;

    @Inject
    public ClockProvider() {
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public Clock get() {
        return clock;
    }
}
