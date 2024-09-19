package com.coreoz.plume.jersey.errors;

import java.util.List;

/**
 * A {@link RuntimeException} that stops the execution of the web-service
 * and provides at the same time information of the error to the web-service consumer.
 * @see WsResultExceptionMapper
 */
public class WsException extends RuntimeException {
    private static final long serialVersionUID = -5694734210679299708L;

	private final WsError error;
	private final Iterable<String> statusArguments;

	public WsException(WsError error) {
		this(error, List.of());
	}

	public WsException(WsError error, String... statusArguments) {
		this(error, List.of(statusArguments));
	}

	public WsException(WsError error, Iterable<String> statusArguments) {
        super(error.name());
		this.error = error;
		this.statusArguments = statusArguments;
	}

	public WsError getError() {
		return error;
	}

	public Iterable<String> getStatusArguments() {
		return statusArguments;
	}
}

