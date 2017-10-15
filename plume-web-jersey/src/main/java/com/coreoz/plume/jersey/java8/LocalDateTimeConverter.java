package com.coreoz.plume.jersey.java8;

import java.time.LocalDateTime;

import javax.ws.rs.ext.ParamConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {

	private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeConverter.class);

	@Override
	public LocalDateTime fromString(String value) {
		try {
			return Strings.isNullOrEmpty(value) ? null : LocalDateTime.parse(value);
		} catch (Exception e) {
			logger.warn("Cannot parse LocalDateTime from {}", value, e);
			return null;
		}
	}

	@Override
	public String toString(LocalDateTime value) {
		return value == null ? null : value.toString();
	}

}
