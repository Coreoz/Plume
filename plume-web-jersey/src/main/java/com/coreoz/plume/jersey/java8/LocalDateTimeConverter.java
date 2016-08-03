package com.coreoz.plume.jersey.java8;

import java.time.LocalDateTime;

import javax.ws.rs.ext.ParamConverter;

import com.google.common.base.Strings;

public class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {

	@Override
	public LocalDateTime fromString(String value) {
		return Strings.isNullOrEmpty(value) ? null : LocalDateTime.parse(value);
	}

	@Override
	public String toString(LocalDateTime value) {
		return value == null ? null : value.toString();
	}

}
