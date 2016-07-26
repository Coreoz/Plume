package com.coreoz.plume.jersey.errors;

import java.util.List;

public class ErrorResponse {
	
	private final String errorCode;
	private final List<String> statusArguments;
	
	public ErrorResponse(WsError error, List<String> statusArguments) {
		this.errorCode = error.getKey();
		this.statusArguments = statusArguments;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public List<String> getStatusArguments() {
		return statusArguments;
	}

}
