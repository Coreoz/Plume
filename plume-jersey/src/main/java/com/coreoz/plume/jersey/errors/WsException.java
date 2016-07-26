package com.coreoz.plume.jersey.errors;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Une exception qui empêche le traitement normal d'un web-service
 * et dont le code d'erreur doit remonter à l'utilisateur
 */
public class WsException extends RuntimeException {

	private static final long serialVersionUID = -5694734210679299708L;
	
	private final WsError error;
	private final List<String> statusArguments;
	
	public WsException(WsError error) {
		this(error, ImmutableList.of());
	}

	public WsException(WsError error, List<String> statusArguments) {
		this.error = error;
		this.statusArguments = statusArguments;
	}

	public WsError getError() {
		return error;
	}

	public List<String> getStatusArguments() {
		return statusArguments;
	}
	
}
