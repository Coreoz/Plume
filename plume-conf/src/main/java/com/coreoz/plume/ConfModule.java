package com.coreoz.plume;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public class ConfModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Config.class).toProvider(ConfigProvider.class);
	}

}
