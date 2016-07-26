package com.coreoz.plume.jersey.errors;

/**
 * Centralise les erreurs pouvant être levées dans les WS
 * @author amanteaux
 */
public interface WsError {

	// erreurs communes
	public static final WsError INTERNAL_ERROR = new WsErrorInternal("internal_error");

	/**
	 * Retourne la clé de l'erreur
	 */
	String getKey();

	static class WsErrorInternal implements WsError {

		private final String key;

		public WsErrorInternal(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	}

}
