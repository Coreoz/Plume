package com.coreoz.plume.jersey.dagger;

import com.coreoz.plume.jersey.jackson.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;

import jakarta.inject.Singleton;

@Module
public class DaggerJacksonModule {

	@Provides
	@Singleton
	static ObjectMapper provideObjectMapper(ObjectMapperProvider objectMapperProvider) {
		return objectMapperProvider.get();
	}

}
