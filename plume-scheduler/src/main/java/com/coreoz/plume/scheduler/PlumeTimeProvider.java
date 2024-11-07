package com.coreoz.plume.scheduler;

import com.coreoz.wisp.time.TimeProvider;

import java.time.Clock;

class PlumeTimeProvider implements TimeProvider {

	private final Clock clock;

	public PlumeTimeProvider(Clock clock) {
		this.clock = clock;
	}

	@Override
	public long currentTime() {
		return clock.millis();
	}

}
