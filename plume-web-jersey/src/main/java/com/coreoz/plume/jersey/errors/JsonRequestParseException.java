package com.coreoz.plume.jersey.errors;

/**
 * Exception thrown when a JSON parsing error occurred in an HTTP request.
 * It is useful to differentiate general parsing errors from a specific HTTP request parsing error.
 */
public class JsonRequestParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
