package com.coreoz.plume.scheduler;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import com.coreoz.plume.services.time.TimeProvider;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.SchedulerConfig;

@Singleton
public class SchedulerProvider implements Provider<Scheduler> {

	private final Scheduler scheduler;

	@Inject
	public SchedulerProvider(TimeProvider timeProvider) {
		this.scheduler = new Scheduler(
			SchedulerConfig
				.builder()
				.timeProvider(new PlumeTimeProvider(timeProvider))
				.build()
		);
	}

	@Override
	public Scheduler get() {
		return scheduler;
	}

}
