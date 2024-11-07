package com.coreoz.plume.guice;

import com.coreoz.plume.services.time.ClockProvider;
import com.coreoz.plume.services.time.SystemTimeProvider;
import com.coreoz.plume.services.time.TimeProvider;
import com.google.inject.AbstractModule;

import java.time.Clock;

public class GuiceServicesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Clock.class).toProvider(ClockProvider.class);
		bind(TimeProvider.class).to(SystemTimeProvider.class);
	}

}
