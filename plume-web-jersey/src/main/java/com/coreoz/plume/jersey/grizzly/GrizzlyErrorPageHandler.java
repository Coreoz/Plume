package com.coreoz.plume.jersey.grizzly;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.Request;

/**
 * Replace the default Grizzly error handler to avoid leaking server information
 */
@Slf4j
public class GrizzlyErrorPageHandler implements ErrorPageGenerator {
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
