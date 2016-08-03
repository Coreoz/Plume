package com.coreoz.plume.jersey.jackson;

import javax.inject.Provider;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Singleton
public class ObjectMapperProvider implements Provider<ObjectMapper> {

	private final ObjectMapper objectMapper = new ObjectMapper()
		.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	@Override
	public ObjectMapper get() {
		return objectMapper;
	}

}
