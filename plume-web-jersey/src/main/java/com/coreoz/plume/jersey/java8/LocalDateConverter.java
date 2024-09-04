package com.coreoz.plume.jersey.java8;

import java.time.LocalDate;

import jakarta.ws.rs.ext.ParamConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class LocalDateConverter implements ParamConverter<LocalDate> {

	private static final Logger logger = LoggerFactory.getLogger(LocalDateConverter.class);

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
