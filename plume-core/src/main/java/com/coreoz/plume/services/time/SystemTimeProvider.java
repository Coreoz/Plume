package com.coreoz.plume.services.time;

import java.time.Clock;

import javax.inject.Singleton;

@Singleton
public class SystemTimeProvider implements TimeProvider {

	private final Clock clock = Clock.systemDefaultZone();

	@Override
	public Clock clock() {
		return clock;
	}

}
