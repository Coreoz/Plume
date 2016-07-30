package com.coreoz.plume.conf.dagger;

import com.coreoz.plume.conf.ConfigProvider;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaggerConfModule {

	@Provides
	@Singleton
	static Config provideConfig(ConfigProvider configProvider) {
		return configProvider.get();
	}

}
