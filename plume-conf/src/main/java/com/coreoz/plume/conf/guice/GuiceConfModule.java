package com.coreoz.plume.conf.guice;

import com.coreoz.plume.conf.ConfigProvider;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public class GuiceConfModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Config.class).toProvider(ConfigProvider.class);
	}

}
