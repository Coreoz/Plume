package com.coreoz.plume.jersey.java8;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

@Slf4j
public class LocalDateConverter implements ParamConverter<LocalDate> {
	@Override
	public LocalDate fromString(String value) {
		try {
			return Strings.isNullOrEmpty(value) ? null : LocalDate.parse(value);
		} catch (Exception e) {
			logger.warn("Cannot parse LocalDate from {}", value, e);
			return null;
		}
	}

	@Override
	public String toString(LocalDate value) {
		return value == null ? null : value.toString();
	}
}
