package com.coreoz.plume.scheduler.guice;

import com.coreoz.plume.guice.GuiceServicesModule;
import com.coreoz.plume.scheduler.SchedulerProvider;
import com.coreoz.wisp.Scheduler;
import com.google.inject.AbstractModule;

public class GuiceSchedulerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GuiceServicesModule());
		bind(Scheduler.class).toProvider(SchedulerProvider.class);
	}

}
