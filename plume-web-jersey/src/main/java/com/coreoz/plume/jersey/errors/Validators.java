package com.coreoz.plume.jersey.errors;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides common validators that will throw {@link WsException}
 * when the input data does not pass the validator.
 */
public class Validators {
	public static<T> @NotNull T checkRequired(@Nullable T parameter) {
		if(parameter == null) {
			throw new WsException(WsError.FIELD_REQUIRED);
		}
		return parameter;
	}

	public static @NotNull String checkRequired(@NotNull String fieldName, @Nullable String fieldValue) {
		if(Strings.isNullOrEmpty(fieldValue)) {
			throw new WsException(WsError.FIELD_REQUIRED, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static<T> @NotNull T checkRequired(@NotNull String fieldName, @Nullable T fieldValue) {
		if(fieldValue == null) {
			throw new WsException(WsError.FIELD_REQUIRED, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkEmail(@NotNull String fieldName, @Nullable String fieldValue) {
		if(!EmailValidator.getInstance().isValid(fieldValue)) {
			throw new WsException(WsError.EMAIL_INVALID, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkHexaColor(@NotNull String fieldName, @Nullable String fieldValue) {
		if(fieldValue != null && !fieldValue.matches("[0-9a-fA-F]{6}")) {
			throw new WsException(WsError.COLOR_INVALID, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static @Nullable String checkHexaColorWithStartingHash(@NotNull String fieldName, @Nullable String fieldValue) {
		if(fieldValue != null) {
            if (!fieldValue.startsWith("#")) {
                throw new WsException(WsError.COLOR_INVALID, ImmutableList.of(fieldName));
            }
            return "#" + checkHexaColor(fieldName, fieldValue.substring(1));
		}
		return null;
	}
}
