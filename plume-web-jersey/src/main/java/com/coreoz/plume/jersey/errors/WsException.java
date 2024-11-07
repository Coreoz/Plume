package com.coreoz.plume.jersey.errors;

import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * A {@link RuntimeException} that stops the execution of the web-service
 * and provides at the same time information of the error to the web-service consumer.
 * @see WsResultExceptionMapper
 */
public class WsException extends RuntimeException {
	private final transient WsError error;
	private final transient Iterable<String> statusArguments;

	public WsException(@Nonnull WsError error) {
		this(error, List.of());
	}

	public WsException(@Nonnull WsError error, String... statusArguments) {
		this(error, List.of(statusArguments));
	}

	public WsException(@Nonnull WsError error, @Nonnull Iterable<String> statusArguments) {
        super(error.name());
		this.error = error;
		this.statusArguments = statusArguments;
	}

    @Nonnull
	public WsError getError() {
		return error;
	}

    @Nonnull
	public Iterable<String> getStatusArguments() {
		return statusArguments;
	}
}

