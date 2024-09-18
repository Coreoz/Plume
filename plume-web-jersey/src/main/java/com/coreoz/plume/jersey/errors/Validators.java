package com.coreoz.plume.jersey.errors;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.common.base.Strings;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Provides common validators that will throw {@link WsException}
 * when the input data does not pass the validator.
 */
public class Validators {
	public static @Nonnull <T> T checkRequired(@Nullable T parameter) {
		if(parameter == null) {
			throw new WsException(WsError.FIELD_REQUIRED);
		}
		return parameter;
	}

	public static @Nonnull String checkRequired(@Nonnull String fieldName, @Nullable String fieldValue) {
		if(Strings.isNullOrEmpty(fieldValue)) {
			throw new WsException(WsError.FIELD_REQUIRED, List.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nonnull <T> T checkRequired(@Nonnull String fieldName, @Nullable T fieldValue) {
		if(fieldValue == null) {
			throw new WsException(WsError.FIELD_REQUIRED, List.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkEmail(@Nonnull String fieldName, @Nullable String fieldValue) {
		if(!EmailValidator.getInstance().isValid(fieldValue)) {
			throw new WsException(WsError.EMAIL_INVALID, List.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkHexaColor(@Nonnull String fieldName, @Nullable String fieldValue) {
		if(fieldValue != null && !fieldValue.matches("[0-9a-fA-F]{6}")) {
			throw new WsException(WsError.COLOR_INVALID, List.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkHexaColorWithStartingHash(@Nonnull String fieldName, @Nullable String fieldValue) {
		if(fieldValue != null) {
            if (!fieldValue.startsWith("#")) {
                throw new WsException(WsError.COLOR_INVALID, List.of(fieldName));
            }
            return "#" + checkHexaColor(fieldName, fieldValue.substring(1));
		}
		return null;
	}
}
