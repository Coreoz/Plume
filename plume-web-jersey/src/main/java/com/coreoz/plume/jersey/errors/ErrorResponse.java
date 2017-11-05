package com.coreoz.plume.jersey.errors;

/**
 * Represent an error that should be serialized by Jersey
 */
public class ErrorResponse {

	private final String errorCode;
	private final Iterable<String> statusArguments;

	public ErrorResponse(WsError error, Iterable<String> statusArguments) {
		this.errorCode = error.name();
		this.statusArguments = statusArguments;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Iterable<String> getStatusArguments() {
		return statusArguments;
	}

}
