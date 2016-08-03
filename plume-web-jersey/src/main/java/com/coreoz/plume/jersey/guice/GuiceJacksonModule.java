package com.coreoz.plume.jersey.guice;

import com.coreoz.plume.jersey.jackson.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

public class GuiceJacksonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);
	}

}
