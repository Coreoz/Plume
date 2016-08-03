package com.coreoz.plume.jersey.java8;

import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;

import com.google.common.base.Strings;

public class LocalDateConverter implements ParamConverter<LocalDate> {

	@Override
	public LocalDate fromString(String value) {
		return Strings.isNullOrEmpty(value) ? null : LocalDate.parse(value);
	}

	@Override
	public String toString(LocalDate value) {
		return value == null ? null : value.toString();
	}

}
