package com.coreoz.plume.services.time;

import java.time.Clock;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @deprecated {@link Clock} should be used instead
 */
@Deprecated
@Singleton
public class SystemTimeProvider implements TimeProvider {

	private final Clock clock;

	@Inject
	public SystemTimeProvider(Clock clock) {
		this.clock = clock;
	}

	@Override
	public Clock clock() {
		return clock;
	}

}
