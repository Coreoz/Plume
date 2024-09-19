package com.coreoz.plume.jersey.jackson;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// check https://github.com/FasterXML/jackson-databind/issues/779 to see when we can have the SafeObjectMapper
@Singleton
public class ObjectMapperProvider implements Provider<ObjectMapper> {

	private final ObjectMapper objectMapper;

	@Inject
	public ObjectMapperProvider() {
		this.objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	public ObjectMapper get() {
		return objectMapper;
	}

}

