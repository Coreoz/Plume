package com.coreoz.plume.jersey.errors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

@Provider
public class WsResultExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LoggerFactory.getLogger(WsResultExceptionMapper.class);

	@Override
	public Response toResponse(Throwable e) {
		if (e instanceof WsException) {
			WsException wsException = (WsException) e;
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new ErrorResponse(wsException.getError(), wsException.getStatusArguments()))
					.type(MediaType.APPLICATION_JSON_TYPE)
					.build();
		}
		if(e instanceof WebApplicationException) {
			return ((WebApplicationException) e).getResponse();
		}

		logger.error("Erreur inconnue sur le WS", e);

		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorResponse(WsError.INTERNAL_ERROR, ImmutableList.of()))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

}