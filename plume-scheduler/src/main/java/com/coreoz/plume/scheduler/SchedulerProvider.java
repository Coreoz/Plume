package com.coreoz.plume.scheduler;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.SchedulerConfig;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.time.Clock;

@Singleton
public class SchedulerProvider implements Provider<Scheduler> {

	private final Scheduler scheduler;

	@Inject
	public SchedulerProvider(Clock clock) {
		this.scheduler = new Scheduler(
			SchedulerConfig
				.builder()
				.timeProvider(new PlumeTimeProvider(clock))
				.build()
		);
	}

	@Override
	public Scheduler get() {
		return scheduler;
	}

}
