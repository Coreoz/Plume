package com.coreoz.plume.guice;

import com.coreoz.plume.services.time.SystemTimeProvider;
import com.coreoz.plume.services.time.TimeProvider;
import com.google.inject.AbstractModule;

public class GuiceCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TimeProvider.class).to(SystemTimeProvider.class);
	}

}
