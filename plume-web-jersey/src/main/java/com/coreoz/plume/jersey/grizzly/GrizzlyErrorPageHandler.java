package com.coreoz.plume.jersey.grizzly;

import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replace the default Grizzly error handler to avoid leaking server information
 */
public class GrizzlyErrorPageHandler implements ErrorPageGenerator {

	private static final Logger logger = LoggerFactory.getLogger(GrizzlyErrorPageHandler.class);

	@Override
	public String generate(Request request, int status, String reasonPhrase, String description,
			Throwable exception) {
		if(exception != null) {
			logger.error(
				"Error raised on {}?{} - {}",
				request.getPathInfo(),
				request.getQueryString(),
				description,
				exception
			);
		}

		return status + " " + reasonPhrase;
	}

}
