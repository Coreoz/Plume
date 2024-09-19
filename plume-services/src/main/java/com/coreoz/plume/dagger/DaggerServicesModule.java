package com.coreoz.plume.dagger;

import jakarta.inject.Singleton;

import com.coreoz.plume.services.time.SystemTimeProvider;
import com.coreoz.plume.services.time.TimeProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerServicesModule {

	@Provides
	@Singleton
	static TimeProvider provideTimeProvider(SystemTimeProvider systemTimeProvider) {
		return systemTimeProvider;
	}

}
