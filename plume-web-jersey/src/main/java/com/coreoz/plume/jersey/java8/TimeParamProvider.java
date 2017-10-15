package com.coreoz.plume.jersey.java8;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

public class TimeParamProvider implements ParamConverterProvider {

	private static final LocalDateConverter LOCAL_DATE_CONVERTER = new LocalDateConverter();
	private static final LocalDateTimeConverter LOCAL_DATE_TIME_CONVERTER = new LocalDateTimeConverter();
	private static final InstantConverter INSTANT_CONVERTER = new InstantConverter();

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType == LocalDate.class) {
			return (ParamConverter<T>) LOCAL_DATE_CONVERTER;
		}
		if (rawType == LocalDateTime.class) {
			return (ParamConverter<T>) LOCAL_DATE_TIME_CONVERTER;
		}
		if (rawType == Instant.class) {
			return (ParamConverter<T>) INSTANT_CONVERTER;
		}
		return null;
	}

}
