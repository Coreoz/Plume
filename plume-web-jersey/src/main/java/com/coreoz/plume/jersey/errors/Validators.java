package com.coreoz.plume.jersey.errors;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class Validators {

	public static<T> T checkRequired(T parameter) {
		if(parameter == null) {
			throw new WsException(WsError.FIELD_REQUIRED);
		}
		return parameter;
	}

	public static String checkRequired(String fieldName, String fieldValue) {
		if(Strings.isNullOrEmpty(fieldValue)) {
			throw new WsException(WsError.FIELD_REQUIRED, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static<T> T checkRequired(String fieldName, T fieldValue) {
		if(fieldValue == null) {
			throw new WsException(WsError.FIELD_REQUIRED, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static String checkEmail(String fieldName, String fieldValue) {
		if(!EmailValidator.getInstance().isValid(fieldValue)) {
			throw new WsException(WsError.EMAIL_INVALID, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static String checkHexaColor(String fieldName, String fieldValue) {
		if(fieldValue != null && !fieldValue.matches("[0-9a-fA-F]{6}")) {
			throw new WsException(WsError.COLOR_INVALID, ImmutableList.of(fieldName));
		}
		return fieldValue;
	}

	public static String checkHexaColorWithStartingHash(String fieldName, String fieldValue) {
		if(fieldValue != null && !fieldValue.startsWith("#")) {
			throw new WsException(WsError.COLOR_INVALID, ImmutableList.of(fieldName));
		}
		checkHexaColor(fieldName, fieldValue.substring(1));
		return fieldValue;
	}

}
