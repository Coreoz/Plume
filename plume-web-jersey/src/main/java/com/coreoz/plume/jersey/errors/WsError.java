package com.coreoz.plume.jersey.errors;

/**
 * Reference errors that can be raised in a web-service
 */
public interface WsError {

	// erreurs communes
	WsError INTERNAL_ERROR = new WsErrorInternal("INTERNAL_ERROR");

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
