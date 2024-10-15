package com.coreoz.plume.jersey.errors;

/**
 * Reference common errors that can be raised in a web-service.
 * This interface should be implemented inside an enum.
 */
public interface WsError {

	// common errors
	WsError INTERNAL_ERROR = new WsErrorInternal("INTERNAL_ERROR");
	WsError REQUEST_INVALID = new WsErrorInternal("REQUEST_INVALID");
	WsError FIELD_REQUIRED = new WsErrorInternal("FIELD_REQUIRED");
	WsError EMAIL_INVALID = new WsErrorInternal("EMAIL_INVALID");
	WsError COLOR_INVALID = new WsErrorInternal("COLOR_INVALID");
	WsError CONTENT_SIZE_LIMIT_EXCEEDED = new WsErrorInternal("CONTENT_SIZE_LIMIT_EXCEEDED");

	/**
	 * Returns the name of the error
	 */
	String name();

	class WsErrorInternal implements WsError {

		private final String name;

		public WsErrorInternal(String name) {
			this.name = name;
		}

		@Override
		public String name() {
			return name;
		}
	}

}
