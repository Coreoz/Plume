package com.coreoz.plume.jersey.java8;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDateTime;

@Slf4j
public class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {
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
