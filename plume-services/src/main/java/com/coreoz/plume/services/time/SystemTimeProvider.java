package com.coreoz.plume.services.time;

import java.time.Clock;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SystemTimeProvider implements TimeProvider {

	private final Clock clock;

	@Inject
	public SystemTimeProvider() {
		this.clock = Clock.systemDefaultZone();
	}

	@Override
	public Clock clock() {
		return clock;
	}

}
