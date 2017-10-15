package com.coreoz.plume.jersey.java8;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.ws.rs.ext.ParamConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class InstantConverter implements ParamConverter<Instant> {

	private static final Logger logger = LoggerFactory.getLogger(InstantConverter.class);

	@Override
	public Instant fromString(String value) {
		try {
			if (Strings.isNullOrEmpty(value)) {
				return null;
			}

			if (value.endsWith("Z")) {
				return Instant.parse(value);
			}

			return LocalDateTime.parse(value).atZone(ZoneId.systemDefault()).toInstant();
		} catch (Exception e) {
			logger.warn("Cannot parse Instant from {}", value, e);
			return null;
		}
	}

	@Override
	public String toString(Instant value) {
		return value == null ? null : value.toString();
	}

}
